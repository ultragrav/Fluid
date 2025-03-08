package net.ultragrav.fluid

import net.minestom.server.inventory.InventoryType
import net.ultragrav.fluid.component.dimensions.Dimensions

object Util {
    val InventoryType.dimensions: Dimensions get() {
        return when (this) {
            InventoryType.CHEST_1_ROW -> Dimensions(9, 1)
            InventoryType.CHEST_2_ROW -> Dimensions(9, 2)
            InventoryType.CHEST_3_ROW -> Dimensions(9, 3)
            InventoryType.CHEST_4_ROW -> Dimensions(9, 4)
            InventoryType.CHEST_5_ROW -> Dimensions(9, 5)
            InventoryType.CHEST_6_ROW -> Dimensions(9, 6)
            InventoryType.WINDOW_3X3 -> Dimensions(3, 3)
            InventoryType.CRAFTER_3X3 -> Dimensions(3, 3)
            InventoryType.ANVIL -> Dimensions(3, 1)
            InventoryType.BEACON -> Dimensions(1, 1)
            InventoryType.BLAST_FURNACE -> Dimensions(3, 1)
            InventoryType.BREWING_STAND -> Dimensions(5, 1)
            InventoryType.CRAFTING -> Dimensions(10, 1)
            InventoryType.ENCHANTMENT -> Dimensions(2, 1)
            InventoryType.FURNACE -> Dimensions(3, 1)
            InventoryType.GRINDSTONE -> Dimensions(3, 1)
            InventoryType.HOPPER -> Dimensions(5, 1)
            InventoryType.LECTERN -> Dimensions(1, 1)
            InventoryType.LOOM -> Dimensions(4, 1)
            InventoryType.MERCHANT -> Dimensions(3, 1)
            InventoryType.SHULKER_BOX -> Dimensions(9, 3)
            InventoryType.SMITHING -> Dimensions(4, 1)
            InventoryType.SMOKER -> Dimensions(3, 1)
            InventoryType.CARTOGRAPHY -> Dimensions(3, 1)
            InventoryType.STONE_CUTTER -> Dimensions(2, 1)
        }
    }
}