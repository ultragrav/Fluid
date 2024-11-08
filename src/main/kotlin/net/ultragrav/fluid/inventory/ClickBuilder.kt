package net.ultragrav.fluid.inventory

import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

class ClickBuilder {
    companion object {
        private val LEFT_CLICKS = arrayOf(ClickType.LEFT, ClickType.SHIFT_LEFT)
        private val RIGHT_CLICKS = arrayOf(ClickType.RIGHT, ClickType.SHIFT_RIGHT)
    }

    private var default: (InventoryClickEvent) -> Unit = { it.isCancelled = true }
    private val clickHandlers = mutableMapOf<ClickType, (InventoryClickEvent) -> Unit>()

    fun left(click: InventoryClickEvent.() -> Unit) = assign(LEFT_CLICKS, click)
    fun shiftLeft(click: InventoryClickEvent.() -> Unit) = assign(ClickType.SHIFT_LEFT, click)
    fun right(click: InventoryClickEvent.() -> Unit) = assign(RIGHT_CLICKS, click)
    fun shiftRight(click: InventoryClickEvent.() -> Unit) = assign(ClickType.SHIFT_RIGHT, click)
    fun middle(click: InventoryClickEvent.() -> Unit) = assign(ClickType.MIDDLE, click)
    fun drop(click: InventoryClickEvent.() -> Unit) = assign(ClickType.DROP, click)

    fun assign(type: ClickType, click: InventoryClickEvent.() -> Unit): ClickBuilder {
        clickHandlers[type] = click
        return this
    }

    fun assign(types: Array<ClickType>, click: InventoryClickEvent.() -> Unit): ClickBuilder {
        types.forEach { clickHandlers[it] = click }
        return this
    }

    fun default(click: InventoryClickEvent.() -> Unit): ClickBuilder {
        default = click
        return this
    }

    fun build(): (InventoryClickEvent) -> Unit {
        return { (clickHandlers[it.click] ?: default)(it) }
    }
}