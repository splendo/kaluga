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

package com.splendo.kaluga.permissions.storage

import com.splendo.kaluga.permissions.base.BasePermissionsBuilder
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.defaultPermissionContext
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * A [PermissionManager] for managing [StoragePermission]
 */
expect class StoragePermissionManager : PermissionManager<StoragePermission> {
    /**
     * The [StoragePermission] managed by this manager.
     */
    val storagePermission: StoragePermission
}

/**
 * Alias for [StoragePermissionManager]
 */
typealias PhotosPermissionManager = StoragePermissionManager

interface BaseStoragePermissionManagerBuilder : BasePermissionsBuilder<StoragePermission> {
    /**
     * Creates a [StoragePermissionManager]
     * @param repo The [StoragePermissionStateRepo] associated with the [StoragePermission]
     */
    fun create(storagePermission: StoragePermission, repo: PermissionStateRepo<StoragePermission>): PermissionManager<StoragePermission>
}

/**
 * A builder for creating a [StoragePermissionManager]
 */
expect class StoragePermissionManagerBuilder(context: PermissionContext = defaultPermissionContext) : BaseStoragePermissionManagerBuilder

/**
 * Alias for [StoragePermissionManagerBuilder]
 */
typealias PhotosPermissionManagerBuilder = StoragePermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [StoragePermission]
 * @param builder The [StoragePermissionManagerBuilder] for creating the [StoragePermissionManager] associated with the permission
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class StoragePermissionStateRepo(
    storagePermission: StoragePermission,
    builder: BaseStoragePermissionManagerBuilder,
    monitoringInterval: Long = defaultMonitoringInterval,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : PermissionStateRepo<StoragePermission>(monitoringInterval, coroutineContext, { builder.create(storagePermission, it) })
