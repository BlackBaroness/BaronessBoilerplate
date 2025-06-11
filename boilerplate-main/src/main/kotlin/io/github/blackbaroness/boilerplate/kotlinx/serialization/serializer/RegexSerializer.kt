package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries

object RegexSerializer : KSerializer<Regex> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(Regex::class.qualifiedName!!, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Regex) =
        encoder.encodeString(value.pattern)

    override fun deserialize(decoder: Decoder): Regex =
        Regex(decoder.decodeString())
}
