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

import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

actual class DefaultLocationManager(
    settings: Settings,
    coroutineScope: CoroutineScope
) : BaseLocationManager(settings, coroutineScope) {

    class Builder : BaseLocationManager.Builder {
        override fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseLocationManager {
            return DefaultLocationManager(settings, coroutineScope)
        }
    }

    override val locationMonitor: LocationMonitor = LocationMonitor.Builder().create()

    override suspend fun requestEnableLocation() {
        TODO("Not yet implemented")
    }

    override suspend fun startMonitoringLocation() {
        TODO("not implemented")
    }

    override suspend fun stopMonitoringLocation() {
        TODO("not implemented")
    }
}

actual class LocationStateRepoBuilder(
    private val permissionsBuilder: (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder().apply { registerLocationPermission() },
            context
        )
    }
) : LocationStateRepo.Builder {

    override fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> BaseLocationManager.Settings,
        coroutineContext: CoroutineContext,
        contextCreator: CoroutineContext.(String) -> CoroutineContext
    ): LocationStateRepo {
        return LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, DefaultLocationManager.Builder(), coroutineContext, contextCreator)
    }
}
