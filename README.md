# Fluid
A component-oriented framework for building minecraft GUIs

## Example
```kotlin
class ExampleGUI {
    val gui = FluidGUI(Component.empty(), 3)
    
    init {
        gui.addComponent(ItemStack(Material.STONE), 4, 1)
    }
}
```