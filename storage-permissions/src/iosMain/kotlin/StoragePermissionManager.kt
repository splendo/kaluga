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

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.DefaultAuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.CurrentAuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.requestAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSBundle
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import kotlin.time.Duration

const val NSPhotoLibraryUsageDescription = "NSPhotoLibraryUsageDescription"

actual class DefaultStoragePermissionManager(
    private val bundle: NSBundle,
    storagePermission: StoragePermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<StoragePermission>(storagePermission, settings, coroutineScope) {

    private class Provider : CurrentAuthorizationStatusProvider {
        override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus = PHPhotoLibrary.authorizationStatus().toAuthorizationStatus()
    }

    private val provider = Provider()

    private val permissionHandler = DefaultAuthorizationStatusHandler(eventChannel, logTag, logger)
    private var timerHelper = PermissionRefreshScheduler(provider, permissionHandler, coroutineScope)

    override fun requestPermissionDidStart() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NSPhotoLibraryUsageDescription).isEmpty()) {
            permissionHandler.requestAuthorizationStatus(timerHelper, CoroutineScope(coroutineContext)) {
                val deferred = CompletableDeferred<PHAuthorizationStatus>()
                PHPhotoLibrary.requestAuthorization  { status ->
                    deferred.complete(status)
                    Unit
                }

                deferred.await().toAuthorizationStatus()
            }
        } else {
            permissionHandler.status(IOSPermissionsHelper.AuthorizationStatus.Restricted)
        }
    }

    override fun monitoringDidStart(interval: Duration) {
        timerHelper.startMonitoring(interval)
    }

    override fun monitoringDidStop() {
        timerHelper.stopMonitoring()
    }
}

actual class StoragePermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseStoragePermissionManagerBuilder {

    override fun create(storagePermission: StoragePermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): StoragePermissionManager {
        return DefaultStoragePermissionManager(context, storagePermission, settings, coroutineScope)
    }
}

private fun PHAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        PHAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        PHAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        PHAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        PHAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            error(
                "StoragePermissionManager",
                "Unknown StorageManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
