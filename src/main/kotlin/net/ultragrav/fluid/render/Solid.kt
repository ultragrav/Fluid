package net.ultragrav.fluid.render

import org.bukkit.inventory.ItemStack

class Solid(val width: Int, val height: Int, val grid: List<ItemStack?>) {
    override fun toString(): String {
        return "Solid(width=$width, height=$height)"
    }
}