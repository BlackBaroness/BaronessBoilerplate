package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer.keyed

import io.github.blackbaroness.boilerplate.paper.asMinimalString
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Sound

object SoundSerializer : KeyedSerializer<Sound>() {

    override val clazz = Sound::class

    override fun resolveEntityFromKey(key: NamespacedKey): Sound {
        return Registry.SOUNDS.get(key)
            ?: throw IllegalArgumentException("Unknown sound '${key.asMinimalString}'")
    }
}
