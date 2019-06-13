package com.splendo.components.location

import platform.CoreLocation.*

actual open class DefaultLocationManager: LocationManager {
    private val configuration: Configuration
    actual constructor(configuration: Configuration) {
        this.configuration = configuration
    }

    override var availability = Availability.NOT_DETERMINED

    private val locationManager = CLLocationManager()


    override val location: Location?
        get() = TODO("not implemented")

    override fun requestLocation() {
        TODO("not implemented")
    }

    override fun requestAccess() {
        when (configuration.accessOptions) {
            AccessOptions.WHEN_IN_USE -> locationManager.requestWhenInUseAuthorization()
            AccessOptions.ALWAYS -> locationManager.requestAlwaysAuthorization()
        }
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