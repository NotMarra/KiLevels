package dev.notmarra.kilevels.managers

import dev.notmarra.kilevels.KiLevels
import dev.notmarra.kilevels.api.models.Level
import dev.notmarra.kilevels.utils.config.level.LevelAction
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

class RewardManager(private val plugin: KiLevels) {

    fun processReward(level: UInt, uuid: UUID) {
        processReward(level, Bukkit.getOfflinePlayer(uuid))
    }

    fun processReward(level: UInt, player: OfflinePlayer) {
        processReward(plugin.levelManager.listLevels[level], player)
    }

    fun processReward(level: Level?, player: OfflinePlayer) {
        if (level == null) {
            plugin.log.severe("Level is null.")
            return
        }

        processReward(level.rewards, player)
        processRewardTexts(level.rewardTexts, player)
    }

    private fun processReward(list: List<LevelAction>, player: OfflinePlayer) {
        // TODO: Add process actions
    }

    private fun processRewardTexts(list: List<String>, player: OfflinePlayer) {
        if (player.isOnline) {
            // TODO: Process texts and add language folder
        }
    }
}