package io.github.blackbaroness.boilerplate.configurate

import io.github.blackbaroness.boilerplate.adventure.parseMiniMessage
import io.github.blackbaroness.boilerplate.adventure.replace
import io.github.blackbaroness.boilerplate.configurate.type.LocationRetriever
import io.github.blackbaroness.boilerplate.configurate.type.MiniMessageComponent
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Location
import org.spongepowered.configurate.loader.ConfigurationLoader
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

inline fun <reified T> TypeSerializerCollection.Builder.register(
    serializer: TypeSerializer<T>
): TypeSerializerCollection.Builder = register(T::class.java, serializer)

inline fun <reified T : Any> ConfigurationLoader<*>.loadAndSave(): T {
    val obj = this.load().get(T::class.java)!!
    this.save(this.createNode().set(T::class.java, obj))
    return obj
}

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

fun Location.toLocationRetriever(): LocationRetriever {
    return LocationRetriever(
        this.world!!.name,
        this.x,
        this.y,
        this.z,
        this.yaw.takeIf { it != 0f },
        this.pitch.takeIf { it != 0f }
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

