package dev.notmarra.kilevels

import org.bukkit.plugin.java.JavaPlugin

class KiLevels : JavaPlugin() {

    override fun onEnable() {
        logger.info("Plugin enabled successfully!")
    }

    override fun onDisable() {
        logger.info("Plugin disabled successfully!")
    }
}
