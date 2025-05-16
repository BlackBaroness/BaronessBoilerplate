package io.github.blackbaroness.boilerplate.base

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

class PluginMessagePayload(
    val className: String,
    val isMessageCompressed: Boolean,
    val message: ByteArray,
)

fun PluginMessagePayload.toByteArray(): ByteArray {
    val output = ByteArrayOutputStream()
    DataOutputStream(output).use { out ->
        out.writeUTF(className)
        out.writeBoolean(isMessageCompressed)
        out.writeInt(message.size)
        out.write(message)
    }
    return output.toByteArray()
}

fun ByteArray.toPluginMessagePayload(): PluginMessagePayload = DataInputStream(ByteArrayInputStream(this)).use {
    PluginMessagePayload(
        it.readUTF(),
        it.readBoolean(),
        it.readNBytes(it.readInt())
    )
}
