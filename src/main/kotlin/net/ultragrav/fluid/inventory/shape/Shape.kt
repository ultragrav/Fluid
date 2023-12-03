package net.ultragrav.fluid.inventory.shape

import net.ultragrav.fluid.component.dimensions.Dimensions

interface Shape {
    fun shift(shiftX: Int = 0, shiftY: Int = 0): Shape
    fun iterator(boundingBox: Dimensions): Iterator<Int>
}