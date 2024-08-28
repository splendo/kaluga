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
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.DefaultAndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

/**
 * Options for configuring a [NotificationsPermission]
 */
actual class NotificationOptions

/**
 * The [BasePermissionManager] to use as a default for [NotificationsPermission]
 * @param context the [Context] the [NotificationsPermission] is to be granted in
 * @param notificationsPermission the [NotificationsPermission] to manage
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultNotificationsPermissionManager(
    context: Context,
    notificationsPermission: NotificationsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope,
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
            permissionHandler,
        )

    actual override fun requestPermissionDidStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsManager.requestPermissions()
        } else {
            permissionHandler.status(AndroidPermissionState.GRANTED)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsManager.startMonitoring(interval)
        } else {
            permissionHandler.status(AndroidPermissionState.GRANTED)
        }
    }

    actual override fun monitoringDidStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsManager.stopMonitoring()
        }
    }
}

/**
 * A [BaseNotificationsPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class NotificationsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseNotificationsPermissionManagerBuilder {

    actual override fun create(notificationsPermission: NotificationsPermission, settings: Settings, coroutineScope: CoroutineScope): NotificationsPermissionManager {
        return DefaultNotificationsPermissionManager(context.context, notificationsPermission, settings, coroutineScope)
    }
}
