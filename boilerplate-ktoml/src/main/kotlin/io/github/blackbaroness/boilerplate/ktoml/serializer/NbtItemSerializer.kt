package io.github.blackbaroness.boilerplate.ktoml.serializer

import de.tr7zw.nbtapi.NBT
import io.github.blackbaroness.boilerplate.ktoml.type.NbtItem
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class NbtItemSerializer : KSerializer<NbtItem> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("NbtItem") {
            element<String>("nbtString")
        }

    override fun deserialize(decoder: Decoder): NbtItem {
        val input = decoder.beginStructure(descriptor)
        var nbtString: String? = null

        // Read the elements
        loop@ while (true) {
            when (val index = input.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                0 -> nbtString = input.decodeStringElement(descriptor, index)
                else -> throw SerializationException("Unexpected index $index")
            }
        }

        // Check for missing or invalid NBT string
        if (nbtString == null) {
            throw SerializationException("Missing NBT string")
        }

        // Validate the NBT string by checking if it's a valid NBT format
        NBT.itemStackFromNBT(NBT.parseNBT(nbtString))
            ?: throw IllegalStateException("Not a valid item NBT: '$nbtString'")

        return NbtItem(nbtString)
    }

    override fun serialize(encoder: Encoder, value: NbtItem) {
        val output = encoder.beginStructure(descriptor)
        output.encodeStringElement(descriptor, 0, value.nbtString)
        output.endStructure(descriptor)
    }
}
