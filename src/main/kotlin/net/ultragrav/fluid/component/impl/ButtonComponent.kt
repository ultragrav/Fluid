package net.ultragrav.fluid.component.impl

import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import java.util.*

open class ButtonComponent(
    private val item: ItemStack,
    private val disabledItem: ItemStack = ItemStack.AIR,
    val clickHandler: (InventoryPreClickEvent) -> Unit = { }
) : Component(Dimensions(1, 1)) {
    var active = true
        set(value) {
            field = value
            update()
        }

    override fun render(): Solid {
        return if (active) {
            Solid(1, 1, Collections.singletonList(item))
        } else {
            Solid(1, 1, Collections.singletonList(disabledItem))
        }
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        if (!active || x != 0 || y != 0) return
        clickHandler(clickEvent)
    }

}