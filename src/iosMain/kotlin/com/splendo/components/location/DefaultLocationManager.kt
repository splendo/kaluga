package com.splendo.components.location

import kotlinx.cinterop.*
import platform.CoreLocation.*
import platform.darwin.NSObject

actual class DefaultLocationManager actual constructor(
    private val configuration: Configuration
) : LocationManager {

    override var availability = Availability.NOT_DETERMINED
    private val locationManager = CLLocationManager().apply {
        delegate = locationManagerDelegate
    }
    override var location: Location? = null

    // CLLocationManagerDelegateProtocol
    private val locationManagerDelegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            location = (didUpdateLocations.last() as? CLLocation)?.location()

            println("🛣location: $location")
        }

        /*
         0 - notDetermined
         1 - restricted
         2 - denied
         3 - authorizedAlways
         4 - authorizedWhenInUse
        */
        override fun locationManager(
            manager: CLLocationManager,
            didChangeAuthorizationStatus: CLAuthorizationStatus
        ) {
            println("🛣didChangeAuthorizationStatus: $didChangeAuthorizationStatus")
            availability = when (didChangeAuthorizationStatus) {
                1, 2 -> Availability.DISABLED
                3, 4 -> Availability.AVAILABLE
                else -> Availability.NOT_DETERMINED
            }
        }
    }

    // LocationManager}

    override fun requestAccess() {
//        locationManager.delegate = locationManagerDelegate
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
        println("🛣 start ${CLLocationManager.authorizationStatus()} / delegate: $locationManagerDelegate")
        println("location: ${locationManager.location?.location()}")
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