package net.ultragrav.fluid

import org.bukkit.plugin.java.JavaPlugin

class FluidPlugin : JavaPlugin() {
    override fun onEnable() {
        FluidAPI.init(this)
    }
}