package io.github.blackbaroness.boilerplate.ktoml.serializer

import io.github.blackbaroness.boilerplate.base.asMinecraftTicks
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

class PotionEffectSerializer : KSerializer<PotionEffect> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("PotionEffect") {
            element<String>("type")
            element<Int>("duration")
            element<Int>("amplifier")
            element<Boolean>("ambient")
            element<Boolean>("particles")
            element<Boolean>("icon")
        }

    override fun deserialize(decoder: Decoder): PotionEffect {
        val input = decoder.beginStructure(descriptor)

        var type: PotionEffectType? = null
        var durationInSeconds: Long = 0
        var amplifier = 1
        var ambient = true
        var particles = true
        var icon = true

        loop@ while (true) {
            when (val index = input.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                0 -> type = PotionEffectType.getByName(input.decodeStringElement(descriptor, index))
                1 -> durationInSeconds = input.decodeLongElement(descriptor, index)
                2 -> amplifier = input.decodeIntElement(descriptor, index)
                3 -> ambient = input.decodeBooleanElement(descriptor, index)
                4 -> particles = input.decodeBooleanElement(descriptor, index)
                5 -> icon = input.decodeBooleanElement(descriptor, index)
                else -> throw SerializationException("Unknown index $index")
            }
        }

        input.endStructure(descriptor)

        val duration = Duration.ofSeconds(durationInSeconds)
        return PotionEffect(
            type ?: throw IllegalArgumentException("PotionEffect type is missing"),
            duration.asMinecraftTicks.toInt(),
            amplifier,
            ambient,
            particles,
            icon
        )
    }

    override fun serialize(encoder: Encoder, value: PotionEffect) {
        val output = encoder.beginStructure(descriptor)

        output.encodeStringElement(descriptor, 0, value.type.name)
        output.encodeLongElement(descriptor, 1, value.duration.toLong())
        output.encodeIntElement(descriptor, 2, value.amplifier)
        output.encodeBooleanElement(descriptor, 3, value.isAmbient)
        output.encodeBooleanElement(descriptor, 4, value.hasParticles())
        output.encodeBooleanElement(descriptor, 5, value.hasIcon())

        output.endStructure(descriptor)
    }
}
