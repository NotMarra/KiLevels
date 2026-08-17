package dev.notmarra.kilevels.utils.config

import dev.notmarra.kilevels.KiLevels
import org.bukkit.plugin.Plugin
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File

class ConfigManager(private val plugin: KiLevels) {
    private val configFile = File(plugin.dataFolder, "config.yml")
    private lateinit var loader: YamlConfigurationLoader

    lateinit var config: MainConfig
        private set

    init {
        load()
    }

    fun load() {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

        if (!configFile.exists()) plugin.saveResource("config.yml", false)

        loader = YamlConfigurationLoader.builder()
            .path(configFile.toPath())
            .nodeStyle(NodeStyle.BLOCK)
            .build()

        try {
            val root = loader.load()
            config = root.get(MainConfig::class.java) ?: MainConfig()
        } catch (e: Exception) {
            plugin.log.severe(e.message)
            config = MainConfig()
        }
    }
}