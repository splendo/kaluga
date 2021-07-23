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
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import kotlin.coroutines.CoroutineContext

/**
 * A [PermissionManager] for managing [LocationPermission]
 */
expect class LocationPermissionManager : PermissionManager<LocationPermission> {
    /**
     * The [LocationPermission] managed by this manager.
     */
    val location: LocationPermission
}

interface BaseLocationPermissionManagerBuilder : BasePermissionsBuilder {

    /**
     * Creates a [LocationPermissionManager]
     * @param repo The [LocationPermissionStateRepo] associated with the [LocationPermission]
     */
    fun create(location: LocationPermission, repo: LocationPermissionStateRepo): PermissionManager<LocationPermission>
}

/**
 * A builder for creating a [LocationPermissionManager]
 */
expect class LocationPermissionManagerBuilder : BaseLocationPermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [LocationPermission]
 * @param builder The [LocationPermissionManagerBuilder] for creating the [LocationPermissionManager] associated with the permission
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class LocationPermissionStateRepo(location: LocationPermission, builder: BaseLocationPermissionManagerBuilder, coroutineContext: CoroutineContext) : PermissionStateRepo<LocationPermission>(coroutineContext = coroutineContext) {

    override val permissionManager: PermissionManager<LocationPermission> = builder.create(location, this)
}
