package net.ultragrav.fluid.component.dimensions

data class Box(val x: Int, val y: Int, val dimensions: Dimensions) {
    val width by dimensions::width
    val height by dimensions::height
}