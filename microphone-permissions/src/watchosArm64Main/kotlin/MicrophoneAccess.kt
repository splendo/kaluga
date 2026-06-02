/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.permissions.base.ApplePermissionsHelper
import kotlinx.coroutines.CompletableDeferred
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionRecordPermissionDenied
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVAudioSessionRecordPermissionUndetermined

internal actual suspend fun currentMicrophoneAuthorizationStatus(): ApplePermissionsHelper.AuthorizationStatus =
    when (AVAudioSession.sharedInstance().recordPermission) {
        AVAudioSessionRecordPermissionGranted -> ApplePermissionsHelper.AuthorizationStatus.Authorized
        AVAudioSessionRecordPermissionDenied -> ApplePermissionsHelper.AuthorizationStatus.Denied
        AVAudioSessionRecordPermissionUndetermined -> ApplePermissionsHelper.AuthorizationStatus.NotDetermined
        else -> ApplePermissionsHelper.AuthorizationStatus.NotDetermined
    }

internal actual suspend fun requestMicrophoneAccess(): ApplePermissionsHelper.AuthorizationStatus {
    val deferred = CompletableDeferred<Boolean>()
    AVAudioSession.sharedInstance().requestRecordPermission { granted ->
        deferred.complete(granted)
        Unit
    }
    return if (deferred.await()) ApplePermissionsHelper.AuthorizationStatus.Authorized else ApplePermissionsHelper.AuthorizationStatus.Denied
}

internal actual fun isMicrophoneAvailable(): Boolean = true
