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
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * [Permission] to access the users Contacts
 * @param allowWrite If `true` writing to the contacts is permitted
 */
data class ContactsPermission(val allowWrite: Boolean = false) : Permission() {
    override val name: String = "Contacts - ${if (allowWrite) "ReadWrite" else "ReadOnly"}"
}

/**
 * Registers a [BaseContactsPermissionManagerBuilder] and [PermissionStateRepo] for [ContactsPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param contactsPermissionManagerBuilderBuilder method for creating a [BaseContactsPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseContactsPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseContactsPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerContactsPermission(
    contactsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseContactsPermissionManagerBuilder = ::ContactsPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerContactsPermission(contactsPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
    ContactsPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Registers a [BaseContactsPermissionManagerBuilder] and [PermissionStateRepo] for [ContactsPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param contactsPermissionManagerBuilderBuilder method for creating a [BaseContactsPermissionManagerBuilder] from a [PermissionContext]
 * @param contactsPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [ContactsPermission] given a [BaseContactsPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseContactsPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseContactsPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerContactsPermission(
    contactsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseContactsPermissionManagerBuilder = ::ContactsPermissionManagerBuilder,
    contactsPermissionStateRepoBuilder: (ContactsPermission, BaseContactsPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<ContactsPermission>,
) = contactsPermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<ContactsPermission> { permission, coroutineContext ->
        contactsPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}

/**
 * Gets the [BaseContactsPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseContactsPermissionManagerBuilder] and [PermissionStateRepo] for [ContactsPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param contactsPermissionManagerBuilderBuilder method for creating a [BaseContactsPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseContactsPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerContactsPermissionIfNotRegistered(
    contactsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseContactsPermissionManagerBuilder = ::ContactsPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerContactsPermissionIfNotRegistered(contactsPermissionManagerBuilderBuilder) { permission, builder, coroutineContext ->
    ContactsPermissionStateRepo(permission, builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Gets the [BaseContactsPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseContactsPermissionManagerBuilder] and [PermissionStateRepo] for [ContactsPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param contactsPermissionManagerBuilderBuilder method for creating a [BaseContactsPermissionManagerBuilder] from a [PermissionContext]
 * @param contactsPermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [ContactsPermission] given a [BaseContactsPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseContactsPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerContactsPermissionIfNotRegistered(
    contactsPermissionManagerBuilderBuilder: (PermissionContext) -> BaseContactsPermissionManagerBuilder = ::ContactsPermissionManagerBuilder,
    contactsPermissionStateRepoBuilder: (ContactsPermission, BaseContactsPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<ContactsPermission>,
) = contactsPermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<ContactsPermission> { permission, coroutineContext ->
        contactsPermissionStateRepoBuilder(permission, it, coroutineContext)
    }
}
