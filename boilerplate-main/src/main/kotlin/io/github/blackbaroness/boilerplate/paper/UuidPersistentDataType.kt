package io.github.blackbaroness.boilerplate.paper

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.util.*

object UuidPersistentDataType : PersistentDataType<ByteArray, UUID> {

    override fun getPrimitiveType() = ByteArray::class.java

    override fun getComplexType() = UUID::class.java

    override fun toPrimitive(complex: UUID, context: PersistentDataAdapterContext): ByteArray {
        val array = ByteArray(16)
        putLongBigEndian(array, 0, complex.mostSignificantBits)
        putLongBigEndian(array, 8, complex.leastSignificantBits)
        return array
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): UUID {
        require(primitive.size == 16) { "UUID byte array must be 16 bytes, got ${primitive.size}" }
        return UUID(
            getLongBigEndian(primitive, 0),
            getLongBigEndian(primitive, 8)
        )
    }

    private fun putLongBigEndian(array: ByteArray, offset: Int, value: Long) {
        array[offset + 0] = (value ushr 56).toByte()
        array[offset + 1] = (value ushr 48).toByte()
        array[offset + 2] = (value ushr 40).toByte()
        array[offset + 3] = (value ushr 32).toByte()
        array[offset + 4] = (value ushr 24).toByte()
        array[offset + 5] = (value ushr 16).toByte()
        array[offset + 6] = (value ushr 8).toByte()
        array[offset + 7] = value.toByte()
    }

    private fun getLongBigEndian(array: ByteArray, offset: Int): Long {
        return ((array[offset + 0].toLong() and 0xff) shl 56) or
            ((array[offset + 1].toLong() and 0xff) shl 48) or
            ((array[offset + 2].toLong() and 0xff) shl 40) or
            ((array[offset + 3].toLong() and 0xff) shl 32) or
            ((array[offset + 4].toLong() and 0xff) shl 24) or
            ((array[offset + 5].toLong() and 0xff) shl 16) or
            ((array[offset + 6].toLong() and 0xff) shl 8) or
            ((array[offset + 7].toLong() and 0xff))
    }
}
