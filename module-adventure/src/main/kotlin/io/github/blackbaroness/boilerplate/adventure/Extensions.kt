package io.github.blackbaroness.boilerplate.adventure

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.awt.Color

fun ComponentLike.replace(what: String, with: String): Component =
    asComponent().replaceText { builder -> builder.matchLiteral(what).replacement(with) }

fun ComponentLike.replace(what: String, with: ComponentLike): ComponentLike =
    asComponent().replaceText { builder -> builder.matchLiteral(what).replacement(with) }

val ComponentLike.asLegacy: String
    get() = LegacyComponentSerializer.legacySection().serialize(asComponent())

val ComponentLike.asPlain: String
    get() = PlainTextComponentSerializer.plainText().serialize(asComponent())

val Component.asJson: String
    get() = GsonComponentSerializer.gson().serialize(this)

fun String.parseJsonToComponent(): Component =
    GsonComponentSerializer.gson().deserialize(this)

val TextColor.asJwtColor: Color
    get() = Color(red(), green(), blue())

val Color.asTextColor: TextColor
    get() = TextColor.color(red, green, blue)

fun Audience.sendMessage(unparsed: String, vararg tagResolvers: TagResolver): Unit =
    sendMessage(unparsed.parseMiniMessage(*tagResolvers))

fun Audience.sendMessage(unparsed: String, tagResolvers: Iterable<TagResolver>) =
    sendMessage(unparsed, *tagResolvers.toList().toTypedArray())

fun Audience.sendMessage(unparsed: String, tagResolvers: Collection<TagResolver>) =
    sendMessage(unparsed, *tagResolvers.toTypedArray())

fun Audience.sendActionBar(unparsed: String, vararg tagResolvers: TagResolver): Unit =
    sendActionBar(unparsed.parseMiniMessage(*tagResolvers))

fun Audience.sendActionBar(unparsed: String, tagResolvers: Iterable<TagResolver>) =
    sendActionBar(unparsed, *tagResolvers.toList().toTypedArray())

fun Audience.sendActionBar(unparsed: String, tagResolvers: Collection<TagResolver>) =
    sendActionBar(unparsed, *tagResolvers.toTypedArray())

fun List<TextColor>.createMiniMessageGradient() = when (size) {
    0 -> ""
    1 -> "<color:${single().asHexString()}>"
    else -> "<gradient:${joinToString(separator = ":") { it.asHexString() }}>"
}

fun Component.apply(characterAndFormat: CharacterAndFormat) = when (val format = characterAndFormat.format()) {
    is TextDecoration -> decorate(format)
    is TextColor -> color(format)
    else -> throw IllegalArgumentException("Unsupported: $characterAndFormat")
}

fun String.parseMiniMessage(vararg tagResolvers: TagResolver): Component {
    if (isEmpty()) return Component.empty()
    return MiniMessage.miniMessage().deserialize(this, *tagResolvers)
        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

fun String.parseMiniMessage(tagResolvers: Iterable<TagResolver>) =
    parseMiniMessage(*tagResolvers.toList().toTypedArray())

fun String.parseMiniMessage(tagResolvers: Collection<TagResolver>) =
    parseMiniMessage(*tagResolvers.toTypedArray())
