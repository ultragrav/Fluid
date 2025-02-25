package net.ultragrav.fluid.inventory

import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.click.ClickType

class ClickBuilder {
    private var default: (InventoryPreClickEvent) -> Unit = { it.isCancelled = true }
    private val clickHandlers = mutableMapOf<ClickType, (InventoryPreClickEvent) -> Unit>()

    fun left(click: InventoryPreClickEvent.() -> Unit) = assign(ClickType.LEFT_CLICK, click)
    fun right(click: InventoryPreClickEvent.() -> Unit) = assign(ClickType.RIGHT_CLICK, click)
    fun shift(click: InventoryPreClickEvent.() -> Unit) = assign(ClickType.START_SHIFT_CLICK, click)
    fun drop(click: InventoryPreClickEvent.() -> Unit) = assign(ClickType.DROP, click)

    fun assign(type: ClickType, click: InventoryPreClickEvent.() -> Unit): ClickBuilder {
        clickHandlers[type] = click
        return this
    }

    fun assign(types: Array<ClickType>, click: InventoryPreClickEvent.() -> Unit): ClickBuilder {
        types.forEach { clickHandlers[it] = click }
        return this
    }

    fun default(click: InventoryPreClickEvent.() -> Unit): ClickBuilder {
        default = click
        return this
    }

    fun build(): (InventoryPreClickEvent) -> Unit {
        return { (clickHandlers[it.clickType] ?: default)(it) }
    }
}