@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.configurate.adventure

import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.configurate.adventure.serializer.MiniMessageComponentSerializer
import io.github.blackbaroness.boilerplate.configurate.adventure.serializer.TextColorSerializer
import io.github.blackbaroness.boilerplate.configurate.register
import org.spongepowered.configurate.serialize.TypeSerializerCollection

fun Boilerplate.createConfigurateSerializersAdventure() = TypeSerializerCollection.builder()
    .register(MiniMessageComponentSerializer())
    .register(TextColorSerializer())
    .build()
