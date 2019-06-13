package com.splendo.components.location

actual class DefaultLocationManager: LocationManager {
    actual constructor(configuration: Configuration) {

    }

    override var availability = Availability.NOT_DETERMINED

    override var location: Location? = null

    override fun requestAccess() {
        TODO("not implemented")
    }

    override fun addListener(listener: LocationListener) {
        TODO("not implemented")
    }

    override fun removeListener(listener: LocationListener) {
        TODO("not implemented")
    }

    override fun start() {
        TODO("not implemented")
    }

    override fun stop() {
        TODO("not implemented")
    }
}