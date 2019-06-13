package com.splendo.components.location

actual open class DefaultLocationManager: LocationManager {
    actual constructor(configuration: Configuration) {

    }

    override var availability = Availability.NOT_DETERMINED

    override val location: Location?
        get() = TODO("not implemented")

    override fun requestLocation() {
        TODO("not implemented")
    }

    override fun requestAccess() {
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