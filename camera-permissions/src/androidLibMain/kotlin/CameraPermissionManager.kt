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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.AndroidPermissionState
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.DefaultAndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

actual class DefaultCameraPermissionManager(
    context: Context,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<CameraPermission>(CameraPermission, settings, coroutineScope) {

    private val permissionHandler = DefaultAndroidPermissionStateHandler(eventChannel, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(
        context,
        arrayOf(Manifest.permission.CAMERA),
        coroutineScope,
        logTag,
        logger,
        permissionHandler
    )
    private val supported = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    override fun requestPermissionDidStart() {
        if (supported) {
            permissionsManager.requestPermissions()
        } else {
            logger.error(logTag) { "Camera not Supported" }
        }
    }

    override fun monitoringDidStart(interval: Duration) {
        if (supported) {
            permissionsManager.startMonitoring(interval)
        } else {
            permissionHandler.status(AndroidPermissionState.DENIED_DO_NOT_ASK)
        }
    }

    override fun monitoringDidStop() {
        if (supported) {
            permissionsManager.stopMonitoring()
        }
    }
}

actual class CameraPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCameraPermissionManagerBuilder {

    override fun create(settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): CameraPermissionManager {
        return DefaultCameraPermissionManager(context.context, settings, coroutineScope)
    }
}
