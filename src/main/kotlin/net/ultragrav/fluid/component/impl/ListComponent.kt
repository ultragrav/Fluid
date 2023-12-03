package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.inventory.shape.Lines
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.max
import kotlin.math.min

class ListComponent<T>(
    size: Dimensions,
    val renderer: (T) -> ItemStack,
    val clickHandler: (Int, T, InventoryClickEvent) -> Unit,
    private val backingList: MutableList<T> = ArrayList()
) : Component(size), MutableList<T> by backingList {
    var offset: Int = 0
        set(value) {
            field = value
            update()
        }

    override fun render(): Solid {
        val items = ArrayList<ItemStack?>(dimensions.size)
        items.addAll(this.subList(offset, min(offset + dimensions.size, size)).map(renderer))
        items.addAll(List(dimensions.size - items.size) { null })
        return Solid(dimensions.width, dimensions.height, items)
    }

    private fun updateElements(range: IntRange) {
        val renderOffset = max(range.first - offset, 0)
        val renderLength = min(range.last - range.first + 1, dimensions.size - renderOffset)
        val shape = Lines(dimensions, 0, 0, renderOffset, renderLength)
        val solid = Solid(renderLength, 1, this.subList(range.first, range.last + 1).map(renderer))
        update(shape, solid)
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {
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
        updateElements(size - 1..<size)
        return ret
    }

    override fun add(index: Int, element: T) {
        backingList.add(index, element)
        updateElements(index..<size)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val ret = backingList.addAll(index, elements)
        updateElements(index..<size)
        return ret
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val ret = backingList.addAll(elements)
        updateElements(size - elements.size..<size)
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
        updateElements(index..<size)
        return ret
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val ret = backingList.retainAll(elements)
        update()
        return ret
    }
}