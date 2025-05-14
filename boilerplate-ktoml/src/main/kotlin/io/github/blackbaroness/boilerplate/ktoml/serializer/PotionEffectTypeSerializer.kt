package io.github.blackbaroness.boilerplate.ktoml.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.bukkit.potion.PotionEffectType

class PotionEffectTypeSerializer : KSerializer<PotionEffectType> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("PotionEffectType") {
            element<String>("name")
        }

    override fun deserialize(decoder: Decoder): PotionEffectType {
        val name = decoder.decodeString()
        return PotionEffectType.getByName(name)
            ?: throw IllegalArgumentException("Unknown potion effect type '$name'")
    }

    override fun serialize(encoder: Encoder, value: PotionEffectType) {
        encoder.encodeString(value.name)
    }
}
