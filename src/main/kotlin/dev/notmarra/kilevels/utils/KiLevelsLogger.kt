package dev.notmarra.kilevels.utils

import dev.notmarra.kilevels.KiLevels

class KiLevelsLogger(private val plugin: KiLevels) {
    fun info(message: String?) {
        plugin.logger.info(message)
    }

    fun warn(message: String?) {
        plugin.logger.warning("[WARNING] $message")
    }

    fun severe(message: String?) {
        plugin.logger.severe("[ERROR] $message")
    }

    fun debug(message: String?) {
        if(plugin.configManager.config.debug) {
            plugin.logger.warning("[DEBUG] $message")
        }
    }
}