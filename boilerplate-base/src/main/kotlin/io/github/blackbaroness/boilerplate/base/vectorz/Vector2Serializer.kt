package io.github.blackbaroness.boilerplate.base.vectorz

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import mikera.vectorz.Vector2

class Vector2Serializer : KSerializer<Vector2> {

    override val descriptor = SerialDescriptor(Vector2::class.qualifiedName!!, Vector2Surrogate.serializer().descriptor)

    override fun serialize(encoder: Encoder, value: Vector2) {
        val surrogate = Vector2Surrogate(value.x, value.y)
        encoder.encodeSerializableValue(Vector2Surrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Vector2 {
        val surrogate = decoder.decodeSerializableValue(Vector2Surrogate.serializer())
        return Vector2(surrogate.x, surrogate.y)
    }
}

@Serializable
@SerialName("Vector2")
private class Vector2Surrogate(val x: Double, val y: Double)
