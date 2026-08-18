package dev.notmarra.kilevels.managers

import dev.notmarra.kilevels.KiLevels
import dev.notmarra.kilevels.api.events.LevelDownEvent
import dev.notmarra.kilevels.api.events.LevelUpEvent
import dev.notmarra.kilevels.api.events.XpGainEvent
import dev.notmarra.kilevels.api.events.XpRemoveEvent
import dev.notmarra.kilevels.api.model.PlayerProfile
import dev.notmarra.kilevels.utils.XpSource
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class LevelManager(private val plugin: KiLevels) {
    private val config = plugin.configManager.config

    fun giveXp(player: OfflinePlayer, amount: ULong, source: XpSource = XpSource.OTHER) {
        val event = XpGainEvent(player, amount, source)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled) return

        val finalAmount = event.amount
        if (finalAmount <= 0UL) return

        val profile = plugin.cacheManager.getProfile(player.uniqueId) ?: return

        profile.xp += finalAmount
        plugin.log.debug("Profile ${profile.name} gained $finalAmount XP (Source: $source)")

        checkAndProcessLevelUp(profile)
    }

    fun removeXp(player: OfflinePlayer, amount: ULong) {
        val event = XpRemoveEvent(player, amount)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled) return

        val finalAmount = event.amount
        if (finalAmount <= 0UL) return

        val profile = plugin.cacheManager.getProfile(player.uniqueId) ?: return

        if (finalAmount >= profile.xp) {
            profile.xp = 0u
        } else {
            profile.xp -= finalAmount
        }
    }

    fun giveLevel(player: OfflinePlayer, amount: UInt) {
        if (amount <= 0UL) return
        val profile = plugin.cacheManager.getProfile(player.uniqueId) ?: return
        val newLevel = profile.level+amount
        val event = LevelUpEvent(player, profile.level, newLevel)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled) return

        val maxLevel: UInt = config.levels.count().toUInt()
        if (newLevel > maxLevel) {
            profile.level = maxLevel
        } else {
            profile.level = newLevel
        }
    }

    fun removeLevel(player: OfflinePlayer, amount: UInt) {
        if (amount <= 0UL) return
        val profile = plugin.cacheManager.getProfile(player.uniqueId) ?: return
        var newLevel: UInt
        if (amount >= profile.level) {
            newLevel = 0u
        } else {
            newLevel = profile.level - amount
        }
        val event = LevelDownEvent(player, profile.level, newLevel)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled) return

        profile.level = newLevel
    }


    private fun checkAndProcessLevelUp(profile: PlayerProfile) {
        plugin.log.debug("Trying to check and process level up for profile ${profile.uuid}")

        val xpNeeded = evaluateNeededXp(profile)

        val maxLevel: UInt = config.levels.count().toUInt()

        if (profile.xp >= xpNeeded && profile.level < maxLevel) {
            val event = LevelUpEvent(Bukkit.getOfflinePlayer(profile.uuid), profile.level, profile.level+1u)
            Bukkit.getPluginManager().callEvent(event)

            if (event.isCancelled) return

            profile.xp -= xpNeeded
            profile.level += 1u

            // TODO("PROCESS REWARDS")

            checkAndProcessLevelUp(profile)
        }
    }

    private fun evaluateNeededXp(profile: PlayerProfile): ULong {
        val expressionString = config.levelFormula
        plugin.log.debug("ExpressionString: $expressionString")

        val expression = ExpressionBuilder(expressionString)
            .variable("%level%")
            .build()
            .setVariable("%level%", profile.level.toDouble())
        plugin.log.debug("Constructed expression: $expression")

        val xpNeeded: ULong = expression.evaluate().toULong()
        plugin.log.debug("Result: $xpNeeded")

        return xpNeeded
    }
}