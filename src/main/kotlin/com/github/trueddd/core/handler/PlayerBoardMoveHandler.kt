package com.github.trueddd.core.handler

import com.github.trueddd.core.events.Action
import com.github.trueddd.core.events.BoardMove
import com.github.trueddd.data.GlobalState
import com.github.trueddd.utils.coerceDiceValue
import com.trueddd.github.annotations.IntoMap

@IntoMap(mapName = ActionConsumer.TAG, key = Action.Keys.BoardMove)
class PlayerBoardMoveHandler : ActionConsumer<BoardMove> {

    override suspend fun consume(action: BoardMove, currentState: GlobalState): GlobalState {
        val newPlayersState = currentState.players
            .mapValues { (participant, playerState) ->
                if (action.rolledBy == participant) {
                    val moveValue = coerceDiceValue(action.diceValue + playerState.diceModifier)
                    playerState.copy(position = playerState.position + moveValue)
                } else {
                    playerState
                }
            }
        return currentState.copy(players = newPlayersState)
    }
}
