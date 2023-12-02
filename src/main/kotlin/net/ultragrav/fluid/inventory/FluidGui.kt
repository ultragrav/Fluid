package net.ultragrav.fluid.inventory

import net.kyori.adventure.text.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class FluidGui(title: Component, rows: Int) : ContainerComponent(Dimensions(9, rows)) {
    val inv = Bukkit.createInventory(Holder(), rows * 9, title)

    init {
        update()
    }

    override fun update(area: Shape, solid: Solid) {
        for ((j, i) in area.withIndex()) {
            inv.setItem(i, solid.grid[j])
        }
    }

    inner class Holder : InventoryHolder {
        val gui: FluidGui get() = this@FluidGui

        override fun getInventory(): Inventory {
            return inv
        }
    }
}