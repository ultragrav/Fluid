package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.inventory.shape.Lines
import net.ultragrav.fluid.render.Solid
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import kotlin.math.max
import kotlin.math.min

open class ImmutableListComponent<T>(
    dimensions: Dimensions,
    val renderer: (T) -> ItemStack,
    val clickHandler: (Int, T, InventoryPreClickEvent) -> Unit = { _, _, _ -> },
    val list: List<T>,
    private val emptyElement: ItemStack = ItemStack.AIR
) : Component(dimensions) {
    var offset: Int = 0
        set(value) {
            field = value
            update()
        }

    override fun render(): Solid {
        val items = ArrayList<ItemStack>(dimensions.size)
        items.addAll(list.subList(offset, min(offset + dimensions.size, list.size)).map(renderer))
        items.addAll(List(dimensions.size - items.size) { emptyElement })
        return Solid(dimensions.width, dimensions.height, items)
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        val index = x + y * dimensions.width + offset
        if (index < 0 || index >= list.size) return
        clickHandler(index, list[index], clickEvent)
    }
}