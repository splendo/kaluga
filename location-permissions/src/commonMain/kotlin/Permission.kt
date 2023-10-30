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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * [Permission] to access the users Location
 * @param background If `true` scanning for location in the background is permitted
 * @param precise If `true` precise location scanning is permitted
 */
data class LocationPermission(val background: Boolean = false, val precise: Boolean = false) :
    Permission() {
        override val name: String = listOfNotNull(
            if (background) "Background" else null,
            "Location",
            "-",
            if (precise) "Precise" else "Coarse",
        ).joinToString(" ")
    }

/**
 * Registers a [BaseLocationPermissionManagerBuilder] and [PermissionStateRepo] for [LocationPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param locationPermissionManagerBuilderBuilder method for creating a [BaseLocationPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseLocationPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseLocationPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerLocationPermission(
    locationPermissionManagerBuilderBuilder: (PermissionContext) -> BaseLocationPermissionManagerBuilder = ::LocationPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) =
    registerLocationPermission(locationPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
        LocationPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
    }

/**
 * Registers a [BaseLocationPermissionManagerBuilder] and [PermissionStateRepo] for [LocationPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param locationPermissionManagerBuilderBuilder method for creating a [BaseLocationPermissionManagerBuilder] from a [PermissionContext]
 * @param locationPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [LocationPermission] given a [BaseLocationPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseLocationPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseLocationPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerLocationPermission(
    locationPermissionManagerBuilderBuilder: (PermissionContext) -> BaseLocationPermissionManagerBuilder = ::LocationPermissionManagerBuilder,
    locationPermissionStateRepoBuilder: (LocationPermission, BaseLocationPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<LocationPermission>,
) = locationPermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<LocationPermission> { permission, coroutineContext ->
        locationPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}

/**
 * Gets the [BaseLocationPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseLocationPermissionManagerBuilder] and [PermissionStateRepo] for [LocationPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param locationPermissionManagerBuilderBuilder method for creating a [BaseLocationPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseLocationPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerLocationPermissionIfNotRegistered(
    locationPermissionManagerBuilderBuilder: (PermissionContext) -> BaseLocationPermissionManagerBuilder = ::LocationPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) =
    registerLocationPermissionIfNotRegistered(locationPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
        LocationPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
    }

/**
 * Gets the [BaseLocationPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseLocationPermissionManagerBuilder] and [PermissionStateRepo] for [LocationPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param locationPermissionManagerBuilderBuilder method for creating a [BaseLocationPermissionManagerBuilder] from a [PermissionContext]
 * @param locationPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [LocationPermission] given a [BaseLocationPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseLocationPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerLocationPermissionIfNotRegistered(
    locationPermissionManagerBuilderBuilder: (PermissionContext) -> BaseLocationPermissionManagerBuilder = ::LocationPermissionManagerBuilder,
    locationPermissionStateRepoBuilder: (LocationPermission, BaseLocationPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<LocationPermission>,
) = locationPermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<LocationPermission> { permission, coroutineContext ->
        locationPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}
