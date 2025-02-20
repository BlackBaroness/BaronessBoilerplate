package io.github.blackbaroness.boilerplate.bungeecord

import com.github.shynixn.mccoroutine.bungeecord.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.md_5.bungee.api.ServerConnectRequest
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.event.AsyncEvent
import net.md_5.bungee.api.plugin.Cancellable
import net.md_5.bungee.api.plugin.Event
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import java.io.Closeable
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

inline fun <reified T : Event> Plugin.eventListener(
    attached: Boolean,
    priority: Byte = EventPriority.NORMAL,
    ignoreCancelled: Boolean = true,
    autoHandleIntents: Boolean = true,
    crossinline action: suspend (T) -> Unit,
) = generateEventListener(this, T::class, priority = priority) { event ->
    if (ignoreCancelled && event is Cancellable && event.isCancelled) return@generateEventListener

    if (!attached) {
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
