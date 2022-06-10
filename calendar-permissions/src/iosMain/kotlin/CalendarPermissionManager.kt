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

import co.touchlab.stately.freeze
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.handleAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.EventKit.EKAuthorizationStatus
import platform.EventKit.EKAuthorizationStatusAuthorized
import platform.EventKit.EKAuthorizationStatusDenied
import platform.EventKit.EKAuthorizationStatusNotDetermined
import platform.EventKit.EKAuthorizationStatusRestricted
import platform.EventKit.EKEntityType
import platform.EventKit.EKEventStore
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import kotlin.time.Duration

const val NSCalendarsUsageDescription = "NSCalendarsUsageDescription"

actual class DefaultCalendarPermissionManager(
    private val bundle: NSBundle,
    calendarPermission: CalendarPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<CalendarPermission>(calendarPermission, settings, coroutineScope) {

    private val eventStore = EKEventStore()
    private val authorizationStatus = suspend {
        EKEventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent).toAuthorizationStatus()
    }
    private var timerHelper = PermissionRefreshScheduler(authorizationStatus, ::handleAuthorizationStatus, coroutineScope)

    override fun requestPermission() {
        super.requestPermission()
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NSCalendarsUsageDescription).isEmpty()) {
            launch {
                timerHelper.isWaiting.value = true
                val deferred = CompletableDeferred<Boolean>()
                val callback = { success: Boolean, error: NSError? ->
                    error?.let { deferred.completeExceptionally(Throwable(it.localizedDescription)) } ?: deferred.complete(success)
                    Unit
                }.freeze()
                eventStore.requestAccessToEntityType(
                    EKEntityType.EKEntityTypeEvent,
                    callback
                )

                try {
                    if (deferred.await())
                        grantPermission()
                    else
                        revokePermission(true)
                } catch (t : Throwable) {
                    revokePermission(true)
                } finally {
                    timerHelper.isWaiting.value = false
                }
            }
        } else {
            revokePermission(true)
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

actual class CalendarPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCalendarPermissionManagerBuilder {

    override fun create(calendarPermission: CalendarPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): CalendarPermissionManager {
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
                "Unknown CBManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
