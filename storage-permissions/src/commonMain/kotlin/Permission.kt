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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionsBuilder

/**
 * Permission to access the users device storage.
 * On iOS this corresponds to the Photos permission
 * @param allowWrite If `true` writing to the storage is permitted
 */
data class StoragePermission(val allowWrite: Boolean = false) : Permission()

fun PermissionsBuilder.registerStoragePermission() =
    registerStoragePermissionBuilder(context).also { builder ->
        registerRepoFactory(StoragePermission::class) { permission, coroutineContext ->
            StoragePermissionStateRepo(permission as StoragePermission, builder as BaseStoragePermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerStoragePermissionBuilder(context: Any?) : StoragePermissionManagerBuilder

