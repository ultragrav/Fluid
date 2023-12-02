package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.FluidRenderer
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

open class ContainerComponent(size: Dimensions) : Component(size) {
    protected val children: MutableList<Child> = ArrayList()
    fun <T : Component> addComponent(component: T, x: Int, y: Int): T {
        children.add(Child(component, x, y))
        component.parent = this
        component.update()
        return component
    }

    private var background: ItemStack? = null
    fun setBackground(background: ItemStack?) {
        this.background = background
        update()
    }

    override fun render(): Solid {
        val renderer = FluidRenderer(size.width, size.height)
        for (child in children) {
            val solid = child.component.render()
            renderer.drawSolid(child.x, child.y, solid)
        }
        background?.let { renderer.fillEmpty(it) }
        return renderer.render()
    }

    fun updateChild(childComponent: Component, area: Shape, solid: Solid) {
        val child = children.first { it.component == childComponent }
        val newArea = area.shift(child.x, child.y)
        update(newArea, solid)
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {
        if (x == -1) {
            // Own inventory click
            children.forEach { it.component.click(x, y, clickEvent) }
            return
        }

        for (child in children) {
            if (
                x in child.x..<child.x + child.component.size.width &&
                y in child.y..<child.y + child.component.size.height
            ) {
                child.component.click(x - child.x, y - child.y, clickEvent)
                return
            }
        }
    }

    class Child(val component: Component, val x: Int, val y: Int)
}