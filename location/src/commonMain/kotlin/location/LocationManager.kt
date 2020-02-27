/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.location

import com.splendo.kaluga.log.debug
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseLocationManager(private val locationPermissionRepo: LocationPermissionStateRepo,
                                   private val autoRequestPermission: Boolean,
                                   internal val autoEnableLocations: Boolean,
                                   private val locationStateRepo: LocationStateRepo) : CoroutineScope by locationStateRepo {

    interface Builder {
        fun create(locationPermissionRepo: LocationPermissionStateRepo, autoRequestPermission: Boolean, autoEnableLocations: Boolean, locationStateRepo: LocationStateRepo): BaseLocationManager
    }

    private var monitoringPermissionsJob: Job? = null

    @InternalCoroutinesApi
    internal suspend fun startMonitoringPermissions() {
        if (monitoringPermissionsJob != null) return
        monitoringPermissionsJob = launch {
            locationPermissionRepo.flow().collect {
                    state ->
                when(state) {
                    is PermissionState.Denied.Requestable -> if (autoRequestPermission) state.request()
                }
                val hasPermission = state is PermissionState.Allowed
                locationStateRepo.takeAndChangeState { locationState ->
                    when(locationState) {
                        is LocationState.Disabled.NoGPS, is LocationState.Enabled -> if (hasPermission) locationState.remain else (locationState as LocationState.Permitted).revokePermission
                        is LocationState.Disabled.NotPermitted -> if (hasPermission) locationState.permit(isLocationEnabled()) else locationState.remain
                    }
                }
            }
        }
    }

    internal suspend fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    internal suspend fun isPermitted(): Boolean {
        return locationPermissionRepo.flow().first() is PermissionState.Allowed
    }

    internal abstract suspend fun startMonitoringLocationEnabled()
    internal abstract suspend fun stopMonitoringLocationEnabled()
    internal abstract suspend fun isLocationEnabled(): Boolean
    internal abstract suspend fun requestLocationEnable()

    internal fun handleLocationEnabledChanged() {
        launch {
            locationStateRepo.takeAndChangeState { state ->
                when (state) {
                    is LocationState.Disabled.NoGPS -> if (isLocationEnabled()) state.enable else state.remain
                    is LocationState.Disabled.NotPermitted -> state.remain
                    is LocationState.Enabled -> if (isLocationEnabled()) state.remain else state.disable
                }
            }
        }
    }

    internal abstract suspend fun startMonitoringLocation()
    internal abstract suspend fun stopMonitoringLocation()

    internal fun handleLocationChanged(locations: List<Location>) {
        launch {
            locations.forEach { location ->
                handleLocationChanged(location)
            }
        }
    }

    internal suspend fun handleLocationChanged(location: Location) {
        locationStateRepo.takeAndChangeState { state ->
            when (state) {
                is LocationState.Disabled.NoGPS -> {
                    { state.copy(location = LocationState.Disabled.NoGPS.generateLocation(location)) }
                }
                is LocationState.Disabled.NotPermitted -> {
                    { state.copy(location = LocationState.Disabled.NotPermitted.generateLocation(location)) }
                }
                is LocationState.Enabled -> {
                    { state.copy(location = location) }
                }
            }
        }
    }

}

expect class LocationManager : BaseLocationManager
