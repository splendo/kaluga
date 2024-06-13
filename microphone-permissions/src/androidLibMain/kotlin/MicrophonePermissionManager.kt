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
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.DefaultAndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

/**
 * The [BasePermissionManager] to use as a default for [MicrophonePermission]
 * @param context the [Context] the [MicrophonePermission] is to be granted in
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultMicrophonePermissionManager(
    context: Context,
    settings: Settings,
    coroutineScope: CoroutineScope,
) : BasePermissionManager<MicrophonePermission>(MicrophonePermission, settings, coroutineScope) {

    private val permissionHandler = DefaultAndroidPermissionStateHandler(eventChannel, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(context, arrayOf(Manifest.permission.RECORD_AUDIO), coroutineScope, logTag, logger, permissionHandler)
    private val supported = context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)

    actual override fun requestPermissionDidStart() {
        if (supported) {
            permissionsManager.requestPermissions()
        } else {
            permissionHandler.status(AndroidPermissionState.DENIED_DO_NOT_ASK)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        if (supported) {
            permissionsManager.startMonitoring(interval)
        } else {
            permissionHandler.status(AndroidPermissionState.DENIED_DO_NOT_ASK)
        }
    }

    actual override fun monitoringDidStop() {
        if (supported) {
            permissionsManager.stopMonitoring()
        }
    }
}

/**
 * A [BaseMicrophonePermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class MicrophonePermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseMicrophonePermissionManagerBuilder {

    actual override fun create(settings: Settings, coroutineScope: CoroutineScope): MicrophonePermissionManager {
        return DefaultMicrophonePermissionManager(context.context, settings, coroutineScope)
    }
}
