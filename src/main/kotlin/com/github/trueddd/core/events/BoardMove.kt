package com.github.trueddd.core.events

import com.github.trueddd.data.Participant
import kotlinx.serialization.Serializable

@Serializable
data class BoardMove(
    val rolledBy: Participant,
    val diceValue: Int,
) : Action(Keys.BoardMove)
