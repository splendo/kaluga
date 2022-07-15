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
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface LocationManager {

    sealed class Event {
        data class PermissionChanged(val hasPermission: Boolean) : Event()
        object LocationEnabled : Event()
        object LocationDisabled : Event()
    }

    val events: Flow<Event>
    val locations: Flow<Location.KnownLocation>

    val locationPermission: LocationPermission

    fun startMonitoringPermissions()
    fun stopMonitoringPermissions()
    suspend fun startMonitoringLocationEnabled()
    fun stopMonitoringLocationEnabled()
    fun isLocationEnabled(): Boolean
    suspend fun requestEnableLocation()
    suspend fun startMonitoringLocation()
    suspend fun stopMonitoringLocation()
}

abstract class BaseLocationManager(
    private val settings: Settings,
    private val coroutineScope: CoroutineScope
) : LocationManager, CoroutineScope by coroutineScope {

    companion object {
        private const val LOG_TAG = "Location Manager"
    }

    data class Settings(
        val locationPermission: LocationPermission,
        val permissions: Permissions,
        val autoRequestPermission: Boolean = true,
        val autoEnableLocations: Boolean = true,
        val locationBufferCapacity: Int = 16,
        val logger: Logger = RestrictedLogger(RestrictedLogLevel.None)
    )

    interface Builder {
        fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseLocationManager
    }

    private val logger = settings.logger

    override val locationPermission = settings.locationPermission
    private val locationPermissionRepo get() = settings.permissions[settings.locationPermission]
    private val autoRequestPermission: Boolean = settings.autoRequestPermission
    private val autoEnableLocations: Boolean = settings.autoEnableLocations

    private val eventChannel = Channel<LocationManager.Event>(UNLIMITED)
    override val events: Flow<LocationManager.Event> = eventChannel.receiveAsFlow()
    private val sharedLocations = MutableSharedFlow<Location.KnownLocation>(replay = 0, extraBufferCapacity = settings.locationBufferCapacity, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val locations: Flow<Location.KnownLocation> = sharedLocations.asSharedFlow()

    abstract val locationMonitor: LocationMonitor

    private val _monitoringPermissionsJob: AtomicReference<Job?> = AtomicReference(null)
    private var monitoringPermissionsJob: Job?
        get() = _monitoringPermissionsJob.get()
        set(value) { _monitoringPermissionsJob.set(value) }
    private val _monitoringLocationEnabledJob: AtomicReference<Job?> = AtomicReference(null)
    private var monitoringLocationEnabledJob: Job?
        get() = _monitoringLocationEnabledJob.get()
        set(value) { _monitoringLocationEnabledJob.set(value) }

    override fun startMonitoringPermissions() {
        logger.debug(LOG_TAG) { "Start monitoring permission" }
        if (monitoringPermissionsJob != null) return // optimization to skip making a job

        val job = Job(this.coroutineContext[Job])
        monitoringPermissionsJob = job
        launch(job) {
            locationPermissionRepo.filterOnlyImportant().collect { state ->
                handlePermissionState(state)
            }
        }
    }

    private fun handlePermissionState(state: PermissionState<LocationPermission>) {
        if (autoRequestPermission) {
            when (state) {
                is PermissionState.Denied.Requestable -> {
                    logger.info(LOG_TAG) { "Request Permission" }
                    state.request()
                }
                else -> {}
            }
        }

        val hasPermission = state is PermissionState.Allowed
        logger.info(LOG_TAG) { "Permission now ${if (hasPermission) "Granted" else "Denied"}" }
        emitEvent(LocationManager.Event.PermissionChanged(hasPermission))
    }

    override fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    override suspend fun startMonitoringLocationEnabled() {
        locationMonitor.startMonitoring()
        if (monitoringLocationEnabledJob != null)
            return
        monitoringLocationEnabledJob = launch {
            locationMonitor.isEnabled.collect {
                handleLocationEnabledChanged()
            }
        }
    }
    override fun stopMonitoringLocationEnabled() {
        locationMonitor.stopMonitoring()
        monitoringLocationEnabledJob?.cancel()
        monitoringLocationEnabledJob = null
    }

    override fun isLocationEnabled(): Boolean = locationMonitor.isServiceEnabled

    private fun handleLocationEnabledChanged() {
        val isEnabled = isLocationEnabled()
        logger.info(LOG_TAG) { "Location Service now ${if (isEnabled) "enabled" else "disabled"}" }
        if (isEnabled)
            emitEvent(LocationManager.Event.LocationEnabled)
        else {
            emitEvent(LocationManager.Event.LocationDisabled)
            if (autoEnableLocations) {
                launch {
                    logger.info(LOG_TAG) { "Location Service disabled. Attempt to automatically enable" }
                    requestEnableLocation()
                }
            }
        }
    }

    fun handleLocationChanged(location: Location.KnownLocation) = handleLocationChanged(listOf(location))
    fun handleLocationChanged(locations: List<Location.KnownLocation>) {
        launch {
            locations.forEach { location ->
                logger.info(LOG_TAG) { "Location changed to $location" }
                sharedLocations.tryEmit(location)
            }
        }
    }

    private fun emitEvent(event: LocationManager.Event) {
        // Channel has unlimited buffer so this will never fail due to capacity
        eventChannel.trySend(event)
    }
}

/**
 * A manager for tracking the user's [Location]
 */
expect class DefaultLocationManager : BaseLocationManager
