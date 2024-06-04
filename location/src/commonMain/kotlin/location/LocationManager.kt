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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.location.BaseLocationManager.Settings
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages monitoring for [Location]
 */
interface LocationManager {

    /**
     * Events detected by [LocationManager]
     */
    sealed class Event {

        /**
         * An [Event] indicating permissions have changed
         * @property hasPermission if `true` the permissions required for Location have been granted
         */
        data class PermissionChanged(val hasPermission: Boolean) : Event()

        /**
         * An [Event] indicating the location service has become enabled
         */
        data object LocationEnabled : Event()

        /**
         * An [Event] indicating the location service has become disabled
         */
        data object LocationDisabled : Event()
    }

    /**
     * A [Flow] of all the [Event] detected by the location manager
     */
    val events: Flow<Event>

    /**
     * A [Flow] of the [Location.KnownLocation] detected by the location manager
     */
    val locations: Flow<Location.KnownLocation>

    /**
     * The [LocationPermission] used for managing this location.
     * If [LocationPermission.precise] this will scan location at high accuracy
     * If [LocationPermission.background] this will monitor location from the background
     */
    val locationPermission: LocationPermission

    /**
     * Starts monitoring for changes to permissions related to [locationPermission]
     * This will result in [Event.PermissionChanged] on the [events] flow
     */
    suspend fun startMonitoringPermissions()

    /**
     * Stops monitoring for changes to permissions related to [locationPermission]
     */
    suspend fun stopMonitoringPermissions()

    /**
     * Starts monitoring for changes related to the location service being enabled
     * This will result in [Event.LocationEnabled] and [Event.LocationDisabled] on the [events] flow
     */
    suspend fun startMonitoringLocationEnabled()

    /**
     * Stops monitoring for changes related to the location service being enabled
     */
    suspend fun stopMonitoringLocationEnabled()

    /**
     * If `true` the location service is currently enabled
     */
    fun isLocationEnabled(): Boolean

    /**
     * Attempts to request the user to enable the location service
     */
    suspend fun requestEnableLocation()

    /**
     * Starts monitoring for new [Location.KnownLocation] that will be emitted on the [locations] flow
     */
    suspend fun startMonitoringLocation()

    /**
     * Stops monitoring for new [Location.KnownLocation]
     */
    suspend fun stopMonitoringLocation()
}

/**
 * An abstract implementation for [LocationManager]
 * @param settings the [Settings] to configure this location manager
 * @param coroutineScope the [CoroutineScope] this location manager runs on
 */
