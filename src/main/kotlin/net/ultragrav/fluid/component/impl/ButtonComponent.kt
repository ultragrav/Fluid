package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

open class ButtonComponent(
    private val item: ItemStack,
    private val disabledItem: ItemStack? = null,
    val clickHandler: (InventoryClickEvent) -> Unit = { }
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

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {
        if (!active) return
        clickHandler(clickEvent)
    }

}