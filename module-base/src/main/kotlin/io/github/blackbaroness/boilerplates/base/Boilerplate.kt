@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplates.base

import com.google.inject.assistedinject.FactoryModuleBuilder
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

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
}

inline fun <reified T> Boilerplate.createAssistedFactory() =
    FactoryModuleBuilder().build(T::class.java)