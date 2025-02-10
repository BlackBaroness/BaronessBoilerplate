@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.paper

import io.github.blackbaroness.boilerplate.base.Boilerplate
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles

internal var bukkitAudiences: Any? = null

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

fun Boilerplate.initializeAdventure(plugin: Plugin) {
    if (bukkitAudiences != null) {
        // Already initialized
        return
    }

    if (isNativeAdventureApiAvailable) {
        // No need to initialize since we can use the native API
        plugin.slF4JLogger.info("Using the native Adventure")
        return
    }

    plugin.slF4JLogger.info("Using the embedded Adventure")
    bukkitAudiences = BukkitAudiences.create(plugin)
}

fun Boilerplate.destroyAdventure() {
    bukkitAudiences?.let { it as BukkitAudiences }?.close()
    bukkitAudiences = null
}

val Boilerplate.methodMaterialGetDefaultAttributeModifiers: MethodHandle? by lazy {
    try {
        return@lazy MethodHandles.lookup().unreflect(
            Material::class.java.getDeclaredMethod("getDefaultAttributeModifiers")
        )
    } catch (e: NoSuchMethodException) {
        return@lazy null
    }
}

