package dev.notmarra.kilevels.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.notmarra.kilevels.KiLevels
import org.bukkit.entity.Player
import java.sql.Connection
import java.sql.SQLException
import java.util.Locale.getDefault
import java.util.UUID

class DatabaseManager(private val plugin: KiLevels) {
    private val config = plugin.configManager.config.data
    lateinit var dataSource: HikariDataSource
        private set

    init {
        setup()
        init()
    }

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

    fun init() {
        val query = """
            CREATE TABLE IF NOT EXISTS ${config.table} (
                uuid CHAR(36) NOT NULL PRIMARY KEY,
                level INT UNSIGNED NOT NULL DEFAULT 1,
                xp BIGINT UNSIGNED NOT NULL DEFAULT 0
            )
        """.trimIndent()

        try {
            dataSource.connection.use { conn ->
                conn.createStatement().use { statement ->
                    statement.execute(query)
                }
            }
        } catch (e: SQLException) {
            plugin.logger.severe("Problem with database initialization: ${e.message}")
        }
    }

    fun getProfile(uuid: UUID): PlayerProfile? {
        val query = "SELECT level, xp FROM ${config.table} WHERE uuid = ?"
        try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setString(1, uuid.toString())

                    stmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            val level = rs.getInt(1)
                            val xp = rs.getLong(2)

                            return PlayerProfile(
                                uuid = uuid,
                                name = plugin.server.getOfflinePlayer(uuid).name.toString(),
                                level = level,
                                xp = xp
                            )
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            plugin.log.severe(e.message)
        }

        return null
    }

    fun createProfile(profile: PlayerProfile) {
        val query = "INSERT IGNORE ${config.table} (uuid, level, xp) VALUES (?, ?, ?)"

        try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setString(1, profile.uuid.toString())
                    stmt.setInt(2, profile.level)
                    stmt.setLong(3, profile.xp)

                    stmt.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            plugin.log.severe(e.message)
        }
    }

    fun saveProfile(profile: PlayerProfile) {
        val query = "UPDATE ${config.table} SET level=?, xp=? WHERE uuid = ?"

        try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setInt(1, profile.level)
                    stmt.setLong(2, profile.xp)
                    stmt.setString(3, profile.uuid.toString())

                    stmt.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            plugin.log.severe(e.message)
        }
    }

    fun upsertProfile(profile: PlayerProfile) {
        val query = """
        INSERT INTO ${config.table} (uuid, level, xp) 
        VALUES (?, ?, ?) 
        ON DUPLICATE KEY UPDATE level = VALUES(level), xp = VALUES(xp)
    """.trimIndent()

        try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setString(1, profile.uuid.toString())
                    stmt.setInt(2, profile.level)
                    stmt.setLong(3, profile.xp)

                    stmt.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            plugin.logger.severe(e.message)
        }
    }
}