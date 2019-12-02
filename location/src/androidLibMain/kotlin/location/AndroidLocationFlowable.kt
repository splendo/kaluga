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

import com.google.android.gms.location.*
import com.splendo.kaluga.log.debug
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

data class FusedLocationProviderHandler(
    private val locationFlowable: LocationFlowable,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) :
    CoroutineScope by CoroutineScope(Dispatchers.Default + CoroutineName("Android Location Updates")) {

    private val locationCallback: LocationCallback

    init {
        val (task, locationCallback) = fusedLocationProviderClient.onLocation(
            coroutineScope = this,
            locationRequest = LocationRequest.create().setInterval(1).setMaxWaitTime(1000).setFastestInterval(1).setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY
            ),
            available = {
                debug(TAG, "available in listener: $it")
                if (!it.isLocationAvailable) {
                    debug(TAG, "set location unknown..")
                    locationFlowable.setUnknownLocation()
                }
            },
            location = { result ->
                result.toKnownLocations().forEach {
                    debug(TAG, "known locations: $it")
                    locationFlowable.set(it)
                }

            })
        this.locationCallback = locationCallback

        launch {
            fusedLocationProviderClient.lastLocation.await()?.let {
                locationFlowable.set(it.toKnownLocation())
                debug(TAG, "last location sent: $it")
            }
            task.await()
        }
    }

    companion object {
        val TAG = "HasFusedLocationProvider"
    }
}

actual class LocationFlowable : BaseLocationFlowable() {

    class Builder(private val provider: FusedLocationProviderClient) : BaseLocationFlowable.Builder {
        override fun create() = LocationFlowable().setFusedLocationProviderClient(provider)
    }

    private lateinit var locationProviderHandler: FusedLocationProviderHandler

    private fun setFusedLocationProviderClient(provider: FusedLocationProviderClient) = apply {
        locationProviderHandler = FusedLocationProviderHandler(this, provider)
    }
}
