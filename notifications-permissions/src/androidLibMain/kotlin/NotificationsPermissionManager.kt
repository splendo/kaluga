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

import com.splendo.kaluga.permissions.base.AndroidPermissionState
import com.splendo.kaluga.permissions.base.AndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration

actual class NotificationOptions

actual class DefaultNotificationsPermissionManager(
    notificationsPermission: NotificationsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<NotificationsPermission>(notificationsPermission, settings, coroutineScope) {

    private val permissionHandler = AndroidPermissionStateHandler(sharedEvents, logTag, logger)
    override fun requestPermission() {
        super.requestPermission()
        val handler = permissionHandler
        launch {
            handler.emit(AndroidPermissionState.GRANTED)
        }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        val handler = permissionHandler
        launch {
            handler.emit(AndroidPermissionState.GRANTED)
        }
    }
}

actual class NotificationsPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseNotificationsPermissionManagerBuilder {

    override fun create(notificationsPermission: NotificationsPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): NotificationsPermissionManager {
        return DefaultNotificationsPermissionManager(notificationsPermission, settings, coroutineScope)
    }
}
