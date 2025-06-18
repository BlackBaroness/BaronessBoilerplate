package io.github.blackbaroness.boilerplate.broker

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.time.Duration

interface MessageBroker<TRANSPORT> {

    suspend fun <MESSAGE : Any> publish(
        topic: String,
        message: MESSAGE,
        messageClass: KClass<MESSAGE>,
        serializer: KSerializer<MESSAGE>? = null,
    )

    fun <MESSAGE : Any> subscribe(
        topic: String,
        messageClass: KClass<MESSAGE>,
        serializer: KSerializer<MESSAGE>? = null,
    ): Flow<ReceivedMessage<TRANSPORT, MESSAGE>>

    @Throws(TimeoutCancellationException::class)
    suspend fun <MESSAGE : Any> await(
        topic: String,
        messageClass: KClass<MESSAGE>,
        timeout: Duration,
        serializer: KSerializer<MESSAGE>? = null,
        filter: suspend (ReceivedMessage<TRANSPORT, MESSAGE>) -> Boolean,
    ): ReceivedMessage<TRANSPORT, MESSAGE>
}

suspend inline fun <reified MESSAGE : Any> MessageBroker<*>.publish(
    topic: String,
    message: MESSAGE,
) = publish(topic, message, MESSAGE::class, serializer<MESSAGE>())

inline fun <reified MESSAGE : Any> MessageBroker<*>.subscribe(
    topic: String,
) = subscribe(topic, MESSAGE::class, serializer<MESSAGE>())

@Throws(TimeoutCancellationException::class)
suspend inline fun <TRANSPORT, reified MESSAGE : Any> MessageBroker<TRANSPORT>.await(
    topic: String,
    timeout: Duration,
    noinline filter: suspend (ReceivedMessage<TRANSPORT, MESSAGE>) -> Boolean,
): ReceivedMessage<TRANSPORT, MESSAGE> = await(topic, MESSAGE::class, timeout, serializer<MESSAGE>(), filter)
