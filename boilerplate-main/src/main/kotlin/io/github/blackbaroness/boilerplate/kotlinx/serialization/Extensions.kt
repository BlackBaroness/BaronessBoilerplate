package io.github.blackbaroness.boilerplate.kotlinx.serialization

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.base.isClassPresent
import io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer.*
import io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer.keyed.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import net.kyori.adventure.text.ComponentLike
import org.bukkit.Bukkit
import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Suppress("UnusedReceiverParameter")
fun Boilerplate.getBuiltInKotlinxSerializers(compact: Boolean): SerializersModule = SerializersModule {
    contextual(LocalTimeSerializer)
    contextual(UUIDSerializer)
    contextual(RegexSerializer)
    contextual(ZoneIdSerializer)
    contextual(LocaleSerializer)
    contextual(IntRangeSerializer)
    contextual(BigIntegerSerializer)
    contextual(BigDecimalSerializer)
    contextual(if (compact) ColorIntSerializer else ColorHexSerializer)
    contextual(if (compact) DurationBinarySerializer else DurationStringSerializer)

    // Adventure support
    if (isClassPresent<ComponentLike>()) {
        contextual(MiniMessageComponentSerializer)
        contextual(if (compact) TextColorIntSerializer else TextColorHexSerializer)
    }

    // Paper support
    if (isClassPresent<Bukkit>()) {
        contextual(AttributeSerializer)
        contextual(AttributeModifierSerializer)
        contextual(EnchantmentSerializer)
        contextual(SoundSerializer)
        contextual(MaterialSerializer)
        contextual(EntityTypeSerializer)
        contextual(PotionEffectTypeSerializer)
        contextual(PotionEffectSerializer)
        contextual(NbtItemSerializer)
    }
}

inline fun <reified T : Any> SerializersModuleBuilder.contextual(serializer: KSerializer<T>) =
    contextual(T::class, serializer)

inline fun <reified T> StringFormat.read(file: Path): T =
    decodeFromString(file.readText())

inline fun <reified T> StringFormat.write(file: Path, value: T) {
    file.createParentDirectories()
    file.writeText(encodeToString(value))
}

inline fun <reified T> StringFormat.update(readFrom: Path, writeTo: Path = readFrom, default: () -> T): T {
    if (readFrom.exists()) {
        val value = read<T>(readFrom)
        write(writeTo, value)
        return value
    }

    write(writeTo, default.invoke())
    return read(writeTo)
}
