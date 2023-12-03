package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.FluidRenderer
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

open class ContainerComponent(size: Dimensions) : Component(size) {
    private val children: MutableList<Child> = ArrayList()
    fun <T : Component> addComponent(component: T, x: Int, y: Int): T {
        children.add(Child(component, x, y))
        component.parent = this
        component.update()
        return component
    }

    var background: ItemStack? = null
        set(value) {
            field = value
            update()
        }

    override fun render(): Solid {
        val renderer = FluidRenderer(this)
        background?.let { renderer.fill(it) }
        for (child in children) {
            val solid = child.component.render()
            renderer.drawSolid(child.x, child.y, solid)
        }
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
                x in child.x..<child.x + child.component.dimensions.width &&
                y in child.y..<child.y + child.component.dimensions.height
            ) {
                child.component.click(x - child.x, y - child.y, clickEvent)
                return
            }
        }
    }

    class Child(val component: Component, val x: Int, val y: Int)
}