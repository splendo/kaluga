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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    sealed class Disabled(location: Location, locationManager: BaseLocationManager) : LocationState(location, locationManager) {

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

            fun permit(enabled: Boolean) : suspend () -> LocationState = {
                if (enabled) Enabled(location, locationManager) else NoGPS(NoGPS.generateLocation(location), locationManager)
            }

        }

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

            override val revokePermission: suspend () -> NotPermitted = permittedHandler.revokePermission

            val enable : suspend () -> Enabled = {
                Enabled(location, locationManager)
            }

            override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
                permittedHandler.beforeOldStateIsRemoved(oldState)
                when (oldState) {
                    !is NoGPS -> if(locationManager.autoEnableLocations) locationManager.requestLocationEnable()
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

    data class Enabled(override val location: Location, private val locationManager: BaseLocationManager) : LocationState(location, locationManager), Permitted {

        private val permittedHandler = PermittedHandler(location, locationManager)

        override val revokePermission: suspend () -> Disabled.NotPermitted = permittedHandler.revokePermission

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

class LocationStateRepo(locationPermission: Permission.Location,
                        permissions: Permissions,
                        autoRequestPermission: Boolean,
                        autoEnableLocations: Boolean,
                        locationManagerBuilder: BaseLocationManager.Builder,
                        coroutineScope: CoroutineScope) : ColdStateRepo<LocationState>(coroutineContext = coroutineScope.coroutineContext) {

    interface Builder {
        fun create(locationPermission: Permission.Location, autoRequestPermission: Boolean = true,
                   autoEnableLocations: Boolean = true, coroutineScope: CoroutineScope): LocationStateRepo
    }

    private var lastKnownLocation: Location = Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)
    private val locationManager = locationManagerBuilder.create(locationPermission, permissions, autoRequestPermission, autoEnableLocations, this, coroutineScope)

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

fun Flow<LocationState>.location(): Flow<Location> {
    return this.map { it.location }
}

