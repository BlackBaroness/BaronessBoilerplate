package io.github.blackbaroness.boilerplates.configurate.paper.type

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class AttributeConfiguration(
    val attribute: Attribute,
    val modifier: AttributeModifier
)
