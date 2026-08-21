package dev.notmarra.kilevels.utils.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class MainConfig(
    val debug: Boolean = false,
    val prefix: String = "",
    @Setting("levelFormula")
    val levelFormula: String = "",
    val data: DataConfig = DataConfig(),
    val cache: CacheConfig = CacheConfig(),
    val levels: Map<Int, Level> = mapOf(),
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
    @Setting("maximumPoolSize") val maximumPoolSize: Int = 10,
    @Setting("minimumIdle") val minimumIdle: Int = 2,
    @Setting("connectionTimeout") val connectionTimeout: Long = 30000,
    @Setting("idleTimeout") val idleTimeout: Long = 600000,
    @Setting("maxLifetime") val maxLifetime: Long = 1800000
)

@ConfigSerializable
data class CacheConfig(
    val ttl: Long = 300,
    @Setting("destroyOnLeave") val destroyOnLeave: Boolean = true,
)

@ConfigSerializable
data class Level(
    val title: String = "",
    val rewards: LevelReward = LevelReward(),
)

@ConfigSerializable
data class LevelReward(
    val text: List<String> = listOf(),
    val actions: List<String> = listOf(),
)