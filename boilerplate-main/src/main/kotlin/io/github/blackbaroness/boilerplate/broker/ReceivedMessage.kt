package io.github.blackbaroness.boilerplate.broker

data class ReceivedMessage<TRANSPORT, MESSAGE>(
    val transport: TRANSPORT,
    val message: MESSAGE,
)
