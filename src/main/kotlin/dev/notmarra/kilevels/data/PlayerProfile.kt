package dev.notmarra.kilevels.data

import java.util.UUID

data class PlayerProfile(
    val uuid: UUID,
    val name: String,
    val level: Int,
    val xp: Long
)
