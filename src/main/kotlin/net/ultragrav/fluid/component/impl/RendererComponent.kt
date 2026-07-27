package net.ultragrav.fluid.component.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import net.minestom.server.component.DataComponents
import net.minestom.server.item.Material
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

open class RendererComponent(
    private val renderer: () -> ItemStack,
    val clickHandler: Context.(InventoryPreClickEvent) -> Unit = { }
) : Component(Dimensions(1, 1)) {

    private val ctx = Context()

    private val temporaryLore = mutableListOf<net.kyori.adventure.text.Component>()

    private fun applyTempLore(item: ItemStack): ItemStack {
        if (item.isAir) return item
        if (temporaryLore.isEmpty()) return item
        return item.withLore(item.get(DataComponents.LORE).orEmpty() + temporaryLore)
    }

    override fun render(): Solid {
        return Solid(1, 1, listOf(applyTempLore(renderer())))
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryPreClickEvent) {
        if (x != 0 || y != 0) return
        ctx.clickHandler(clickEvent)
    }

    class Builder {
        private var renderer: () -> ItemStack = { ItemStack.of(Material.BARRIER) }
        private var clickHandler: Context.(InventoryPreClickEvent) -> Unit = { }

        fun renderer(renderer: () -> ItemStack) {
            this.renderer = renderer
        }

        fun onClick(clickHandler: Context.(InventoryPreClickEvent) -> Unit) {
            this.clickHandler = clickHandler
        }

        fun build(): RendererComponent {
            return RendererComponent(renderer, clickHandler)
        }
    }

    inner class Context {
        fun tempLore(lore: net.kyori.adventure.text.Component, duration: Duration = 3.seconds) {
            scope.launch {
                temporaryLore.add(lore)
                try {
                    update()
                    delay(duration)
                } finally {
                    temporaryLore.remove(lore)
                }
            }
        }
    }

}
