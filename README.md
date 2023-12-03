# Fluid
A component-oriented framework for building minecraft GUIs

## Build
Run `gradlew build publishToMavenLocal` to build and publish to your local maven repository

## Usage
Include the dependency in your project
```kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("net.ultragrav:Fluid:1.0.0")
}
```

## Example
### GUI
```kotlin
class ExampleGUI {
    val gui = FluidGUI(Component.empty(), 3)
    
    init {
        gui.addComponent(UnitComponent(ItemStack(Material.STONE)), 4, 1)
    }
}
```

### Component
```kotlin
```