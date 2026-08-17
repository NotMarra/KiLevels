package dev.notmarra.kilevels.api.events

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class LevelUpEvent(
    val player: Player,
    val oldLevel: Int,
    val newLevel: Int,
): Event(), Cancellable {
    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = cancelled
    override fun setCancelled(cancelled: Boolean) { this.cancelled = cancelled }

    override fun getHandlers(): HandlerList {
        return HandlerList()
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}