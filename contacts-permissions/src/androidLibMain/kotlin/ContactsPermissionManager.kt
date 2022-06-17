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
import com.splendo.kaluga.permissions.base.AndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

actual class DefaultContactsPermissionManager(
    context: Context,
    contactsPermission: ContactsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<ContactsPermission>(contactsPermission, settings, coroutineScope) {

    private val permissionHandler = AndroidPermissionStateHandler(sharedEvents, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(
        context,
        if (contactsPermission.allowWrite)
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
        else
            arrayOf(Manifest.permission.READ_CONTACTS),
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

actual class ContactsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseContactsPermissionManagerBuilder {

    override fun create(contactsPermission: ContactsPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): ContactsPermissionManager {
        return DefaultContactsPermissionManager(context.context, contactsPermission, settings, coroutineScope)
    }
}
