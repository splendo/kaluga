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
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.permissions.AndroidPermissionsManager
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState


actual class ContactsPermissionManager(
    context: Context,
    actual val contacts: Permission.Contacts,
    stateRepo: ContactsPermissionStateRepo
) : PermissionManager<Permission.Contacts>(stateRepo) {

    private val permissionsManager = AndroidPermissionsManager(context, this, if (contacts.allowWrite) arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS) else arrayOf(Manifest.permission.READ_CONTACTS))

    override suspend fun requestPermission() {
        permissionsManager.requestPermissions()
    }

    override suspend fun initializeState(): PermissionState<Permission.Contacts> {
        return when {
            permissionsManager.hasPermissions -> PermissionState.Allowed(this)
            else -> PermissionState.Denied.Requestable(this)
        }
    }

    override suspend fun startMonitoring(interval: Long) {
        permissionsManager.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        permissionsManager.stopMonitoring()
    }


}

actual class ContactsPermissionManagerBuilder(private val context: Context = ApplicationHolder.applicationContext) : BaseContactsPermissionManagerBuilder {

    override fun create(contacts: Permission.Contacts, repo: ContactsPermissionStateRepo): ContactsPermissionManager {
        return ContactsPermissionManager(context, contacts, repo)
    }

}

