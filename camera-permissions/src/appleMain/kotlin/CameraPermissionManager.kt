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

package com.splendo.kaluga.permissions.camera

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.ApplePermissionsHelper
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.CurrentAuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.DefaultAuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.requestAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSBundle
import kotlin.time.Duration

private const val NS_CAMERA_USAGE_DESCRIPTION = "NSCameraUsageDescription"

/**
 * The [BasePermissionManager] to use as a default for [CameraPermission]
 * @param bundle the [NSBundle] the [CameraPermission] is to be granted in
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultCameraPermissionManager(private val bundle: NSBundle, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<CameraPermission>(CameraPermission, settings, coroutineScope) {

    private val permissionHandler = DefaultAuthorizationStatusHandler(eventChannel, logTag, logger)
    private val provider = object : CurrentAuthorizationStatusProvider {
        override suspend fun provide(): ApplePermissionsHelper.AuthorizationStatus = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo).toAuthorizationStatus()
    }
    private val timerHelper = PermissionRefreshScheduler(provider, permissionHandler, coroutineScope)

    actual override fun requestPermissionDidStart() {
        if (ApplePermissionsHelper.missingDeclarationsInPList(bundle, NS_CAMERA_USAGE_DESCRIPTION).isEmpty()) {
            permissionHandler.requestAuthorizationStatus(timerHelper, this) {
                val deferred = CompletableDeferred<Boolean>()
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { allowed ->
                    deferred.complete(allowed)
                    Unit
                }
                if (deferred.await()) ApplePermissionsHelper.AuthorizationStatus.Authorized else ApplePermissionsHelper.AuthorizationStatus.Denied
            }
        } else {
            permissionHandler.status(ApplePermissionsHelper.AuthorizationStatus.Denied)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        when {
            AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) == null ->
                permissionHandler.status(ApplePermissionsHelper.AuthorizationStatus.Denied)

            else -> timerHelper.startMonitoring(interval)
        }
    }

    actual override fun monitoringDidStop() {
        timerHelper.stopMonitoring()
    }
}

/**
 * A [BaseCameraPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class CameraPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCameraPermissionManagerBuilder {

    actual override fun create(settings: Settings, coroutineScope: CoroutineScope): CameraPermissionManager = DefaultCameraPermissionManager(context, settings, coroutineScope)
}

private fun AVAuthorizationStatus.toAuthorizationStatus(): ApplePermissionsHelper.AuthorizationStatus = when (this) {
    AVAuthorizationStatusAuthorized -> ApplePermissionsHelper.AuthorizationStatus.Authorized

    AVAuthorizationStatusDenied -> ApplePermissionsHelper.AuthorizationStatus.Denied

    AVAuthorizationStatusRestricted -> ApplePermissionsHelper.AuthorizationStatus.Restricted

    AVAuthorizationStatusNotDetermined -> ApplePermissionsHelper.AuthorizationStatus.NotDetermined

    else -> {
        error("CameraPermissionManager", "Unknown AVAuthorizationStatus={$this}")
        ApplePermissionsHelper.AuthorizationStatus.NotDetermined
    }
}
