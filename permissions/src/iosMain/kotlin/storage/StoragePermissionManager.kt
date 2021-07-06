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

import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.IOSPermissionsHelper
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.StoragePermission
import platform.Foundation.NSBundle
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary

const val NSPhotoLibraryUsageDescription = "NSPhotoLibraryUsageDescription"

actual class StoragePermissionManager(
    private val bundle: NSBundle,
    actual val storage: StoragePermission,
    stateRepo: StoragePermissionStateRepo
) : PermissionManager<StoragePermission>(stateRepo) {

    private val authorizationStatus = suspend {
        PHPhotoLibrary.authorizationStatus().toAuthorizationStatus()
    }
    private var timerHelper: PermissionRefreshScheduler<StoragePermission> = PermissionRefreshScheduler(this, authorizationStatus)

    override suspend fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NSPhotoLibraryUsageDescription).isEmpty()) {
            timerHelper.isWaiting.value = true
            PHPhotoLibrary.requestAuthorization(
                mainContinuation { status ->
                    timerHelper.isWaiting.value = false
                    IOSPermissionsHelper.handleAuthorizationStatus(status.toAuthorizationStatus(), this)
                }
            )
        } else {
            revokePermission(true)
        }
    }

    override suspend fun initializeState(): PermissionState<StoragePermission> {
        return IOSPermissionsHelper.getPermissionState(authorizationStatus())
    }

    override suspend fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }
}

actual class StoragePermissionManagerBuilder(private val bundle: NSBundle = NSBundle.mainBundle) : BaseStoragePermissionManagerBuilder {

    override fun create(storage: StoragePermission, repo: StoragePermissionStateRepo): PermissionManager<StoragePermission> {
        return StoragePermissionManager(bundle, storage, repo)
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
