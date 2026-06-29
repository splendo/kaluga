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

package com.splendo.kaluga.permissions.contacts

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
 * A [PermissionManager] for managing [ContactsPermission]
 */
typealias ContactsPermissionManager = PermissionManager<ContactsPermission>

/**
 * The [BasePermissionManager] to use as a default for [ContactsPermission]
 */
expect class DefaultContactsPermissionManager : BasePermissionManager<ContactsPermission> {
    override fun requestPermissionDidStart()
    override fun monitoringDidStart(interval: Duration)
    override fun monitoringDidStop()
}

/**
 * A [BasePermissionsBuilder] for [ContactsPermission]
 */
interface BaseContactsPermissionManagerBuilder : BasePermissionsBuilder<ContactsPermission> {

    /**
     * Creates a [ContactsPermissionManager]
     * @param contactsPermission the [ContactsPermission] to manage.
     * @param settings [BasePermissionManager.Settings] to apply to the manager
     * @param coroutineScope The [CoroutineScope] the manager runs on
     * @return a [ContactsPermissionManager]
     */
    fun create(
        contactsPermission: ContactsPermission,
        settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
        coroutineScope: CoroutineScope,
    ): ContactsPermissionManager
}

/**
 * A [BaseContactsPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
expect class ContactsPermissionManagerBuilder(context: PermissionContext = defaultPermissionContext) : BaseContactsPermissionManagerBuilder {
    override fun create(contactsPermission: ContactsPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): ContactsPermissionManager
}

/**
 * A [PermissionStateRepo] for [ContactsPermission]
 * @param contactsPermission the [ContactsPermission] to manage.
 * @param builder The [ContactsPermissionManagerBuilder] for creating the [ContactsPermissionManager] associated with the permission
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] used by the [ContactsPermissionManager] created by the builder
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class ContactsPermissionStateRepo(
    contactsPermission: ContactsPermission,
    builder: BaseContactsPermissionManagerBuilder,
    monitoringInterval: Duration = defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
    coroutineContext: CoroutineContext,
) : PermissionStateRepo<ContactsPermission>(monitoringInterval, { builder.create(contactsPermission, settings, it) }, coroutineContext)
