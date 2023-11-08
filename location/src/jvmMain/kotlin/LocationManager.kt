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

import com.splendo.kaluga.location.BaseLocationManager.Settings
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseLocationManager]
 * @param settings the [Settings] to configure this location manager
 * @param coroutineScope the [CoroutineScope] this location manager runs on
 */
actual class DefaultLocationManager(
    settings: Settings,
    coroutineScope: CoroutineScope,
) : BaseLocationManager(settings, coroutineScope) {

    /**
     * Builder for creating a [DefaultLocationManager]
     */
    class Builder : BaseLocationManager.Builder {
        override fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager {
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

/**
 * Default [BaseLocationStateRepoBuilder]
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Location permissions.
 * Needs to have [com.splendo.kaluga.permissions.location.LocationPermission] registered.
 */
actual class LocationStateRepoBuilder(
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder().apply { registerLocationPermissionIfNotRegistered() },
            context,
        )
    },
) : BaseLocationStateRepoBuilder {

    override fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> Settings,
        coroutineContext: CoroutineContext,
    ): LocationStateRepo {
        return LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, DefaultLocationManager.Builder(), coroutineContext)
    }
}
