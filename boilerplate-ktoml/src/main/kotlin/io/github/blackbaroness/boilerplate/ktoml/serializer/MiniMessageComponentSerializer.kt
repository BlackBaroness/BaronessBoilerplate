package io.github.blackbaroness.boilerplate.ktoml.serializer


import io.github.blackbaroness.boilerplate.adventure.parseMiniMessage
import io.github.blackbaroness.boilerplate.ktoml.type.MiniMessageComponent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class MiniMessageComponentSerializer : KSerializer<MiniMessageComponent> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("MiniMessageComponent", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): MiniMessageComponent {
        val raw = decoder.decodeString()
        return MiniMessageComponent(raw, raw.parseMiniMessage())
    }

    override fun serialize(encoder: Encoder, value: MiniMessageComponent) {
        encoder.encodeString(value.originalString)
    }
}
