package net.ultragrav.fluid

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.ultragrav.fluid.inventory.FluidGui
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

object FluidAPI {

    internal var coroutineScope = CoroutineScope(Dispatchers.Default)

    /**
     * Initializes the Fluid API.
     *
     * Not required when using fluid as a standalone plugin
     */
    fun init(plugin: Plugin, scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {
        Bukkit.getPluginManager().registerEvents(Events(), plugin)
        setCoroutineScope(scope)
    }

    fun setCoroutineScope(scope: CoroutineScope) {
        coroutineScope = scope
    }
}

fun ItemStack.isFluidTransparent() = this === FluidGui.TRANSPARENT