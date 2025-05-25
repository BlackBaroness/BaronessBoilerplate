package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.UUID

object UUIDSerializer : KSerializer<UUID> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(UUID::class.qualifiedName!!, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}
