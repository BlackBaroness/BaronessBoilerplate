@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.configurate.paper

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.configurate.paper.serializer.*
import io.github.blackbaroness.boilerplate.configurate.register
import org.spongepowered.configurate.serialize.TypeSerializerCollection

fun Boilerplate.createConfigurateSerializersPaper() = TypeSerializerCollection.builder()
    .register(AttributeSerializer())
    .register(EnchantmentSerializer())
    .register(SoundSerializer())
    .register(MaterialSerializer())
    .register(EntityTypeSerializer())
    .register(PotionEffectTypeSerializer())
    .register(PotionEffectSerializer())
    .register(LocationRetrieverSerializer())
    .register(NbtItemSerializer())
    .build()
