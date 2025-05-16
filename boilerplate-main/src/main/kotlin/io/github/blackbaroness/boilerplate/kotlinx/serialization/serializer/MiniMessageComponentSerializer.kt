package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer

import io.github.blackbaroness.boilerplate.adventure.parseMiniMessage
import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.MiniMessageComponent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MiniMessageComponentSerializer : KSerializer<MiniMessageComponent> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(MiniMessageComponent::class.java.name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: MiniMessageComponent) =
        encoder.encodeString(value.originalString)

    override fun deserialize(decoder: Decoder): MiniMessageComponent =
        decoder.decodeString().let { MiniMessageComponent(it, it.parseMiniMessage()) }
}
