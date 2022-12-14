package com.github.trueddd.core

import com.github.trueddd.core.events.Action
import com.github.trueddd.data.GlobalState
import com.github.trueddd.utils.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import org.jetbrains.annotations.TestOnly
import org.koin.core.annotation.Single

@Single
class EventManager(
    private val actionHandlerRegistry: ActionHandlerRegistry,
    private val stateHolder: StateHolder,
) : CoroutineScope {

    companion object {
        const val TAG = "EventManager"
    }

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.Default
    }

    private val actionsPipe = MutableSharedFlow<Action>()

    private var eventHandlingJob: Job? = null

    private val eventHandlingMonitor = Mutex(false)

    private val handledActionsFlow = MutableSharedFlow<Int>()

    init {
        startEventHandling()
    }

    fun consumeAction(action: Action) {
        launch {
            Log.info(TAG, "Consuming action: $action")
            actionsPipe.emit(action)
        }
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    @TestOnly
    suspend fun suspendConsumeAction(action: Action) {
        handledActionsFlow
            .onStart {
                this@EventManager.launch {
                    Log.info(TAG, "Consuming action: $action")
                    actionsPipe.emit(action)
                }
            }
            .filter { it == action.id }
            .take(1)
            .collect { Log.info(TAG, "Action(${action.id}) consumed") }
    }

    fun stopHandling() {
        if (eventHandlingJob?.isActive == true) {
            eventHandlingJob?.cancel()
            eventHandlingJob = null
        }
    }

    fun startHandling(initState: GlobalState = GlobalState.default()) {
        stateHolder.update { initState }
        startEventHandling()
    }

    private fun startEventHandling() {
        if (eventHandlingJob?.isActive == true) {
            Log.error(TAG, "EventManager is already running; skip start")
            return
        }
        eventHandlingJob = actionsPipe
            .onStart { Log.info(TAG, "Starting") }
            .onEach { action ->
                val handler = actionHandlerRegistry.handlerOf(action) ?: return@onEach
                eventHandlingMonitor.lock()
                val result = handler.consume(action, stateHolder.globalStateFlow.value)
                stateHolder.update { result }
                eventHandlingMonitor.unlock()
                handledActionsFlow.emit(action.id)
            }
            .onCompletion { Log.info(TAG, "Finishing") }
            .launchIn(this)
    }
}
