package io.github.blackbaroness.boilerplates.configurate.adventure.type

import io.github.blackbaroness.boilerplates.adventure.parseMiniMessage
import io.github.blackbaroness.boilerplates.configurate.adventure.asMiniMessageComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class MiniMessageComponent(
    val originalString: String,
    parsedComponent: Component
) : ComponentLike by parsedComponent {

    fun resolve(vararg tagResolvers: TagResolver): MiniMessageComponent {
        if (tagResolvers.isEmpty()) return this
        return originalString.parseMiniMessage(*tagResolvers).asMiniMessageComponent
    }
}