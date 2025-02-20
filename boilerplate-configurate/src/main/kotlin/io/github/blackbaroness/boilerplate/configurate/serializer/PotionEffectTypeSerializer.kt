package io.github.blackbaroness.boilerplate.configurate.serializer

import org.bukkit.potion.PotionEffectType
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

class PotionEffectTypeSerializer : TypeSerializer<PotionEffectType> {

    override fun deserialize(type: Type, node: ConfigurationNode): PotionEffectType? {
        val string = node.string ?: return null
        return PotionEffectType.getByName(string)
            ?: throw IllegalArgumentException("Unknown potion effect type '$string'")
    }

    override fun serialize(type: Type, obj: PotionEffectType?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.set(obj.name)
    }
}
