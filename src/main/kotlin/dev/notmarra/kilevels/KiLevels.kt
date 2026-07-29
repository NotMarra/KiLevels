package dev.notmarra.kilevels

import dev.notmarra.kilevels.utils.config.ConfigManager
import org.bukkit.plugin.java.JavaPlugin
import xyz.xenondevs.invui.InvUI

class KiLevels : JavaPlugin() {
    lateinit var configManager: ConfigManager
        private set

    override fun onEnable() {
        configManager = ConfigManager(this)
        configManager.load()

        InvUI.getInstance().setPlugin(this)

        logger.info("Plugin enabled successfully!")
    }

    override fun onDisable() {
        logger.info("Plugin disabled successfully!")
    }
}
