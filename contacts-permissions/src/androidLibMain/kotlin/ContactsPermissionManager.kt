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
import com.splendo.kaluga.permissions.AndroidPermissionsManager
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

actual class ContactsPermissionManager(
    context: Context,
    actual val contactsPermission: ContactsPermission,
    stateRepo: PermissionStateRepo<ContactsPermission>
) : PermissionManager<ContactsPermission>(stateRepo) {

    private val permissionsManager = AndroidPermissionsManager(context, this, if (contactsPermission.allowWrite) arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS) else arrayOf(Manifest.permission.READ_CONTACTS))

    override suspend fun requestPermission() {
        permissionsManager.requestPermissions()
    }

    override suspend fun startMonitoring(interval: Long) {
        permissionsManager.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        permissionsManager.stopMonitoring()
    }
}

actual class ContactsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseContactsPermissionManagerBuilder {

    override fun create(contactsPermission: ContactsPermission, repo: PermissionStateRepo<ContactsPermission>): PermissionManager<ContactsPermission> {
        return ContactsPermissionManager(context.context, contactsPermission, repo)
    }
}
