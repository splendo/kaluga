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

import com.splendo.kaluga.permissions.BasePermissionsBuilder
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.NotificationsPermission
import kotlin.coroutines.CoroutineContext

/**
 * Options for configuring a [Permission.Notifications]
 */
expect class NotificationOptions

/**
 * A [PermissionManager] for managing [Permission.Notifications]
 */
expect class NotificationsPermissionManager : PermissionManager<NotificationsPermission> {
    /**
     * The [Permission.Notifications] managed by this manager.
     */
    val notifications: NotificationsPermission
}

interface BaseNotificationsPermissionManagerBuilder : BasePermissionsBuilder {
    /**
     * Creates a [NotificationsPermissionManager]
     * @param repo The [NotificationsPermissionStateRepo] associated with the [Permission.Notifications]
     */
    fun create(notifications: NotificationsPermission, repo: NotificationsPermissionStateRepo): PermissionManager<NotificationsPermission>
}

/**
 * A builder for creating a [NotificationsPermissionManager]
 */
expect class NotificationsPermissionManagerBuilder : BaseNotificationsPermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [Permission.Notifications]
 * @param builder The [NotificationsPermissionManagerBuilder] for creating the [NotificationsPermissionManager] associated with the permission
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class NotificationsPermissionStateRepo(notifications: NotificationsPermission, builder: BaseNotificationsPermissionManagerBuilder, coroutineContext: CoroutineContext) : PermissionStateRepo<NotificationsPermission>(coroutineContext = coroutineContext) {

    override val permissionManager: PermissionManager<NotificationsPermission> = builder.create(notifications, this)
}
