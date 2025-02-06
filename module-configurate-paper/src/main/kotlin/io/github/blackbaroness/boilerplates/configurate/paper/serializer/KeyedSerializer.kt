package io.github.blackbaroness.boilerplates.configurate.paper.serializer

import io.github.blackbaroness.boilerplates.base.Boilerplate
import io.github.blackbaroness.boilerplates.paper.asMinimalString
import io.github.blackbaroness.boilerplates.paper.resolveNamespacedKey
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

abstract class KeyedSerializer<T : Keyed> : TypeSerializer<T> {

    override fun deserialize(type: Type, node: ConfigurationNode): T? {
        val string = node.string ?: return null
        val key = Boilerplate.resolveNamespacedKey(string)
            ?: throw IllegalArgumentException("Invalid key '$string'")

        return resolveEntityFromKey(key)
    }

    override fun serialize(type: Type, obj: T?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.set(obj.key.asMinimalString)
    }

    abstract fun resolveEntityFromKey(key: NamespacedKey): T
}