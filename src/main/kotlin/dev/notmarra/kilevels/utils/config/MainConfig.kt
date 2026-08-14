package dev.notmarra.kilevels.utils.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MainConfig(
    val debug: Boolean = false,
    val prefix: String = "",
    val levelFormula: String = "",
    val data: DataConfig = DataConfig(),
    val cache: CacheConfig = CacheConfig(),
    val levels: List<Level> = listOf(),
)

@ConfigSerializable
data class DataConfig(
    val type: String = "SQLite",
    val table: String = "KiLevels",
    val host: String = "",
    val port: Int = 3306,
    val username: String = "",
    val database: String = "data",
    val password: String = "",
    val pool: DataPool = DataPool(),
    val jdbcUrl: String? = null,
)

@ConfigSerializable
data class DataPool(
    val maximumPoolSize: Int = 10,
    val minimumIdle: Int = 2,
    val connectionTimeout: Long = 30000,
    val idleTimeout: Long = 600000,
    val maxLifetime: Long = 1800000
)

@ConfigSerializable
data class CacheConfig(
    val ttl: Int = 300,
    val destroyOnLeave: Boolean = true,
)

@ConfigSerializable
data class Level(
    val title: String = "",
    val reward: LevelReward = LevelReward(),
)

@ConfigSerializable
data class LevelReward(
    val texts: LevelRewardsTexts = LevelRewardsTexts(),
    val actions: LevelRewardsActions = LevelRewardsActions(),
)

@ConfigSerializable
data class LevelRewardsTexts(
    val texts: List<String> = listOf(),
)

@ConfigSerializable
data class LevelRewardsActions(
    val actions: List<String> = listOf(),
)