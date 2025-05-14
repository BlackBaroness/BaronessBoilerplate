package io.github.blackbaroness.boilerplate.ktoml

import com.akuleshov7.ktoml.Toml
import io.github.blackbaroness.boilerplate.adventure.parseMiniMessage
import io.github.blackbaroness.boilerplate.adventure.replace
import io.github.blackbaroness.boilerplate.ktoml.type.LocationRetriever
import io.github.blackbaroness.boilerplate.ktoml.type.MiniMessageComponent
import kotlinx.serialization.serializer
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Location
import java.nio.file.Files
import java.nio.file.Path

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

inline fun <reified T> loadOrSaveDefault(path: Path, toml: Toml, defaultValue: T): T {
    if (!Files.exists(path)) {
        saveTomlFile(path, defaultValue, toml)
        return defaultValue
    }

    val content = Files.readString(path).trim()
    if (content.isBlank()) {
        saveTomlFile(path, defaultValue, toml)
        return defaultValue
    }

    return try {
        toml.decodeFromString(serializer(), content)
    } catch (_: Exception) {
        saveTomlFile(path, defaultValue, toml)
        defaultValue
    }
}

inline fun <reified T> saveTomlFile(path: Path, value: T, toml: Toml) {
    val string = toml.encodeToString(serializer(), value)
    Files.writeString(path, string)
}
