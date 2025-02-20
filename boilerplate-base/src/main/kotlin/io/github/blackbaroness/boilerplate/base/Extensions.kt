package io.github.blackbaroness.boilerplate.base

import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import java.util.*
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

fun Iterable<String>.insertReplacing(what: String, with: Iterable<String>): List<String> {
    return buildList {
        addAll(this@insertReplacing)

        val insertIndex = this.indexOfFirst { it.contains(what) }
        if (insertIndex != -1) {
            removeAt(insertIndex)
            with.forEachIndexed { index, component -> add(insertIndex + index, component) }
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
