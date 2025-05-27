@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.paper

import io.github.blackbaroness.boilerplate.base.Boilerplate
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.CoroutineContext

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

val Boilerplate.Reflection.material_getDefaultAttributeModifiers: MethodHandle? by lazy {
    Material::class.java.methods
        .firstOrNull { it.name == "getDefaultAttributeModifiers" && it.parameterCount == 0 }
        ?.let { MethodHandles.lookup().unreflect(it) }
        ?.also { logger.info("Detected Material.getDefaultAttributeModifiers()") }
}

val Boilerplate.Reflection.itemMeta_setAttributeModifiers: MethodHandle? by lazy {
    ItemMeta::class.java.methods
        .firstOrNull { it.name == "setAttributeModifiers" && it.parameterCount == 1 }
        ?.let { MethodHandles.lookup().unreflect(it) }
        ?.also { logger.info("Detected ItemMeta.setAttributeModifiers()") }
}

fun Boilerplate.papiTagResolver(player: Player?, selfClosing: Boolean = true) =
    TagResolver.resolver("papi") { argumentQueue, _ ->
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            logger.info("PlaceholderAPI is missing, unable to resolve <papi> placeholders")
            return@resolver Tag.selfClosingInserting(Component.text("PlaceholderAPI is missing"))
        }

        val papiPlaceholder = argumentQueue.popOr("use <papi:placeholder>").value()
        val parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, "%$papiPlaceholder%")
        val componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder)
        if (selfClosing) Tag.selfClosingInserting(componentPlaceholder) else Tag.inserting(componentPlaceholder)
    }

private val logger by lazy { JavaPlugin.getProvidingPlugin(Boilerplate::class.java).slF4JLogger }

private val customEventDispatcherResolvers = CopyOnWriteArrayList<(Event) -> CoroutineContext?>()

fun Boilerplate.registerEventDispatcherResolver(resolver: (Event) -> CoroutineContext?) {
    customEventDispatcherResolvers += resolver
}

fun Boilerplate.getCustomEventDispatcherResolvers(): Collection<(Event) -> CoroutineContext?> {
    return customEventDispatcherResolvers
}

