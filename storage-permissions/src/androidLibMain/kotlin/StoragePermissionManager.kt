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

import android.Manifest
import android.content.Context
import com.splendo.kaluga.permissions.AndroidPermissionsManager
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo

actual class StoragePermissionManager(
    context: Context,
    actual val storagePermission: StoragePermission,
    stateRepo: PermissionStateRepo<StoragePermission>
) : PermissionManager<StoragePermission>(stateRepo) {

    private val permissionsManager = AndroidPermissionsManager(context, this, if (storagePermission.allowWrite) arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) else arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))

    override suspend fun requestPermission() {
        permissionsManager.requestPermissions()
    }

    override suspend fun startMonitoring(interval: Long) {
        permissionsManager.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        permissionsManager.stopMonitoring()
    }
}

actual class StoragePermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseStoragePermissionManagerBuilder {

    override fun create(storagePermission: StoragePermission, repo: PermissionStateRepo<StoragePermission>): PermissionManager<StoragePermission> {
        return StoragePermissionManager(context.context, storagePermission, repo)
    }
}
