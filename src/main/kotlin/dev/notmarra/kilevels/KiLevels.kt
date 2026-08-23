package dev.notmarra.kilevels

import dev.notmarra.kilevels.data.CacheManager
import dev.notmarra.kilevels.data.DatabaseManager
import dev.notmarra.kilevels.events.JoinEvent
import dev.notmarra.kilevels.events.LeaveEvent
import dev.notmarra.kilevels.managers.LevelManager
import dev.notmarra.kilevels.utils.config.ConfigManager
import dev.notmarra.kilevels.utils.KiLevelsLogger
import org.bukkit.plugin.java.JavaPlugin
import xyz.xenondevs.invui.InvUI

class KiLevels : JavaPlugin() {
    lateinit var configManager: ConfigManager
        private set
    lateinit var databaseManager: DatabaseManager
        private set
    lateinit var cacheManager: CacheManager
        private set
    lateinit var levelManager: LevelManager
        private set
    val log = KiLevelsLogger(this)

    override fun onEnable() {
        configManager = ConfigManager(this)
        databaseManager = DatabaseManager(this)
        cacheManager = CacheManager(this)
        levelManager = LevelManager(this)
        InvUI.getInstance().setPlugin(this)

        log.debug("Initializing listeners")
        server.pluginManager.registerEvents(JoinEvent(this), this)
        server.pluginManager.registerEvents(LeaveEvent(this), this)

        log.info("Plugin enabled successfully!")
    }

    override fun onDisable() {
        if (::cacheManager.isInitialized) {
            cacheManager.saveAll()
        }
        if (::databaseManager.isInitialized) {
            databaseManager.close()
        }
        log.info("Plugin disabled successfully!")
    }
}
