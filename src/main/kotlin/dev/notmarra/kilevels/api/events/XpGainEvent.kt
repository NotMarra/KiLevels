package dev.notmarra.kilevels.api.events

import dev.notmarra.kilevels.utils.XpSource
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class XpGainEvent(
    val player: Player,
    var amount: Long,
    val source: XpSource
) : Event(), Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = cancelled
    override fun setCancelled(cancelled: Boolean) { this.cancelled = cancelled }

    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
