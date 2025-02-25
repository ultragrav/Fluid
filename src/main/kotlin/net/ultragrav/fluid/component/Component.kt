package net.ultragrav.fluid.component

import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Rectangle
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid

abstract class Component(val dimensions: Dimensions) {
    lateinit var parent: ContainerComponent

    open val root: ContainerComponent
        get() = parent.root

    abstract fun render(): Solid
    abstract fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent)

    open fun onOpen(player: Player) {}
    open fun onClose(event: InventoryCloseEvent) {}

    open fun update(area: Shape = Rectangle(dimensions), solid: Solid = render()) {
        // Not initialized yet, this update will be superseded by the one called by the parent
        if (!::parent.isInitialized) return

        parent.updateChild(this, area, solid)
    }

    internal fun initializeSelfParent() {
        if (!::parent.isInitialized && this is ContainerComponent) {
            parent = this
        }
    }
}