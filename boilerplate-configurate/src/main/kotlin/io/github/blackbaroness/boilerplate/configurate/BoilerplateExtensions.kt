@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.configurate

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.base.isClassPresent
import io.github.blackbaroness.boilerplate.configurate.serializer.*
import net.kyori.adventure.text.ComponentLike
import org.bukkit.Bukkit
import org.spongepowered.configurate.serialize.TypeSerializerCollection

val Boilerplate.defaultConfigurateSerializers
    get() = TypeSerializerCollection.builder().apply {
        register(ColorSerializer())
        register(DurationSerializer())

        // Adventure support
        if (isClassPresent<ComponentLike>()) {
            register(MiniMessageComponentSerializer())
            register(TextColorSerializer())
        }

        // Paper support
        if (isClassPresent<Bukkit>()) {
            register(AttributeSerializer())
            register(AttributeModifierSerializer())
            register(EnchantmentSerializer())
            register(SoundSerializer())
            register(MaterialSerializer())
            register(EntityTypeSerializer())
            register(PotionEffectTypeSerializer())
            register(PotionEffectSerializer())
            register(LocationRetrieverSerializer()) // requires kotlinx.serialization but doesn't crash on just launch
            register(NbtItemSerializer()) // requires NBT-API but doesn't crash on just launch
        }
    }
