package net.ultragrav.fluid.inventory

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.ultragrav.fluid.InventoryCloseInfo

class FluidInventory(val gui: FluidGui, type: InventoryType, title: Component) : Inventory(type, title) {
    override fun removeViewer(player: Player): Boolean {
        gui.onClose(InventoryCloseInfo(player, this, !player.didCloseInventory()))
        return super.removeViewer(player)
    }
}