package net.ultragrav.fluid.component.impl

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.ultragrav.fluid.component.dimensions.Box
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.dimensions.Point
import kotlin.math.ceil

class PagedImmutableListComponent<T>(
    dimensions: Dimensions,
    val actionArea: Box,
    previousPageButtonLocation: Point,
    nextPageButtonLocation: Point,
    renderer: (T) -> ItemStack,
    clickHandler: (Int, T, InventoryPreClickEvent) -> Unit = { _, _, _ -> },
    list: List<T>
) : ContainerComponent(dimensions) {
    private val actionList = addComponent(
        ImmutableListComponent<T>(
            actionArea.dimensions,
            renderer,
            clickHandler,
            list
        ), actionArea.x, actionArea.y
    )

    private val numPages = ceil(list.size / actionArea.dimensions.size.toFloat()).toInt()
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

    init {
        updatePageButtons()
    }
}