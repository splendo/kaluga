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

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.handleAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.UserNotifications.UNAuthorizationOptionNone
import platform.UserNotifications.UNAuthorizationOptions
import platform.UserNotifications.UNAuthorizationStatus
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.time.Duration

actual data class NotificationOptions(val options: UNAuthorizationOptions)

actual class DefaultNotificationsPermissionManager(
    notificationsPermission: NotificationsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<NotificationsPermission>(notificationsPermission, settings, coroutineScope) {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private var authorization: suspend () -> IOSPermissionsHelper.AuthorizationStatus = {
        val authorizationStatus = CompletableDeferred<IOSPermissionsHelper.AuthorizationStatus>()
        launch {
            notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
                authorizationStatus.complete(settings?.authorizationStatus?.toAuthorizationStatus() ?: IOSPermissionsHelper.AuthorizationStatus.NotDetermined)
            }
        }
        authorizationStatus.await()
    }
    private val timerHelper = PermissionRefreshScheduler(authorization, ::handleAuthorizationStatus, coroutineScope)

    override fun requestPermission() {
        super.requestPermission()
        timerHelper.isWaiting.value = true
        notificationCenter.requestAuthorizationWithOptions(
            permission.options?.options ?: UNAuthorizationOptionNone
        ) { authorization, error ->
            timerHelper.isWaiting.value = false
            error?.let {
                revokePermission(true)
            } ?: run {
                if (authorization) grantPermission() else revokePermission(true)
            }
        }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        timerHelper.startMonitoring(interval)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        timerHelper.stopMonitoring()
    }
}

actual class NotificationsPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseNotificationsPermissionManagerBuilder {

    override fun create(notificationsPermission: NotificationsPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): NotificationsPermissionManager {
        return DefaultNotificationsPermissionManager(notificationsPermission, settings, coroutineScope)
    }
}

private fun UNAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        UNAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        UNAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        UNAuthorizationStatusProvisional -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        UNAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            error(
                "CalendarPermissionManager",
                "Unknown CBManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
