package io.github.blackbaroness.boilerplate.base

import com.github.luben.zstd.Zstd
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*
import kotlin.io.path.inputStream
import kotlin.io.path.listDirectoryEntries

inline fun <reified T> isClassPresent() =
    runCatching { Class.forName(T::class.qualifiedName) }.isSuccess

fun <T> Collection<T>.containsAny(of: Collection<T>): Boolean {
    val set = of.toSet()
    for (element in this) {
        if (set.contains(element)) {
            return true
        }
    }
    return false
}

fun <T> Sequence<T>.cycle() = sequence {
    while (true) yieldAll(this@cycle)
}

fun Iterable<String>.bulkReplace(what: String, with: String): List<String> {
    return this.map { it.replace(what, with) }
}

fun Iterable<String>.insertReplacing(what: String, with: Iterable<String>): List<String> = buildList {
    for (originalString in this@insertReplacing) {
        if (what in originalString) {
            addAll(with)
        } else {
            add(originalString)
        }
    }
}

fun <KEY, VALUE> Sequence<Map.Entry<KEY, Iterable<VALUE>>>.flattenValues(): Sequence<Pair<KEY, VALUE>> {
    return flatMap { (key, value) -> value.map { key to it } }
}

fun Throwable.rootCause(): Throwable {
    var cause = this.cause ?: return this


    while (true) {
        cause = cause.cause ?: return cause
    }
}

inline fun <T> Iterable<T>.forEachOther(action: (T, T) -> Unit) =
    forEach { t1 -> forEach { t2 -> action(t1, t2) } }

fun <K, V, R> Map<K, V?>.mapNotNullValues(transform: (K, V) -> R): Map<K, R> {
    return this
        .filterValues { it != null }
        .mapValues { transform(it.key, it.value!!) }
}

fun Path.findSingleFile(glob: String = "*"): Path {
    val entries = listDirectoryEntries(glob)
    when (entries.size) {
        0 -> throw IllegalStateException("$this contains no file matching glob '$glob'")
        1 -> return entries.first()
        else -> throw IllegalStateException("$this contains more than one file matching glob '$glob'")
    }
}

fun String?.toUuidOrNull(): UUID? {
    if (this == null) return null

    return try {
        UUID.fromString(this)
    } catch (_: IllegalArgumentException) {
        null
    }
}

fun copyAndClose(from: InputStream, to: OutputStream) =
    from.use { to.use { from.copyTo(to) } }

val ipv4AddressRegex by lazy {
    Regex("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}\$")
}

val String.isValidIpv4Address: Boolean
    get() = matches(ipv4AddressRegex)

fun ByteBuffer.putUuid(uuid: UUID): ByteBuffer {
    putLong(uuid.mostSignificantBits)
    putLong(uuid.leastSignificantBits)
    return this
}

fun UUID.toBytes(): ByteArray = ByteBuffer.allocate(Long.SIZE_BYTES * 2).putUuid(this).array()

fun ByteArray.compressZstd(level: Int = 1, minimumLength: Int = 1500): ByteArray? {
    if (minimumLength > this.size) {
        // the input is too small, no need to compress
        return null
    }

    val compressed = Zstd.compress(this, level)
    if (compressed.size > this.size) {
        // compression made the array bigger
        return null
    }

    // we need to store the original size as 4 extra bytes to decompress later
    val packed = ByteBuffer.allocate(Int.SIZE_BYTES + compressed.size)
        .putInt(this.size)
        .put(compressed)
        .array()

    if (packed.size > this.size) {
        // that extra header made our array bigger than the original one
        return null
    }

    return packed
}

fun ByteArray.decompressZstd(): ByteArray {
    require(this.size >= Int.SIZE_BYTES) { "Invalid compressed format: too short" }

    val buffer = ByteBuffer.wrap(this)
    val length = buffer.int
    val packed = ByteArray(buffer.remaining())
    buffer.get(packed)
    return Zstd.decompress(packed, length)
}

inline fun Path.useChunks(chunkSize: Int, action: (ByteBuffer) -> Unit) {
    require(chunkSize >= 1)

    // If the chunk size is small (less than 1 MB), we better use a buffered stream to avoid too many I/O calls
    val input = if (chunkSize < DEFAULT_BUFFER_SIZE * 128) {
        Channels.newChannel(inputStream().buffered())
    } else {
        FileChannel.open(this, StandardOpenOption.READ)
    }

    input.use { channel ->
        val buffer = ByteBuffer.allocate(chunkSize)

        while (channel.read(buffer) != -1) {
            buffer.flip()

            val currentChunkSize = buffer.remaining()
            val chunk = if (currentChunkSize == buffer.capacity()) {
                buffer
            } else {
                val partialBuffer = ByteBuffer.allocate(currentChunkSize)
                partialBuffer.put(buffer)
                partialBuffer.flip()
                partialBuffer
            }

            action(chunk)
            buffer.clear()
        }
    }
}
