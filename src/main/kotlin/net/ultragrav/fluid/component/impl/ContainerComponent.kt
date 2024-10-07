package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.layout.FlexLayout
import net.ultragrav.fluid.component.layout.LayoutStrategy
import net.ultragrav.fluid.inventory.FluidGui
import net.ultragrav.fluid.inventory.shape.Rectangle
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.FluidRenderer
import net.ultragrav.fluid.render.Solid
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

open class ContainerComponent(size: Dimensions) : Component(size) {
    private val children: MutableList<Child> = ArrayList()

    fun <T : Component> addComponent(component: T, x: Int = -1, y: Int = -1): T {
        // Check bounds
        val child = Child(component, x, y)
        if (x != -1 && y != -1) checkCandidate(child)
        children.add(child)
        component.parent = this
        component.update()
        return component
    }

    fun button(x: Int = -1, y: Int = -1, builder: RendererComponent.Builder.() -> Unit) {
        val component = RendererComponent.Builder().apply(builder).build()
        addComponent(component, x, y)
    }

    private fun checkCandidate(candidate: Child) {
        require(candidate.x >= 0 && candidate.y >= 0) { "Negative coordinates!" }
        val maxX = candidate.x + candidate.component.dimensions.width
        val maxY = candidate.y + candidate.component.dimensions.height
        require(maxX <= dimensions.width && maxY <= dimensions.height) { "Out of bounds!" }

        val occupied = children.flatMap {
            Rectangle(it.component.dimensions, it.x, it.y)
                .iterator(dimensions).asSequence().toSet()
        }.toSet()

        val area = Rectangle(candidate.component.dimensions, candidate.x, candidate.y)
        val areaOccupied = area.iterator(dimensions).asSequence().toSet()

        require(areaOccupied.intersect(occupied).isEmpty()) { "Overlapping component!" }
    }

    var background: ItemStack? = null
        set(value) {
            field = value
            update()
        }

    fun layout(strategy: LayoutStrategy) {
        val newChildren = strategy.layout(children.map { it.component }, dimensions)
        children.clear()
        children.addAll(newChildren)
    }

    fun flexLayout(
        direction: FlexLayout.Direction = FlexLayout.Direction.HORIZONTAL,
        justify: FlexLayout.Justify = FlexLayout.Justify.START,
        align: FlexLayout.Justify = FlexLayout.Justify.START,
        wrap: Boolean = true
    ) {
        layout(FlexLayout(direction, justify, align, wrap))
    }

    override fun render(): Solid {
        if (children.any { it.x == -1 && it.y == -1 }) error("Must call layout()!")
        val renderer = FluidRenderer(this)
        background?.let { renderer.fill(it) }
        for (child in children) {
            val solid = child.component.render()
            renderer.drawSolid(child.x, child.y, solid)
        }
        return renderer.render()
    }

    open fun updateChild(childComponent: Component, area: Shape, solid: Solid) {
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

    override fun onClose(event: InventoryCloseEvent) {
        super.onClose(event)
        children.forEach { it.component.onClose(event) }
    }

    override fun onOpen(player: HumanEntity) {
        super.onOpen(player)
        children.forEach { it.component.onOpen(player) }
    }

    fun asGui(title: net.kyori.adventure.text.Component): FluidGui {
        return FluidGui(title, dimensions.height)
            .also { it.addComponent(this, 0, 0) }
    }

    class Child(val component: Component, val x: Int, val y: Int)
}