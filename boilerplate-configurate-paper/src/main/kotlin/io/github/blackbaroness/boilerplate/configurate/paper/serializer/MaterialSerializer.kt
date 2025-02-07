package io.github.blackbaroness.boilerplate.configurate.paper.serializer

import io.github.blackbaroness.boilerplate.paper.asMinimalString
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry

class MaterialSerializer : KeyedSerializer<Material>() {

    override fun resolveEntityFromKey(key: NamespacedKey): Material {
        return Registry.MATERIAL.get(key)
            ?: throw IllegalArgumentException("Unknown material '${key.asMinimalString}'")
    }
}
