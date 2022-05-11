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

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.KalugaState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

/**
 * State of a [LocationStateRepo]
 * @param location The [Location] associated with the state.
 * @param locationManager The [BaseLocationManager] managing the location state
 */
sealed class LocationState : KalugaState {

    abstract val location: Location

    data class Unknown(
        override val location: Location
    ) : LocationState(), SpecialFlowValue.NotImportant

    sealed class Known() : LocationState(), HandleBeforeOldStateIsRemoved<LocationState>, HandleAfterNewStateIsSet<LocationState> {

        protected abstract val locationManager: BaseLocationManager

        override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            when (oldState) {
                is Unknown -> locationManager.startMonitoringPermissions()
                else -> {}
            }
        }

        override suspend fun afterNewStateIsSet(newState: LocationState) {
            when (newState) {
                is Unknown -> locationManager.stopMonitoringPermissions()
                else -> {}
            }
        }
    }

    interface Permitted : HandleBeforeOldStateIsRemoved<LocationState>, HandleAfterNewStateIsSet<LocationState> {
        val revokePermission: suspend () -> Disabled.NotPermitted
    }

    class PermittedHandler(val location: Location, private val locationManager: BaseLocationManager) : Permitted {

        override val revokePermission: suspend () -> Disabled.NotPermitted = {
            Disabled.NotPermitted(location.unknownLocationOf(Location.UnknownLocation.Reason.PERMISSION_DENIED), locationManager)
        }

        override suspend fun afterNewStateIsSet(newState: LocationState) {
            when (newState) {
                is Unknown,
                is Initializing,
                is Disabled.NotPermitted -> locationManager.stopMonitoringLocationEnabled()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            when (oldState) {
                is Unknown,
                is Initializing,
                is Disabled.NotPermitted -> locationManager.startMonitoringLocationEnabled()
                else -> {}
            }
        }
    }

    data class Initializing(override val location: Location, override val locationManager: BaseLocationManager) : Known(), SpecialFlowValue.NotImportant {

        fun initialize(hasPermission: Boolean, enabled: Boolean): suspend () -> LocationState = suspend {
            when {
                !hasPermission -> Disabled.NotPermitted(location.unknownLocationOf(Location.UnknownLocation.Reason.PERMISSION_DENIED), locationManager)
                !enabled -> Disabled.NoGPS(location.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS), locationManager)
                else -> Enabled(location, locationManager)
            }
        }
    }

    /**
     * A [LocationState] that is not actively fetching new [Location]s.
     */
    sealed class Disabled : Known() {

        /**
         * A [LocationState.Disabled] that was disabled due to missing permissions.
         */
        data class NotPermitted(override val location: Location, override val locationManager: BaseLocationManager) : Disabled() {

            /**
             * Transforms this state into [LocationState] that has sufficient permissions
             * @param enabled `true` if GPS is turned on, `false` otherwise.
             */
            fun permit(enabled: Boolean): suspend () -> LocationState = {
                if (enabled) Enabled(location, locationManager) else NoGPS(location.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS), locationManager)
            }
        }

        /**
         * A [LocationState.Disabled] that was disabled due to GPS being turned off.
         */
        data class NoGPS(override val location: Location, override val locationManager: BaseLocationManager) : Disabled(), Permitted {

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
                super.beforeOldStateIsRemoved(oldState)
                permittedHandler.beforeOldStateIsRemoved(oldState)
                when (oldState) {
                    !is NoGPS -> if (locationManager.autoEnableLocations) locationManager.requestLocationEnable()
                    else -> {}
                }
            }

            override suspend fun afterNewStateIsSet(newState: LocationState) {
                super.afterNewStateIsSet(newState)
                permittedHandler.afterNewStateIsSet(newState)
            }
        }
    }

    /**
     * A [LocationState] that is actively updating its [Location].
     */
    data class Enabled(override val location: Location, override val locationManager: BaseLocationManager) : Known(), Permitted {

        private val permittedHandler = PermittedHandler(location, locationManager)

        /**
         * Transforms this state into a [LocationState.Disabled.NotPermitted] state.
         */
        override val revokePermission: suspend () -> Disabled.NotPermitted = permittedHandler.revokePermission

        /**
         * Transforms this state into a [LocationState.Disabled.NoGPS] state.
         */
        val disable: suspend () -> Disabled.NoGPS = {
            Disabled.NoGPS(location.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS), locationManager)
        }

        override suspend fun afterNewStateIsSet(newState: LocationState) {
            super.afterNewStateIsSet(newState)
            permittedHandler.afterNewStateIsSet(newState)
            when (newState) {
                !is Enabled -> locationManager.stopMonitoringLocation()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            super.beforeOldStateIsRemoved(oldState)
            permittedHandler.beforeOldStateIsRemoved(oldState)
            when (oldState) {
                !is Enabled -> locationManager.startMonitoringLocation()
                else -> {}
            }
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
    locationPermission: LocationPermission,
    permissions: Permissions,
    autoRequestPermission: Boolean,
    autoEnableLocations: Boolean,
    locationManagerBuilder: BaseLocationManager.Builder,
    coroutineContext: CoroutineContext
) : ColdStateFlowRepo<LocationState>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { locationState, repo ->
        val locationManager = (repo as LocationStateRepo).locationManager
        val lastKnownLocation = locationState.location
        {
            LocationState.Initializing(lastKnownLocation, locationManager)
        }
    },
    deinitChangeStateWithRepo = { locationState, _ ->
        { LocationState.Unknown(locationState.location) }
    },
    firstState = { LocationState.Unknown(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)) }
) {

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
            locationPermission: LocationPermission,
            autoRequestPermission: Boolean = true,
            autoEnableLocations: Boolean = true,
            coroutineContext: CoroutineContext = Dispatchers.Main
        ): LocationStateRepo
    }

    private val locationManager = locationManagerBuilder.create(locationPermission, permissions, autoRequestPermission, autoEnableLocations, this)
}

expect class LocationStateRepoBuilder : LocationStateRepo.Builder

internal fun Location.unknownLocationOf(reason: Location.UnknownLocation.Reason): Location {
    return when (this) {
        is Location.KnownLocation -> Location.UnknownLocation.WithLastLocation(this, reason)
        is Location.UnknownLocation.WithLastLocation -> Location.UnknownLocation.WithLastLocation(this.lastKnownLocation, reason)
        is Location.UnknownLocation.WithoutLastLocation -> Location.UnknownLocation.WithoutLastLocation(reason)
    }
}

/**
 * Transforms a [Flow] of [LocationState] into a flow of its associated [Location]
 */
fun Flow<LocationState>.location(): Flow<Location> {
    return this.filterOnlyImportant().map { it.location }
}
