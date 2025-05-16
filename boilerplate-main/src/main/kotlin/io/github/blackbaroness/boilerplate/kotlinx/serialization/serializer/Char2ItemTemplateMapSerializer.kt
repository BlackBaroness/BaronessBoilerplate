package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer

import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.ItemTemplate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer

object CharItemTemplateMapSerializer : KSerializer<Map<Char, ItemTemplate>>
by MapSerializer(CharStringSerializer, ItemTemplate.serializer())
