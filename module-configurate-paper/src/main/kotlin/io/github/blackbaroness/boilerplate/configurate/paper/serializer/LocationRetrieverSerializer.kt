package io.github.blackbaroness.boilerplate.configurate.paper.serializer

import io.github.blackbaroness.boilerplate.configurate.paper.type.LocationRetriever
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

class LocationRetrieverSerializer : TypeSerializer<LocationRetriever> {

    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): LocationRetriever? {
        val string = node.string ?: return null
        return LocationRetriever.fromString(string)
    }

    override fun serialize(
        type: Type,
        obj: LocationRetriever?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.set(obj.toString())
    }
}
