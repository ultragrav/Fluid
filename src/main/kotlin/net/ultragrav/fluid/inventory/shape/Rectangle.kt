package net.ultragrav.fluid.inventory.shape

import net.ultragrav.fluid.component.dimensions.Dimensions

class Rectangle(val dimensions: Dimensions, val x: Int = 0, val y: Int = 0) : Shape() {
    override fun shift(shiftX: Int, shiftY: Int): Shape {
        return Rectangle(dimensions, x + shiftX, y + shiftY)
    }

    override fun iterator(): Iterator<Int> {
        return RectangleIterator()
    }

    inner class RectangleIterator : Iterator<Int> {
        private var itX = x
        private var itY = y

        override fun hasNext(): Boolean {
            return itY < dimensions.height
        }

        override fun next(): Int {
            val ret = itX + itY * dimensions.width
            itX++
            if (itX >= dimensions.width) {
                itX = x
                itY++
            }
            return ret
        }
    }
}