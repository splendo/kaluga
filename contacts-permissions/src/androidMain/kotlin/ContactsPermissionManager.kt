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

import android.Manifest
import android.content.Context
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.DefaultAndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

/**
 * The [BasePermissionManager] to use as a default for [ContactsPermission]
 * @param context the [Context] the [ContactsPermission] is to be granted in
 * @param contactsPermission the [ContactsPermission] to manage.
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultContactsPermissionManager(context: Context, contactsPermission: ContactsPermission, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<ContactsPermission>(contactsPermission, settings, coroutineScope) {

    private val permissionHandler = DefaultAndroidPermissionStateHandler(eventChannel, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(
        context,
        listOfNotNull(
            Manifest.permission.READ_CONTACTS,
            if (contactsPermission.allowWrite) Manifest.permission.WRITE_CONTACTS else null,
        ).toTypedArray(),
        coroutineScope,
        logTag,
        logger,
        permissionHandler,
    )

    actual override fun requestPermissionDidStart() {
        permissionsManager.requestPermissions()
    }

    actual override fun monitoringDidStart(interval: Duration) {
        permissionsManager.startMonitoring(interval)
    }

    actual override fun monitoringDidStop() {
        permissionsManager.stopMonitoring()
    }
}

/**
 * A [BaseContactsPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class ContactsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseContactsPermissionManagerBuilder {

    actual override fun create(contactsPermission: ContactsPermission, settings: Settings, coroutineScope: CoroutineScope): ContactsPermissionManager =
        DefaultContactsPermissionManager(context.context, contactsPermission, settings, coroutineScope)
}
