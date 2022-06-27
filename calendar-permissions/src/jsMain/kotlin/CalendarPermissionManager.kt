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

import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo

actual class CalendarPermissionManager(actual val calendarPermission: CalendarPermission, repo: PermissionStateRepo<CalendarPermission>) : PermissionManager<CalendarPermission>(repo) {

    override suspend fun requestPermission() {
        TODO("not implemented")
    }

    override suspend fun startMonitoring(interval: Long) {
        TODO("not implemented")
    }

    override suspend fun stopMonitoring() {
        TODO("not implemented")
    }
}

actual class CalendarPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseCalendarPermissionManagerBuilder {

    override fun create(calendarPermission: CalendarPermission, repo: PermissionStateRepo<CalendarPermission>): PermissionManager<CalendarPermission> {
        return CalendarPermissionManager(calendarPermission, repo)
    }
}
