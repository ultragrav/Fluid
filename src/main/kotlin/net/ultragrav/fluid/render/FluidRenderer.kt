package net.ultragrav.fluid.render

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import org.bukkit.inventory.ItemStack

class FluidRenderer(val width: Int, val height: Int) {
    constructor(dimensions: Dimensions) : this(dimensions.width, dimensions.height)
    constructor(component: Component) : this(component.dimensions)

    private val elements = Array<ItemStack?>(width * height) { null }

    fun drawElement(x: Int, y: Int, element: ItemStack?) {
        elements[y * width + x] = element
    }

    fun drawSolid(x: Int, y: Int, solid: Solid) {
        for (i in 0..<solid.width) {
            for (j in 0..<solid.height) {
                val ex = x + i
                val ey = y + j
                if (ex < 0 || ex >= width || ey < 0 || ey >= height) continue
                drawElement(x + i, y + j, solid.grid[j * solid.width + i])
            }
        }
    }

    fun fill(element: ItemStack) {
        for (i in 0..<width) {
            for (j in 0..<height) {
                drawElement(i, j, element)
            }
        }
    }

    fun render(): Solid {
        return Solid(width, height, elements.toList())
    }
}