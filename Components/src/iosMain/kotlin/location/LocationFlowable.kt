package com.splendo.kaluga.location
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

import com.splendo.kaluga.location.Location.Time.*
import com.splendo.kaluga.location.Location.UnknownReason.*
import com.splendo.kaluga.location.LocationFlowable.CLAuthorizationStatusKotlin.*
import com.splendo.kaluga.util.byOrdinalOrDefault
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
            runBlocking {
                (didUpdateLocations.last() as? CLLocation)?.kalugaLocation()?.also { location ->
                    set(location)
                }
            }
        }

        override fun locationManager(manager: CLLocationManager, didUpdateToLocation: CLLocation, fromLocation: CLLocation) = runBlocking {
            set(didUpdateToLocation.kalugaLocation())
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

fun CLLocation.kalugaLocation(): Location.KnownLocation {
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
