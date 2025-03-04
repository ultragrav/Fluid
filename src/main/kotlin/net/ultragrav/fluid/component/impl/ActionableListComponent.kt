package net.ultragrav.fluid.component.impl

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.ultragrav.fluid.component.dimensions.Box
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.dimensions.Point
import net.ultragrav.fluid.inventory.ClickBuilder
import kotlin.math.ceil

class ActionableListComponent(
    dimensions: Dimensions,
    val actionArea: Box,
    previousPageButtonLocation: Point,
    nextPageButtonLocation: Point
) : ContainerComponent(dimensions) {
    private val actionList = addComponent(
        ListComponent<Action>(
        actionArea.dimensions,
        { it.item },
        { _, el, ev -> el.action(ev) }
    ), actionArea.x, actionArea.y)

    private val numPages get() = ceil(actionList.size / actionArea.dimensions.size.toFloat()).toInt()
    private var page = 0
        set(value) {
            field = value.coerceAtLeast(0).coerceAtMost(numPages - 1)
            actionList.offset = field * actionArea.dimensions.size

            updatePageButtons()
        }

    private val previousPageButton = addComponent(
        ButtonComponent(
        ItemStack.builder(Material.ARROW)
            .customName(Component.text("Previous Page", NamedTextColor.GRAY))
            .build(),
        clickHandler = { ev ->
            ev.isCancelled = true
            page--
        }
    ), previousPageButtonLocation.x, previousPageButtonLocation.y)

    private val nextPageButton = addComponent(
        ButtonComponent(
        ItemStack.builder(Material.ARROW)
            .customName(Component.text("Next Page", NamedTextColor.GRAY))
            .build(),
        clickHandler = { ev ->
            ev.isCancelled = true
            page++
        }
    ), nextPageButtonLocation.x, nextPageButtonLocation.y)

    private fun updatePageButtons() {
        previousPageButton.active = page > 0
        nextPageButton.active = page < numPages - 1
    }

    fun addAction(item: ItemStack, actionBuilder: ClickBuilder.() -> Unit) {
        actionList.add(Action(item, ClickBuilder().also(actionBuilder).build()))
        updatePageButtons()
    }

    init {
        updatePageButtons()
    }

    private data class Action(val item: ItemStack, val action: (InventoryPreClickEvent) -> Unit)
}