package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Optional, use only when needed
object CharStringSerializer : KSerializer<Char> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("${Char::class.qualifiedName}_asString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Char) =
        encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder) =
        decoder.decodeString().single()
}
