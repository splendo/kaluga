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

import android.Manifest
import android.content.Context
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo

actual class CalendarPermissionManager(
    context: Context,
    actual val calendarPermission: CalendarPermission,
    stateRepo: PermissionStateRepo<CalendarPermission>
) : PermissionManager<CalendarPermission>(stateRepo) {

    private val permissionsManager = AndroidPermissionsManager(context, this, if (calendarPermission.allowWrite) arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR) else arrayOf(Manifest.permission.READ_CALENDAR))

    override suspend fun requestPermission() {
        permissionsManager.requestPermissions()
    }

    override suspend fun startMonitoring(interval: Long) {
        permissionsManager.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        permissionsManager.stopMonitoring()
    }
}

actual class CalendarPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCalendarPermissionManagerBuilder {

    override fun create(calendarPermission: CalendarPermission, repo: PermissionStateRepo<CalendarPermission>): PermissionManager<CalendarPermission> {
        return CalendarPermissionManager(context.context, calendarPermission, repo)
    }
}
