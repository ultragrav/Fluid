package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

open class UnitComponent(
    private val item: ItemStack,
    val clickHandler: (InventoryClickEvent) -> Unit = { }
) : Component(Dimensions(1, 1)) {
    private val rendered by lazy { Solid(1, 1, Collections.singletonList(item)) }

    override fun render(): Solid {
        return rendered
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {
        clickHandler(clickEvent)
    }

}