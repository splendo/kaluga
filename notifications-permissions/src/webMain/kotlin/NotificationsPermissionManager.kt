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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * Options for configuring a [NotificationsPermission]. The browser
 * [Notification API](https://developer.mozilla.org/en-US/docs/Web/API/Notification) has no
 * authorization options to configure, so this is empty on the JS family.
 */
actual class NotificationOptions

// `Notification.permission` is a synchronous getter (`"granted"`/`"denied"`/`"default"`), unlike the
// async `navigator.permissions.query`, so the whole manager can be shared by the JS family.
private fun notificationPermissionState(): String = js("(typeof Notification !== 'undefined') ? Notification.permission : 'unsupported'")

private fun requestNotificationPermission() {
    js("if (typeof Notification !== 'undefined' && Notification.requestPermission) { Notification.requestPermission(); }")
}

/**
 * The default [BasePermissionManager] for [NotificationsPermission] on the JS family (js + wasmJs).
 *
 * Backed by the browser [Notification API](https://developer.mozilla.org/en-US/docs/Web/API/Notification):
 * `Notification.permission` is polled on the monitoring interval and [requestPermissionDidStart]
 * calls `Notification.requestPermission()` to surface the prompt.
 * @param notificationsPermission the [NotificationsPermission] to manage
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultNotificationsPermissionManager(notificationsPermission: NotificationsPermission, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<NotificationsPermission>(notificationsPermission, settings, coroutineScope) {

    private var monitoringJob: Job? = null
    private var lastState: String? = null

    actual override fun requestPermissionDidStart() {
        requestNotificationPermission()
    }

    actual override fun monitoringDidStart(interval: Duration) {
        if (monitoringJob != null) return
        monitoringJob = launch {
            while (isActive) {
                emitForState(notificationPermissionState())
                delay(interval)
            }
        }
    }

    actual override fun monitoringDidStop() {
        monitoringJob?.cancel()
        monitoringJob = null
        lastState = null
    }

    private fun emitForState(state: String) {
        if (state == lastState) return
        lastState = state
        when (state) {
            "granted" -> emitEvent(PermissionManager.Event.PermissionGranted)

            // A browser "denied" cannot be re-prompted programmatically, so treat it as locked.
            "denied" -> emitEvent(PermissionManager.Event.PermissionDenied(locked = true))

            // "unsupported": no Notification API at all — there is nothing to request.
            "unsupported" -> emitEvent(PermissionManager.Event.PermissionDenied(locked = true))

            // "default": not yet decided, still requestable.
            else -> emitEvent(PermissionManager.Event.PermissionDenied(locked = false))
        }
    }
}

/**
 * A [BaseNotificationsPermissionManagerBuilder] for the JS family. The [context] is unused — the
 * browser has no ambient permission context.
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class NotificationsPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseNotificationsPermissionManagerBuilder {
    actual override fun create(
        notificationsPermission: NotificationsPermission,
        settings: BasePermissionManager.Settings,
        coroutineScope: CoroutineScope,
    ): NotificationsPermissionManager = DefaultNotificationsPermissionManager(notificationsPermission, settings, coroutineScope)
}
