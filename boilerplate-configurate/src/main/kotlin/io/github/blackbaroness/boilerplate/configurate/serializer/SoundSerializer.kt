package io.github.blackbaroness.boilerplate.configurate.serializer

import io.github.blackbaroness.boilerplate.paper.asMinimalString
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Sound

class SoundSerializer : KeyedSerializer<Sound>() {

    override fun resolveEntityFromKey(key: NamespacedKey): Sound {
        return Registry.SOUNDS.get(key)
            ?: throw IllegalArgumentException("Unknown sound '${key.asMinimalString}'")
    }
}
