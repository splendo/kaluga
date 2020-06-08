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

import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.IOSPermissionsHelper
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.PermissionState
import platform.EventKit.EKAuthorizationStatus
import platform.EventKit.EKAuthorizationStatusAuthorized
import platform.EventKit.EKAuthorizationStatusDenied
import platform.EventKit.EKAuthorizationStatusNotDetermined
import platform.EventKit.EKAuthorizationStatusRestricted
import platform.EventKit.EKEntityType
import platform.EventKit.EKEventStore
import platform.Foundation.NSBundle

const val NSCalendarsUsageDescription = "NSCalendarsUsageDescription"

actual class CalendarPermissionManager(
    private val bundle: NSBundle,
    actual val calendar: Permission.Calendar,
    stateRepo: CalendarPermissionStateRepo
) : PermissionManager<Permission.Calendar>(stateRepo) {

    private val eventStore = EKEventStore()
    private val authorizationStatus = suspend {
        EKEventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent).toAuthorizationStatus()
    }
    private var timerHelper = PermissionRefreshScheduler(this, authorizationStatus)

    override suspend fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NSCalendarsUsageDescription).isEmpty()) {
            timerHelper.isWaiting = true
            eventStore.requestAccessToEntityType(EKEntityType.EKEntityTypeEvent, mainContinuation { success, error ->
                timerHelper.isWaiting = false
                error?.let {
                    debug(it.localizedDescription)
                    revokePermission(true)
                } ?: run {
                    if (success) grantPermission() else revokePermission(true)
                }
            })
        } else {
            revokePermission(true)
        }
    }

    override suspend fun initializeState(): PermissionState<Permission.Calendar> {
        return IOSPermissionsHelper.getPermissionState(authorizationStatus(), this)
    }

    override suspend fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }
}

actual class CalendarPermissionManagerBuilder(private val bundle: NSBundle = NSBundle.mainBundle) {

    actual fun create(calendar: Permission.Calendar, repo: CalendarPermissionStateRepo): CalendarPermissionManager {
        return CalendarPermissionManager(bundle, calendar, repo)
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
