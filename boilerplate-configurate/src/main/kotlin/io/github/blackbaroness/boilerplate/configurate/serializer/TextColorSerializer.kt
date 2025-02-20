package io.github.blackbaroness.boilerplate.configurate.serializer

import net.kyori.adventure.text.format.TextColor
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

class TextColorSerializer : TypeSerializer<TextColor> {

    override fun deserialize(type: Type, node: ConfigurationNode): TextColor? {
        val string = node.string ?: return null
        return TextColor.fromHexString(string) ?: throw IllegalArgumentException("Invalid HEX color '$string'")
    }

    override fun serialize(type: Type, obj: TextColor?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.set(obj.asHexString())
    }
}
