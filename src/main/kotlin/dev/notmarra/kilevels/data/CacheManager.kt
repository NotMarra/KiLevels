package dev.notmarra.kilevels.data

import dev.notmarra.kilevels.KiLevels
import org.bukkit.entity.Player
import java.util.UUID

class CacheManager(private val plugin: KiLevels) {
    var cache: HashMap<UUID, PlayerProfile> = HashMap()
        private set;
    private val config = plugin.configManager.config.cache

    init {
        startAutoSaveTask()
    }

    fun add(player: Player) {
        val uuid = player.uniqueId

        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
            val profile = loadProfile(player)

            cache[uuid] = profile

            plugin.log.debug("Profile ${player.name} has been added to cache")
        })
    }

    fun remove(uuid: UUID) {
        val profile = cache[uuid] ?: return

        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
            saveProfile(profile)
            if (config.destroyOnLeave) {
                cache.remove(uuid)
            }
            plugin.log.debug("Profile ${profile.name} has been removed from cache")
        })
    }

    fun saveAll() {
        for (profile in cache.values) {
            saveProfile(profile)
        }
        cache.clear()
    }

    private fun startAutoSaveTask() {
        val intervalSeconds = config.ttl
        val intervalTicks = intervalSeconds * 20L

        plugin.server.scheduler.runTaskTimerAsynchronously(plugin, Runnable {
            if (cache.isNotEmpty()) return@Runnable

            plugin.log.debug("Starting autosaveTask (interval: $intervalTicks , size: ${cache.size})")

            for (profile in cache.values) {
                try {
                    plugin.databaseManager.saveProfile(profile)
                } catch (e: Exception) {
                    plugin.log.severe("Error while saving profile ${profile.uuid}: ${e.message}")
                }
            }

            plugin.log.debug("Saving finished autosaveTask (interval: $intervalTicks , size: ${cache.size})")
        }, intervalTicks, intervalSeconds)
    }

    private fun loadProfile(player: Player): PlayerProfile {
        var profile = plugin.databaseManager.getProfile(player.uniqueId)

        if (profile == null) {
            profile = PlayerProfile(player.uniqueId, player.name, 0, 0L )
            plugin.databaseManager.createProfile(profile)
        }

        return profile
    }

    private fun saveProfile(profile: PlayerProfile) {
        plugin.databaseManager.saveProfile(profile)
    }
}