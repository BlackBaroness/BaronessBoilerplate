@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplates.configurate.adventure

import io.github.blackbaroness.boilerplates.base.Boilerplate
import io.github.blackbaroness.boilerplates.configurate.adventure.serializer.MiniMessageComponentSerializer
import io.github.blackbaroness.boilerplates.configurate.adventure.serializer.TextColorSerializer
import io.github.blackbaroness.boilerplates.configurate.register
import org.spongepowered.configurate.serialize.TypeSerializerCollection

fun Boilerplate.createConfigurateSerializersAdventure() = TypeSerializerCollection.builder()
    .register(MiniMessageComponentSerializer())
    .register(TextColorSerializer())
    .build()