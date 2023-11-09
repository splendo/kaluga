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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * [Permission] to access the users Calendar
 * @param allowWrite If `true` writing to the calendar is permitted
 */
data class CalendarPermission(val allowWrite: Boolean = false) : Permission() {
    override val name: String = "Calendar - ${if (allowWrite) "ReadWrite" else "ReadOnly"}"
}

/**
 * Registers a [BaseCalendarPermissionManagerBuilder] and [PermissionStateRepo] for [CalendarPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param calendarPermissionManagerBuilderBuilder method for creating a [BaseCalendarPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseCalendarPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseCalendarPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerCalendarPermission(
    calendarPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCalendarPermissionManagerBuilder = ::CalendarPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerCalendarPermission(calendarPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
    CalendarPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Registers a [BaseCalendarPermissionManagerBuilder] and [PermissionStateRepo] for [CalendarPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param calendarPermissionManagerBuilderBuilder method for creating a [BaseCalendarPermissionManagerBuilder] from a [PermissionContext]
 * @param calendarPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [CalendarPermission] given a [BaseCalendarPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseCalendarPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseCalendarPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerCalendarPermission(
    calendarPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCalendarPermissionManagerBuilder = ::CalendarPermissionManagerBuilder,
    calendarPermissionStateRepoBuilder: (CalendarPermission, BaseCalendarPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<CalendarPermission>,
) = calendarPermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<CalendarPermission> { permission, coroutineContext ->
        calendarPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}

/**
 * Gets the [BaseCalendarPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseCalendarPermissionManagerBuilder] and [PermissionStateRepo] for [CalendarPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param calendarPermissionManagerBuilderBuilder method for creating a [BaseCalendarPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseCalendarPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerCalendarPermissionIfNotRegistered(
    calendarPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCalendarPermissionManagerBuilder = ::CalendarPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerCalendarPermissionIfNotRegistered(calendarPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
    CalendarPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Gets the [BaseCalendarPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseCalendarPermissionManagerBuilder] and [PermissionStateRepo] for [CalendarPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param calendarPermissionManagerBuilderBuilder method for creating a [BaseCalendarPermissionManagerBuilder] from a [PermissionContext]
 * @param calendarPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [CalendarPermission] given a [BaseCalendarPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseCalendarPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerCalendarPermissionIfNotRegistered(
    calendarPermissionManagerBuilderBuilder: (PermissionContext) -> BaseCalendarPermissionManagerBuilder = ::CalendarPermissionManagerBuilder,
    calendarPermissionStateRepoBuilder: (CalendarPermission, BaseCalendarPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<CalendarPermission>,
) = calendarPermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<CalendarPermission> { permission, coroutineContext ->
        calendarPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}
