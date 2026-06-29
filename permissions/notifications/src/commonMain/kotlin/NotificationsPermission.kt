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
import com.splendo.kaluga.permissions.base.BasePermissionsBuilder
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.defaultPermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Options for configuring a [NotificationsPermission]
 */
expect class NotificationOptions

/**
 * A [PermissionManager] for managing [NotificationsPermission]
 */
typealias NotificationsPermissionManager = PermissionManager<NotificationsPermission>

/**
 * The [BasePermissionManager] to use as a default for [NotificationsPermission]
 */
expect class DefaultNotificationsPermissionManager : BasePermissionManager<NotificationsPermission> {
    override fun requestPermissionDidStart()
    override fun monitoringDidStart(interval: Duration)
    override fun monitoringDidStop()
}

/**
 * A [BasePermissionsBuilder] for [NotificationsPermission]
 */
interface BaseNotificationsPermissionManagerBuilder : BasePermissionsBuilder<NotificationsPermission> {

    /**
     * Creates a [NotificationsPermissionManager]
     * @param notificationsPermission the [NotificationsPermission] to be managed.
     * @param settings [BasePermissionManager.Settings] to apply to the manager
     * @param coroutineScope The [CoroutineScope] the manager runs on
     * @return a [NotificationsPermissionManager]
     */
    fun create(
        notificationsPermission: NotificationsPermission,
        settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
        coroutineScope: CoroutineScope,
    ): NotificationsPermissionManager
}

/**
 * A [BaseNotificationsPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
expect class NotificationsPermissionManagerBuilder(context: PermissionContext = defaultPermissionContext) : BaseNotificationsPermissionManagerBuilder {
    override fun create(notificationsPermission: NotificationsPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): NotificationsPermissionManager
}

/**
 * A [PermissionStateRepo] for [NotificationsPermission]
 * @param notificationsPermission the [NotificationsPermission] to be managed
 * @param builder The [NotificationsPermissionManagerBuilder] for creating the [NotificationsPermissionManager] associated with the permission
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] used by the [NotificationsPermissionManager] created by the builder
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class NotificationsPermissionStateRepo(
    notificationsPermission: NotificationsPermission,
    builder: BaseNotificationsPermissionManagerBuilder,
    monitoringInterval: Duration = defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
    coroutineContext: CoroutineContext,
) : PermissionStateRepo<NotificationsPermission>(monitoringInterval, { builder.create(notificationsPermission, settings, it) }, coroutineContext)
