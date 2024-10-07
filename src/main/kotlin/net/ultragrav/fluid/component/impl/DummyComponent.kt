package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent

class DummyComponent(dimensions: Dimensions) : Component(dimensions) {
    override fun render(): Solid {
        return Solid(dimensions.width, dimensions.height, (0..<dimensions.width * dimensions.height).map { null })
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {}
}