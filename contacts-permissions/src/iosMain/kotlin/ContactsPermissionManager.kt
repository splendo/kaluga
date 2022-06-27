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
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import platform.Contacts.CNAuthorizationStatus
import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNAuthorizationStatusDenied
import platform.Contacts.CNAuthorizationStatusNotDetermined
import platform.Contacts.CNAuthorizationStatusRestricted
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType
import platform.Foundation.NSBundle

const val NSContactsUsageDescription = "NSContactsUsageDescription"

actual class ContactsPermissionManager(
    private val bundle: NSBundle,
    actual val contactsPermission: ContactsPermission,
    stateRepo: PermissionStateRepo<ContactsPermission>
) : PermissionManager<ContactsPermission>(stateRepo) {

    private val contactStore = CNContactStore()
    private val authorizationStatus = suspend {
        CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts).toAuthorizationStatus()
    }
    private var timerHelper = PermissionRefreshScheduler(this, authorizationStatus)

    override suspend fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NSContactsUsageDescription).isEmpty()) {
            timerHelper.isWaiting.value = true
            contactStore.requestAccessForEntityType(
                CNEntityType.CNEntityTypeContacts,
                mainContinuation { success, error ->
                    error?.let {
                        debug(it.localizedDescription)
                        revokePermission(true)
                    } ?: run {
                        timerHelper.isWaiting.value = false
                        if (success) grantPermission() else revokePermission(true)
                    }
                }
            )
        } else {
            revokePermission(true)
        }
    }

    override suspend fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }
}

actual class ContactsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseContactsPermissionManagerBuilder {

    override fun create(contactsPermission: ContactsPermission, repo: PermissionStateRepo<ContactsPermission>): PermissionManager<ContactsPermission> {
        return ContactsPermissionManager(context, contactsPermission, repo)
    }
}

private fun CNAuthorizationStatus.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        CNAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        CNAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        CNAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        CNAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            error(
                "ContactsPermissionManager",
                "Unknown ContactManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
