package io.github.blackbaroness.boilerplate.base.vectorz

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import mikera.vectorz.Vector3

class Vector3Serializer : KSerializer<Vector3> {

    override val descriptor = SerialDescriptor(Vector3::class.qualifiedName!!, Vector3Surrogate.serializer().descriptor)

    override fun serialize(encoder: Encoder, value: Vector3) {
        val surrogate = Vector3Surrogate(value.x, value.y, value.z)
        encoder.encodeSerializableValue(Vector3Surrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Vector3 {
        val surrogate = decoder.decodeSerializableValue(Vector3Surrogate.serializer())
        return Vector3(surrogate.x, surrogate.y, surrogate.z)
    }
}

@Serializable
@SerialName("Vector3")
private class Vector3Surrogate(val x: Double, val y: Double, val z: Double)
