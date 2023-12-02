package net.ultragrav.fluid

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

object FluidAPI {
    /**
     * Initializes the Fluid API.
     *
     * Not required when using fluid as a standalone plugin
     */
    fun init(plugin: Plugin) {
        Bukkit.getPluginManager().registerEvents(Events(), plugin)
    }
}