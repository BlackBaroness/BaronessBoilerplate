package io.github.blackbaroness.boilerplate.ktoml.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.awt.Color

class ColorSerializer : KSerializer<Color> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Color") {
            element<Int>("red")
            element<Int>("green")
            element<Int>("blue")
        }

    override fun deserialize(decoder: Decoder): Color {
        val input = decoder.beginStructure(descriptor)
        var red = Int.MIN_VALUE
        var green = Int.MIN_VALUE
        var blue = Int.MIN_VALUE

        // Read the elements
        loop@ while (true) {
            when (val index = input.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                0 -> red = input.decodeIntElement(descriptor, index)
                1 -> green = input.decodeIntElement(descriptor, index)
                2 -> blue = input.decodeIntElement(descriptor, index)
                else -> throw SerializationException("Unexpected index $index")
            }
        }

        // If any of the components is invalid, return null
        if (red == Int.MIN_VALUE || green == Int.MIN_VALUE || blue == Int.MIN_VALUE) {
            throw SerializationException("Invalid color components")
        }

        return Color(red, green, blue)
    }

    override fun serialize(encoder: Encoder, value: Color) {
        val output = encoder.beginStructure(descriptor)
        output.encodeIntElement(descriptor, 0, value.red)
        output.encodeIntElement(descriptor, 1, value.green)
        output.encodeIntElement(descriptor, 2, value.blue)
        output.endStructure(descriptor)
    }
}
