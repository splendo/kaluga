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
import com.splendo.kaluga.permissions.base.AndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

actual class DefaultCalendarPermissionManager(
    context: Context,
    calendarPermission: CalendarPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<CalendarPermission>(calendarPermission, settings, coroutineScope) {

    private val permissionHandler = AndroidPermissionStateHandler(eventChannel, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(
        context,
        if (permission.allowWrite) arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR) else arrayOf(Manifest.permission.READ_CALENDAR),
        coroutineScope,
        logTag,
        logger,
        permissionHandler
    )

    override fun requestPermission() {
        super.requestPermission()
        permissionsManager.requestPermissions()
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        permissionsManager.startMonitoring(interval)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        permissionsManager.stopMonitoring()
    }
}

actual class CalendarPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCalendarPermissionManagerBuilder {

    override fun create(calendarPermission: CalendarPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): CalendarPermissionManager {
        return DefaultCalendarPermissionManager(context.context, calendarPermission, settings, coroutineScope)
    }
}
