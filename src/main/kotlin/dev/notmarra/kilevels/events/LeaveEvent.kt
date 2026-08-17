package dev.notmarra.kilevels.events

import dev.notmarra.kilevels.KiLevels
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class LeaveEvent(private val plugin: KiLevels): Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        val player = event.player

        plugin.log.debug("Trying to remove ${player.uniqueId} from cache")
        plugin.cacheManager.remove(player.uniqueId)
    }
}