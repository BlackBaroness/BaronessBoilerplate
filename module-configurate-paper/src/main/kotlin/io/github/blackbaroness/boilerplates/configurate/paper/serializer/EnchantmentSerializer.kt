package io.github.blackbaroness.boilerplates.configurate.paper.serializer

import io.github.blackbaroness.boilerplates.paper.asMinimalString
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment

class EnchantmentSerializer : KeyedSerializer<Enchantment>() {

    override fun resolveEntityFromKey(key: NamespacedKey): Enchantment {
        return Registry.ENCHANTMENT.get(key)
            ?: throw IllegalArgumentException("Unknown enchantment '${key.asMinimalString}'")
    }
}