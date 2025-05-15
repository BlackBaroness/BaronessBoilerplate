@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.ktoml

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.base.isClassPresent
import io.github.blackbaroness.boilerplate.ktoml.serializer.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.kyori.adventure.text.ComponentLike
import org.bukkit.Bukkit

val Boilerplate.defaultKotlinxSerializersModule
    get() = SerializersModule {
        contextual(ColorSerializer())
        contextual(DurationSerializer())

        // Adventure support
        if (isClassPresent<ComponentLike>()) {
            contextual(MiniMessageComponentSerializer())
            contextual(TextColorSerializer())
        }

        // Paper support
        if (isClassPresent<Bukkit>()) {
            contextual(AttributeSerializer())
            contextual(AttributeModifierSerializer())
            contextual(EnchantmentSerializer())
            contextual(SoundSerializer())
            contextual(MaterialSerializer())
            contextual(EntityTypeSerializer())
            contextual(PotionEffectTypeSerializer())
            contextual(PotionEffectSerializer())
            contextual(LocationRetrieverSerializer())
            contextual(NbtItemSerializer())
        }
    }
