package io.github.blackbaroness.boilerplate.kotlinx.serialization

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.base.isClassPresent
import io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer.*
import io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer.keyed.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import net.kyori.adventure.text.ComponentLike
import org.bukkit.Bukkit

@Suppress("UnusedReceiverParameter")
fun Boilerplate.getBuiltInKotlinxSerializers(compact: Boolean): SerializersModule = SerializersModule {
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
