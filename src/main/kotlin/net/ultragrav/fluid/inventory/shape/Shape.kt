package net.ultragrav.fluid.inventory.shape

abstract class Shape : Iterable<Int> {
    abstract fun shift(shiftX: Int = 0, shiftY: Int = 0): Shape
}