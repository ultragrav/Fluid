package net.ultragrav.fluid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.minestom.server.item.ItemStack
import net.ultragrav.fluid.inventory.FluidGui

object FluidAPI {

    internal var coroutineScope = CoroutineScope(Dispatchers.Default)

    /**
     * Initializes the Fluid API.
     */
    fun init(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {
        Events.register()
        setCoroutineScope(scope)
    }

    fun setCoroutineScope(scope: CoroutineScope) {
        coroutineScope = scope
    }
}

fun ItemStack.isFluidTransparent() = this === FluidGui.TRANSPARENT
