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
import com.splendo.kaluga.location.LocationFlowableState.NoLocationClient
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.log.debug
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LocationManagerStateRepo(
    val locationFlowable: LocationFlowable
) : StateRepo<LocationFlowableState>() {

    override fun initialState(): LocationFlowableState {
        return NoLocationClient(this)
    }
}

sealed class LocationFlowableState(override val repo: LocationManagerStateRepo) : State<LocationFlowableState>(repo) {

    class NoLocationClient(override val repo: LocationManagerStateRepo) : LocationFlowableState(repo) {
        operator fun plus(locationProvider: FusedLocationProviderClient): HasFusedLocationProvider {
            return HasFusedLocationProvider(repo, locationProvider)
        }
    }

    data class HasFusedLocationProvider(
        override val repo: LocationManagerStateRepo,
        private val fusedLocationProviderClient: FusedLocationProviderClient
    ) :
        LocationFlowableState(repo),
        CoroutineScope by CoroutineScope(repo.coroutineContext + Dispatchers.Default + CoroutineName("Android Location Updates")) {
        val locationCallback: LocationCallback

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
                        repo.locationFlowable.setUnknownLocation()
                    }
                },
                location = { result ->
                    result.toKnownLocations().forEach {
                        debug(TAG, "known locations: $it")
                        repo.locationFlowable.set(it)
                    }

                })
            this.locationCallback = locationCallback

            launch {
                fusedLocationProviderClient.lastLocation.await()?.let {
                    repo.locationFlowable.set(it.toKnownLocation())
                    debug(TAG, "last location sent: $it")
                }
                task.await()
            }
        }

        fun removeFusedLocationProvider(): NoLocationClient {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            return NoLocationClient(repo)
        }

        companion object {
            val TAG = "HasFusedLocationProvider"
        }
    }
}

actual class LocationFlowable :
    BaseLocationFlowable() {

    private var stateRepo = LocationManagerStateRepo(this)

    fun setFusedLocationProviderClient(provider: FusedLocationProviderClient) {
        stateRepo.changeStateBlocking {
            when (it) {
                is NoLocationClient -> it + provider
                is LocationFlowableState.HasFusedLocationProvider -> it.removeFusedLocationProvider() + provider
            }
        }
    }
}