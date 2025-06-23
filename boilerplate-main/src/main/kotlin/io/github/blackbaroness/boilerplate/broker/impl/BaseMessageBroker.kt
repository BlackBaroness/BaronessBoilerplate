package io.github.blackbaroness.boilerplate.broker.impl

import io.github.blackbaroness.boilerplate.broker.MessageBroker
import io.github.blackbaroness.boilerplate.broker.ReceivedMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialFormat
import kotlin.reflect.KClass
import kotlin.time.Duration

abstract class BaseMessageBroker<TRANSPORT> : MessageBroker<TRANSPORT> {

    override suspend fun <MESSAGE : Any> await(
        topic: String,
        messageClass: KClass<MESSAGE>,
        timeout: Duration,
        serializer: KSerializer<MESSAGE>?,
        filter: suspend (ReceivedMessage<TRANSPORT, MESSAGE>) -> Boolean,
    ): ReceivedMessage<TRANSPORT, MESSAGE> = withTimeout(timeout) {
        subscribe(topic, messageClass, serializer)
            .first { filter(it) }
    }

    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class)
    protected fun <T : Any> SerialFormat.findSerializer(clazz: KClass<T>, exiting: KSerializer<T>?): KSerializer<T> {
        if (exiting != null) return exiting
        return serializersModule.getContextual(clazz) ?: error("No serializer for ${clazz.qualifiedName} was set")
    }
}
