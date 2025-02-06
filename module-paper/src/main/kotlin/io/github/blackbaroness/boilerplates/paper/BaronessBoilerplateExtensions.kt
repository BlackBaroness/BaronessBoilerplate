@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplates.paper

import io.github.blackbaroness.boilerplates.base.Boilerplate
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

fun Boilerplate.resolveNamespacedKey(input: String): NamespacedKey? =
    if (input.contains(':'))
        NamespacedKey.fromString(input)
    else
        NamespacedKey.minecraft(input)

val Boilerplate.isNativeAdventureApiAvailable by lazy {
    try {
        ItemStack(Material.STONE).editMeta { it.displayName(Component.empty()) }
        return@lazy true
    } catch (e: NoSuchMethodError) {
        return@lazy false
    }
}