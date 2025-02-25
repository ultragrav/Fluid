package net.ultragrav.fluid

object FluidAPI {
    /**
     * Initializes the Fluid API.
     *
     * Not required when using fluid as a standalone plugin
     */
    fun init() {
        Events.register()
    }
}