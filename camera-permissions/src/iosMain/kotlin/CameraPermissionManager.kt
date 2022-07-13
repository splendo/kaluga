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

import com.splendo.kaluga.permissions.base.AuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.av.AVPermissionHelper
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSBundle
import kotlin.time.Duration

actual class DefaultCameraPermissionManager(
    bundle: NSBundle,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<CameraPermission>(CameraPermission, settings, coroutineScope) {

    private val permissionHandler = AuthorizationStatusHandler(eventChannel, logTag, logger)
    private val avPermissionHelper = AVPermissionHelper(bundle, AVTypeCamera(), permissionHandler, coroutineScope)

    override fun requestPermission() {
        super.requestPermission()
        avPermissionHelper.requestPermission()
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        avPermissionHelper.startMonitoring(interval)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        avPermissionHelper.stopMonitoring()
    }
}

actual class CameraPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCameraPermissionManagerBuilder {

    override fun create(settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): CameraPermissionManager {
        return DefaultCameraPermissionManager(context, settings, coroutineScope)
    }
}
