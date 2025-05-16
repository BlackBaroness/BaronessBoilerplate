package io.github.blackbaroness.boilerplate.configurate.serializer

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.awt.Color
import java.lang.reflect.Type

class ColorSerializer : TypeSerializer<Color> {

    override fun deserialize(type: Type, node: ConfigurationNode): Color? {
        val invalidValue = Int.MIN_VALUE

        val red = node.node("red").getInt(invalidValue).takeIf { it != invalidValue } ?: return null
        val green = node.node("green").getInt(invalidValue).takeIf { it != invalidValue } ?: return null
        val blue = node.node("blue").getInt(invalidValue).takeIf { it != invalidValue } ?: return null

        return Color(red, green, blue)
    }

    override fun serialize(type: Type, obj: Color?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.node("red").set(obj.red)
        node.node("green").set(obj.green)
        node.node("blue").set(obj.blue)
    }
}
