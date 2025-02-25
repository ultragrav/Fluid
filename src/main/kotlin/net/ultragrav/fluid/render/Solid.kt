package net.ultragrav.fluid.render

import net.minestom.server.item.ItemStack

class Solid(val width: Int, val height: Int, val grid: List<ItemStack>) {
    override fun toString(): String {
        return "Solid(width=$width, height=$height)"
    }
}