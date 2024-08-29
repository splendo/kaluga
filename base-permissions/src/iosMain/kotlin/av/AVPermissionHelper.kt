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
import com.splendo.kaluga.permissions.base.AuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.CurrentAuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
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
import platform.AVFoundation.AVMediaType
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSBundle
import kotlin.time.Duration

/**
 * A type of permission associated with an [AVMediaType]
 */
abstract class AVType {
    /**
     * The [AVMediaType] associated with this permission
     */
    abstract val avMediaType: AVMediaType

    /**
     * The declaration name that should be added to the PList in order to request this permission
     */
    abstract val declarationName: String
}

/**
 * A helper for managing the [IOSPermissionsHelper.AuthorizationStatus] of an [AVType] permission.
 * @param bundle the [NSBundle] for which the permission should be applied
 * @param type the [AVType] of the permission to manage
 * @param authorizationStatusHandler the [AuthorizationStatusHandler] to handle changes to the permission
 * @param coroutineScope the [CoroutineScope] to manage monitoring the permission
 */
class AVPermissionHelper(
    private val bundle: NSBundle,
    private val type: AVType,
    private val authorizationStatusHandler: AuthorizationStatusHandler,
    private val coroutineScope: CoroutineScope,
) : CoroutineScope by coroutineScope {

    private class Provider(private val type: AVMediaType) : CurrentAuthorizationStatusProvider {
        override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus = AVCaptureDevice.authorizationStatusForMediaType(type).toAuthorizationStatus()
    }

    private val provider = Provider(type.avMediaType)
    private val timerHelper = PermissionRefreshScheduler(provider, authorizationStatusHandler, coroutineScope)

    /**
     * Attempts to request the [AVType] permission and updates the provided [AuthorizationStatusHandler]
     */
    fun requestPermission() {
        val authorizationStatusHandler = authorizationStatusHandler
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, type.declarationName).isEmpty()) {
            val mediaType = type.avMediaType
            authorizationStatusHandler.requestAuthorizationStatus(timerHelper, coroutineScope) {
                val deferred = CompletableDeferred<Boolean>()
                AVCaptureDevice.requestAccessForMediaType(
                    mediaType,
                ) { allowed ->
                    deferred.complete(allowed)
                    Unit
                }
                if (deferred.await()) IOSPermissionsHelper.AuthorizationStatus.Authorized else IOSPermissionsHelper.AuthorizationStatus.Denied
            }
        } else {
            authorizationStatusHandler.status(IOSPermissionsHelper.AuthorizationStatus.Denied)
        }
    }

    /**
     * Starts monitoring for changes to the [IOSPermissionsHelper.AuthorizationStatus]
     * @param interval the [Duration] between checks for changes to the [IOSPermissionsHelper.AuthorizationStatus]
     */
    fun startMonitoring(interval: Duration) {
        when {
            AVCaptureDevice.devicesWithMediaType(type.avMediaType).isEmpty() -> {
                authorizationStatusHandler.status(IOSPermissionsHelper.AuthorizationStatus.Denied)
            }
            else -> timerHelper.startMonitoring(interval)
        }
    }

    /**
     * Stops monitoring for changes to the [IOSPermissionsHelper.AuthorizationStatus]
     */
    fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }
}

private fun AVAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus = when (this) {
    AVAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
    AVAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
    AVAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
    AVAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
    else -> {
        error(
            "AVPermissionManager",
            "Unknown AVManagerAuthorization status={$this}",
        )
        IOSPermissionsHelper.AuthorizationStatus.NotDetermined
    }
}
