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

import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.splendo.kaluga.log.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val ON_LOCATION_TAG = "onLocation"

fun FusedLocationProviderClient.onLocation(
    coroutineScope: CoroutineScope,
    locationRequest: LocationRequest,
    available: suspend (LocationAvailability) -> Unit,
    location: suspend (LocationResult) -> Unit
): Pair<Task<Void>, LocationCallback> {
    val locationCallback = object : LocationCallback() {
        override fun onLocationAvailability(availability: LocationAvailability?) {
            debug(ON_LOCATION_TAG, "Availability: $availability")
            availability?.let {
                coroutineScope.launch {
                    debug(ON_LOCATION_TAG, "Availability inside scope: $availability")
                    available(it)
                }
            }
        }

        override fun onLocationResult(result: LocationResult?) {
            debug(ON_LOCATION_TAG, "Result: $result")
            result?.let { coroutineScope.launch { location(it) } }
        }
    }

    return Pair(requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper()), locationCallback)
}

fun android.location.Location.toKnownLocation(): Location.KnownLocation {
    return Location.KnownLocation(
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        horizontalAccuracy = accuracy.toDouble(),
        verticalAccuracy = verticalAccuracyMeters.toDouble(),
        speed = speed.toDouble(),
        time = Location.Time.MeasuredTime(time)
    )
}

fun LocationResult.toKnownLocations(): List<Location.KnownLocation> {
    return locations.mapNotNull {
        it.toKnownLocation()
    }
}
