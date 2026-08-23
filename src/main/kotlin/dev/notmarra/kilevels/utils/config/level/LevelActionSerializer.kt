package dev.notmarra.kilevels.utils.config.level

import dev.notmarra.kilevels.api.enums.Actions
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object LevelActionSerializer : TypeSerializer<LevelAction> {
    override fun deserialize(type: Type, node: ConfigurationNode): LevelAction {
        val actionType = node.node("action").get(Actions::class.java)
            ?: throw SerializationException(node, type, "Missing or invalid 'action' field")

        val chance = node.node("chance").getDouble(1.0)

        return when (actionType) {
            Actions.MONEY -> LevelAction.Money(
                amount = node.node("amount").getLong(0),
                chance = chance,
            )
            Actions.XP -> LevelAction.Xp(
                amount = node.node("amount").getLong(0),
                chance = chance,
            )
            Actions.COMMAND -> LevelAction.Command(
                command = node.node("command").getString("") ?: "",
                chance = chance,
            )
            Actions.PERMISSION -> LevelAction.Permission(
                permission = node.node("permission").getString("") ?: "",
                chance = chance,
            )
        }
    }

    override fun serialize(type: Type, obj: LevelAction?, node: ConfigurationNode) {
        if (obj == null) { node.set(null); return }

        node.node("action").set(obj.type)

        when (obj) {
            is LevelAction.Money -> node.node("amount").set(obj.type)
            is LevelAction.Xp -> node.node("amount").set(obj.type)
            is LevelAction.Command -> node.node("command").set(obj.type)
            is LevelAction.Permission -> node.node("permission").set(obj.type)
        }
        if (obj.chance != 1.0) node.node("chance").set(obj.chance)
    }
}