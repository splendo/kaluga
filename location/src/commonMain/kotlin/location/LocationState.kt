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
import com.splendo.kaluga.base.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.permissions.base.PermissionState.Inactive
import com.splendo.kaluga.permissions.base.PermissionState.Initialized
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

/**
 * A [KalugaState] of a [Location]
 */
sealed interface LocationState : KalugaState {

    /**
     * The [Location] during this state
     */
    val location: Location

    /**
     * A [LocationState] indicating observation is not active
     */
    sealed interface Inactive :
        LocationState,
        SpecialFlowValue.NotImportant

    /**
     * An [Inactive] State indicating observation has not started yet
     */
    interface NotInitialized : Inactive

    /**
     * An [Inactive] State indicating observation has stopped after being started
     */
    interface Deinitialized : Inactive {

        /**
         * Transitions into an [Initializing] State
         */
        val reinitialize: suspend () -> Initializing
    }

    /**
     * A [LocationState] indicating observation has started
     */
    sealed interface Active : LocationState {

        /**
         * Transitions into a [Deinitialized] State
         */
        val deinitialized: suspend () -> Deinitialized
    }

    /**
     * An [Active] State indicating the state is transitioning from [Inactive] to [Initialized]
     */
    interface Initializing :
        Active,
        SpecialFlowValue.NotImportant {

        /**
         * Transitions into an [Initialized] State
         * @param hasPermission if `true` the [com.splendo.kaluga.permissions.location.LocationPermission] has been granted
         * @param enabled if `true` the location service is enabled
         * @return method for transitioning into an [Initialized] State
         */
        fun initialize(hasPermission: Boolean, enabled: Boolean): suspend () -> Initialized
    }

    /**
     * An [Active] State indicating observation has started and initialization has completed
     */
    sealed interface Initialized : Active

    /**
     * An [Initialized] State indicating the [com.splendo.kaluga.permissions.location.LocationPermission] has been granted
     */
    sealed interface Permitted : Initialized {

        /**
         * Transitions into a [Disabled.NotPermitted] State
         */
        val revokePermission: suspend () -> Disabled.NotPermitted
    }

    /**
     * An [Initialized] State indicating that location is not being collected
     */
    sealed interface Disabled : Initialized {

        /**
         * A [Disabled] State indicating the Location service is disabled
         */
        interface NoGPS :
            Disabled,
            Permitted {

            /**
             * Transitions into an [Enabled] State
             */
            val enable: suspend () -> Enabled
        }

        /**
         * A [Disabled] State indicating the [com.splendo.kaluga.permissions.location.LocationPermission] has not been granted
         */
        interface NotPermitted : Disabled {

            /**
             * Transitions into a [Permitted] State
             * @param enabled if `true` the Location service is enabled
             * @return method for transition into a [Permitted] State
             */
            fun permit(enabled: Boolean): suspend () -> Permitted
        }
    }

    /**
     * A [Permitted] State indicating location is being collected
     */
    interface Enabled : Permitted {

        /**
         * Transitions into a [Disabled.NoGPS] State
         */
        val disable: suspend () -> Disabled.NoGPS

        /**
         * Transitions into an [Enabled] State with a new [Location.KnownLocation]
         * @param location the new [Location.KnownLocation]
         * @return method for transitioning into an [Enabled] State with the new [location]
         */
        fun updateWithLocation(location: Location.KnownLocation): suspend () -> Enabled
    }
}

internal sealed class LocationStateImpl {

    abstract val location: Location

    data object NotInitialized : LocationStateImpl(), LocationState.NotInitialized {
        override val location: Location = Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)

