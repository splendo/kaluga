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

package com.splendo.kaluga.permissions.av

import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.permissions.*
import com.splendo.kaluga.permissions.camera.CameraPermissionManager
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionManager
import platform.AVFoundation.*
import platform.Foundation.NSBundle

internal class AVPermissionHelper<P:Permission>(private val bundle: NSBundle, private val type: Type<P>) {

    sealed class Type<P:Permission> {

        abstract val permissionManager: PermissionManager<P>
        abstract val avMediaType: AVMediaType
        abstract val declarationName: String

        class Camera(override val permissionManager: CameraPermissionManager) : Type<Permission.Camera>() {

            override val avMediaType = AVMediaTypeVideo
            override val declarationName = "NSCameraUsageDescription"

        }

        class Microphone(override val permissionManager: MicrophonePermissionManager) : Type<Permission.Microphone>() {

            override val avMediaType = AVMediaTypeAudio
            override val declarationName = "NSMicrophoneUsageDescription"

        }
    }
    private val authorizationStatus = {
        AVCaptureDevice.authorizationStatusForMediaType(type.avMediaType).toAuthorizationStatus()
    }
    private val timerHelper = PermissionTimerHelper(type.permissionManager, authorizationStatus)


    internal fun requestPermission() {
        if (IOSPermissionsHelper.checkDeclarationInPList(bundle, type.declarationName).isEmpty()) {
            timerHelper.isWaiting = true
            AVCaptureDevice.requestAccessForMediaType(type.avMediaType, mainContinuation { allowed ->
                timerHelper.isWaiting = false
                if (allowed) {
                    type.permissionManager.grantPermission()
                } else {
                    type.permissionManager.revokePermission(true)
                }
            })
        } else {
            type.permissionManager.revokePermission(true)
        }
    }

    internal fun initializeState(): PermissionState<P> {
        return when {
            AVCaptureDevice.devicesWithMediaType(type.avMediaType).isEmpty() -> PermissionState.Denied.Locked(type.permissionManager)
            else -> IOSPermissionsHelper.getPermissionState(authorizationStatus(), type.permissionManager)
        }
    }

    internal fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    internal fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }

}


private fun AVAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when(this) {
        AVAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        AVAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        AVAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        AVAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            com.splendo.kaluga.log.error(
                "AVPermissionManager",
                "Unknown AVManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
