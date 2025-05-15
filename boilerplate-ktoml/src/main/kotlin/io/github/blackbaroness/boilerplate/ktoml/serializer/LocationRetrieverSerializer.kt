package io.github.blackbaroness.boilerplate.ktoml.serializer

import io.github.blackbaroness.boilerplate.ktoml.type.LocationRetriever
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class LocationRetrieverSerializer : KSerializer<LocationRetriever> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocationRetriever", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocationRetriever {
        val string = decoder.decodeString()
        return LocationRetriever.fromString(string)
    }

    override fun serialize(encoder: Encoder, value: LocationRetriever) {
        encoder.encodeString(value.toString())
    }
}
