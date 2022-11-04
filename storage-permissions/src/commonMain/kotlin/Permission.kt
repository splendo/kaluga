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
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Permission to access the users device storage.
 * On iOS this corresponds to the Photos permission
 * @param allowWrite If `true` writing to the storage is permitted
 */
data class StoragePermission(val allowWrite: Boolean = false) : Permission() {
    override val name: String = "Storage - ${if (allowWrite) "ReadWrite" else "ReadOnly"}"
}

suspend fun PermissionsBuilder.registerStoragePermission(
    storagePermissionManagerBuilderBuilder: (PermissionContext) -> BaseStoragePermissionManagerBuilder = ::StoragePermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings()
) =
    registerStoragePermission(storagePermissionManagerBuilderBuilder) { storagePermission, baseStoragePermissionManagerBuilder, coroutineContext ->
        StoragePermissionStateRepo(
            storagePermission,
            baseStoragePermissionManagerBuilder,
            monitoringInterval,
            settings,
            coroutineContext
        )
    }

suspend fun PermissionsBuilder.registerStoragePermission(
    storagePermissionManagerBuilderBuilder: (PermissionContext) -> BaseStoragePermissionManagerBuilder = ::StoragePermissionManagerBuilder,
    storagePermissionStateRepoBuilder: (StoragePermission, BaseStoragePermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<StoragePermission>
) = storagePermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<StoragePermission> { permission, coroutineContext ->
        storagePermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}

suspend fun PermissionsBuilder.registerStoragePermissionIfNotRegistered(
    storagePermissionManagerBuilderBuilder: (PermissionContext) -> BaseStoragePermissionManagerBuilder = ::StoragePermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings()
) =
    registerStoragePermissionIfNotRegistered(storagePermissionManagerBuilderBuilder) { storagePermission, baseStoragePermissionManagerBuilder, coroutineContext ->
        StoragePermissionStateRepo(
            storagePermission,
            baseStoragePermissionManagerBuilder,
            monitoringInterval,
            settings,
            coroutineContext
        )
    }

suspend fun PermissionsBuilder.registerStoragePermissionIfNotRegistered(
    storagePermissionManagerBuilderBuilder: (PermissionContext) -> BaseStoragePermissionManagerBuilder = ::StoragePermissionManagerBuilder,
    storagePermissionStateRepoBuilder: (StoragePermission, BaseStoragePermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<StoragePermission>
) = storagePermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<StoragePermission> { permission, coroutineContext ->
        storagePermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}
