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

import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.permissions.*
import platform.Contacts.*
import platform.Foundation.NSBundle

actual class ContactsPermissionManager(
    private val bundle: NSBundle,
    actual val contacts: Permission.Contacts,
    stateRepo: ContactsPermissionStateRepo
) : PermissionManager<Permission.Contacts>(stateRepo) {

    private val contactStore = CNContactStore()
    private val authorizationStatus = suspend {
        CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts).toAuthorizationStatus()
    }
    private var timerHelper = PermissionTimerHelper(this, authorizationStatus)

    override suspend fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, "NSContactsUsageDescription").isEmpty()) {
            timerHelper.isWaiting = true
            contactStore.requestAccessForEntityType(CNEntityType.CNEntityTypeContacts, mainContinuation { success, error ->
                error?.let {
                    debug(it.localizedDescription)
                    revokePermission(true)
                } ?: run {
                    timerHelper.isWaiting = false
                    if (success) grantPermission() else revokePermission(true)
                }
            })
        } else {
            revokePermission(true)
        }
    }

    override suspend fun initializeState(): PermissionState<Permission.Contacts> {
        return IOSPermissionsHelper.getPermissionState(authorizationStatus(), this)
    }

    override suspend fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }

}

actual class ContactsPermissionManagerBuilder(private val bundle: NSBundle = NSBundle.mainBundle) : BaseContactsPermissionManagerBuilder {

    override fun create(contacts: Permission.Contacts, repo: ContactsPermissionStateRepo): ContactsPermissionManager {
        return ContactsPermissionManager(bundle, contacts, repo)
    }

}

private fun CNAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when(this) {
        CNAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        CNAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        CNAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        CNAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            com.splendo.kaluga.log.error(
                "ContactsPermissionManager",
                "Unknown ContactManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}