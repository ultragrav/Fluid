package net.ultragrav.fluid.inventory.shape

import net.ultragrav.fluid.component.dimensions.Dimensions

class Rectangle(val dimensions: Dimensions, val x: Int = 0, val y: Int = 0) : Shape {
    override fun shift(shiftX: Int, shiftY: Int): Shape {
        return Rectangle(dimensions, x + shiftX, y + shiftY)
    }

    override fun iterator(boundingBox: Dimensions): Iterator<Int> {
        return RectangleIterator(boundingBox)
    }

    inner class RectangleIterator(val box: Dimensions) : Iterator<Int> {
        private var itX = x
        private var itY = y

        override fun hasNext(): Boolean {
            return itY < dimensions.height - 1 ||
                    (itY == dimensions.height - 1 && itX < dimensions.width)
        }

        override fun next(): Int {
            val ret = itX + itY * box.width
            itX++
            if (itX >= x + dimensions.width) {
                itX = x
                itY++
            }
            return ret
        }
    }
}