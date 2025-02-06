@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplates.configurate

import io.github.blackbaroness.boilerplates.base.Boilerplate
import io.github.blackbaroness.boilerplates.configurate.serializer.DurationSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

fun Boilerplate.createConfigurateSerializersBase() = TypeSerializerCollection.builder()
    .register(io.github.blackbaroness.boilerplates.configurate.serializer.ColorSerializer())
    .register(DurationSerializer())
    .build()