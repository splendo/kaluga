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

package com.splendo.kaluga.permissions.microphone

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.ApplePermissionsHelper
import kotlinx.coroutines.CompletableDeferred
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType

internal actual suspend fun currentMicrophoneAuthorizationStatus(): ApplePermissionsHelper.AuthorizationStatus =
    AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeAudio).toAuthorizationStatus()

internal actual suspend fun requestMicrophoneAccess(): ApplePermissionsHelper.AuthorizationStatus {
    val deferred = CompletableDeferred<Boolean>()
    AVCaptureDevice.requestAccessForMediaType(AVMediaTypeAudio) { allowed ->
        deferred.complete(allowed)
    }
    return if (deferred.await()) ApplePermissionsHelper.AuthorizationStatus.Authorized else ApplePermissionsHelper.AuthorizationStatus.Denied
}

internal actual fun isMicrophoneAvailable(): Boolean = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeAudio) != null

private fun AVAuthorizationStatus.toAuthorizationStatus(): ApplePermissionsHelper.AuthorizationStatus = when (this) {
    AVAuthorizationStatusAuthorized -> ApplePermissionsHelper.AuthorizationStatus.Authorized

    AVAuthorizationStatusDenied -> ApplePermissionsHelper.AuthorizationStatus.Denied

    AVAuthorizationStatusRestricted -> ApplePermissionsHelper.AuthorizationStatus.Restricted

    AVAuthorizationStatusNotDetermined -> ApplePermissionsHelper.AuthorizationStatus.NotDetermined

    else -> {
        error("MicrophonePermissionManager", "Unknown AVAuthorizationStatus={$this}")
        ApplePermissionsHelper.AuthorizationStatus.NotDetermined
    }
}
