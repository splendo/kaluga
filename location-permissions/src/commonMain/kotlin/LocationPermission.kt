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

package com.splendo.kaluga.permissions.location

import com.splendo.kaluga.permissions.BasePermissionsBuilder
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.defaultPermissionContext
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * A [PermissionManager] for managing [LocationPermission]
 */
expect class LocationPermissionManager : PermissionManager<LocationPermission> {
    /**
     * The [LocationPermission] managed by this manager.
     */
    val locationPermission: LocationPermission
}

interface BaseLocationPermissionManagerBuilder : BasePermissionsBuilder<LocationPermission> {

    /**
     * Creates a [LocationPermissionManager]
     * @param repo The [LocationPermissionStateRepo] associated with the [LocationPermission]
     */
    fun create(locationPermission: LocationPermission, repo: PermissionStateRepo<LocationPermission>): PermissionManager<LocationPermission>
}

/**
 * A builder for creating a [LocationPermissionManager]
 */
expect class LocationPermissionManagerBuilder(context: PermissionContext = defaultPermissionContext) : BaseLocationPermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [LocationPermission]
 * @param builder The [LocationPermissionManagerBuilder] for creating the [LocationPermissionManager] associated with the permission
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class LocationPermissionStateRepo(
    locationPermission: LocationPermission,
    builder: BaseLocationPermissionManagerBuilder,
    monitoringInterval: Long = defaultMonitoringInterval,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : PermissionStateRepo<LocationPermission>(monitoringInterval, coroutineContext, { builder.create(locationPermission, it) })
