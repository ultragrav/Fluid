package net.ultragrav.fluid.component

import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Rectangle
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent

abstract class Component(val dimensions: Dimensions) {
    lateinit var parent: ContainerComponent

    abstract fun render(): Solid
    abstract fun click(x: Int, y: Int, clickEvent: InventoryClickEvent)
    open fun update(area: Shape = Rectangle(dimensions), solid: Solid = render()) {
        // Not initialized yet, this update will be superseded by the one called by the parent
        if (!::parent.isInitialized) return

        parent.updateChild(this, area, solid)
    }
}