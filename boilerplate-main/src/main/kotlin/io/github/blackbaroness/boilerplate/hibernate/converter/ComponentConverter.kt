package io.github.blackbaroness.boilerplate.hibernate.converter

import jakarta.persistence.AttributeConverter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class ComponentConverter : AttributeConverter<Component, String> {

    override fun convertToDatabaseColumn(value: Component?): String? =
        value?.let { GsonComponentSerializer.gson().serialize(it) }

    override fun convertToEntityAttribute(value: String?): Component? =
        value?.let { GsonComponentSerializer.gson().deserialize(it) }
}
