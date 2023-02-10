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
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.DefaultAndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

/**
 * The [BasePermissionManager] to use as a default for [StoragePermission]
 * @param context the [Context] the [StoragePermission] is to be granted in
 * @param storagePermission the [StoragePermission] to manage
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultStoragePermissionManager(
    context: Context,
    storagePermission: StoragePermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<StoragePermission>(storagePermission, settings, coroutineScope) {

    private val permissionHandler = DefaultAndroidPermissionStateHandler(eventChannel, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(
        context,
        if (storagePermission.allowWrite)
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        else
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        coroutineScope,
        logTag,
        logger,
        permissionHandler
    )

    override fun requestPermissionDidStart() {
        permissionsManager.requestPermissions()
    }

    override fun monitoringDidStart(interval: Duration) {
        permissionsManager.startMonitoring(interval)
    }

    override fun monitoringDidStop() {
        permissionsManager.stopMonitoring()
    }
}

/**
 * A [BaseStoragePermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class StoragePermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseStoragePermissionManagerBuilder {

    override fun create(storagePermission: StoragePermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): StoragePermissionManager {
        return DefaultStoragePermissionManager(context.context, storagePermission, settings, coroutineScope)
    }
}
