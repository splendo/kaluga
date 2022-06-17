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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.splendo.kaluga.permissions.base.AndroidPermissionState
import com.splendo.kaluga.permissions.base.AndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration

actual class DefaultMicrophonePermissionManager(
    context: Context,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<MicrophonePermission>(MicrophonePermission, settings, coroutineScope) {

    private val permissionHandler = AndroidPermissionStateHandler(sharedEvents, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(context, arrayOf(Manifest.permission.RECORD_AUDIO), coroutineScope, logTag, logger, permissionHandler)
    private val supported = context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)

    override fun requestPermission() {
        super.requestPermission()
        if (supported)
            permissionsManager.requestPermissions()
        else {
            val handler = permissionHandler
            launch {
                handler.emit(AndroidPermissionState.DENIED_DO_NOT_ASK)
            }
        }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        if (supported)
            permissionsManager.startMonitoring(interval)
        else {
            val handler = permissionHandler
            launch {
                handler.emit(AndroidPermissionState.DENIED_DO_NOT_ASK)
            }
        }
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        if (supported)
            permissionsManager.stopMonitoring()
    }
}

actual class MicrophonePermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseMicrophonePermissionManagerBuilder {

    override fun create(settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): MicrophonePermissionManager {
        return DefaultMicrophonePermissionManager(context.context, settings, coroutineScope)
    }
}
