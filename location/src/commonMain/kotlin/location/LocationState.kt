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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

/**
 * State of a [LocationStateRepo]
 */
sealed interface LocationState : KalugaState {
    val location: Location

    sealed interface Inactive : LocationState, SpecialFlowValue.NotImportant
    interface NotInitialized : Inactive
    interface Deinitialized : Inactive {
        val reinitialize: suspend () -> Initializing
    }

    sealed interface Active : LocationState {
        val deinitialized: suspend () -> Deinitialized
    }

    interface Initializing : Active, SpecialFlowValue.NotImportant {
        fun initialize(hasPermission: Boolean, enabled: Boolean): suspend () -> Initialized
    }

    sealed interface Initialized : Active
    sealed interface Permitted : Initialized {
        val revokePermission: suspend () -> Disabled.NotPermitted
    }

    sealed interface Disabled : Initialized {
        interface NoGPS : Disabled, Permitted {
            val enable: suspend () -> Enabled
        }

        interface NotPermitted : Disabled {
            fun permit(enabled: Boolean): suspend () -> Permitted
        }
    }

    interface Enabled : Permitted {
        val disable: suspend () -> Disabled.NoGPS
        fun updateWithLocation(location: Location.KnownLocation): suspend () -> Enabled
    }
}

sealed class LocationStateImpl {

    abstract val location: Location

    object NotInitialized : LocationStateImpl(), LocationState.NotInitialized {
        override val location: Location = Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)
        fun startInitializing(locationManager: LocationManager): suspend () -> LocationState.Initializing = { Initializing(location, locationManager) }
    }

    data class Deinitialized(
        override val location: Location,
        internal val locationManager: LocationManager
    ) : LocationStateImpl(), LocationState.Deinitialized {
        override val reinitialize: suspend () -> LocationState.Initializing = { Initializing(location, locationManager) }
    }

    sealed class Active : LocationStateImpl(), HandleBeforeOldStateIsRemoved<LocationState>, HandleAfterNewStateIsSet<LocationState> {

        protected abstract val locationManager: LocationManager

        val deinitialized: suspend () -> LocationState.Deinitialized = { Deinitialized(location, locationManager) }

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

        val revokePermission: suspend () -> Disabled.NotPermitted = {
            Disabled.NotPermitted(location, locationManager)
        }

        suspend fun afterNewStateIsSet(newState: LocationState) {
            when (newState) {
                is LocationState.Inactive,
                is LocationState.Initializing,
                is LocationState.Disabled.NotPermitted -> locationManager.stopMonitoringLocationEnabled()
                else -> {}
            }
        }

        suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            when (oldState) {
                is LocationState.Inactive,
                is LocationState.Initializing,
                is LocationState.Disabled.NotPermitted -> locationManager.startMonitoringLocationEnabled()
                else -> {}
            }
        }
    }

    data class Initializing(override val location: Location, override val locationManager: LocationManager) : Active(), LocationState.Initializing {

        override fun initialize(hasPermission: Boolean, enabled: Boolean): suspend () -> LocationState.Initialized = suspend {
            when {
                !hasPermission -> Disabled.NotPermitted(location, locationManager)
                !enabled -> Disabled.NoGPS(location, locationManager)
                else -> Enabled(location.known.orUnknown, locationManager)
            }
        }
    }

    /**
     * A [LocationState] that is not actively fetching new [Location]s.
     */
    sealed class Disabled : Active() {

        /**
         * A [LocationState.Disabled] that was disabled due to missing permissions.
         */
        class NotPermitted(lastKnownLocation: Location, override val locationManager: LocationManager) : Disabled(), LocationState.Disabled.NotPermitted {

            override val location: Location = lastKnownLocation.unknownLocationOf(Location.UnknownLocation.Reason.PERMISSION_DENIED)

            /**
             * Transforms this state into [LocationState] that has sufficient permissions
             * @param enabled `true` if GPS is turned on, `false` otherwise.
             */
            override fun permit(enabled: Boolean): suspend () -> LocationState.Permitted = {
                if (enabled) Enabled(location.known.orUnknown, locationManager) else NoGPS(location, locationManager)
            }
        }

        /**
         * A [LocationState.Disabled] that was disabled due to GPS being turned off.
         */
        class NoGPS(lastKnownLocation: Location, override val locationManager: LocationManager) : Disabled(), LocationState.Disabled.NoGPS {

            override val location: Location = lastKnownLocation.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS)
            private val permittedHandler = PermittedHandler(location, locationManager)

            /**
             * Transforms this state into a [LocationState.Disabled.NotPermitted] state.
             */
            override val revokePermission: suspend () -> NotPermitted = permittedHandler.revokePermission

            /**
             * Transforms this state into a [LocationState.Enabled] state.
             */
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

    /**
     * A [LocationState] that is actively updating its [Location].
     */
    data class Enabled(override val location: Location, override val locationManager: LocationManager) : Active(), LocationState.Enabled {

        private val permittedHandler = PermittedHandler(location, locationManager)

        /**
         * Transforms this state into a [LocationState.Disabled.NotPermitted] state.
         */
        override val revokePermission: suspend () -> Disabled.NotPermitted = permittedHandler.revokePermission

        /**
         * Transforms this state into a [LocationState.Disabled.NoGPS] state.
         */
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
 */
fun Flow<LocationState>.location(): Flow<Location> {
    return this.filterOnlyImportant().map { it.location }
}

/**
 * Transforms a [Flow] of [LocationState] into a flow of its associated optional [Location.KnownLocation]
 *
 * @param maxAge Controls both the max age of a location by filtering out locations that are older and
 * is used to set a timeout for the last emission that will emit null when triggered
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
