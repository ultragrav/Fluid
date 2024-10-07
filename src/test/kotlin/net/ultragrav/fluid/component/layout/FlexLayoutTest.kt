package net.ultragrav.fluid.component.layout

import net.ultragrav.fluid.component.Component
import net.ultragrav.fluid.component.dimensions.Dimensions
import net.ultragrav.fluid.component.impl.ContainerComponent
import net.ultragrav.fluid.component.impl.DummyComponent
import net.ultragrav.fluid.render.Solid
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FlexLayoutTest {

    @Test
    fun testLayout() {
        val layout = FlexLayout()
        val components = listOf(
            DummyComponent(Dimensions(1, 1)),
            DummyComponent(Dimensions(1, 1)),
            DummyComponent(Dimensions(1, 1))
        )

        val children = layout.layout(components, Dimensions(9, 1))
        assertEquals(3, children.size)
        assertEquals(0, children[0].x)
        assertEquals(1, children[1].x)
        assertEquals(2, children[2].x)

        val layout2 = FlexLayout(justify = FlexLayout.Justify.END)
        val children2 = layout2.layout(components, Dimensions(9, 1))
        assertEquals(6, children2[0].x)
        assertEquals(7, children2[1].x)
        assertEquals(8, children2[2].x)

        val layout3 = FlexLayout(justify = FlexLayout.Justify.CENTER)
        val children3 = layout3.layout(components, Dimensions(9, 1))
        assertEquals(3, children3[0].x)
        assertEquals(4, children3[1].x)
        assertEquals(5, children3[2].x)

        val layout4 = FlexLayout(justify = FlexLayout.Justify.SPACE_BETWEEN)
        val children4 = layout4.layout(components, Dimensions(9, 1))
        assertEquals(0, children4[0].x)
        assertEquals(4, children4[1].x)
        assertEquals(8, children4[2].x)

        val layout5 = FlexLayout(justify = FlexLayout.Justify.SPACE_AROUND)
        val children5 = layout5.layout(components, Dimensions(9, 1))
        assertEquals(1, children5[0].x)
        assertEquals(4, children5[1].x)
        assertEquals(7, children5[2].x)

        val fourComponents = listOf(
            DummyComponent(Dimensions(1, 1)),
            DummyComponent(Dimensions(1, 1)),
            DummyComponent(Dimensions(1, 1)),
            DummyComponent(Dimensions(1, 1))
        )
        val layout6 = FlexLayout(justify = FlexLayout.Justify.SPACE_EVENLY)
        val children6 = layout6.layout(fourComponents, Dimensions(9, 1))
        assertEquals(1, children6[0].x)
        assertEquals(3, children6[1].x)
        assertEquals(5, children6[2].x)
        assertEquals(7, children6[3].x)
    }
}