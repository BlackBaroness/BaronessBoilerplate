package io.github.blackbaroness.boilerplate.ktoml.serializer

import io.github.blackbaroness.durationserializer.DurationFormats
import io.github.blackbaroness.durationserializer.DurationSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Duration

class DurationSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Duration", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Duration {
        val string = decoder.decodeString()
        return DurationSerializer.deserialize(string)
    }

    override fun serialize(encoder: Encoder, value: Duration) {
        val string = DurationSerializer.serialize(value, DurationFormats.mediumLengthRussian())
        encoder.encodeString(string)
    }
}
