package io.github.blackbaroness.boilerplate.configurate.paper.serializer

import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlot
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import java.util.*

class AttributeModifierSerializer : TypeSerializer<AttributeModifier> {

    override fun deserialize(type: Type, node: ConfigurationNode): AttributeModifier? {
        val uuid = node.node("uuid").string?.let { UUID.fromString(it) } ?: return null
        val name = node.node("name").string ?: return null
        val operation = node.node("operation").get(Operation::class) ?: return null
        val amount = node.node("amount").getDouble(Double.NaN).takeUnless { it.isNaN() } ?: return null
        val slot = node.node("slot").get(EquipmentSlot::class)

        return AttributeModifier(uuid, name, amount, operation, slot)
    }

    override fun serialize(type: Type, obj: AttributeModifier?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.node("uuid").set(obj.uniqueId.toString())
        node.node("name").set(obj.name)
        node.node("operation").set(obj.operation)
        node.node("amount").set(obj.amount)
        node.node("slot").set(obj.slot)
    }
}
