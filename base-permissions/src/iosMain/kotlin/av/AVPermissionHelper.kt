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

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import kotlinx.coroutines.CoroutineScope
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
import kotlin.time.Duration

abstract class AVType {
    abstract val avMediaType: AVMediaType
    abstract val declarationName: String
}

class AVPermissionHelper(
    private val bundle: NSBundle,
    private val type: AVType,
    private val onPermissionChanged: (IOSPermissionsHelper.AuthorizationStatus) -> Unit,
    coroutineScope: CoroutineScope
) {

    private val authorizationStatus: suspend () -> IOSPermissionsHelper.AuthorizationStatus get() = suspend {
        AVCaptureDevice.authorizationStatusForMediaType(type.avMediaType).toAuthorizationStatus()
    }
    private val timerHelper = PermissionRefreshScheduler(authorizationStatus, onPermissionChanged, coroutineScope)

    fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, type.declarationName).isEmpty()) {
            timerHelper.isWaiting.value = true
            AVCaptureDevice.requestAccessForMediaType(
                type.avMediaType
            ) { allowed ->
                timerHelper.isWaiting.value = false
                if (allowed) {
                    onPermissionChanged(IOSPermissionsHelper.AuthorizationStatus.Authorized)
                } else {
                    onPermissionChanged(IOSPermissionsHelper.AuthorizationStatus.Denied)
                }
            }
        } else {
            onPermissionChanged(IOSPermissionsHelper.AuthorizationStatus.Denied)
        }
    }

    fun startMonitoring(interval: Duration) {
        when {
            AVCaptureDevice.devicesWithMediaType(type.avMediaType).isEmpty() -> onPermissionChanged(IOSPermissionsHelper.AuthorizationStatus.Denied)
            else -> timerHelper.startMonitoring(interval)
        }
    }

    fun stopMonitoring() {
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
