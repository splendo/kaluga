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
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.permissions.*
import platform.EventKit.*
import platform.Foundation.NSBundle

actual class CalendarPermissionManager(
    private val bundle: NSBundle,
    actual val calendar: Permission.Calendar,
    stateRepo: CalendarPermissionStateRepo
) : PermissionManager<Permission.Calendar>(stateRepo) {

    private val eventStore = EKEventStore()
    private val authorizationStatus = {
        EKEventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent).toAuthorizationStatus()
    }
    private var timerHelper = PermissionTimerHelper(this, authorizationStatus)

    override suspend fun requestPermission() {
        if (IOSPermissionsHelper.checkDeclarationInPList(bundle, "NSCalendarsUsageDescription").isEmpty()) {
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

    override fun initializeState(): PermissionState<Permission.Calendar> {
        return IOSPermissionsHelper.getPermissionState(authorizationStatus(), this)
    }

    override fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }

}

actual class CalendarPermissionManagerBuilder(private val bundle: NSBundle = NSBundle.mainBundle) : BaseCalendarPermissionManagerBuilder {

    override fun create(calendar: Permission.Calendar, repo: CalendarPermissionStateRepo): CalendarPermissionManager {
        return CalendarPermissionManager(bundle, calendar, repo)
    }

}

private fun EKAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when(this) {
        EKAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        EKAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        EKAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        EKAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            com.splendo.kaluga.log.error(
                "CalendarPermissionManager",
                "Unknown CBManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}