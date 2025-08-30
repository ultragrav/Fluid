package net.ultragrav.fluid.inventory

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.ultragrav.fluid.FluidAPI
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import kotlin.coroutines.EmptyCoroutineContext

open class FluidGui(title: Component, rows: Int) : ContainerComponent(Dimensions(9, rows)) {
    val inv = Bukkit.createInventory(Holder(), rows * 9, title)

    override var scope = FluidAPI.coroutineScope + SupervisorJob()
    private val tasks = mutableListOf<(CoroutineScope) -> Unit>()

    init {
        initializeSelfParent()
    }

    /**
     * Called when the GUI is first opened by a player. The task is
     * cancelled when the GUI is closed by the last player.
     */
    fun onFirstOpen(dispatcher: CoroutineDispatcher, task: suspend () -> Unit) {
        tasks.add {
            scope.launch(dispatcher) {
                task()
            }
        }
    }

    fun task(task: suspend () -> Unit) {
        tasks.add {
            scope.launch { task() }
        }
    }

    override fun update(area: Shape, solid: Solid) {

        val numUpdates = area.iterator(dimensions).asSequence().count()
        if (numUpdates <= 5) {
            // Small update
            for ((j, i) in area.iterator(dimensions).withIndex()) {
                inv.setItem(i, solid.grid[j])
            }
        } else {
            // Large update (send only one packet)
            val contents = inv.contents
            for ((j, i) in area.iterator(dimensions).withIndex()) {
                contents[i] = solid.grid[j]
            }
            inv.contents = contents
        }
    }

    open fun onDrag(event: InventoryDragEvent) {}

    fun open(player: HumanEntity) {
        val previous = player.openInventory.topInventory
        if (previous.holder is Holder) {
            (previous.holder as Holder).gui.onClose(InventoryCloseEvent(player.openInventory))
        }
        update()
        player.openInventory(inv)
        onOpen(player)
    }

    fun closeAll() {
        inv.close()
    }

    override fun onOpen(player: HumanEntity) {
        super.onOpen(player)
        if (inv.viewers.size == 1) {
            scope = FluidAPI.coroutineScope + SupervisorJob()
            tasks.forEach { it(scope) }
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        super.onClose(event)
        if (event.viewers.size == 1) {
            scope.cancel()
        }
    }

    inner class Holder : InventoryHolder {
        val gui: FluidGui get() = this@FluidGui

        override fun getInventory(): Inventory {
            return inv
        }
    }
}