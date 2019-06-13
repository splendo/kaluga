package com.splendo.components.location

import kotlinx.cinterop.*
import platform.CoreLocation.*
import platform.darwin.NSObject

actual class DefaultLocationManager actual constructor(
    private val configuration: Configuration
) : LocationManager {

    override var availability = Availability.NOT_DETERMINED
    private val locationManager = CLLocationManager()
    override var location: Location? = null

    private val locationManagerDelegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            location = (didUpdateLocations.last() as? CLLocation)?.location()

            println("🛣location: $location")
        }

        override fun locationManager(
            manager: CLLocationManager,
            didChangeAuthorizationStatus: CLAuthorizationStatus
        ) {

            // 0 - notDetermined
            // 1 - restricted
            // 2 - denied
            // 3 - authorizedAlways
            // 4 - authorizedWhenInUse
            availability = when (didChangeAuthorizationStatus) {
                1, 2 -> Availability.DISABLED
                3, 4 -> Availability.AVAILABLE
                else -> Availability.NOT_DETERMINED
            }
        }
    }

    init {
        locationManager.delegate = locationManagerDelegate
    }

    // LocationManager

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

    override fun start() {
        locationManager.startUpdatingLocation()
    }

    override fun stop() {
        locationManager.stopUpdatingLocation()
    }
}

fun CLLocation.location(): Location? {
    return coordinate.useContents {
        Location(latitude = latitude, longitude = longitude)
    }
}
