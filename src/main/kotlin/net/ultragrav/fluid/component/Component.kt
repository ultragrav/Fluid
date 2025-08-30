package net.ultragrav.fluid.component

import kotlinx.coroutines.CoroutineScope
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Rectangle
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

abstract class Component(val dimensions: Dimensions) {
    lateinit var parent: ContainerComponent

    open val root: ContainerComponent
        get() = parent.root

    internal open val scope: CoroutineScope get() = parent.scope

    abstract fun render(): Solid
    abstract fun click(x: Int, y: Int, clickEvent: InventoryClickEvent)

    open fun onOpen(player: HumanEntity) {}
    open fun onClose(event: InventoryCloseEvent) {}

    open fun updateRoot() {
        if (!::parent.isInitialized) return
        if (parent == this) update()
        else parent.updateRoot()
    }

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