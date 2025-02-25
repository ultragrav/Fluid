package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import java.util.*

open class UnitComponent(
    private val item: ItemStack,
    val clickHandler: (InventoryPreClickEvent) -> Unit = { }
) : Component(Dimensions(1, 1)) {
    private val rendered by lazy { Solid(1, 1, listOf(item)) }

    override fun render(): Solid {
        return rendered
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        if (x != 0 || y != 0) return
        clickHandler(clickEvent)
    }

}