package io.github.blackbaroness.boilerplates.configurate.adventure

import io.github.blackbaroness.boilerplates.adventure.parseMiniMessage
import io.github.blackbaroness.boilerplates.adventure.replace
import io.github.blackbaroness.boilerplates.configurate.adventure.type.MiniMessageComponent
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

val String.asMiniMessageComponent: MiniMessageComponent
    get() = MiniMessageComponent(this, parseMiniMessage())

val ComponentLike.asMiniMessageComponent: MiniMessageComponent
    get() {
        if (this is MiniMessageComponent) return this
        return MiniMessageComponent(
            MiniMessage.miniMessage().serialize(asComponent())
                .removePrefix("<!italic><!underlined><!strikethrough><!bold><!obfuscated>"),
            asComponent()
        )
    }

fun Iterable<MiniMessageComponent>.resolve(vararg tagResolver: TagResolver): List<MiniMessageComponent> {
    return map { it.resolve(*tagResolver) }
}

fun Iterable<MiniMessageComponent>.replace(what: String, with: String): List<MiniMessageComponent> {
    return map { it.originalString.replace(what, with).asMiniMessageComponent }
}

fun Iterable<MiniMessageComponent>.replace(what: String, with: ComponentLike): List<MiniMessageComponent> {
    return map { it.replace(what, with).asMiniMessageComponent }
}
