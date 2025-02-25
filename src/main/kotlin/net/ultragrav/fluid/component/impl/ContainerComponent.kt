package net.ultragrav.fluid.component.impl

import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.layout.FlexLayout
import net.ultragrav.fluid.component.layout.LayoutStrategy
import net.ultragrav.fluid.inventory.FluidGui
import net.ultragrav.fluid.inventory.shape.Rectangle
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.FluidRenderer
import net.ultragrav.fluid.render.Solid

open class ContainerComponent(size: Dimensions) : Component(size) {

    private var layoutStrategy: LayoutStrategy? = null
    private var dynamicRenderer: (ContainerComponent.() -> Unit)? = null
    private val children0: MutableList<Child> = ArrayList()
    val children get() = children0.toList()

    override val root: ContainerComponent
        get() {
            if (parent == this) return this
            return parent.root
        }

    fun dynamic(builder: ContainerComponent.() -> Unit) {
        dynamicRenderer = builder
    }

    fun <T : Component> addComponent(component: T, x: Int = -1, y: Int = -1): T {
        // Check bounds
        val child = Child(component, x, y)
        if (x != -1 && y != -1) checkCandidate(child)
        children0.add(child)
        component.parent = this
        if (x != -1 && y != -1) component.update()
        return component
    }

    fun removeComponent(component: Component) {
        val child = children0.firstOrNull { it.component == component } ?: return
        children0.remove(child)
    }

    fun button(x: Int = -1, y: Int = -1, builder: RendererComponent.Builder.() -> Unit) {
        val component = RendererComponent.Builder().apply(builder).build()
        addComponent(component, x, y)
    }

    fun container(x: Int = -1, y: Int = -1, width: Int, height: Int, builder: ContainerComponent.() -> Unit) {
        val component = ContainerComponent(Dimensions(width, height)).apply(builder)
        addComponent(component, x, y)
    }

    private fun checkCandidate(candidate: Child) {
        require(candidate.x >= 0 && candidate.y >= 0) { "Negative coordinates!" }
        val maxX = candidate.x + candidate.component.dimensions.width
        val maxY = candidate.y + candidate.component.dimensions.height
        require(maxX <= dimensions.width && maxY <= dimensions.height) { "Out of bounds!" }

        val occupied = children0.flatMap {
            Rectangle(it.component.dimensions, it.x, it.y)
                .iterator(dimensions).asSequence().toSet()
        }.toSet()

        val area = Rectangle(candidate.component.dimensions, candidate.x, candidate.y)
        val areaOccupied = area.iterator(dimensions).asSequence().toSet()

        require(areaOccupied.intersect(occupied).isEmpty()) { "Overlapping component!" }
    }

    var background: ItemStack = ItemStack.AIR
        set(value) {
            field = value
            update()
        }

    fun layout(strategy: LayoutStrategy) {
        layoutStrategy = strategy
    }

    fun flexLayout(
        direction: FlexLayout.Direction = FlexLayout.Direction.HORIZONTAL,
        justify: FlexLayout.Justify = FlexLayout.Justify.START,
        align: FlexLayout.Justify = FlexLayout.Justify.START,
        wrap: Boolean = true
    ) {
        layout(FlexLayout(direction, justify, align, wrap))
    }

    fun layoutImmediate(strategy: LayoutStrategy) {
        layoutStrategy = strategy
        doLayout()
    }

    private fun doLayout() {
        val layout = layoutStrategy ?: return
        val newChildren = layout.layout(children0.map { it.component }, dimensions)
        children0.clear()
        children0.addAll(newChildren)
        check(children0.none { it.x == -1 || it.y == -1 }) { "Layout failed!" }
    }

    private fun doDynamicSetup() {
        val renderer = dynamicRenderer ?: return
        children0.clear()
        renderer()
    }

    override fun render(): Solid {
        doDynamicSetup()
        if (children0.any { it.x == -1 || it.y == -1 }) doLayout()
        if (children0.any { it.x == -1 || it.y == -1 }) throw IllegalStateException("Layout failed!")
        val renderer = FluidRenderer(this)
        background?.let { renderer.fill(it) }
        for (child in children0) {
            val solid = child.component.render()
            renderer.drawSolid(child.x, child.y, solid)
        }
        return renderer.render()
    }

    open fun updateChild(childComponent: Component, area: Shape, solid: Solid) {
        val child = children0.first { it.component == childComponent }
        val newArea = area.shift(child.x, child.y)
        update(newArea, solid)
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        if (x == -1) {
            // Own inventory click
            children0.forEach { it.component.click(x, y, clickEvent) }
            return
        }

        for (child in children0) {
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
        children0.forEach { it.component.onClose(event) }
    }

    override fun onOpen(player: Player) {
        super.onOpen(player)
        children0.forEach { it.component.onOpen(player) }
    }

    fun asGui(title: net.kyori.adventure.text.Component): FluidGui {
        return FluidGui(title, dimensions.height)
            .also { it.addComponent(this, 0, 0) }
    }

    class Child(val component: Component, val x: Int, val y: Int)
}