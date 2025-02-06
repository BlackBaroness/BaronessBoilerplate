package io.github.blackbaroness.boilerplates.configurate.paper.serializer

import io.github.blackbaroness.boilerplates.paper.asMinimalString
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Sound

class SoundSerializer : KeyedSerializer<Sound>() {

    override fun resolveEntityFromKey(key: NamespacedKey): Sound {
        return Registry.SOUNDS.get(key)
            ?: throw IllegalArgumentException("Unknown sound '${key.asMinimalString}'")
    }
}