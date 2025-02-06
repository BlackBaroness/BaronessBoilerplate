package io.github.blackbaroness.boilerplates.configurate.paper.serializer

import de.tr7zw.nbtapi.NBT
import io.github.blackbaroness.boilerplates.configurate.paper.type.NbtItem
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

class NbtItemSerializer : TypeSerializer<NbtItem> {

    override fun deserialize(type: Type, node: ConfigurationNode): NbtItem? {
        val string = node.string ?: return null

        // check if the nbt is valid to find errors early
        NBT.itemStackFromNBT(NBT.parseNBT(string))
            ?: throw IllegalStateException("Not a valid item NBT: '$string'")

        return NbtItem(string)
    }

    override fun serialize(type: Type, obj: NbtItem?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.set(obj.nbtString)
    }
}