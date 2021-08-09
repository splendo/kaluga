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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.defaultPermissionContext

/**
 * Permission to access the users Location
 * @param background If `true` scanning for location in the background is permitted
 * @param precise If `true` precise location scanning is permitted
 */
data class LocationPermission(val background: Boolean = false, val precise: Boolean = false) : Permission()

fun PermissionsBuilder.registerLocationPermission()  =
    registerLocationPermissionBuilder(context).also { builder ->
        registerPermissionStateRepoBuilder(LocationPermission::class) { permission, coroutineContext ->
            DefaultLocationPermissionStateRepo(permission as LocationPermission, builder as BaseLocationPermissionManagerBuilder, coroutineContext)
        }
    }

internal fun PermissionsBuilder.registerLocationPermissionBuilder(context: PermissionContext = defaultPermissionContext) : LocationPermissionManagerBuilder = register(
    builder = LocationPermissionManagerBuilder(context),
    permission = LocationPermission::class
)
