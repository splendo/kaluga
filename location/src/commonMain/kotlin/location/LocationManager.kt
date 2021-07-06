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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.basepermissions.PermissionState
import com.splendo.kaluga.basepermissions.Permissions
import com.splendo.kaluga.locationpermissions.LocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseLocationManager(
    protected val locationPermission: LocationPermission,
    private val permissions: Permissions,
    private val autoRequestPermission: Boolean,
    internal val autoEnableLocations: Boolean,
    private val locationStateRepo: LocationStateRepo
) : CoroutineScope by locationStateRepo {

    interface Builder {
        fun create(locationPermission: LocationPermission, permissions: Permissions, autoRequestPermission: Boolean, autoEnableLocations: Boolean, locationStateRepo: LocationStateRepo): BaseLocationManager
    }

    private val locationPermissionRepo get() = permissions[locationPermission]
    private var monitoringPermissionsJob: AtomicReference<Job?> = AtomicReference(null)

    internal open fun startMonitoringPermissions() {
        if (monitoringPermissionsJob.get() != null) return // optimization to skip making a job

        val job = Job(this.coroutineContext[Job])

        if (monitoringPermissionsJob.compareAndSet(null, job))
            launch(job) {
                locationPermissionRepo.collect { state ->
                    if (state is PermissionState.Denied.Requestable&& autoRequestPermission)
                        state.request(permissions.getManager(locationPermission))
                    val hasPermission = state is PermissionState.Allowed

                    locationStateRepo.takeAndChangeState { locationState ->
                        when (locationState) {
                            is LocationState.Disabled.NoGPS, is LocationState.Enabled -> if (hasPermission) locationState.remain() else (locationState as LocationState.Permitted).revokePermission
                            is LocationState.Disabled.NotPermitted -> if (hasPermission) locationState.permit(isLocationEnabled()) else locationState.remain()
                        }
                    }
                }
            }
        // else job.cancel() <-- not needed since the job is just garbage collected and never started anything.
    }

    internal open fun stopMonitoringPermissions() {
        monitoringPermissionsJob.get()?.let {
            if (monitoringPermissionsJob.compareAndSet(it, null))
                it.cancel()
        }
    }

    internal suspend fun isPermitted(): Boolean {
        return locationPermissionRepo.filterOnlyImportant().first() is PermissionState.Allowed
    }

    internal abstract suspend fun startMonitoringLocationEnabled()
    internal abstract suspend fun stopMonitoringLocationEnabled()
    internal abstract suspend fun isLocationEnabled(): Boolean
    internal abstract suspend fun requestLocationEnable()

    internal fun handleLocationEnabledChanged() {
        launch {
            locationStateRepo.takeAndChangeState { state ->
                when (state) {
                    is LocationState.Disabled.NoGPS -> if (isLocationEnabled()) state.enable else state.remain()
                    is LocationState.Disabled.NotPermitted -> state.remain()
                    is LocationState.Enabled -> if (isLocationEnabled()) state.remain() else state.disable
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
                    { state.copy(location = location.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS)) }
                }
                is LocationState.Disabled.NotPermitted -> {
                    { state.copy(location = location.unknownLocationOf(Location.UnknownLocation.Reason.PERMISSION_DENIED)) }
                }
                is LocationState.Enabled -> {
                    { state.copy(location = location) }
                }
            }
        }
    }
}

/**
 * A manager for tracking the user's [Location]
 */
expect class LocationManager : BaseLocationManager
