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
        for (action in list) {
            when (action) {
                is LevelAction.Money -> {
                    // TODO: add vault hook
                }
                is LevelAction.Xp -> {
                    val online = player.player
                    online?.giveExp(action.amount.toInt())
                    // TODO: add reward to process for offline players
                }
                is LevelAction.Command -> {
                    val name = player.name
                    val replacements = mapOf("%player%" to name)
                    var text = action.command
                    for ((key, value) in replacements) text = text.replace(key, value?:"")

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), text)
                }
                is LevelAction.Permission -> {
                    return
                    // TODO: add luckperms hook
                }
            }
        }
    }

    private fun processRewardTexts(list: List<String>, player: OfflinePlayer) {
        if (player.isOnline) {
            // TODO: Process texts and add language folder
        }
    }
}