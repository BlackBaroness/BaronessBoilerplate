@file:OptIn(ExperimentalSerializationApi::class)

package io.github.blackbaroness.boilerplate

import com.github.shynixn.mccoroutine.folia.launch
import io.github.blackbaroness.boilerplate.base.compressZstd
import io.github.blackbaroness.boilerplate.base.decompressZstd
import io.github.blackbaroness.boilerplate.bungeecord.eventListener
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PluginMessageEvent
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.time.Duration

//
//
// Paper part
//
//

inline fun <reified MESSAGE> Plugin.sendBungeeCordMessage(
    player: Player,
    channel: String,
    message: MESSAGE,
    targetServer: String = "ALL",
    serializer: Cbor = Cbor
) {
    if (!server.messenger.isOutgoingChannelRegistered(this, "BungeeCord")) {
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
    }

    val payload = PluginMessagePayload.fromMessage(message, serializer)
    val payloadEncoded = payload.toByteArray()
    require(payloadEncoded.size <= Short.MAX_VALUE) { "Payload is too large" }

    val packet = ByteArrayOutputStream().use { outer ->
        DataOutputStream(outer).use { out ->
            out.writeUTF("Forward")
            out.writeUTF(targetServer)
            out.writeUTF(channel)
            out.writeShort(payloadEncoded.size)
            out.write(payloadEncoded)
        }
        outer.toByteArray()
    }

    player.sendPluginMessage(this, "BungeeCord", packet)
}

inline fun <reified MESSAGE> Plugin.listenBungeeCordMessages(
    channel: String,
    deserializer: Cbor = Cbor,
    crossinline onReceive: suspend (Player, MESSAGE) -> Unit,
) = server.messenger.registerIncomingPluginChannel(this, channel) { _, player, content ->
    val payload = PluginMessagePayload.fromByteArray(content)
    if (payload.className != MESSAGE::class.qualifiedName)
        return@registerIncomingPluginChannel

    val message = payload.toMessage<MESSAGE>(deserializer)
    launch(Dispatchers.Default, CoroutineStart.UNDISPATCHED) { onReceive(player, message) }
}

suspend inline fun <reified MESSAGE> Plugin.awaitBungeeCordMessage(
    channel: String,
    timeout: Duration,
    deserializer: Cbor = Cbor,
    crossinline filter: suspend (MESSAGE) -> Boolean,
): MESSAGE {
    var registration: PluginMessageListenerRegistration? = null

    return try {
        withTimeout(timeout) {
            val deferred = CompletableDeferred<MESSAGE>()
            registration = listenBungeeCordMessages<MESSAGE>(
                channel = channel,
                deserializer = deserializer
            ) { _, message -> if (filter(message)) deferred.complete(message) }
            deferred.await()
        }
    } finally {
        registration?.also {
            server.messenger.unregisterIncomingPluginChannel(this, it.channel, it.listener)
        }
    }
}

//
//
// BungeeCord part
//
//

inline fun <reified MESSAGE> ProxiedPlayer.sendBungeeCordMessage(
    channel: String,
    message: MESSAGE,
    serializer: Cbor = Cbor
) {
    val payload = PluginMessagePayload.fromMessage(message, serializer)
    val payloadEncoded = payload.toByteArray()
    require(payloadEncoded.size <= Short.MAX_VALUE) { "Payload is too large" }

    val packet = ByteArrayOutputStream().use { outer ->
        DataOutputStream(outer).use { out ->
            out.writeUTF("ForwardToPlayer")
            out.writeUTF(this.name)
            out.writeUTF(channel)
            out.writeShort(payloadEncoded.size)
            out.write(payloadEncoded)
        }
        outer.toByteArray()
    }

    this.sendData("BungeeCord", packet)
}

inline fun <reified MESSAGE> net.md_5.bungee.api.plugin.Plugin.listenBungeeCordMessages(
    channel: String,
    deserializer: Cbor = Cbor,
    crossinline onReceive: suspend (Connection, MESSAGE) -> Unit,
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
    val payload = PluginMessagePayload.fromByteArray(payloadBytes)

    // check if it's a correct message type
    if (payload.className != MESSAGE::class.qualifiedName)
        return@eventListener

    onReceive(event.sender, payload.toMessage(deserializer))
}

class PluginMessagePayload(
    val className: String,
    val isMessageCompressed: Boolean,
    val message: ByteArray,
) {

    fun toByteArray(): ByteArray {
        val output = ByteArrayOutputStream()
        DataOutputStream(output).use { out ->
            out.writeUTF(className)
            out.writeBoolean(isMessageCompressed)
            out.write(message)
        }
        return output.toByteArray()
    }

    inline fun <reified MESSAGE> toMessage(deserializer: Cbor): MESSAGE {
        require(className == MESSAGE::class.qualifiedName) {
            "Invalid class: expected ${MESSAGE::class.qualifiedName}, got $className"
        }

        val raw = if (isMessageCompressed) {
            message.decompressZstd()
        } else {
            message
        }

        return deserializer.decodeFromByteArray(raw)
    }

    companion object {

        fun fromByteArray(array: ByteArray) = DataInputStream(array.inputStream()).use { stream ->
            PluginMessagePayload(
                stream.readUTF(),
                stream.readBoolean(),
                stream.readNBytes(stream.available())
            )
        }

        inline fun <reified T> fromMessage(message: T, serializer: Cbor): PluginMessagePayload {
            val raw = serializer.encodeToByteArray(message)
            val compressed = raw.compressZstd()

            return PluginMessagePayload(
                className = T::class.qualifiedName!!,
                isMessageCompressed = compressed != null,
                message = compressed ?: raw
            )
        }
    }
}
