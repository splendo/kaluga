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

package com.splendo.kaluga.permissions.calendar

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.CurrentAuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.DefaultAuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.requestAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import platform.EventKit.EKAuthorizationStatus
import platform.EventKit.EKAuthorizationStatusAuthorized
import platform.EventKit.EKAuthorizationStatusDenied
import platform.EventKit.EKAuthorizationStatusNotDetermined
import platform.EventKit.EKAuthorizationStatusRestricted
import platform.EventKit.EKEntityType
import platform.EventKit.EKEventStore
import platform.Foundation.NSBundle
import kotlin.time.Duration

private const val NS_CALENDARS_USAGE_DESCRIPTION = "NSCalendarsUsageDescription"

/**
 * The [BasePermissionManager] to use as a default for [CalendarPermission]
 * @param bundle the [NSBundle] the [CalendarPermission] is to be granted in
 * @param calendarPermission the [CalendarPermission] to manage.
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultCalendarPermissionManager(
    private val bundle: NSBundle,
    calendarPermission: CalendarPermission,
    settings: Settings,
    coroutineScope: CoroutineScope,
) : BasePermissionManager<CalendarPermission>(calendarPermission, settings, coroutineScope) {

    private class Provider : CurrentAuthorizationStatusProvider {
        override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus = EKEventStore
            .authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent)
            .toAuthorizationStatus()
    }

    private val eventStore = EKEventStore()
    private val provider = Provider()

    private val permissionHandler = DefaultAuthorizationStatusHandler(eventChannel, logTag, logger)
    private var timerHelper = PermissionRefreshScheduler(provider, permissionHandler, coroutineScope)

    actual override fun requestPermissionDidStart() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NS_CALENDARS_USAGE_DESCRIPTION).isEmpty()) {
            permissionHandler.requestAuthorizationStatus(timerHelper, CoroutineScope(coroutineContext)) {
                val deferred = CompletableDeferred<Boolean>()
                eventStore.requestAccessToEntityType(
                    EKEntityType.EKEntityTypeEvent,
                ) { success, error ->
                    error?.let { deferred.completeExceptionally(Throwable(it.localizedDescription)) } ?: deferred.complete(success)
                    Unit
                }

                try {
                    if (deferred.await()) IOSPermissionsHelper.AuthorizationStatus.Authorized else IOSPermissionsHelper.AuthorizationStatus.Restricted
                } catch (t: Throwable) {
                    IOSPermissionsHelper.AuthorizationStatus.Restricted
                }
            }
        } else {
            val permissionHandler = permissionHandler
            permissionHandler.status(IOSPermissionsHelper.AuthorizationStatus.Restricted)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        timerHelper.startMonitoring(interval)
    }

    actual override fun monitoringDidStop() {
        timerHelper.stopMonitoring()
    }
}

/**
 * A [BaseCalendarPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class CalendarPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCalendarPermissionManagerBuilder {

    actual override fun create(calendarPermission: CalendarPermission, settings: Settings, coroutineScope: CoroutineScope): CalendarPermissionManager {
        return DefaultCalendarPermissionManager(context, calendarPermission, settings, coroutineScope)
    }
}

private fun EKAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        EKAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        EKAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        EKAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        EKAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            error(
                "CalendarPermissionManager",
                "Unknown CBManagerAuthorization status={$this}",
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
