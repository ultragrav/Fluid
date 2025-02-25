package net.ultragrav.fluid.component.impl

import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import kotlin.reflect.KProperty

class VarComponent<T>(
    private var value: T,
    private val renderer: (T) -> ItemStack,
    private val clickHandler: (InventoryPreClickEvent) -> Unit = {}
) : Component(Dimensions(1, 1)) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        update()
    }

    override fun render(): Solid {
        return Solid(1, 1, listOf(renderer(value)))
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        if (x != 0 || y != 0) return
        clickHandler(clickEvent)
    }
}