package io.github.blackbaroness.boilerplate.broker.impl

import io.github.blackbaroness.boilerplate.broker.ReceivedMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import org.bukkit.Bukkit
import org.redisson.api.RTopic
import org.redisson.api.RedissonClient
import org.redisson.api.listener.MessageListener
import java.util.logging.Level
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
class RedissonMessageBroker(
    private val redisson: RedissonClient,
    private val format: BinaryFormat = Cbor,
    private val debug: Boolean = false,
) : BaseMessageBroker<String>() {

    override suspend fun <MESSAGE : Any> publish(
        topic: String,
        message: MESSAGE,
        messageClass: KClass<MESSAGE>,
        serializer: KSerializer<MESSAGE>?,
    ) {
        val resolvedSerializer = format.findSerializer(messageClass, serializer)
        val encoded = format.encodeToByteArray(resolvedSerializer, message)

        val payload = RedisPayload(
            className = messageClass.qualifiedName
                ?: error("Anonymous classes are not supported"),
            data = encoded,
        )

        val bytes = format.encodeToByteArray(RedisPayload.serializer(), payload)

        logDebug { "Publishing to '$topic': ${messageClass.simpleName}($message)" }
        redisson.getTopic(topic).publish(bytes)
    }

    override fun <MESSAGE : Any> subscribe(
        topic: String,
        messageClass: KClass<MESSAGE>,
        serializer: KSerializer<MESSAGE>?,
    ): Flow<ReceivedMessage<String, MESSAGE>> = callbackFlow {

        val redisTopic: RTopic = redisson.getTopic(topic)

        val listener = MessageListener<ByteArray> { _, raw ->
            try {
                val payload = format.decodeFromByteArray(
                    RedisPayload.serializer(),
                    raw,
                )

                if (payload.className != messageClass.qualifiedName) {
                    logDebug {
                        "Ignored: class mismatch. Received='${payload.className}', expected='${messageClass.qualifiedName}'"
                    }
                    return@MessageListener
                }

                val resolvedSerializer = format.findSerializer(messageClass, serializer)
                val message = format.decodeFromByteArray(
                    resolvedSerializer,
                    payload.data,
                )

                logDebug { "Accepted: topic=$topic, message=$message" }
                trySend(ReceivedMessage(topic, message))
            } catch (e: Throwable) {
                logDebug(e) { "Failed to handle message from '$topic'" }
            }
        }

        val listenerId = redisTopic.addListener(ByteArray::class.java, listener)

        logDebug { "Subscribed to '$topic'" }

        awaitClose {
            logDebug { "Unsubscribed from '$topic'" }
            redisTopic.removeListener(listenerId)
        }
    }

    private inline fun logDebug(
        throwable: Throwable? = null,
        message: () -> String,
    ) {
        if (!debug) return

        val text = "[BROKER] ${message()}"

        runCatching {
            val logger = Bukkit.getServer().logger
            if (throwable != null) {
                logger.log(Level.INFO, text, throwable)
            } else {
                logger.log(Level.INFO, text)
            }
        }.getOrElse {
            if (throwable != null) {
                println("$text :: ${throwable.message}")
                throwable.printStackTrace()
            } else {
                println(text)
            }
        }
    }

    @Serializable
    data class RedisPayload(
        val className: String,
        val data: ByteArray,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as RedisPayload

            if (className != other.className) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = className.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }
}
