package io.github.blackbaroness.boilerplates.configurate.paper.serializer

import io.github.blackbaroness.boilerplates.paper.asMinimalString
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.entity.EntityType

class EntityTypeSerializer :
    io.github.blackbaroness.boilerplates.configurate.paper.serializer.KeyedSerializer<EntityType>() {

    override fun resolveEntityFromKey(key: NamespacedKey): EntityType {
        return Registry.ENTITY_TYPE.get(key)
            ?: throw IllegalArgumentException("Unknown entity type '${key.asMinimalString}'")
    }
}
