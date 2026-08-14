package dev.notmarra.kilevels.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.notmarra.kilevels.KiLevels
import java.util.Locale.getDefault

class DatabaseManager(private val plugin: KiLevels) {
    private val config = plugin.configManager.config.data
    lateinit var dataSource: HikariDataSource
        private set

    fun setup() {
        val conf = HikariConfig().apply {
            poolName = "KiLevels"

            maximumPoolSize = config.pool.maximumPoolSize
            minimumIdle = config.pool.minimumIdle
            connectionTimeout = config.pool.connectionTimeout
            idleTimeout = config.pool.idleTimeout
            maxLifetime = config.pool.maxLifetime

            if (!config.jdbcUrl.isNullOrEmpty()) {
                jdbcUrl = config.jdbcUrl
            } else {
                if (config.type.equals("SQLite", ignoreCase = true)) {
                    val dbFile = plugin.dataFolder.resolve("${config.database}.db")
                    jdbcUrl = "jdbc:sqlite:${dbFile.absolutePath}"

                } else {
                    jdbcUrl = "jdbc:mysql://${config.host}:${config.port}/${config.database}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
                    username = config.username
                    password = config.password
                }
            }
        }

        dataSource = HikariDataSource(conf)
    }

    fun close() {
        if (::dataSource.isInitialized && !dataSource.isClosed) {
            dataSource.close()
        }
    }
}