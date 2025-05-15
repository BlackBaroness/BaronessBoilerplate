package io.github.blackbaroness.boilerplate.ktoml.serializer


import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlot
import java.util.*

class AttributeModifierSerializer : KSerializer<AttributeModifier> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("AttributeModifier") {
            element<String>("uuid")
            element<String>("name")
            element<String>("operation")
            element<Double>("amount")
            element<String>("slot", isOptional = true)
        }

    override fun deserialize(decoder: Decoder): AttributeModifier {
        val input = decoder as? CompositeDecoder ?: throw SerializationException("Expected a JSON object")
        var uuid: UUID? = null
        var name: String? = null
        var operation: Operation? = null
        var amount: Double? = null
        var slot: EquipmentSlot? = null

        // Read the elements
        loop@ while (true) {
            when (val index = input.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                0 -> uuid = UUID.fromString(input.decodeStringElement(descriptor, index))
                1 -> name = input.decodeStringElement(descriptor, index)
                2 -> operation = Operation.valueOf(input.decodeStringElement(descriptor, index))
                3 -> amount = input.decodeDoubleElement(descriptor, index)
                4 -> slot = EquipmentSlot.valueOf(input.decodeStringElement(descriptor, index))
                else -> throw SerializationException("Unexpected index $index")
            }
        }

        // Check for necessary fields
        if (uuid == null || name == null || operation == null || amount == null) {
            throw SerializationException("Missing required fields for AttributeModifier")
        }

        return AttributeModifier(uuid, name, amount, operation, slot)
    }

    override fun serialize(encoder: Encoder, value: AttributeModifier) {
        val output = encoder as? CompositeEncoder ?: throw SerializationException("Expected a JSON object")
        output.encodeStringElement(descriptor, 0, value.uniqueId.toString())
        output.encodeStringElement(descriptor, 1, value.name)
        output.encodeStringElement(descriptor, 2, value.operation.name)
        output.encodeDoubleElement(descriptor, 3, value.amount)
        value.slot?.let { output.encodeStringElement(descriptor, 4, it.name) }
    }
}
