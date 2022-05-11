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

import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.IOSPermissionsHelper
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.PermissionState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import platform.UserNotifications.UNAuthorizationOptionNone
import platform.UserNotifications.UNAuthorizationOptions
import platform.UserNotifications.UNAuthorizationStatus
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

actual data class NotificationOptions(val options: UNAuthorizationOptions)

actual class NotificationsPermissionManager(
    actual val notifications: NotificationsPermission,
    stateRepo: NotificationsPermissionStateRepo
) : PermissionManager<NotificationsPermission>(stateRepo) {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private var authorization: suspend () -> IOSPermissionsHelper.AuthorizationStatus = {
        val authorizationStatus = CompletableDeferred<IOSPermissionsHelper.AuthorizationStatus>()
        launch {
            notificationCenter.getNotificationSettingsWithCompletionHandler(
                mainContinuation { settings ->
                    authorizationStatus.complete(settings?.authorizationStatus?.toAuthorizationStatus() ?: IOSPermissionsHelper.AuthorizationStatus.NotDetermined)
                }
            )
        }
        authorizationStatus.await()
    }
    private val timerHelper = PermissionRefreshScheduler(this, authorization)

    override suspend fun requestPermission() {
        timerHelper.isWaiting.value = true
        notificationCenter.requestAuthorizationWithOptions(
            notifications.options?.options ?: UNAuthorizationOptionNone,
            mainContinuation { authorization, error ->
                timerHelper.isWaiting.value = false
                error?.let {
                    revokePermission(true)
                } ?: run {
                    if (authorization) grantPermission() else revokePermission(true)
                }
            }
        )
    }

    override suspend fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }
}

actual class NotificationsPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseNotificationsPermissionManagerBuilder {

    override fun create(notifications: NotificationsPermission, repo: NotificationsPermissionStateRepo): PermissionManager<NotificationsPermission> {
        return NotificationsPermissionManager(notifications, repo)
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
