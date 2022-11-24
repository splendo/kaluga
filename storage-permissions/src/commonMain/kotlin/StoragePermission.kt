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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionsBuilder
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.defaultPermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * A [PermissionManager] for managing [StoragePermission]
 */
typealias StoragePermissionManager = PermissionManager<StoragePermission>
expect class DefaultStoragePermissionManager : BasePermissionManager<StoragePermission>

/**
 * Alias for [StoragePermissionManager]
 */
typealias PhotosPermissionManager = StoragePermissionManager

interface BaseStoragePermissionManagerBuilder : BasePermissionsBuilder<StoragePermission> {
    /**
     * Creates a [StoragePermissionManager]
     * @param notificationsPermission The [StoragePermission] for the PermissionManager to be created
     * @param settings [BasePermissionManager.Settings] to configure the manager
     * @param coroutineScope The [CoroutineScope] the manager runs on
     */
    fun create(storagePermission: StoragePermission, settings: BasePermissionManager.Settings = BasePermissionManager.Settings(), coroutineScope: CoroutineScope): StoragePermissionManager
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
    monitoringInterval: Duration = defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
    coroutineContext: CoroutineContext
) : PermissionStateRepo<StoragePermission>(monitoringInterval, { builder.create(storagePermission, settings, it) }, coroutineContext)
