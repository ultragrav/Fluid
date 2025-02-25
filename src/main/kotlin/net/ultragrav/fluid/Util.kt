package net.ultragrav.fluid

import net.minestom.server.inventory.InventoryType

object Util {
    fun inventoryTypeForSize(size: Int): InventoryType {
        return when (size) {
            9 -> InventoryType.CHEST_1_ROW
            18 -> InventoryType.CHEST_2_ROW
            27 -> InventoryType.CHEST_3_ROW
            36 -> InventoryType.CHEST_4_ROW
            45 -> InventoryType.CHEST_5_ROW
            54 -> InventoryType.CHEST_6_ROW
            else -> error("Invalid inventory size: $size")
        }
    }
}