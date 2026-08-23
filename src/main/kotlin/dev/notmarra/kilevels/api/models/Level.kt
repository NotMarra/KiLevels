package dev.notmarra.kilevels.api.models

import dev.notmarra.kilevels.utils.config.level.LevelAction

data class Level(
    val title: String,
    val xp: ULong,
    val level: UInt,
    val rewards: List<LevelAction> = listOf(),
    val rewardTexts: List<String> = listOf(),
)
