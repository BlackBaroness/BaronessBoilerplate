package io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer

import kotlinx.serialization.Serializable

object IntRangeSerializer : SurrogateSerializer<IntRange, IntRangeSerializer.Surrogate>(
    Surrogate.serializer(),
    IntRange::class
) {

    override fun toSurrogate(value: IntRange) = Surrogate(
        value.first,
        value.last,
    )

    override fun fromSurrogate(value: Surrogate): IntRange = IntRange(
        value.min,
        value.max
    )

    @Serializable
    data class Surrogate(
        val min: Int,
        val max: Int,
    )
}
