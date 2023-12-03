package net.ultragrav.fluid.component.dimensions

data class Dimensions(val width: Int, val height: Int) {
    val size: Int
        get() = width * height
}