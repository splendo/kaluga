package com.splendo.mpp.location

import com.splendo.mpp.location.Location.Time.*
import com.splendo.mpp.location.Location.UnknownReason.*
import com.splendo.mpp.location.LocationFlowable.CLAuthorizationStatusKotlin.*
import com.splendo.mpp.util.byOrdinalOrDefault
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.CoreLocation.*
import platform.Foundation.NSError
import platform.Foundation.timeIntervalSince1970
import platform.darwin.NSObject

actual class LocationFlowable:
    BaseLocationFlowable() {

    @Suppress("EnumEntryName") // we are modeling an iOS construct so we will stick as close to it as possible
    private enum class CLAuthorizationStatusKotlin {
        // https://developer.apple.com/documentation/corelocation/clauthorizationstatus
        notDetermined,
        restricted,
        denied,
        authorizedAlways,
        authorizedWhenInUse
    }

    private val locationManagerDelegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>
        ) {
            println("locations:$didUpdateLocations")
            runBlocking {
                (didUpdateLocations.last() as? CLLocation)?.mppLocation()?.also { location ->
                    set(location)
                }
            }
        }

        override fun locationManager(manager: CLLocationManager, didUpdateToLocation: CLLocation, fromLocation: CLLocation) = runBlocking {
            set(didUpdateToLocation.mppLocation())
        }

        override fun locationManager(manager: CLLocationManager, didFinishDeferredUpdatesWithError: NSError?) = runBlocking {
            setUnknownLocation()
        }

        override fun locationManager(
            manager: CLLocationManager,
            didChangeAuthorizationStatus: CLAuthorizationStatus
        ) = runBlocking {
                when (Enum.byOrdinalOrDefault(didChangeAuthorizationStatus,
                    notDetermined
                )) {
                    restricted -> setUnknownLocation(NO_PERMISSION_GRANTED)
                    denied -> setUnknownLocation(PERMISSION_DENIED)
                    authorizedAlways, authorizedWhenInUse -> {
                        manager.startUpdatingLocation()
                    } // do nothing, a location should be published on it's own
                    else -> setUnknownLocation()
                }
            }
    }



    fun addCLLocationManager(locationManager: CLLocationManager = CLLocationManager()):CLLocationManager {
        locationManager.delegate = locationManagerDelegate
        return locationManager
    }
}

fun CLLocation.mppLocation(): Location.KnownLocation {
    return coordinate.useContents {
        Location.KnownLocation(
            latitude = latitude,
            longitude = longitude,
            altitude = altitude,
            horizontalAccuracy = horizontalAccuracy,
            verticalAccuracy = verticalAccuracy,
            course = course,
            speed = speed,
            time = MeasuredTime(timestamp.timeIntervalSince1970.toLong() * 1000L)
        )

    }
}
