package net.ultragrav.fluid.component.impl

import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid

abstract class AbstractUnitComponent : Component(Dimensions(1, 1)) {
    override fun render(): Solid {
        return Solid(1, 1, listOf(renderItem()))
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        if (x == -1 && y == -1) {
            clickOwnInv(clickEvent)
        } else if (x == 0 && y == 0) {
            clickItem(clickEvent)
        }
    }

    abstract fun renderItem(): ItemStack
    open fun clickOwnInv(event: InventoryPreClickEvent) {}
    open fun clickItem(event: InventoryPreClickEvent) {}
}