package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

open class RendererComponent(
    private val renderer: () -> ItemStack,
    val clickHandler: (InventoryClickEvent) -> Unit = { }
) : Component(Dimensions(1, 1)) {
    override fun render(): Solid {
        return Solid(1, 1, Collections.singletonList(renderer()))
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {
        if (x != 0 || y != 0) return
        clickHandler(clickEvent)
    }

    class Builder {
        private var renderer: () -> ItemStack = { ItemStack(Material.BARRIER) }
        private var clickHandler: (InventoryClickEvent) -> Unit = { }

        fun renderer(renderer: () -> ItemStack) {
            this.renderer = renderer
        }

        fun onClick(clickHandler: (InventoryClickEvent) -> Unit) {
            this.clickHandler = clickHandler
        }

        fun build(): RendererComponent {
            return RendererComponent(renderer, clickHandler)
        }
    }
}