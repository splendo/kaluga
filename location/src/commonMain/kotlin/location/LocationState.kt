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

import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.State
import kotlinx.coroutines.InternalCoroutinesApi

sealed class LocationState(open val location: Location, private val locationManager: BaseLocationManager) : State<LocationState>() {

    interface Permitted : HandleBeforeOldStateIsRemoved<LocationState>, HandleAfterNewStateIsSet<LocationState> {
        val revokePermission: suspend () -> Disabled.NotPermitted
    }

    class PermittedHandler(val location: Location, private val locationManager: BaseLocationManager) : Permitted {

        override val revokePermission: suspend () -> Disabled.NotPermitted = {
            Disabled.NotPermitted(location, locationManager)
        }

        override suspend fun afterNewStateIsSet(newState: LocationState) {
            when (newState) {
                is Disabled.NotPermitted -> locationManager.stopMonitoringLocationEnabled()
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
            when (oldState) {
                is Disabled.NotPermitted -> locationManager.startMonitoringLocationEnabled()
            }
        }

    }

    sealed class Disabled(location: Location, locationManager: BaseLocationManager) : LocationState(location, locationManager) {

        data class NotPermitted(override val location: Location, private val locationManager: BaseLocationManager) : Disabled(location, locationManager) {

            fun permit(enabled: Boolean) : suspend () -> LocationState = {
                if (enabled) Enabled.Idle(location, locationManager) else NoGPS(location, locationManager)
            }

        }

        data class NoGPS(override val location: Location, private val locationManager: BaseLocationManager, val permitedHandler: PermittedHandler = PermittedHandler(location, locationManager)) : Disabled(location, locationManager), Permitted by permitedHandler {

            val enable : suspend () -> Enabled.Idle = {
                Enabled.Idle(location, locationManager)
            }

            override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
                permitedHandler.beforeOldStateIsRemoved(oldState)
                when (oldState) {
                    !is NoGPS -> if(locationManager.autoEnableLocations) locationManager.requestLocationEnable()
                }
            }

        }

    }

    sealed class Enabled(location: Location, private val locationManager: BaseLocationManager) : LocationState(location, locationManager), Permitted by PermittedHandler(location, locationManager) {

        val disable: suspend () -> Disabled.NoGPS = {
            Disabled.NoGPS(location, locationManager)
        }

        data class Idle(override val location: Location, private val locationManager: BaseLocationManager) : Enabled(location, locationManager) {

            val startScanning: suspend () -> Scanning = {
                Scanning(location, locationManager)
            }

        }

        data class Scanning(override val location: Location, private val locationManager: BaseLocationManager) : Enabled(location, locationManager) {

            val stopScanning: suspend () -> Idle = {
                Idle(location, locationManager)
            }

            override suspend fun afterNewStateIsSet(newState: LocationState) {
                super.afterNewStateIsSet(newState)
                when (newState) {
                    !is Scanning -> locationManager.stopMonitoringLocation()
                }
            }

            override suspend fun beforeOldStateIsRemoved(oldState: LocationState) {
                super.beforeOldStateIsRemoved(oldState)
                when (oldState) {
                    !is Scanning -> locationManager.startMonitoringLocation()
                }
            }

        }

    }

}

class LocationStateRepo(locationPermissionRepo: LocationPermissionStateRepo, autoRequestPermission: Boolean, autoEnableLocations: Boolean, locationManagerBuilder: BaseLocationManager.Builder) : ColdStateRepo<LocationState>() {

    private var lastKnownLocation: Location = Location.UnknownLocationWithNoLastLocation(Location.UnknownReason.NOT_CLEAR)
    private val locationManager = locationManagerBuilder.create(locationPermissionRepo, autoRequestPermission, autoEnableLocations, this)

    @InternalCoroutinesApi
    override suspend fun initialValue(): LocationState {
        locationManager.startMonitoringPermissions()
        return if (!locationManager.isPermitted()) {
            LocationState.Disabled.NotPermitted(lastKnownLocation, locationManager)
        } else if (!locationManager.isLocationEnabled()) {
            LocationState.Disabled.NoGPS(lastKnownLocation, locationManager)
        } else {
            LocationState.Enabled.Idle(lastKnownLocation, locationManager)
        }
    }

    override suspend fun deinitialize(state: LocationState) {
        locationManager.stopMonitoringPermissions()
        lastKnownLocation = state.location
    }
}

