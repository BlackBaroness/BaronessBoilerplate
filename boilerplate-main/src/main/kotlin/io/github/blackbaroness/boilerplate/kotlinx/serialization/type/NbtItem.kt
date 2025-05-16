package io.github.blackbaroness.boilerplate.kotlinx.serialization.type

import de.tr7zw.nbtapi.NBT

class NbtItem(val nbtString: String) {
    val itemUnsafe by lazy { NBT.itemStackFromNBT(NBT.parseNBT(nbtString))!! }
    val itemSafe get() = itemUnsafe.clone()
}
