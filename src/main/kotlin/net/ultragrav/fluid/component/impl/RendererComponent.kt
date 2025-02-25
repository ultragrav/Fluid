package net.ultragrav.fluid.component.impl

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import net.minestom.server.item.Material
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import java.util.*

open class RendererComponent(
    private val renderer: () -> ItemStack,
    val clickHandler: (InventoryPreClickEvent) -> Unit = { }
) : Component(Dimensions(1, 1)) {
    override fun render(): Solid {
        return Solid(1, 1, listOf(renderer()))
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        if (x != 0 || y != 0) return
        clickHandler(clickEvent)
    }

    class Builder {
        private var renderer: () -> ItemStack = { ItemStack.of(Material.BARRIER) }
        private var clickHandler: (InventoryPreClickEvent) -> Unit = { }

        fun renderer(renderer: () -> ItemStack) {
            this.renderer = renderer
        }

        fun onClick(clickHandler: (InventoryPreClickEvent) -> Unit) {
            this.clickHandler = clickHandler
        }

        fun build(): RendererComponent {
            return RendererComponent(renderer, clickHandler)
        }
    }
}