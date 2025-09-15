package io.github.blackbaroness.boilerplate.paper

import com.github.shynixn.mccoroutine.folia.*
import io.github.blackbaroness.boilerplate.adventure.ExtendedAudience
import io.github.blackbaroness.boilerplate.adventure.asLegacy
import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.base.copyAndClose
import kotlinx.coroutines.CoroutineStart
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.vehicle.VehicleEvent
import org.bukkit.event.weather.WeatherEvent
import org.bukkit.event.world.ChunkEvent
import org.bukkit.event.world.WorldEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.nio.file.Path
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.outputStream

val NamespacedKey.asMinimalString
    get() = if (namespace == NamespacedKey.MINECRAFT) key else asString()

fun Player.playSound(location: Location, sound: Sound) =
    playSound(location, sound, 1f, 1f)

fun Player.playSound(sound: Sound) =
    playSound(location, sound)

fun Location.playSound(sound: Sound) =
    world.playSound(this, sound, 1f, 1f)

fun Listener.unregister() =
    HandlerList.unregisterAll(this)

fun LivingEntity.clearPotionEffects() =
    activePotionEffects.forEach { removePotionEffect(it.type) }

val ItemStack?.isInvalid: Boolean
    get() = this == null || this.type == Material.AIR || this.amount < 1

val ItemStack?.isValid: Boolean
    get() = !isInvalid

fun ItemStack?.validate(): ItemStack? =
    takeIf { it.isValid }

val ComponentLike.asBungeeCordComponents: Array<BaseComponent>
    get() = BungeeComponentSerializer.get().serialize(asComponent())

val Array<BaseComponent>.asAdventureComponent: Component
    get() = BungeeComponentSerializer.get().deserialize(this)

val Color.asAwtColor: java.awt.Color
    get() = java.awt.Color(red, green, blue)

val java.awt.Color.asBukkitColor: Color
    get() = Color.fromRGB(red, green, blue)

val CommandSender.adventure: Audience
    get() = ExtendedAudience(
        if (Boilerplate.isNativeAdventureApiAvailable)
            this
        else if (this is Player)
            bukkitAudiencesSafe.player(this)
        else
            bukkitAudiencesSafe.sender(this)
    )

val Collection<CommandSender>.adventure: Audience
    get() = Audience.audience(map { it.adventure })

@Suppress("DEPRECATION")
fun Player.kick(reason: ComponentLike?) {
    if (Boilerplate.isNativeAdventureApiAvailable) kick(reason?.asComponent()) else kickPlayer(reason?.asLegacy)
}

fun Inventory.toMap(clone: Boolean = true): Map<Int, ItemStack?> {
    return contents.asSequence()
        .mapIndexed { slot, item -> slot to item.validate()?.let { if (clone) it.clone() else it } }
        .toMap()
}

fun LivingEntity.heal(amount: Double = Double.MAX_VALUE) {
    getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value?.let { maxHealth ->
        health = amount.coerceAtMost(maxHealth)
    }
}

fun Player.feed(amount: Int = Int.MAX_VALUE) {
    foodLevel = amount.coerceIn(0, 20)
}

fun Player.giveOrDrop(items: Collection<ItemStack>, allowOthersPickup: Boolean = false, willAge: Boolean = false) {
    inventory.addItem(*items.toTypedArray()).forEach { (_, item) ->
        location.world.dropItem(location, item) { droppedItem ->
            droppedItem.owner = if (allowOthersPickup) null else uniqueId
            droppedItem.setWillAge(willAge)
        }
    }
}

fun Plugin.saveResource(internalPath: String, destination: Path, overwrite: Boolean = false) {
    if (destination.exists() && !overwrite) {
        slF4JLogger.warn("Could not save '$internalPath' to '$destination' because it already exists.")
        return
    }

    destination.deleteIfExists()
    destination.createParentDirectories()

    (getResource(internalPath) ?: throw IllegalArgumentException("Could not find resource '$internalPath'")).use {
        copyAndClose(it, destination.outputStream())
    }
}

/**
 * Any suspending function call will make the event pass.
 * So, if you need to modify the event result, you must use runBlocking or avoid calling suspending functions.
 */
inline fun <reified T : Event> Plugin.eventListener(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline dispatcher: (T) -> CoroutineContext = { findDispatcherForEvent(this, it) },
    crossinline block: suspend (T) -> Unit,
): Listener = generateEventListener<T>(priority = priority, plugin = this) { event ->
    launch(dispatcher.invoke(event), CoroutineStart.UNDISPATCHED) {
        block.invoke(event)
    }
}

inline fun <reified T : Event> generateEventListener(
    plugin: Plugin,
    priority: EventPriority,
    crossinline action: (T) -> Unit,
): Listener {
    val listener = object : Listener {}
    plugin.server.pluginManager.registerEvent(
        T::class.java,
        listener,
        priority,
        { _, event -> if (event is T) action.invoke(event) },
        plugin
    )
    return listener
}

val bukkitAudiencesSafe: BukkitAudiences
    get() = bukkitAudiences as? BukkitAudiences ?: throw IllegalStateException("Adventure is not initialized")

fun <T : Event> findDispatcherForEvent(plugin: Plugin, event: T): CoroutineContext {
    if (!plugin.mcCoroutineConfiguration.isFoliaLoaded) {
        // A path for non-folia is much easier.
        // "plugin.globalRegionDispatcher" is the main thread on non-folia.
        return if (event.isAsynchronous) plugin.asyncDispatcher else plugin.globalRegionDispatcher
    }

    // There are no async events in folia.
    if (event.isAsynchronous) {
        return plugin.globalRegionDispatcher
    }

    // Since each event can be executed on its specific thread, we have no choice other than trying to find it.
    for (resolver in Boilerplate.getCustomEventDispatcherResolvers()) {
        val context = resolver.invoke(event)
        if (context != null) return context
    }

    return when (event) {
        is EntityEvent -> plugin.entityDispatcher(event.entity)
        is VehicleEvent -> plugin.entityDispatcher(event.vehicle)
        is PlayerEvent -> plugin.entityDispatcher(event.player)
        is BlockEvent -> plugin.regionDispatcher(event.block.location)
        is ChunkEvent -> plugin.regionDispatcher(event.world, event.chunk.x, event.chunk.z)
        is InventoryEvent -> plugin.entityDispatcher(event.view.player)
        is WeatherEvent -> plugin.globalRegionDispatcher
        is WorldEvent -> plugin.globalRegionDispatcher
        is MCCoroutineExceptionEvent -> plugin.asyncDispatcher // can be called on different threads, IDK what to do
        else -> throw IllegalStateException("Cannot find dispatcher for ${event::class.simpleName}, override it manually")
    }
}

val UUID.isOfflineUuid get() = version() == 3
