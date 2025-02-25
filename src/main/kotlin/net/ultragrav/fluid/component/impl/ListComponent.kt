package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.inventory.shape.Lines
import net.ultragrav.fluid.render.Solid
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import kotlin.math.max
import kotlin.math.min

open class ListComponent<T>(
    size: Dimensions,
    val renderer: (T) -> ItemStack,
    val clickHandler: (Int, T, InventoryPreClickEvent) -> Unit = { _, _, _ -> },
    private val backingList: MutableList<T> = ArrayList(),
    private val emptyElement: ItemStack = ItemStack.AIR
) : Component(size), MutableList<T> by backingList {
    var offset: Int = 0
        set(value) {
            field = value
            update()
        }

    override fun render(): Solid {
        val items = ArrayList<ItemStack>(dimensions.size)
        items.addAll(this.subList(offset, min(offset + dimensions.size, size)).map(renderer))
        items.addAll(List(dimensions.size - items.size) { emptyElement })
        return Solid(dimensions.width, dimensions.height, items)
    }

    private fun updateElements(range: IntRange) {
        val renderOffset = max(range.first - offset, 0)
        val renderLength = min(range.last - range.first + 1, dimensions.size - renderOffset)
            .coerceAtLeast(0)
        val shape = Lines(dimensions, 0, 0, renderOffset, renderLength)

        // Render the elements that changed, including nulls after the list
        val elements = ArrayList<ItemStack>(renderLength)
        elements.addAll(this.subList(range.first + offset, min(range.first + offset + renderLength, size)).map(renderer))
        elements.addAll(List(renderLength - elements.size) { emptyElement })

        val solid = Solid(renderLength, 1, elements)
        update(shape, solid)
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        val index = x + y * dimensions.width + offset
        if (index < 0 || index >= size) return
        clickHandler(index, this[index], clickEvent)
    }

    // Handle updates to the list
    override fun set(index: Int, element: T): T {
        val ret = backingList.set(index, element)
        updateElements(index..index)
        return ret
    }

    override fun add(element: T): Boolean {
        val ret = backingList.add(element)
        updateElements(size - 1 until size)
        return ret
    }

    override fun add(index: Int, element: T) {
        backingList.add(index, element)
        updateElements(index until size)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val ret = backingList.addAll(index, elements)
        updateElements(index until size)
        return ret
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val ret = backingList.addAll(elements)
        updateElements(size - elements.size until size)
        return ret
    }

    override fun clear() {
        backingList.clear()
        update()
    }

    override fun remove(element: T): Boolean {
        val ret = backingList.remove(element)
        update()
        return ret
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val ret = backingList.removeAll(elements)
        update()
        return ret
    }

    override fun removeAt(index: Int): T {
        val ret = backingList.removeAt(index)
        updateElements(index..size)
        return ret
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val ret = backingList.retainAll(elements)
        update()
        return ret
    }
}