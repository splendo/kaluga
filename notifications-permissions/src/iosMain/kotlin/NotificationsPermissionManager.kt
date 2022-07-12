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

import co.touchlab.stately.freeze
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.AuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.AuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.requestAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.Foundation.NSError
import platform.UserNotifications.UNAuthorizationOptionNone
import platform.UserNotifications.UNAuthorizationOptions
import platform.UserNotifications.UNAuthorizationStatus
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNNotificationSettings
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.time.Duration

actual data class NotificationOptions(val options: UNAuthorizationOptions)

actual class DefaultNotificationsPermissionManager(
    notificationsPermission: NotificationsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<NotificationsPermission>(notificationsPermission, settings, coroutineScope) {

    class Provider(val notificationCenter: UNUserNotificationCenter, val coroutineScope: CoroutineScope) : AuthorizationStatusProvider {
        override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus {
            val authorizationStatus = CompletableDeferred<IOSPermissionsHelper.AuthorizationStatus>()
            val notificationCenter = notificationCenter
            coroutineScope.launch {
                val deferred = CompletableDeferred<UNNotificationSettings?>()
                val callback = { setting: UNNotificationSettings? ->
                    deferred.complete(setting)
                    Unit
                }.freeze()
                notificationCenter.getNotificationSettingsWithCompletionHandler(callback)
                authorizationStatus.complete(deferred.await()?.authorizationStatus?.toAuthorizationStatus() ?: IOSPermissionsHelper.AuthorizationStatus.NotDetermined)
            }
            return authorizationStatus.await()
        }
    }

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private val provider = Provider(notificationCenter, coroutineScope)

    private val permissionHandler = AuthorizationStatusHandler(sharedEvents, logTag, logger)
    private val timerHelper = PermissionRefreshScheduler(provider, permissionHandler, coroutineScope)

    override fun requestPermission() {
        super.requestPermission()
        permissionHandler.requestAuthorizationStatus(timerHelper, CoroutineScope(coroutineContext)) {
            val deferred = CompletableDeferred<Boolean>()
            val callback = { authorization: Boolean, error: NSError? ->
                error?.let { deferred.completeExceptionally(Throwable(error.localizedDescription)) } ?: run { deferred.complete(authorization) }
                Unit
            }.freeze()
            notificationCenter.requestAuthorizationWithOptions(
                permission.options?.options ?: UNAuthorizationOptionNone,
                callback
            )

            try {
                if (deferred.await())
                    IOSPermissionsHelper.AuthorizationStatus.Authorized
                else
                    IOSPermissionsHelper.AuthorizationStatus.Restricted
            } catch (t: Throwable) {
                IOSPermissionsHelper.AuthorizationStatus.Restricted
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
