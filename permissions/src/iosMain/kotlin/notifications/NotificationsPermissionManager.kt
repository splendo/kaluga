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

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.permissions.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.UserNotifications.*

actual data class NotificationOptions(val options: UNAuthorizationOptions)

actual class NotificationsPermissionManager(actual val notifications: Permission.Notifications,
                                            stateRepo: NotificationsPermissionStateRepo,
                                            coroutineScope: CoroutineScope
) : PermissionManager<Permission.Notifications>(stateRepo, coroutineScope) {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private var authorization: suspend () -> IOSPermissionsHelper.AuthorizationStatus = {
        val authorizationStatus = CompletableDeferred<IOSPermissionsHelper.AuthorizationStatus>()
        launch(MainQueueDispatcher) {
            notificationCenter.getNotificationSettingsWithCompletionHandler(mainContinuation { settings ->
                authorizationStatus.complete(settings?.authorizationStatus?.toAuthorizationStatus() ?: IOSPermissionsHelper.AuthorizationStatus.NotDetermined)
            })
        }
        authorizationStatus.await()
    }
    private val timerHelper = PermissionTimerHelper( this, authorization)

    override suspend fun requestPermission() {
        timerHelper.isWaiting = true
        notificationCenter.requestAuthorizationWithOptions(notifications.options?.options ?: UNAuthorizationOptionNone, mainContinuation { authorization, error ->
            timerHelper.isWaiting = false
            error?.let {
                revokePermission(true)
            } ?: run {
                if (authorization) grantPermission() else revokePermission(true)
            }
        })
    }

    override suspend fun initializeState(): PermissionState<Permission.Notifications> {
        return IOSPermissionsHelper.getPermissionState(authorization(), this)
    }

    override suspend fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }
}

actual class NotificationsPermissionManagerBuilder :BaseNotificationsPermissionManagerBuilder {

    override fun create(notifications: Permission.Notifications, repo: NotificationsPermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Notifications> {
        return NotificationsPermissionManager(notifications, repo, coroutineScope)
    }
}

private fun UNAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when(this) {
        UNAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        UNAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        UNAuthorizationStatusProvisional -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        UNAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            com.splendo.kaluga.logging.error(
                "CalendarPermissionManager",
                "Unknown CBManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
