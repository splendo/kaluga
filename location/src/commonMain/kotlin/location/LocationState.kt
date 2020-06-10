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

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.State
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * State of a [LocationStateRepo]
 * @param location The [Location] associated with the state.
 * @param locationManager The [BaseLocationManager] managing the location state
 */
sealed class LocationState(open val location: Location, private val locationManager: BaseLocationManager) : State<LocationState>() {

    @InternalCoroutinesApi
    override suspend fun initialState() {
        locationManager.startMonitoringPermissions()
    }

    override suspend fun finalState() {
        locationManager.stopMonitoringPermissions()
    }

    interface Permitted : HandleBeforeOldStateIsRemoved<LocationState>, HandleAfterNewStateIsSet<LocationState> {
        val revokePermission: suspend () -> Disabled.NotPermitted
        suspend fun initialState()
        suspend fun finalState()
    }

    class PermittedHandler(val location: Location, private val locationManager: BaseLocationManager) : Permitted {

        override val revokePermission: suspend () -> Disabled.NotPermitted = {
            Disabled.NotPermitted(Disabled.NotPermitted.generateLocation(location), locationManager)
        }

        override suspend fun afterNewStateIsSet(newState: LocationState) {
            when (newState) {
                is Disabled.NotPermitted -> locationManager.stopMonitoringLocationEnabled()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            when (oldState) {
                is Disabled.NotPermitted -> locationManager.startMonitoringLocationEnabled()
                else -> {}
            }
        }

        override suspend fun initialState() {
            locationManager.startMonitoringLocationEnabled()
        }

        override suspend fun finalState() {
            locationManager.stopMonitoringLocationEnabled()
        }
    }

    /**
     * A [LocationState] that is not actively fetching new [Location]s.
     */
    sealed class Disabled(location: Location, locationManager: BaseLocationManager) : LocationState(location, locationManager) {

        /**
         * A [LocationState.Disabled] that was disabled due to missing permissions.
         */
        data class NotPermitted(override val location: Location, private val locationManager: BaseLocationManager) : Disabled(location, locationManager) {

            companion object {
                fun generateLocation(fromLocation: Location): Location {
                    return when (fromLocation) {
                        is Location.KnownLocation -> Location.UnknownLocation.WithLastLocation(fromLocation, Location.UnknownLocation.Reason.PERMISSION_DENIED)
                        is Location.UnknownLocation.WithLastLocation -> Location.UnknownLocation.WithLastLocation(fromLocation.lastKnownLocation, Location.UnknownLocation.Reason.PERMISSION_DENIED)
                        is Location.UnknownLocation.WithoutLastLocation -> Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED)
                    }
                }
            }

            /**
             * Transforms this state into [LocationState] that has sufficient permissions
             * @param enabled `true` if GPS is turned on, `false` otherwise.
             */
            fun permit(enabled: Boolean): suspend () -> LocationState = {
                if (enabled) Enabled(location, locationManager) else NoGPS(NoGPS.generateLocation(location), locationManager)
            }
        }

        /**
         * A [LocationState.Disabled] that was disabled due to GPS being turned off.
         */
        data class NoGPS(override val location: Location, private val locationManager: BaseLocationManager) : Disabled(location, locationManager), Permitted {

            companion object {
                fun generateLocation(fromLocation: Location): Location {
                    return when (fromLocation) {
                        is Location.KnownLocation -> Location.UnknownLocation.WithLastLocation(fromLocation, Location.UnknownLocation.Reason.NO_GPS)
                        is Location.UnknownLocation.WithLastLocation -> Location.UnknownLocation.WithLastLocation(fromLocation.lastKnownLocation, Location.UnknownLocation.Reason.NO_GPS)
                        is Location.UnknownLocation.WithoutLastLocation -> Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS)
                    }
                }
            }

            private val permittedHandler = PermittedHandler(location, locationManager)

            /**
             * Transforms this state into a [LocationState.Disabled.NotPermitted] state.
             */
            override val revokePermission: suspend () -> NotPermitted = permittedHandler.revokePermission

            /**
             * Transforms this state into a [LocationState.Enabled] state.
             */
            val enable: suspend () -> Enabled = {
                Enabled(location, locationManager)
            }

            override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
                permittedHandler.beforeOldStateIsRemoved(oldState)
                when (oldState) {
                    !is NoGPS -> if (locationManager.autoEnableLocations) locationManager.requestLocationEnable()
                    else -> {}
                }
            }

            override suspend fun afterNewStateIsSet(newState: LocationState) {
                permittedHandler.afterNewStateIsSet(newState)
            }

