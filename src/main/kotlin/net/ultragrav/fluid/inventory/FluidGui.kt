package net.ultragrav.fluid.inventory

import net.kyori.adventure.text.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

open class FluidGui(title: Component, rows: Int) : ContainerComponent(Dimensions(9, rows)) {
    val inv = Bukkit.createInventory(Holder(), rows * 9, title)

    override fun update(area: Shape, solid: Solid) {
        for ((j, i) in area.iterator(dimensions).withIndex()) {
            inv.setItem(i, solid.grid[j])
        }
    }

    open fun onClose(event: InventoryCloseEvent) {}

    fun open(player: HumanEntity) {
        val previous = player.openInventory.topInventory
        if (previous.holder is Holder) {
            (previous.holder as Holder).gui.onClose(InventoryCloseEvent(player.openInventory))
        }
        player.openInventory(inv)
    }

    fun closeAll() {
        inv.close()
    }

    inner class Holder : InventoryHolder {
        val gui: FluidGui get() = this@FluidGui

        override fun getInventory(): Inventory {
            return inv
        }
    }
}