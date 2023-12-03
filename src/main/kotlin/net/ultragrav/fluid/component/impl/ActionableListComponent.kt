package net.ultragrav.fluid.component.impl

import net.kyori.adventure.text.format.NamedTextColor
import net.ultragrav.fluid.component.dimensions.Box
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.dimensions.Point
import net.ultragrav.fluid.render.Solid
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

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

    private var page = 0
        set(value) {
            field = value
            actionList.offset = value * actionArea.dimensions.size
            previousPageButton.update()
            nextPageButton.update()
        }

    private val previousPageButton = addComponent(object : UnitComponent(
        ItemStack(Material.ARROW)
            .also {
                it.itemMeta = it.itemMeta.also { meta ->
                    meta.displayName(
                        net.kyori.adventure.text.Component.text("Previous Page", NamedTextColor.GRAY)
                    )
                }
            },
        { ev ->
            ev.isCancelled = true
            page--
        }
    ) {
        override fun render(): Solid {
            return if (page == 0) {
                Solid(1, 1, Collections.singletonList(null))
            } else {
                super.render()
            }
        }
    }, previousPageButtonLocation.x, previousPageButtonLocation.y)

    private val nextPageButton = addComponent(object : UnitComponent(
        ItemStack(Material.ARROW)
            .also {
                it.itemMeta = it.itemMeta.also { meta ->
                    meta.displayName(
                        net.kyori.adventure.text.Component.text("Next Page", NamedTextColor.GRAY)
                    )
                }
            },
        { ev ->
            ev.isCancelled = true
            page++
        }
    ) {
        override fun render(): Solid {
            return if (page == actionList.size / actionArea.dimensions.size) {
                Solid(1, 1, Collections.singletonList(null))
            } else {
                super.render()
            }
        }
    }, nextPageButtonLocation.x, nextPageButtonLocation.y)

    fun addAction(item: ItemStack, action: (InventoryClickEvent) -> Unit) {
        actionList.add(Action(item, action))
    }

    data class Action(val item: ItemStack, val action: (InventoryClickEvent) -> Unit)
}