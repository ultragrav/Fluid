package net.ultragrav.fluid.component.impl

import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.render.Solid

class UnstableComponent(val supplier: () -> Component) : Component(supplier().dimensions) {
    private var cached = supplier()

    override fun render(): Solid {
        cached = supplier()
        return cached.render()
    }

    override fun click(
        x: Int,
        y: Int,
        clickEvent: InventoryPreClickEvent
    ) {
        cached.click(x, y, clickEvent)
    }
}