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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * [Permission] to access the users Camera
 */
object CameraPermission : Permission() {
    override val name: String = "Camera"
}

/**
 * Registers a [BaseCameraPermissionManagerBuilder] and [PermissionStateRepo] for [CameraPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param cameraPermissionManagerBuilderBuilder method for creating a [BaseCameraPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseCameraPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseCameraPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerCameraPermission(
    cameraPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCameraPermissionManagerBuilder = ::CameraPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerCameraPermission(cameraPermissionManagerBuilderBuilder) { builder, coroutineContext ->
    CameraPermissionStateRepo(builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Registers a [BaseCameraPermissionManagerBuilder] and [PermissionStateRepo] for [CameraPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param cameraPermissionManagerBuilderBuilder method for creating a [BaseCameraPermissionManagerBuilder] from a [PermissionContext]
 * @param cameraPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [CameraPermission] given a [BaseCameraPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseCameraPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseCameraPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerCameraPermission(
    cameraPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCameraPermissionManagerBuilder = ::CameraPermissionManagerBuilder,
    cameraPermissionStateRepoBuilder: (BaseCameraPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<CameraPermission>,
) = cameraPermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<CameraPermission> { _, coroutineContext ->
        cameraPermissionStateRepoBuilder(it, coroutineContext)
    }
}

/**
 * Gets the [BaseCameraPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseCameraPermissionManagerBuilder] and [PermissionStateRepo] for [CameraPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param cameraPermissionManagerBuilderBuilder method for creating a [BaseCameraPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseCameraPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerCameraPermissionIfNotRegistered(
    cameraPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCameraPermissionManagerBuilder = ::CameraPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerCameraPermissionIfNotRegistered(cameraPermissionManagerBuilderBuilder) { builder, coroutineContext ->
    CameraPermissionStateRepo(builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Gets the [BaseCameraPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseCameraPermissionManagerBuilder] and [PermissionStateRepo] for [CameraPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param cameraPermissionManagerBuilderBuilder method for creating a [BaseCameraPermissionManagerBuilder] from a [PermissionContext]
 * @param cameraPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [CameraPermission] given a [BaseCameraPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseCameraPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerCameraPermissionIfNotRegistered(
    cameraPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCameraPermissionManagerBuilder = ::CameraPermissionManagerBuilder,
    cameraPermissionStateRepoBuilder: (BaseCameraPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<CameraPermission>,
) = cameraPermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<CameraPermission> { _, coroutineContext ->
        cameraPermissionStateRepoBuilder(it, coroutineContext)
    }
}
