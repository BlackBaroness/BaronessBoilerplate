package io.github.blackbaroness.boilerplate.paper

import io.github.blackbaroness.boilerplate.adventure.asLegacy
import io.github.blackbaroness.boilerplate.base.Boilerplate
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.nio.file.Path
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
    get() = if (Boilerplate.isNativeAdventureApiAvailable) this else bukkitAudiencesSafe.sender(this)

val Collection<CommandSender>.adventure: Audience
    get() = Audience.audience(map { it.adventure })

@Suppress("DEPRECATION")
fun Player.kick(reason: ComponentLike?) {
    if (Boilerplate.isNativeAdventureApiAvailable) kick(reason) else kickPlayer(reason?.asLegacy)
}

fun Inventory.toMap(clone: Boolean = true): Map<Int, ItemStack?> {
    return contents.asSequence()
        .mapIndexed { slot, item -> slot to item.validate()?.let { if (clone) it.clone() else it } }
        .toMap()
}

fun Player.giveOrDrop(items: Collection<ItemStack>, pickupProtect: Boolean, willAge: Boolean) {
    inventory.addItem(*items.toTypedArray()).forEach { (_, item) ->
        location.world.dropItem(location, item) { droppedItem ->
            droppedItem.owner = if (pickupProtect) uniqueId else null
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

    getResource(internalPath)?.use { resource ->
        destination.outputStream().use { out -> resource.copyTo(out) }
    } ?: throw IllegalArgumentException("Could not find resource '$internalPath'")
}

inline fun <reified T : Event> Plugin.eventListener(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline block: (T) -> Unit,
): Listener {
    val listener = object : Listener {}

    server.pluginManager.registerEvent(
        T::class.java,
        listener,
        priority,
        { _, event -> if (event is T) block.invoke(event) },
        this
    )

    return listener
}

private val bukkitAudiencesSafe: BukkitAudiences
    get() = bukkitAudiences as? BukkitAudiences ?: throw IllegalStateException("Adventure is not initialized")
