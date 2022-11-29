package com.github.trueddd.core

import com.github.trueddd.core.events.Action
import com.github.trueddd.core.generator.ActionGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class InputParser(
    private val generators: Set<ActionGenerator<*>>,
) {

    suspend fun parse(input: String): Action? {
        return withContext(Dispatchers.Default) {
            generators.firstNotNullOfOrNull { actionGenerator ->
                val match = actionGenerator.inputMatcher.matchEntire(input)
                if (match != null) {
                    actionGenerator.generate(input)
                } else {
                    null
                }
            }
        }
    }
}
