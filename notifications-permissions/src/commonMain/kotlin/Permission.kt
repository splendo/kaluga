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
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Permission to access the users Notifications.
 * @param options The [NotificationOptions] determining the type of notifications that can be accessed
 */
data class NotificationsPermission(val options: NotificationOptions? = null) : Permission() {
    override val name: String = "Notifications - ${options?.toString().orEmpty().ifEmpty { "Without Options" }}"
}

fun PermissionsBuilder.registerNotificationsPermission(
    notificationsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseNotificationsPermissionManagerBuilder = ::NotificationsPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings()
) =
    registerNotificationsPermission(notificationsPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
        NotificationsPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
    }

fun PermissionsBuilder.registerNotificationsPermission(
    notificationsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseNotificationsPermissionManagerBuilder = ::NotificationsPermissionManagerBuilder,
    notificationsPermissionStateRepoBuilder: (NotificationsPermission, BaseNotificationsPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<NotificationsPermission>
) = notificationsPermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<NotificationsPermission> { permission, coroutineContext ->
        notificationsPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}

fun PermissionsBuilder.registerNotificationsPermissionIfNotRegistered(
    notificationsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseNotificationsPermissionManagerBuilder = ::NotificationsPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings()
) =
    registerNotificationsPermissionIfNotRegistered(notificationsPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
        NotificationsPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
    }

fun PermissionsBuilder.registerNotificationsPermissionIfNotRegistered(
    notificationsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseNotificationsPermissionManagerBuilder = ::NotificationsPermissionManagerBuilder,
    notificationsPermissionStateRepoBuilder: (NotificationsPermission, BaseNotificationsPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<NotificationsPermission>
) = notificationsPermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<NotificationsPermission> { permission, coroutineContext ->
        notificationsPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}
