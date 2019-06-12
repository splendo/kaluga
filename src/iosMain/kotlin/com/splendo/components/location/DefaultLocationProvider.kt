package com.splendo.components.location
import platform.CoreLocation.*

actual open class DefaultLocationProvider: LocationProvider {
    override val location: Location?
        get() = TODO("not implemented")

    override fun requestLocation() {
        TODO("not implemented")
    }

    override fun addListener(listener: LocationListener) {
        TODO("not implemented")
    }

    override fun removeListener(listener: LocationListener) {
        TODO("not implemented")
    }

    override fun startListening() {
        TODO("not implemented")
    }

    override fun stopListening() {
        TODO("not implemented")
    }
}