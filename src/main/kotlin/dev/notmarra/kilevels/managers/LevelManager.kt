package dev.notmarra.kilevels.managers

import dev.notmarra.kilevels.KiLevels
import dev.notmarra.kilevels.api.events.XpGainEvent
import dev.notmarra.kilevels.api.model.PlayerProfile
import dev.notmarra.kilevels.utils.XpSource
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class LevelManager(private val plugin: KiLevels) {

    fun giveXp(player: Player, amount: Long, source: XpSource = XpSource.OTHER) {
        val event = XpGainEvent(player, amount, source)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled) return

        val finalAmount = event.amount
        if (finalAmount <= 0) return

        val profile = plugin.cacheManager.getProfile(player.uniqueId) ?: return

        profile.xp += finalAmount
        plugin.log.debug("Profile ${profile.name} gained $finalAmount XP (Source: $source)")

        checkAndProcessLevelUp(profile)
    }

    private fun checkAndProcessLevelUp(profile: PlayerProfile) {
        TODO("implement level up system")
    }
}