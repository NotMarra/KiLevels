package dev.notmarra.kilevels.utils.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MainConfig(
    val debug: Boolean = false,
    val prefix: String = "",
    val levelFormula: String = "",
    val levels: List<Level> = listOf(),
)

@ConfigSerializable
data class Level(
    val title: String,
    val reward: LevelReward,
)

@ConfigSerializable
data class LevelReward(
    val texts: LevelRewardsTexts,
    val actions: LevelRewardsActions
)

@ConfigSerializable
data class LevelRewardsTexts(
    val texts: List<String>,
)

@ConfigSerializable
data class LevelRewardsActions(
    val actions: List<String>,
)