package dev.notmarra.kilevels.managers

import dev.notmarra.kilevels.KiLevels
import dev.notmarra.kilevels.api.events.LevelDownEvent
import dev.notmarra.kilevels.api.events.LevelUpEvent
import dev.notmarra.kilevels.api.events.XpGainEvent
import dev.notmarra.kilevels.api.events.XpRemoveEvent
import dev.notmarra.kilevels.api.models.Level
import dev.notmarra.kilevels.api.models.PlayerProfile
import dev.notmarra.kilevels.api.enums.XpSource
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class LevelManager(private val plugin: KiLevels) {
    private val config = plugin.configManager.config
    private var listLevels: HashMap<UInt, Level> = HashMap()

    init {
        initLevels()
    }

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
        val newLevel: UInt = if (amount >= profile.level) {
            0u
        } else {
            profile.level - amount
        }
        val event = LevelDownEvent(player, profile.level, newLevel)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled) return

        profile.level = newLevel
    }


    private fun checkAndProcessLevelUp(profile: PlayerProfile) {
        plugin.log.debug("Trying to check and process level up for profile ${profile.uuid}")

        val xpNeeded = listLevels[profile.level]?.xp ?: 0U

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
        return evaluateNeededXp(profile.level)
    }

    private fun evaluateNeededXp(level: UInt): ULong {
        val expressionString = config.levelFormula
        plugin.log.debug("ExpressionString: $expressionString")

        val expression = ExpressionBuilder(expressionString)
            .variable("x")
            .build()
            .setVariable("x", level.toDouble())
        plugin.log.debug("Constructed expression: $expression")

        val xpNeeded: ULong = expression.evaluate().toULong()
        plugin.log.debug("Result: $xpNeeded")

        return xpNeeded
    }

    private fun initLevels() {
        val levels = config.levels

        for ((levelKey, level) in levels) {
            val lvl: UInt = levelKey.toUInt()
            val xp = evaluateNeededXp(lvl)
            plugin.log.debug("Evaluating level $lvl (${level.title}): $xp xp")
            listLevels[lvl] = Level(level.title, xp, lvl)
        }
    }
}