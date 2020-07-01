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
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import kotlin.coroutines.CoroutineContext

/**
 * A [PermissionManager] for managing [Permission.Camera]
 */
expect class CameraPermissionManager : PermissionManager<Permission.Camera>

interface BaseCameraPermissionManagerBuilder {

    /**
     * Creates a [CameraPermissionManager]
     * @param repo The [CameraPermissionStateRepo] associated with the [Permission.Camera]
     */
    fun create(repo: CameraPermissionStateRepo): PermissionManager<Permission.Camera>
}

/**
 * A builder for creating a [CameraPermissionManager]
 */
expect class CameraPermissionManagerBuilder : BaseCameraPermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [Permission.Camera]
 * @param builder The [CameraPermissionManagerBuilder] for creating the [CameraPermissionManager] associated with the permission
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class CameraPermissionStateRepo(builder: BaseCameraPermissionManagerBuilder, coroutineContext: CoroutineContext) : PermissionStateRepo<Permission.Camera>(coroutineContext = coroutineContext) {

    override val permissionManager: PermissionManager<Permission.Camera> = builder.create(this)
}
