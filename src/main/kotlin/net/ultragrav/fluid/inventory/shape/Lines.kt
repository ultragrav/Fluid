package net.ultragrav.fluid.inventory.shape

import net.ultragrav.fluid.component.dimensions.Dimensions

class Lines(val dimensions: Dimensions, val x: Int, val y: Int, val offset: Int, val length: Int) : Shape {
    override fun shift(shiftX: Int, shiftY: Int): Lines {
        return Lines(dimensions, x + shiftX, y + shiftY, offset, length)
    }

    override fun iterator(boundingBox: Dimensions): Iterator<Int> {
        return LinesIterator(boundingBox)
    }

    inner class LinesIterator(val box: Dimensions) : Iterator<Int> {
        private var itX = x + offset % dimensions.width
        private var itY = y + offset / dimensions.width

        override fun hasNext(): Boolean {
            return itY < y + (offset + length) / dimensions.width ||
                    (itY == y + (offset + length) / dimensions.width && itX < x + (offset + length) % dimensions.width)
        }

        override fun next(): Int {
            val ret = itX + itY * box.width
            itX++
            if (itX >= dimensions.width) {
                itX = x
                itY++
            }
            return ret
        }
    }
}