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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

expect class CalendarPermissionManager : PermissionManager<Permission.Calendar> {
    val calendar: Permission.Calendar
}

interface BaseCalendarPermissionManagerBuilder {
    fun create(calendar: Permission.Calendar, repo: CalendarPermissionStateRepo): CalendarPermissionManager
}

expect class CalendarPermissionManagerBuilder : BaseCalendarPermissionManagerBuilder

class CalendarPermissionStateRepo(calendar: Permission.Calendar, builder: CalendarPermissionManagerBuilder) : PermissionStateRepo<Permission.Calendar>() {

    override val permissionManager: PermissionManager<Permission.Calendar> = builder.create(calendar, this)
}
