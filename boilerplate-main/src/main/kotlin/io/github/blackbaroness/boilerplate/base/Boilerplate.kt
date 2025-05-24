@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.base

import com.google.inject.Module
import com.google.inject.assistedinject.FactoryModuleBuilder
import org.rocksdb.*
import java.nio.file.Path
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.time.Duration

object Boilerplate {

    val russianLocale: Locale = Locale.forLanguageTag("ru-RU")

    val defaultDecimalFormat = ThreadLocal.withInitial {
        val symbols = DecimalFormatSymbols(russianLocale)
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'
        DecimalFormat("#,###.##", symbols)
    }

    val niceDateFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("HH:mm z (d MMMM yyyy)", russianLocale)
        .withLocale(russianLocale)
        .withZone(ZoneId.systemDefault())

    val shortDateFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("HH:mm d.MM.yyyy z", russianLocale)
        .withLocale(russianLocale)
        .withZone(ZoneId.systemDefault())

    object Reflection
}

inline fun <reified T> Boilerplate.createAssistedFactory(): Module =
    FactoryModuleBuilder().build(T::class.java)

fun Boilerplate.createRocksDbRepository(
    dir: Path,
    timeToLive: Duration? = null,
    compressionType: CompressionType = CompressionType.ZSTD_COMPRESSION,
    columnFamilyDescriptors: List<ColumnFamilyDescriptor>? = null,
    columnFamilyHandles: List<ColumnFamilyHandle>? = null
): TtlDB {
    dir.createDirectories()
    RocksDB.loadLibrary()

    val options = Options().apply {
        setCreateIfMissing(true)
        setCreateMissingColumnFamilies(true)
        setCompressionType(compressionType)
    }

    val bothAreProvided = columnFamilyDescriptors != null && columnFamilyHandles != null
    val neitherIsProvided = columnFamilyDescriptors == null && columnFamilyHandles == null

    require(bothAreProvided || neitherIsProvided) {
        "You must either provide both columnFamilyDescriptors and columnFamilyHandles, or neither."
    }

    val ttlSeconds = timeToLive?.inWholeSeconds?.toInt() ?: 0
    return if (neitherIsProvided) {
        TtlDB.open(options, dir.absolutePathString(), ttlSeconds, false)
    } else {
        val dbOptions = DBOptions(options)
        TtlDB.open(
            dbOptions,
            dir.absolutePathString(),
            columnFamilyDescriptors,
            columnFamilyHandles,
            columnFamilyDescriptors!!.map { ttlSeconds },
            false
        )
    }
}
