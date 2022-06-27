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

package com.splendo.kaluga.permissions.base.av

import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaType
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSBundle

abstract class AVType<P : Permission> {

    abstract val permissionManager: PermissionManager<P>
    abstract val avMediaType: AVMediaType
    abstract val declarationName: String
}

class AVPermissionHelper<P : Permission>(private val bundle: NSBundle, private val type: AVType<P>) {

    private val authorizationStatus: suspend () -> IOSPermissionsHelper.AuthorizationStatus get() = suspend {
        AVCaptureDevice.authorizationStatusForMediaType(type.avMediaType).toAuthorizationStatus()
    }
    private val timerHelper = PermissionRefreshScheduler(type.permissionManager, authorizationStatus)

    fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, type.declarationName).isEmpty()) {
            timerHelper.isWaiting.value = true
            AVCaptureDevice.requestAccessForMediaType(
                type.avMediaType,
                mainContinuation { allowed ->
                    timerHelper.isWaiting.value = false
                    if (allowed) {
                        type.permissionManager.grantPermission()
                    } else {
                        type.permissionManager.revokePermission(true)
                    }
                }
            )
        } else {
            type.permissionManager.revokePermission(true)
        }
    }

    suspend fun startMonitoring(interval: Long) {
        when {
            AVCaptureDevice.devicesWithMediaType(type.avMediaType).isEmpty() -> type.permissionManager.revokePermission(true)
            else -> timerHelper.startMonitoring(interval)
        }
    }

    suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }
}

private fun AVAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        AVAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        AVAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        AVAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        AVAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            error(
                "AVPermissionManager",
                "Unknown AVManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
