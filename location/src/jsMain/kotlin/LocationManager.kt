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

import com.splendo.kaluga.base.monitor.DefaultServiceMonitor
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual class LocationManager(
    locationPermission: LocationPermission,
    permissions: Permissions,
    autoRequestPermission: Boolean,
    autoEnableLocations: Boolean,
    locationStateRepo: LocationStateRepo
) : BaseLocationManager(locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo) {

    class Builder : BaseLocationManager.Builder {
        override fun create(
            locationPermission: LocationPermission,
            permissions: Permissions,
            autoRequestPermission: Boolean,
            autoEnableLocations: Boolean,
            locationStateRepo: LocationStateRepo
        ): BaseLocationManager {
            return LocationManager(locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo)
        }
    }

    override val locationMonitor: DefaultServiceMonitor = LocationMonitor.Builder().create(coroutineContext)

    override suspend fun requestLocationEnable() {
        TODO("not implemented")
    }

    override suspend fun startMonitoringLocation() {
        TODO("not implemented")
    }

    override suspend fun stopMonitoringLocation() {
        TODO("not implemented")
    }
}

actual class LocationStateRepoBuilder(
    private val permissions: Permissions = Permissions(
        PermissionsBuilder().apply { registerLocationPermission() },
        Dispatchers.Main
    )
) : LocationStateRepo.Builder {

    override fun create(locationPermission: LocationPermission, autoRequestPermission: Boolean, autoEnableLocations: Boolean, coroutineContext: CoroutineContext): LocationStateRepo {
        return LocationStateRepo(locationPermission, permissions, autoRequestPermission, autoEnableLocations, LocationManager.Builder(), coroutineContext)
    }
}
