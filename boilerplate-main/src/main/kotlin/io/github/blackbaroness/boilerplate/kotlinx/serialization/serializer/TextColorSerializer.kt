package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.format.TextColor

class TextColorSerializer(val compact: Boolean) : KSerializer<TextColor> {

    override val descriptor =
        PrimitiveSerialDescriptor(TextColor::class.qualifiedName!!, PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: TextColor) {
        if (compact) {
            encoder.encodeInt(value.value())
        } else {
            encoder.encodeString(value.asHexString())
        }
    }

    override fun deserialize(decoder: Decoder): TextColor {
        return if (compact) {
            TextColor.color(decoder.decodeInt())
        } else {
            val hex = decoder.decodeString()
            TextColor.fromHexString(hex) ?: throw IllegalArgumentException("Invalid color HEX: '$hex'")
        }
    }
}
