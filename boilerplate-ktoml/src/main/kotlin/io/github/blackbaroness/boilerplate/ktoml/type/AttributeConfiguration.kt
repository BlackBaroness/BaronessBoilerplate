package io.github.blackbaroness.boilerplate.ktoml.type

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Serializable
data class AttributeConfiguration(
    val attribute: Attribute,
    @Contextual val modifier: AttributeModifier
)
