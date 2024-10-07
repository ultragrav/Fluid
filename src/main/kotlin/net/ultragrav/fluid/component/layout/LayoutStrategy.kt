package net.ultragrav.fluid.component.layout

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent

interface LayoutStrategy {
    fun layout(components: List<Component>, dimensions: Dimensions): List<ContainerComponent.Child>
}