abstract class BaseLocationManager(
    private val settings: Settings,
    private val coroutineScope: CoroutineScope,
) : LocationManager, CoroutineScope by coroutineScope {

    companion object {
        private const val LOG_TAG = "Location Manager"
    }

    /**
     * Settings to configure a [BaseLocationManager]
     * @property locationPermission The [LocationPermission] to use for monitoring location changes.
     * If passing your own settings pass LocationPermission returned from settingsBuilder when you call [BaseLocationStateRepoBuilder.create]
     * @property permissions The [Permissions] managing the location permission.
     * If passing your own settings pass permissions returned from settingsBuilder when you call [BaseLocationStateRepoBuilder.create]
     * @property autoRequestPermission if `true` the location manager should automatically request permissions if not granted
     * @property autoEnableLocations if `true` the location manager should automatically enable the location service if disabled
     * @param locationBufferCapacity the maximum number of location that can be buffered, if exceed oldest is dropped
     * @param minUpdateDistanceMeters the minimum distance in meters that a location needs to change compared to the last result for a location update to trigger
     * @param logger the [Logger] to use for logging
     */
    data class Settings(
        val locationPermission: LocationPermission,
        val permissions: Permissions,
        val autoRequestPermission: Boolean = true,
        val autoEnableLocations: Boolean = true,
        val locationBufferCapacity: Int = 16,
        val minUpdateDistanceMeters: Float = 0f,
        val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    )

    /**
     * Builder for creating a [BaseLocationManager]
     */
    interface Builder {

        /**
         * Creates a [BaseLocationManager]
         * @param settings the [Settings] to configure the location manager
         * @param coroutineScope the [CoroutineScope] the location manager runs on
         */
        fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager
    }

    private val logger = settings.logger

    override val locationPermission = settings.locationPermission
    private val locationPermissionRepo get() = settings.permissions[settings.locationPermission]
    private val autoRequestPermission: Boolean = settings.autoRequestPermission
    private val autoEnableLocations: Boolean = settings.autoEnableLocations

    private val eventChannel = Channel<LocationManager.Event>(UNLIMITED)
    override val events: Flow<LocationManager.Event> = eventChannel.receiveAsFlow()
    protected val sharedLocations = MutableSharedFlow<Location.KnownLocation>(
        replay = 0,
        extraBufferCapacity = settings.locationBufferCapacity,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val locations: Flow<Location.KnownLocation> = sharedLocations.asSharedFlow()

    protected abstract val locationMonitor: LocationMonitor

    private val permissionsLock = Mutex()
    private var monitoringPermissionsJob: Job? = null
    private val enabledLock = Mutex()
    private var monitoringLocationEnabledJob: Job? = null

    override suspend fun startMonitoringPermissions() {
        permissionsLock.withLock {
            logger.debug(LOG_TAG) { "Start monitoring permission" }
            if (monitoringPermissionsJob != null) return // optimization to skip making a job

            val job = Job(this.coroutineContext[Job])
            monitoringPermissionsJob = job
            coroutineScope.launch(job) {
                locationPermissionRepo.filterOnlyImportant().collect { state ->
                    handlePermissionState(state)
                }
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

    override suspend fun stopMonitoringPermissions() = permissionsLock.withLock {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    override suspend fun startMonitoringLocationEnabled() = enabledLock.withLock {
        locationMonitor.startMonitoring()
        if (monitoringLocationEnabledJob != null) return
        monitoringLocationEnabledJob = coroutineScope.launch {
            locationMonitor.isEnabled.collect {
                handleLocationEnabledChanged()
            }
        }
    }

    override suspend fun stopMonitoringLocationEnabled() = enabledLock.withLock {
        locationMonitor.stopMonitoring()
        monitoringLocationEnabledJob?.cancel()
        monitoringLocationEnabledJob = null
    }

    override fun isLocationEnabled(): Boolean = locationMonitor.isServiceEnabled

    private fun handleLocationEnabledChanged() {
        val isEnabled = isLocationEnabled()
        logger.info(LOG_TAG) { "Location Service now ${if (isEnabled) "enabled" else "disabled"}" }
        if (isEnabled) {
            emitEvent(LocationManager.Event.LocationEnabled)
        } else {
            emitEvent(LocationManager.Event.LocationDisabled)
            if (autoEnableLocations) {
                launch {
                    logger.info(LOG_TAG) { "Location Service disabled. Attempt to automatically enable" }
                    requestEnableLocation()
                }
            }
        }
    }

    protected open fun handleLocationChanged(location: Location.KnownLocation) = handleLocationChanged(listOf(location))
    protected open fun handleLocationChanged(locations: List<Location.KnownLocation>) {
        locations.forEach { location ->
            logger.info(LOG_TAG) { "Location changed to $location" }
            sharedLocations.tryEmit(location) // buffer is DROP_OLDEST so this will always return `true`
        }
    }

    private fun emitEvent(event: LocationManager.Event) {
        // Channel has unlimited buffer so this will never fail due to capacity
        eventChannel.trySend(event)
    }
}

/**
 * A default implementation of [BaseLocationManager]
 */
expect class DefaultLocationManager : BaseLocationManager {
    override val locationMonitor: LocationMonitor

    override suspend fun requestEnableLocation()
    override suspend fun startMonitoringLocation()
    override suspend fun stopMonitoringLocation()
}
