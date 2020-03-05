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

expect class StoragePermissionManager : PermissionManager<Permission.Storage> {
    val storage: Permission.Storage
}

interface BaseStoragePermissionManagerBuilder {
    fun create(storage: Permission.Storage, repo: StoragePermissionStateRepo): PermissionManager<Permission.Storage>
}

expect class StoragePermissionManagerBuilder :BaseStoragePermissionManagerBuilder

class StoragePermissionStateRepo(storage: Permission.Storage, builder: BaseStoragePermissionManagerBuilder) : PermissionStateRepo<Permission.Storage>() {

    override val permissionManager: PermissionManager<Permission.Storage> = builder.create(storage, this)

}