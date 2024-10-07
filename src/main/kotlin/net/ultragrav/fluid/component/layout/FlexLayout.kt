package net.ultragrav.fluid.component.layout

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent

class FlexLayout(
    val direction: Direction = Direction.HORIZONTAL,
    val justify: Justify = Justify.START,
    val align: Justify = Justify.START,
    val wrap: Boolean = false
) : LayoutStrategy {

    enum class Direction {
        HORIZONTAL, VERTICAL
    }

    enum class Justify {
        START, CENTER, END, SPACE_BETWEEN, SPACE_AROUND, SPACE_EVENLY
    }

    private enum class Axis {
        INLINE, CROSS
    }

    private inner class TwoAxis(var inline: Int, var cross: Int) {

        constructor(dimensions: Dimensions) : this(0, 0) {
            if (direction == Direction.HORIZONTAL) {
                inline = dimensions.width
                cross = dimensions.height
            } else {
                inline = dimensions.height
                cross = dimensions.width
            }
        }

        operator fun get(axis: Axis): Int {
            return if (axis == Axis.INLINE) inline else cross
        }
        operator fun set(axis: Axis, value: Int) {
            if (axis == Axis.INLINE) inline = value
            else cross = value
        }
    }

    private interface Flexible {
        var pos: Int
        val size: Int
    }

    private inner class FlexItem(
        val component: Component,
        override var pos: Int,
    ) : Flexible {
        private val dims = TwoAxis(component.dimensions)
        override val size: Int
            get() = dims[Axis.INLINE]
    }

    private inner class FlexGroup(
        val members: List<Flexible>,
        override var pos: Int
    ) : Flexible {
        override val size: Int
            get() = members.sumOf { it.size }
    }
    override fun layout(components: List<Component>, dimensions: Dimensions): List<ContainerComponent.Child> {
        if (dimensions.width == 0 || dimensions.height == 0) {
            return emptyList()
        }
        val dims = TwoAxis(dimensions)
        val items = components.map { FlexItem(it, 0) }

        // Calculate which items are in which rows
        val rows = calculateRows(items, dims.inline)
        rows.forEach { flex(it, justify, dims.inline) }
        val crossGroup = FlexGroup(rows, 0)
        flex(crossGroup, align, dims.cross)

        val children = mutableListOf<ContainerComponent.Child>()
        for (row in rows) {
            for (item in row.members) {
                val x = if (direction == Direction.HORIZONTAL) item.pos else row.pos
                val y = if (direction == Direction.HORIZONTAL) row.pos else item.pos
                children.add(ContainerComponent.Child((item as FlexItem).component, x, y))
            }
        }

        return children
    }

    private fun calculateRows(items: List<FlexItem>, inlineSize: Int): List<FlexGroup> {
        if (!wrap && items.size > inlineSize) return emptyList()
        var sizePartialSums = listOf(0) + items.map { it.size }.runningReduce { acc, i -> acc + i }
        var itemQueue = items.toList()
        val rows: MutableList<FlexGroup> = mutableListOf()
        while (itemQueue.isNotEmpty()) {
            val row = sizePartialSums.takeWhile { it - sizePartialSums.first() <= inlineSize }
            rows.add(FlexGroup(itemQueue.take(row.size), 0))
            sizePartialSums = sizePartialSums.drop(row.size)
            itemQueue = itemQueue.drop(row.size)
        }
        return rows
    }

    private fun flex(group: FlexGroup, justify: Justify, limit: Int) {
        when (justify) {
            Justify.START -> {
                var acc = 0
                for (item in group.members) {
                    item.pos = acc
                    acc += item.size
                }
            }
            Justify.END -> {
                var acc = limit
                for (item in group.members.reversed()) {
                    acc -= item.size
                    item.pos = acc
                }
            }
            Justify.CENTER -> {
                var acc = (limit - group.size) / 2
                for (item in group.members) {
                    item.pos = acc
                    acc += item.size
                }
            }
            Justify.SPACE_BETWEEN -> {
                val space = limit - group.size
                val gap = space / (group.members.size - 1).coerceAtLeast(1)
                var acc = 0
                for (item in group.members) {
                    item.pos = acc
                    acc += item.size + gap
                }
            }
            Justify.SPACE_AROUND -> {
                val space = limit - group.size
                val gap = space / group.members.size
                var acc = gap / 2
                for (item in group.members) {
                    item.pos = acc
                    acc += item.size + gap
                }
            }
            Justify.SPACE_EVENLY -> {
                val space = limit - group.size
                val gap = space / (group.members.size + 1).coerceAtLeast(1)
                var acc = gap
                for (item in group.members) {
                    item.pos = acc
                    acc += item.size + gap
                }
            }
        }
    }
}