        fun startInitializing(locationManager: LocationManager): suspend () -> Initializing = { Initializing(location, locationManager) }
    }

    data class Deinitialized(override val location: Location, internal val locationManager: LocationManager) :
        LocationStateImpl(),
        LocationState.Deinitialized {
        override val reinitialize: suspend () -> LocationState.Initializing = { Initializing(location, locationManager) }
    }

    sealed class Active :
        LocationStateImpl(),
        HandleBeforeOldStateIsRemoved<LocationState>,
        HandleAfterNewStateIsSet<LocationState> {

        protected abstract val locationManager: LocationManager

        val deinitialized: suspend () -> Deinitialized = { Deinitialized(location, locationManager) }

        override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            when (oldState) {
                is LocationState.Inactive -> locationManager.startMonitoringPermissions()
                is LocationState.Active -> {}
            }
        }

        override suspend fun afterNewStateIsSet(newState: LocationState) {
            when (newState) {
                is LocationState.Inactive -> locationManager.stopMonitoringPermissions()
                is LocationState.Active -> {}
            }
        }
    }

    class PermittedHandler(val location: Location, private val locationManager: LocationManager) {

        internal val revokePermission: suspend () -> Disabled.NotPermitted = {
            Disabled.NotPermitted(location, locationManager)
        }

        internal suspend fun afterNewStateIsSet(newState: LocationState) {
            when (newState) {
                is LocationState.Inactive,
                is LocationState.Initializing,
                is LocationState.Disabled.NotPermitted,
                -> locationManager.stopMonitoringLocationEnabled()
                else -> {}
            }
        }

        internal suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            when (oldState) {
                is LocationState.Inactive,
                is LocationState.Initializing,
                is LocationState.Disabled.NotPermitted,
                -> locationManager.startMonitoringLocationEnabled()
                else -> {}
            }
        }
    }

    data class Initializing(override val location: Location, override val locationManager: LocationManager) :
        Active(),
        LocationState.Initializing {

        override fun initialize(hasPermission: Boolean, enabled: Boolean): suspend () -> LocationState.Initialized = suspend {
            when {
                !hasPermission -> Disabled.NotPermitted(location, locationManager)
                !enabled -> Disabled.NoGPS(location, locationManager)
                else -> Enabled(location.known.orUnknown, locationManager)
            }
        }
    }

    sealed class Disabled : Active() {

        class NotPermitted(lastKnownLocation: Location, override val locationManager: LocationManager) :
            Disabled(),
            LocationState.Disabled.NotPermitted {

            override val location: Location = lastKnownLocation.unknownLocationOf(Location.UnknownLocation.Reason.PERMISSION_DENIED)

            override fun permit(enabled: Boolean): suspend () -> LocationState.Permitted = {
                if (enabled) Enabled(location.known.orUnknown, locationManager) else NoGPS(location, locationManager)
            }
        }

        class NoGPS(lastKnownLocation: Location, override val locationManager: LocationManager) :
            Disabled(),
            LocationState.Disabled.NoGPS {

            override val location: Location = lastKnownLocation.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS)
            private val permittedHandler = PermittedHandler(location, locationManager)

            override val revokePermission: suspend () -> NotPermitted = permittedHandler.revokePermission

            override val enable: suspend () -> Enabled = {
                Enabled(location.known.orUnknown, locationManager)
            }

            override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
                super.beforeOldStateIsRemoved(oldState)
                permittedHandler.beforeOldStateIsRemoved(oldState)
            }

            override suspend fun afterNewStateIsSet(newState: LocationState) {
                super.afterNewStateIsSet(newState)
                permittedHandler.afterNewStateIsSet(newState)
            }
        }
    }

    data class Enabled(override val location: Location, override val locationManager: LocationManager) :
        Active(),
        LocationState.Enabled {

        private val permittedHandler = PermittedHandler(location, locationManager)

        override val revokePermission: suspend () -> Disabled.NotPermitted = permittedHandler.revokePermission
        override val disable: suspend () -> Disabled.NoGPS = {
            Disabled.NoGPS(location, locationManager)
        }

        override fun updateWithLocation(location: Location.KnownLocation): suspend () -> LocationState.Enabled = {
            copy(location = location)
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
 * Transforms a [Flow] of [LocationState] into a flow of its associated [Location]
 * @return the [Flow] of [Location] associated with the [LocationState]
 */
fun Flow<LocationState>.location(): Flow<Location> = this.filterOnlyImportant().map { it.location }

/**
 * Transforms a [Flow] of [LocationState] into a flow of its associated optional [Location.KnownLocation]
 *
 * @param maxAge Controls both the max age of a location by filtering out locations that are older and
 * is used to set a timeout for the last emission that will emit null when triggered
 * @return a [Flow] of the [Location.KnownLocation] associated with the [LocationState], emitting `null` if the [maxAge] is reached.
 */
fun Flow<Location>.known(maxAge: Duration = 0.seconds) = known(maxAge) { DefaultKalugaDate.now() }

internal fun Flow<Location>.known(maxAge: Duration, nowProvider: () -> KalugaDate): Flow<Location.KnownLocation?> = transformLatest { location ->
    location.known?.let { knownLocation ->
        val expirationTime = knownLocation.time + maxAge
        val now = nowProvider()
        when {
            maxAge <= ZERO -> emit(knownLocation)
            expirationTime <= now -> emit(null)
            else -> {
                emit(knownLocation)
                delay(expirationTime - now)
                emit(null)
            }
        }
    } ?: emit(null)
}
