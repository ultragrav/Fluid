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
                val inv = event.inventory
                val gui = event.player.openInventory?.getTag(GUI_TAG) ?: return@addListener

                event.isCancelled = true

                if (event.slot < inv.size) { // TODO: CHECK THIS WORKS
                    val x = event.slot % 9
                    val y = event.slot / 9
                    gui.click(x, y, event)
                } else {
                    gui.click(-1, -1, event)
                }
            }
            .addListener(InventoryCloseEvent::class.java) { event ->
                val inv = event.inventory ?: return@addListener // Contrary to the annotation, this IS nullable (for closing own inventory)
                val gui = inv.getTag(GUI_TAG) ?: return@addListener

                gui.onClose(event)
            }
    }
}