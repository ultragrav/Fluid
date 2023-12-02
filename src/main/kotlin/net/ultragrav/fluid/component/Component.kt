package net.ultragrav.fluid.component

import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Rectangle
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent

abstract class Component(val size: Dimensions) {
    lateinit var parent: ContainerComponent

    abstract fun render(): Solid
    abstract fun click(x: Int, y: Int, clickEvent: InventoryClickEvent)
    open fun update(area: Shape = Rectangle(size), solid: Solid = render()) {
        parent.updateChild(this, area, solid)
    }

    abstract fun copy(): Component
}