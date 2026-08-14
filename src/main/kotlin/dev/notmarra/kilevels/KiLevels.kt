package dev.notmarra.kilevels

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
    val log = KiLevelsLogger(this)

    override fun onEnable() {
        configManager = ConfigManager(this)
        configManager.load()
        databaseManager = DatabaseManager(this)
        log.debug("Starting DB")
        databaseManager.setup()

        InvUI.getInstance().setPlugin(this)

        log.info("Plugin enabled successfully!")
    }

    override fun onDisable() {
        if (::databaseManager.isInitialized) {
            databaseManager.close()
        }
        log.info("Plugin disabled successfully!")
    }
}
