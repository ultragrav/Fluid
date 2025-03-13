package net.ultragrav.fluid

import net.minestom.server.entity.Player
import net.minestom.server.inventory.AbstractInventory

class InventoryCloseInfo(
    val player: Player,
    val inventory: AbstractInventory,
    val closedByClient: Boolean
)