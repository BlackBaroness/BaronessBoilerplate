package io.github.blackbaroness.boilerplate.bungeecord

import com.github.shynixn.mccoroutine.bungeecord.launch
import io.github.blackbaroness.boilerplate.adventure.ExtendedAudience
import io.github.blackbaroness.boilerplate.base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ServerConnectRequest
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.AsyncEvent
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Event
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import java.io.*
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Modifier
import kotlin.random.Random
import kotlin.reflect.KClass

val ComponentLike.asBungeeCordComponents: Array<BaseComponent>
    get() = BungeeComponentSerializer.get().serialize(asComponent())

val Array<BaseComponent>.asAdventureComponent: Component
    get() = BungeeComponentSerializer.get().deserialize(this)

val Array<BaseComponent>.asSingleComponent: BaseComponent
    get() = TextComponent.fromArray(*this)

fun Connection.disconnect(reason: ComponentLike) =
    disconnect(*reason.asBungeeCordComponents)

val ServerConnectRequest.Result.isConnected
    get() = this == ServerConnectRequest.Result.SUCCESS
        || this == ServerConnectRequest.Result.ALREADY_CONNECTED
        || this == ServerConnectRequest.Result.ALREADY_CONNECTING

// Available only on Waterfall
val ServerConnectRequestBuilder_sendFeedback_setter: MethodHandle? by lazy {
    try {
        ServerConnectRequest.Builder::class.java
            .getDeclaredMethod("sendFeedback", Boolean::class.java)
            .apply { isAccessible = true }
            .let { MethodHandles.lookup().unreflect(it) }
    } catch (e: NoSuchMethodException) {
        null
    }
}

/**
 * {@code attended=false} makes your block run concurrently on {@code Dispatchers.Default}.
 * You cannot change the results of that event in this mode since it's already finished.
 * Your code might be called concurrently from many threads, so you must care about thread-safety if you use that.
 */
inline fun <reified T : Event> Plugin.eventListener(
    attended: Boolean,
    priority: Byte = EventPriority.NORMAL,
    autoHandleIntents: Boolean = true,
    crossinline action: suspend (T) -> Unit,
) = generateEventListener(this, T::class, priority = priority) { event ->
    if (!attended) {
        launch(Dispatchers.Default) { action.invoke(event) }
        return@generateEventListener
    }

    if (autoHandleIntents && event is AsyncEvent<*>) {
        event.registerIntent(this@eventListener)
        launch(Dispatchers.Default) { action.invoke(event) }.invokeOnCompletion {
            event.completeIntent(this@eventListener)
        }
        return@generateEventListener
    }

    runBlocking {
        action.invoke(event)
    }
}

fun <T : Event> generateEventListener(
    plugin: Plugin,
    eventClass: KClass<T>,
    priority: Byte = EventPriority.NORMAL,
    action: (T) -> Unit
): Closeable {
    val delegate = object : Any() {
        @Suppress("unused")
        fun handleEvent(event: T) = action.invoke(event)
    }

    val listener = ByteBuddy()
        .subclass(Listener::class.java)
        .modifiers(Modifier.PUBLIC)
        .name("${plugin::class.java.packageName}.__generated__.Listener_${Random.nextLong(10_000_000)}")
        .defineMethod("handleEvent", Void.TYPE, Modifier.PUBLIC)
        .withParameters(eventClass.java)
        .intercept(MethodDelegation.to(delegate))
        .annotateMethod(EventHandler(priority = priority))
        .make()
        .load(plugin::class.java.classLoader)
        .loaded.getConstructor().newInstance()

    plugin.proxy.pluginManager.registerListener(plugin, listener)
    return Closeable { plugin.proxy.pluginManager.unregisterListener(listener) }
}

val bungeeAudiencesSafe: BungeeAudiences
    get() = bungeeAudiences ?: throw IllegalStateException("Adventure is not initialized")

val CommandSender.adventure: Audience
    get() = ExtendedAudience(bungeeAudiencesSafe.sender(this))

val Collection<CommandSender>.adventure: Audience
    get() = Audience.audience(map { it.adventure })

inline fun <reified MESSAGE> Plugin.sendBungeeCordMessage(
    player: ProxiedPlayer,
    channel: String,
    message: MESSAGE,
    serializer: Json = Json
) {
    // serialize the message
    var messageSerialized = serializer.encodeToString(message).encodeToByteArray()
    var isMessageCompressed = false

    // try to compress the message
    messageSerialized.compressZstd()?.also {
        isMessageCompressed = true
        messageSerialized = it
    }

    val payload = PluginMessagePayload(
        className = MESSAGE::class.qualifiedName!!,
        isMessageCompressed = isMessageCompressed,
        message = messageSerialized,
    )

    val payloadEncoded = payload.toByteArray()
    require(payloadEncoded.size <= Short.MAX_VALUE) { "Payload is too large" }

    val packet = ByteArrayOutputStream().use { outer ->
        DataOutputStream(outer).use { out ->
            out.writeUTF("ForwardToPlayer")
            out.writeUTF(player.name)
            out.writeUTF(channel)
            out.writeShort(payloadEncoded.size)
            out.write(payloadEncoded)
        }
        outer.toByteArray()
    }

    player.sendData("BungeeCord", packet)
}

inline fun <reified MESSAGE> Plugin.listenBungeeCordMessages(
    channel: String,
    crossinline onReceive: suspend (Connection, MESSAGE) -> Unit,
    deserializer: Json = Json
) = eventListener<PluginMessageEvent>(attended = false) { event ->
    if (event.tag != "BungeeCord") return@eventListener

    val input = DataInputStream(ByteArrayInputStream(event.data))
    if (input.readUTF() != "Forward") return@eventListener

    // it's a "targetServer" - we ignore it for now
    input.readUTF()

    // check if it's a correct channel
    if (input.readUTF() != channel) return@eventListener

    // load payload
    val payloadSize = input.readShort().toInt()
    val payloadBytes = input.readNBytes(payloadSize)
    val payload = payloadBytes.toPluginMessagePayload()

    // check if it's a correct message type
    if (payload.className != MESSAGE::class.qualifiedName)
        return@eventListener

    val messageString = if (payload.isMessageCompressed) {
        payload.message.decompressZstd().decodeToString()
    } else {
        payload.message.decodeToString()
    }

    val message = deserializer.decodeFromString<MESSAGE>(messageString)
    onReceive(event.sender, message)
}
