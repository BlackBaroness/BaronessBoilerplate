package io.github.blackbaroness.boilerplate.configurate.serializer

import io.github.blackbaroness.boilerplate.paper.asMinimalString
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.entity.EntityType

class EntityTypeSerializer : KeyedSerializer<EntityType>() {

    override fun resolveEntityFromKey(key: NamespacedKey): EntityType {
        return Registry.ENTITY_TYPE.get(key)
            ?: throw IllegalArgumentException("Unknown entity type '${key.asMinimalString}'")
    }
}
