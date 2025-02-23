@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.adventure

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.base.format
import io.github.blackbaroness.boilerplate.base.truncate
import io.github.blackbaroness.durationserializer.DurationFormats
import io.github.blackbaroness.durationserializer.format.DurationFormat
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor

fun Boilerplate.tagResolver(name: String, value: String): TagResolver =
    Placeholder.unparsed(name, value)

fun Boilerplate.tagResolver(name: String, value: ComponentLike): TagResolver =
    Placeholder.component(name, value)

fun Boilerplate.tagResolver(name: String, value: Char): TagResolver =
    tagResolver(name, value.toString())

fun Boilerplate.tagResolver(
    name: String,
    value: Number,
    format: DecimalFormat = defaultDecimalFormat.get()
): TagResolver = tagResolver(name, format.format(value))

fun Boilerplate.tagResolver(
    name: String,
    value: Duration,
    accuracy: ChronoUnit = ChronoUnit.SECONDS,
    format: DurationFormat = DurationFormats.mediumLengthRussian()
): TagResolver = tagResolver(name, value.truncate(accuracy).format(format))

fun Boilerplate.tagResolver(
    name: String,
    value: TemporalAccessor,
    nice: Boolean = true
): TagResolver = tagResolver(name, value, if (nice) niceDateFormatter else shortDateFormatter)

fun Boilerplate.tagResolver(
    name: String,
    value: TemporalAccessor,
    formatter: DateTimeFormatter
): TagResolver = tagResolver(name, formatter.format(value))

fun Boilerplate.papiTagResolver(
    player: Player?,
    autoClose: Boolean = false
): TagResolver =
    TagResolver.resolver("papi") { arguments, _ ->
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null)
            throw IllegalStateException("The PlaceholderAPI is missing please install it")

        val placeholder = arguments.popOr("PAPI placeholder is missing").value()
        val resultRaw = PlaceholderAPI.setPlaceholders(player, "%$placeholder%")
        val resultComponent = resultRaw.parseMiniMessage()

        if (autoClose) Tag.selfClosingInserting(resultComponent) else Tag.inserting(resultComponent)
    }
