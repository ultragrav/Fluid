package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack

class DummyComponent(dimensions: Dimensions) : Component(dimensions) {
    override fun render(): Solid {
        return Solid(
            dimensions.width,
            dimensions.height,
            (0 until dimensions.width * dimensions.height).map { ItemStack.AIR }
        )
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {}
}