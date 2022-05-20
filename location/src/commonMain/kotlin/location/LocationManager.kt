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
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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
    abstract val locationMonitor: LocationMonitor
    private val monitoringPermissionsJob: AtomicReference<Job?> = AtomicReference(null)
    private val _monitoringLocationEnabledJob: AtomicReference<Job?> = AtomicReference(null)
    private var monitoringLocationEnabledJob: Job?
        get() = _monitoringLocationEnabledJob.get()
        set(value) { _monitoringLocationEnabledJob.set(value) }

    open fun startMonitoringPermissions() {
        if (monitoringPermissionsJob.get() != null) return // optimization to skip making a job

        val job = Job(this.coroutineContext[Job])

        if (monitoringPermissionsJob.compareAndSet(null, job))
            launch(job) {
                locationPermissionRepo.filterOnlyImportant().collect { state ->
                    if (state is PermissionState.Denied.Requestable && autoRequestPermission)
                        state.request()
                    val hasPermission = state is PermissionState.Allowed

                    locationStateRepo.takeAndChangeState(remainIfStateNot = LocationState.Active::class) { locationState ->
                        when (locationState) {
                            is LocationState.Initializing -> locationState.initialize(hasPermission, isLocationEnabled())
                            is LocationState.Disabled.NoGPS, is LocationState.Enabled -> if (hasPermission) locationState.remain() else (locationState as LocationState.Permitted).revokePermission
                            is LocationState.Disabled.NotPermitted -> if (hasPermission) locationState.permit(isLocationEnabled()) else locationState.remain()
                        }
                    }
                }
            }
        // else job.cancel() <-- not needed since the job is just garbage collected and never started anything.
    }

    open fun stopMonitoringPermissions() {
        monitoringPermissionsJob.get()?.let {
            if (monitoringPermissionsJob.compareAndSet(it, null))
                it.cancel()
        }
    }

    open suspend fun startMonitoringLocationEnabled() {
        locationMonitor.startMonitoring()
        if (monitoringLocationEnabledJob != null)
            return
        monitoringLocationEnabledJob = launch {
            locationMonitor.isEnabled.collect {
                handleLocationEnabledChanged()
            }
        }
    }
    open fun stopMonitoringLocationEnabled() {
        locationMonitor.stopMonitoring()
        monitoringLocationEnabledJob?.cancel()
        monitoringLocationEnabledJob = null
    }
    internal fun isLocationEnabled(): Boolean = locationMonitor.isServiceEnabled
    abstract suspend fun requestLocationEnable()

    private suspend fun handleLocationEnabledChanged() {
        locationStateRepo.takeAndChangeState(remainIfStateNot = LocationState.Active::class) { state ->
            when (state) {
                is LocationState.Initializing -> state.remain()
                is LocationState.Disabled.NoGPS -> if (isLocationEnabled()) state.enable else state.remain()
                is LocationState.Disabled.NotPermitted -> state.remain()
                is LocationState.Enabled -> if (isLocationEnabled()) state.remain() else state.disable
            }
        }
    }

    abstract suspend fun startMonitoringLocation()
    abstract suspend fun stopMonitoringLocation()

    fun handleLocationChanged(locations: List<Location>) {
        launch {
            locations.forEach { location ->
                handleLocationChanged(location)
            }
        }
    }

    suspend fun handleLocationChanged(location: Location) {
        locationStateRepo.takeAndChangeState(remainIfStateNot = LocationState.Active::class) { state ->
            when (state) {
                is LocationState.Initializing -> {
                    { state.copy(location = location) }
                }
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
