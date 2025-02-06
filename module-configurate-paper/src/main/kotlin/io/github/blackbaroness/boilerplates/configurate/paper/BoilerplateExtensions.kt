@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplates.configurate.paper

import io.github.blackbaroness.boilerplates.base.Boilerplate
import io.github.blackbaroness.boilerplates.configurate.paper.serializer.*
import io.github.blackbaroness.boilerplates.configurate.register
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