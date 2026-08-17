package dev.notmarra.kilevels

import dev.notmarra.kilevels.data.CacheManager
import dev.notmarra.kilevels.data.DatabaseManager
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
    val log = KiLevelsLogger(this)

    override fun onEnable() {
        configManager = ConfigManager(this)
        databaseManager = DatabaseManager(this)
        cacheManager = CacheManager(this)
        InvUI.getInstance().setPlugin(this)

        log.info("Plugin enabled successfully!")
    }

    override fun onDisable() {
        cacheManager.saveAll()
        if (::databaseManager.isInitialized) {
            databaseManager.close()
        }
        log.info("Plugin disabled successfully!")
    }
}
