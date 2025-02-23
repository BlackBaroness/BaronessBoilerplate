package io.github.blackbaroness.boilerplate.configurate.serializer

import io.github.blackbaroness.durationserializer.DurationFormats
import io.github.blackbaroness.durationserializer.DurationSerializer
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import java.time.Duration

class DurationSerializer : TypeSerializer<Duration> {

    override fun deserialize(type: Type, node: ConfigurationNode): Duration? {
        return node.string?.let { DurationSerializer.deserialize(it) }
    }

    override fun serialize(type: Type, obj: Duration?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.set(DurationSerializer.serialize(obj, DurationFormats.mediumLengthEnglish()))
    }
}
