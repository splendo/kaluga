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
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import kotlinx.coroutines.CoroutineScope

actual class DefaultStoragePermissionManager(storagePermission: StoragePermission, settings: Settings, coroutineScope: CoroutineScope) : BasePermissionManager<StoragePermission>(storagePermission, settings, coroutineScope)

actual class StoragePermissionManagerBuilder actual constructor(context: PermissionContext) : BaseStoragePermissionManagerBuilder {

    override fun create(storagePermission: StoragePermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): PermissionManager<StoragePermission> {
        return DefaultStoragePermissionManager(storagePermission, settings, coroutineScope)
    }
}