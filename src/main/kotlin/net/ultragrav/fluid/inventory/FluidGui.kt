package net.ultragrav.fluid.inventory

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.inventory.Inventory
import net.ultragrav.fluid.Events
import net.ultragrav.fluid.Util
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid

open class FluidGui(title: Component, rows: Int) : ContainerComponent(Dimensions(9, rows)) {
    val inv = Inventory(Util.inventoryTypeForSize(rows * 9), title)

    init {
        initializeSelfParent()
    }

    override fun update(area: Shape, solid: Solid) {
        for ((j, i) in area.iterator(dimensions).withIndex()) {
            inv.setItemStack(i, solid.grid[j])
        }
    }

    fun open(player: Player) {
        // TODO: Check if this isn't already fixed in minestom
        player.openInventory?.getTag(Events.GUI_TAG)?.onClose(InventoryCloseEvent(player.openInventory!!, player))

        update()
        player.openInventory(inv)
        onOpen(player)
    }

    fun closeAll() {
        inv.viewers.forEach { it.closeInventory() }
    }
}