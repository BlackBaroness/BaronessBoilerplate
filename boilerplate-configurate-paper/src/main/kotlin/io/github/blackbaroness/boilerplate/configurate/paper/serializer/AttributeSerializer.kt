package io.github.blackbaroness.boilerplate.configurate.paper.serializer

import io.github.blackbaroness.boilerplate.paper.asMinimalString
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.Attribute

class AttributeSerializer : KeyedSerializer<Attribute>() {

    override fun resolveEntityFromKey(key: NamespacedKey): Attribute {
        return Registry.ATTRIBUTE.get(key)
            ?: throw IllegalArgumentException("Unknown attribute '${key.asMinimalString}'")
    }
}
