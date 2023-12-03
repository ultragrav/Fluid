package net.ultragrav.fluid.component.impl

import net.kyori.adventure.text.format.NamedTextColor
import net.ultragrav.fluid.component.dimensions.Box
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.dimensions.Point
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil

class ActionableListComponent(
    dimensions: Dimensions,
    val actionArea: Box,
    previousPageButtonLocation: Point,
    nextPageButtonLocation: Point
) : ContainerComponent(dimensions) {
    private val actionList = addComponent(ListComponent<Action>(
        actionArea.dimensions,
        { it.item },
        { _, el, ev -> el.action(ev) }
    ), actionArea.x, actionArea.y)

    private val numPages get() = ceil(actionList.size / actionArea.dimensions.size.toFloat()).toInt()
    private var page = 0
        set(value) {
            field = value
            actionList.offset = value * actionArea.dimensions.size

            previousPageButton.active = value > 0
            nextPageButton.active = value < numPages
        }

    private val previousPageButton = addComponent(ButtonComponent(
        ItemStack(Material.ARROW)
            .also {
                it.itemMeta = it.itemMeta.also { meta ->
                    meta.displayName(
                        net.kyori.adventure.text.Component.text("Previous Page", NamedTextColor.GRAY)
                    )
                }
            },
        clickHandler = { ev ->
            ev.isCancelled = true
            page--
        }
    ), previousPageButtonLocation.x, previousPageButtonLocation.y)

    private val nextPageButton = addComponent(ButtonComponent(
        ItemStack(Material.ARROW)
            .also {
                it.itemMeta = it.itemMeta.also { meta ->
                    meta.displayName(
                        net.kyori.adventure.text.Component.text("Next Page", NamedTextColor.GRAY)
                    )
                }
            },
        clickHandler = { ev ->
            ev.isCancelled = true
            page++
        }
    ), nextPageButtonLocation.x, nextPageButtonLocation.y)

    fun addAction(item: ItemStack, action: (InventoryClickEvent) -> Unit) {
        actionList.add(Action(item, action))
    }

    private data class Action(val item: ItemStack, val action: (InventoryClickEvent) -> Unit)
}