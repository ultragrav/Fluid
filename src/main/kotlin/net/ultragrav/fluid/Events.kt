package net.ultragrav.fluid

import net.minestom.server.MinecraftServer
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.tag.Tag
import net.ultragrav.fluid.inventory.FluidGui

object Events {
    val GUI_TAG = Tag.Transient<FluidGui>("fluid_gui")

    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(InventoryPreClickEvent::class.java) { event ->
                val inv = event.player.openInventory ?: return@addListener
                val gui = inv.getTag(GUI_TAG) ?: return@addListener

                event.isCancelled = true

                if (event.inventory != null && event.inventory != event.player.inventory) {
                    val x = event.slot % gui.dimensions.width
                    val y = event.slot / gui.dimensions.width
                    gui.click(x, y, event)
                } else {
                    gui.click(-1, -1, event)
                }
            }
    }
}