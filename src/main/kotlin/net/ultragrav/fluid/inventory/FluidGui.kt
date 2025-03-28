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
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

open class FluidGui(title: Component, rows: Int) : ContainerComponent(Dimensions(9, rows)) {
    val inv = Bukkit.createInventory(Holder(), rows * 9, title)

    init {
        initializeSelfParent()
    }

    override fun update(area: Shape, solid: Solid) {

        val numUpdates = area.iterator(dimensions).asSequence().count()
        if (numUpdates <= 5) {
            // Small update
            for ((j, i) in area.iterator(dimensions).withIndex()) {
                inv.setItem(i, solid.grid[j])
            }
        } else {
            // Large update (send only one packet)
            val contents = inv.contents
            for ((j, i) in area.iterator(dimensions).withIndex()) {
                contents[i] = solid.grid[j]
            }
            inv.contents = contents
        }
    }

    open fun onDrag(event: InventoryDragEvent) {}

    fun open(player: HumanEntity) {
        val previous = player.openInventory.topInventory
        if (previous.holder is Holder) {
            (previous.holder as Holder).gui.onClose(InventoryCloseEvent(player.openInventory))
        }
        update()
        player.openInventory(inv)
        onOpen(player)
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