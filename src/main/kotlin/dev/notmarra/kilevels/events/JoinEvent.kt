package dev.notmarra.kilevels.events

import dev.notmarra.kilevels.KiLevels
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent(private val plugin: KiLevels): Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        plugin.log.debug("Trying to solve player ${player.uniqueId}")
        plugin.cacheManager.add(player)
    }
}