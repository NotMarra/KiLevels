package dev.notmarra.kilevels.api.model

import java.util.UUID

data class PlayerProfile(
    val uuid: UUID,
    val name: String,
    var level: UInt,
    var xp: ULong
)