package io.github.blackbaroness.boilerplate.ktoml.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.format.TextColor

class TextColorSerializer : KSerializer<TextColor> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TextColor", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): TextColor {
        val hex = decoder.decodeString()
        return TextColor.fromHexString(hex)
            ?: throw IllegalArgumentException("Invalid HEX color '$hex'")
    }

    override fun serialize(encoder: Encoder, value: TextColor) {
        encoder.encodeString(value.asHexString())
    }
}
