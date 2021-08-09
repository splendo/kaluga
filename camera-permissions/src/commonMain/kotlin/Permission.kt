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

package com.splendo.kaluga.permissions.camera

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.defaultPermissionContext

/**
 * Permission to access the users Camera
 */
object CameraPermission : Permission()

fun PermissionsBuilder.registerCameraPermission() =
    registerCameraPermissionBuilder(context).also { builder ->
        registerPermissionStateRepoBuilder(CameraPermission::class) { _, coroutineContext ->
            CameraPermissionStateRepo(builder as BaseCameraPermissionManagerBuilder, coroutineContext)
        }
    }

internal fun PermissionsBuilder.registerCameraPermissionBuilder(context: PermissionContext = defaultPermissionContext) = register(
    builder = CameraPermissionManagerBuilder(context),
    permission = CameraPermission::class
)
