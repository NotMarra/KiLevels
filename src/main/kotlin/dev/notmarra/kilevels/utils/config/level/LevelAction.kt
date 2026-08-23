package dev.notmarra.kilevels.utils.config.level

import dev.notmarra.kilevels.api.enums.Actions
import org.spongepowered.configurate.objectmapping.ConfigSerializable

sealed class LevelAction {
    abstract val type: Actions
    abstract val chance: Double

    @ConfigSerializable
    data class Money(
        val amount: Long = 0,
        override val chance: Double = 1.0
    ) : LevelAction() {
        override val type = Actions.MONEY
    }

    @ConfigSerializable
    data class Xp(
        val amount: Long = 0,
        override val chance: Double = 1.0
    ) : LevelAction() {
        override val type = Actions.XP
    }

    @ConfigSerializable
    data class Command(
        val command: String = "",
        override val chance: Double = 1.0
    ) : LevelAction() {
        override val type = Actions.COMMAND
    }

    @ConfigSerializable
    data class Permission(
        val permission: String = "",
        override val chance: Double = 1.0
    ) : LevelAction() {
        override val type = Actions.PERMISSION
    }
}