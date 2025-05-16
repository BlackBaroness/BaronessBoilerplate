package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer.keyed

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.paper.asMinimalString
import io.github.blackbaroness.boilerplate.paper.resolveNamespacedKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import kotlin.reflect.KClass

abstract class KeyedSerializer<T : Keyed> : KSerializer<T> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(clazz.qualifiedName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): T {
        val string = decoder.decodeString()
        val key = Boilerplate.resolveNamespacedKey(string)
            ?: throw IllegalArgumentException("Invalid key '$string'")

        return resolveEntityFromKey(key)
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.key.asMinimalString)
    }

    protected abstract val clazz: KClass<T>

    abstract fun resolveEntityFromKey(key: NamespacedKey): T
}
