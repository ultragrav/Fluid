package net.ultragrav.fluid.component.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.render.Solid
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

open class RendererComponent(
    private val renderer: () -> ItemStack,
    val clickHandler: Context.(InventoryClickEvent) -> Unit = { }
) : Component(Dimensions(1, 1)) {

    private val ctx = Context()

    private val temporaryLore = mutableListOf<net.kyori.adventure.text.Component>()

    private fun applyTempLore(item: ItemStack) {
        if (item.type == Material.AIR) return
        if (temporaryLore.isEmpty()) return
        val lore = item.lore().orEmpty().toMutableList()
        lore.addAll(temporaryLore)
        item.lore(lore.ifEmpty { null })
    }

    override fun render(): Solid {
        val item = renderer()
        applyTempLore(item)
        return Solid(1, 1, Collections.singletonList(item))
    }

    override fun click(x: Int, y: Int, clickEvent: InventoryClickEvent) {
        if (x != 0 || y != 0) return
        ctx.clickHandler(clickEvent)
    }

    class Builder {
        private var renderer: () -> ItemStack = { ItemStack(Material.BARRIER) }
        private var clickHandler: Context.(InventoryClickEvent) -> Unit = { }

        fun renderer(renderer: () -> ItemStack) {
            this.renderer = renderer
        }

        fun onClick(clickHandler: Context.(InventoryClickEvent) -> Unit) {
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