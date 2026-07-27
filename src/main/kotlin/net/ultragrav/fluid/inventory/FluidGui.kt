package net.ultragrav.fluid.inventory

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.ultragrav.fluid.Events
import net.ultragrav.fluid.FluidAPI
import net.ultragrav.fluid.InventoryCloseInfo
import net.ultragrav.fluid.Util
import net.ultragrav.fluid.Util.dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.inventory.shape.Shape
import net.ultragrav.fluid.render.Solid

open class FluidGui(title: Component, type: InventoryType) : ContainerComponent(type.dimensions) {
    constructor(title: Component, rows: Int) : this(title, Util.inventoryTypeForSize(rows * 9))

    val inv = FluidInventory(this, type, title)

    override var scope = FluidAPI.coroutineScope + SupervisorJob()
    private val tasks = mutableListOf<(CoroutineScope) -> Unit>()

    init {
        inv.setTag(Events.GUI_TAG, this)
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
        for ((j, i) in area.iterator(dimensions).withIndex()) {
            val element = solid.grid[j]
            inv.setItemStack(i, if (element === TRANSPARENT) ItemStack.AIR else element)
        }
    }

    fun open(player: Player): CompletableJob {
        val job = Job()
        task {
            try {
                awaitCancellation()
            } catch (e: CancellationException) {
                job.complete()
                throw e
            }
        }
        update()
        player.openInventory(inv)
        onOpen(player)
        return job
    }

    fun closeAll() {
        inv.viewers.forEach { it.closeInventory() }
    }

    override fun onOpen(player: Player) {
        super.onOpen(player)
        if (inv.viewers.size == 1) {
            scope = FluidAPI.coroutineScope + SupervisorJob()
            tasks.forEach { it(scope) }
        }
    }

    override fun onClose(event: InventoryCloseInfo) {
        super.onClose(event)
        if (inv.viewers.size == 1) {
            scope.cancel()
        }
    }

    companion object {
        val TRANSPARENT: ItemStack = ItemStack.of(Material.STRUCTURE_VOID)
    }
}
