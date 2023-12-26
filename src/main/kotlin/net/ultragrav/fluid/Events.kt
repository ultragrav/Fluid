package net.ultragrav.fluid

import net.ultragrav.fluid.inventory.FluidGui
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class Events : Listener {
    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val inv = event.inventory
        if (inv.holder is FluidGui.Holder) {
            event.isCancelled = true

            val gui = (inv.holder as FluidGui.Holder).gui
            if (event.clickedInventory == inv) {
                val x = event.slot % 9
                val y = event.slot / 9
                gui.click(x, y, event)
            } else {
                gui.click(-1, -1, event)
            }
        }
    }

    @EventHandler
    fun onDrag(event: InventoryDragEvent) {
        val inv = event.inventory
        if (inv.holder is FluidGui.Holder) {
            event.isCancelled = true

            val gui = (inv.holder as FluidGui.Holder).gui
            gui.onDrag(event)
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        val inv = event.inventory
        if (inv.holder is FluidGui.Holder) {
            val gui = (inv.holder as FluidGui.Holder).gui
            gui.onClose(event)
        }
    }
}