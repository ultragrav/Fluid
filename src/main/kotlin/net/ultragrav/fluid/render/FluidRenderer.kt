package net.ultragrav.fluid.render

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import org.bukkit.inventory.ItemStack

class FluidRenderer(val width: Int, val height: Int) {
    constructor(dimensions: Dimensions) : this(dimensions.width, dimensions.height)
    constructor(component: Component) : this(component.size)

    private val elements = Array<ItemStack?>(width * height) { null }

    fun drawElement(x: Int, y: Int, element: ItemStack?) {
        elements[y * width + x] = element
    }

    fun drawSolid(x: Int, y: Int, solid: Solid) {
        if (x + solid.width > width || y + solid.height > height)
            throw IllegalArgumentException("Solid does not fit in renderer")

        for (i in 0..<solid.width) {
            for (j in 0..<solid.height) {
                drawElement(x + i, y + j, solid.grid[j * solid.width + i])
            }
        }
    }

    fun fillEmpty(element: ItemStack) {
        for (i in 0..<width) {
            for (j in 0..<height) {
                if (elements[j * width + i] == null) {
                    drawElement(i, j, element)
                }
            }
        }
    }

    fun render(): Solid {
        return Solid(width, height, elements.toList())
    }
}