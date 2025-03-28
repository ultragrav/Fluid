package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.inventory.shape.Lines
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.max
import kotlin.math.min

open class ImmutableListComponent<T>(
    dimensions: Dimensions,
    val renderer: (T) -> ItemStack,
    val clickHandler: (Int, T, InventoryClickEvent) -> Unit = { _, _, _ -> },
    private val list: List<T>,
    private val emptyElement: ItemStack? = null
) : Component(dimensions) {
    var offset: Int = 0
        set(value) {
            field = value
            update()
        }

    override fun render(): Solid {
        val items = ArrayList<ItemStack?>(dimensions.size)
        items.addAll(list.subList(offset, min(offset + dimensions.size, list.size)).map(renderer))
        items.addAll(List(dimensions.size - items.size) { emptyElement })
        return Solid(dimensions.width, dimensions.height, items)
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {
        val index = x + y * dimensions.width + offset
        if (index < 0 || index >= list.size) return
        clickHandler(index, list[index], clickEvent)
    }
}