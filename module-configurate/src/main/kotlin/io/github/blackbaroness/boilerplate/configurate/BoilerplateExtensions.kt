@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.configurate

import io.github.blackbaroness.boilerplate.configurate.serializer.ColorSerializer
import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.configurate.serializer.DurationSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

fun Boilerplate.createConfigurateSerializersBase() = TypeSerializerCollection.builder()
    .register(ColorSerializer())
    .register(DurationSerializer())
    .build()
