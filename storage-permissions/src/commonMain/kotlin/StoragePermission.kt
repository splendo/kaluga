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

/**
 * The [BasePermissionManager] to use as a default for [StoragePermission]
 */
expect class DefaultStoragePermissionManager : BasePermissionManager<StoragePermission> {
    override fun requestPermissionDidStart()
    override fun monitoringDidStart(interval: Duration)
    override fun monitoringDidStop()
}

/**
 * Alias for [StoragePermissionManager]
 */
typealias PhotosPermissionManager = StoragePermissionManager

/**
 * A [BasePermissionsBuilder] for [StoragePermission]
 */
interface BaseStoragePermissionManagerBuilder : BasePermissionsBuilder<StoragePermission> {
    /**
     * Creates a [StoragePermissionManager]
     * @param storagePermission the [StoragePermission] to manage
     * @param settings [BasePermissionManager.Settings] to apply to the manager
     * @param coroutineScope The [CoroutineScope] the manager runs on
     * @return a [StoragePermissionManager]
     */
    fun create(
        storagePermission: StoragePermission,
        settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
        coroutineScope: CoroutineScope,
    ): StoragePermissionManager
}

/**
 * A [BaseStoragePermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
expect class StoragePermissionManagerBuilder(context: PermissionContext = defaultPermissionContext) : BaseStoragePermissionManagerBuilder {
    override fun create(storagePermission: StoragePermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): StoragePermissionManager
}

/**
 * Alias for [StoragePermissionManagerBuilder]
 */
typealias PhotosPermissionManagerBuilder = StoragePermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [StoragePermission]
 * @param storagePermission the [StoragePermission] to manage
 * @param builder The [StoragePermissionManagerBuilder] for creating the [StoragePermissionManager] associated with the permission
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] used by the [StoragePermissionManager] created by the builder
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class StoragePermissionStateRepo(
    storagePermission: StoragePermission,
    builder: BaseStoragePermissionManagerBuilder,
    monitoringInterval: Duration = defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
    coroutineContext: CoroutineContext,
) : PermissionStateRepo<StoragePermission>(monitoringInterval, { builder.create(storagePermission, settings, it) }, coroutineContext)
