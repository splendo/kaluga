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

package com.splendo.kaluga.permissions.notifications

import android.Manifest
import android.content.Context
import android.os.Build
import com.splendo.kaluga.permissions.base.AndroidPermissionState
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.DefaultAndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

actual class NotificationOptions

actual class DefaultNotificationsPermissionManager(
    context: Context,
    notificationsPermission: NotificationsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<NotificationsPermission>(notificationsPermission, settings, coroutineScope) {

    private val permissionHandler = DefaultAndroidPermissionStateHandler(eventChannel, logTag, logger)
    private val permissionsManager =
        AndroidPermissionsManager(
            context,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                emptyArray()
            },
            coroutineScope,
            logTag,
            logger,
            permissionHandler
        )

    override fun requestPermissionDidStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsManager.requestPermissions()
        } else {
            permissionHandler.status(AndroidPermissionState.GRANTED)
        }
    }

    override fun monitoringDidStart(interval: Duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsManager.startMonitoring(interval)
        } else {
            permissionHandler.status(AndroidPermissionState.GRANTED)
        }
    }

    override fun monitoringDidStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsManager.stopMonitoring()
        }
    }
}

actual class NotificationsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseNotificationsPermissionManagerBuilder {

    override fun create(notificationsPermission: NotificationsPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): NotificationsPermissionManager {
        return DefaultNotificationsPermissionManager(context.context, notificationsPermission, settings, coroutineScope)
    }
}