            @InternalCoroutinesApi
            override suspend fun initialState() {
                super.initialState()
                permittedHandler.initialState()
                if (locationManager.autoEnableLocations) locationManager.requestLocationEnable()
            }

            override suspend fun finalState() {
                super.finalState()
                permittedHandler.finalState()
            }
        }
    }

    /**
     * A [LocationState] that is actively updating its [Location].
     */
    data class Enabled(override val location: Location, private val locationManager: BaseLocationManager) : LocationState(location, locationManager), Permitted {

        private val permittedHandler = PermittedHandler(location, locationManager)

        /**
         * Transforms this state into a [LocationState.Disabled.NotPermitted] state.
         */
        override val revokePermission: suspend () -> Disabled.NotPermitted = permittedHandler.revokePermission

        /**
         * Transforms this state into a [LocationState.Disabled.NoGPS] state.
         */
        val disable: suspend () -> Disabled.NoGPS = {
            Disabled.NoGPS(Disabled.NoGPS.generateLocation(location), locationManager)
        }

        override suspend fun afterNewStateIsSet(newState: LocationState) {
            permittedHandler.afterNewStateIsSet(newState)
            when (newState) {
                !is Enabled -> locationManager.stopMonitoringLocation()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            permittedHandler.beforeOldStateIsRemoved(oldState)
            when (oldState) {
                !is Enabled -> locationManager.startMonitoringLocation()
                else -> {}
            }
        }

        @InternalCoroutinesApi
        override suspend fun initialState() {
            super.initialState()
            permittedHandler.initialState()
            locationManager.startMonitoringLocation()
        }

        override suspend fun finalState() {
            super.finalState()
            permittedHandler.finalState()
            locationManager.stopMonitoringLocation()
        }
    }
}

/**
 * A [ColdStateRepo] that tracks the [LocationState] of the user.
 * Since this is a coldStateRepo location changes will only be requested when there is at least one observer.
 * @param locationPermission The [Permission.Location] to define the type of location state to track.
 * @param autoRequestPermission If 'true` the user will automatically receive a request to provide permissions when missing. Set this to `false` if manual permission requests are required.
 * @param autoEnableLocations If `true` the user will automatically receive a request to enable GPS if it is disabled. Set this to `false` if manual gps enabling is required.
 * @param locationManagerBuilder The [BaseLocationManager.Builder] to create the [LocationManager] managing the location state.
 */
class LocationStateRepo(
    locationPermission: Permission.Location,
    permissions: Permissions,
    autoRequestPermission: Boolean,
    autoEnableLocations: Boolean,
    locationManagerBuilder: BaseLocationManager.Builder,
    coroutineContext: CoroutineContext
) : ColdStateRepo<LocationState>(coroutineContext = coroutineContext) {

    /**
     * Builder for creating a [LocationStateRepo]
     */
    interface Builder {

        /**
         * Creates a [LocationStateRepo]
         * @param locationPermission The [Permission.Location] to define the type of location state to track.
         * @param autoRequestPermission If 'true` the user will automatically receive a request to provide permissions when missing. Set this to `false` if manual permission requests are required. Defaults to `true`.
         * @param autoEnableLocations If `true` the user will automatically receive a request to enable GPS if it is disabled. Set this to `false` if manual gps enabling is required. Defaults to `true`.
         * @return The created [LocationStateRepo]
         */
        fun create(
            locationPermission: Permission.Location,
            autoRequestPermission: Boolean = true,
            autoEnableLocations: Boolean = true,
            coroutineContext: CoroutineContext = MainQueueDispatcher
        ): LocationStateRepo
    }

    private var lastKnownLocation: Location = Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)
    private val locationManager = locationManagerBuilder.create(locationPermission, permissions, autoRequestPermission, autoEnableLocations, this)

    @InternalCoroutinesApi
    override suspend fun initialValue(): LocationState {
        return if (!locationManager.isPermitted()) {
            LocationState.Disabled.NotPermitted(LocationState.Disabled.NotPermitted.generateLocation(lastKnownLocation), locationManager)
        } else if (!locationManager.isLocationEnabled()) {
            LocationState.Disabled.NoGPS(LocationState.Disabled.NoGPS.generateLocation(lastKnownLocation), locationManager)
        } else {
            LocationState.Enabled(lastKnownLocation, locationManager)
        }
    }

    override suspend fun deinitialize(state: LocationState) {
        lastKnownLocation = state.location
    }
}

expect class LocationStateRepoBuilder : LocationStateRepo.Builder

/**
 * Transforms a [Flow] of [LocationState] into a flow of its associated [Location]
 */
fun Flow<LocationState>.location(): Flow<Location> {
    return this.map { it.location }
}
