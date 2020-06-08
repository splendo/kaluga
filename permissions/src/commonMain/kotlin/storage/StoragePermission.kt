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
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

/**
 * A [PermissionManager] for managing [Permission.Storage]
 */
expect class StoragePermissionManager : PermissionManager<Permission.Storage> {
    /**
     * The [Permission.Storage] managed by this manager.
     */
    val storage: Permission.Storage
}

/**
 * Alias for [StoragePermissionManager]
 */
typealias PhotosPermissionManager = StoragePermissionManager

/**
 * A builder for creating a [StoragePermissionManager]
 */
expect class StoragePermissionManagerBuilder {
    /**
     * Creates a [StoragePermissionManager]
     * @param repo The [StoragePermissionStateRepo] associated with the [Permission.Storage]
     */
    fun create(storage: Permission.Storage, repo: StoragePermissionStateRepo): StoragePermissionManager
}

/**
 * Alias for [StoragePermissionManagerBuilder]
 */
typealias PhotosPermissionManagerBuilder = StoragePermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [Permission.Storage]
 * @param builder The [StoragePermissionManagerBuilder] for creating the [StoragePermissionManager] associated with the permission
 */
class StoragePermissionStateRepo(storage: Permission.Storage, builder: StoragePermissionManagerBuilder) : PermissionStateRepo<Permission.Storage>() {

    override val permissionManager: PermissionManager<Permission.Storage> = builder.create(storage, this)
}
