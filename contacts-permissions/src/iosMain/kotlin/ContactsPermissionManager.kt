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

import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.handleAuthorizationStatus
import kotlinx.coroutines.CoroutineScope
import platform.Contacts.CNAuthorizationStatus
import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNAuthorizationStatusDenied
import platform.Contacts.CNAuthorizationStatusNotDetermined
import platform.Contacts.CNAuthorizationStatusRestricted
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType
import platform.Foundation.NSBundle
import kotlin.time.Duration

const val NSContactsUsageDescription = "NSContactsUsageDescription"

actual class DefaultContactsPermissionManager(
    private val bundle: NSBundle,
    contactsPermission: ContactsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<ContactsPermission>(contactsPermission, settings, coroutineScope) {

    private val contactStore = CNContactStore()
    private val authorizationStatus = suspend {
        CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts).toAuthorizationStatus()
    }
    private var timerHelper = PermissionRefreshScheduler(authorizationStatus, ::handleAuthorizationStatus, coroutineScope)

    override fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NSContactsUsageDescription).isEmpty()) {
            timerHelper.isWaiting.value = true
            contactStore.requestAccessForEntityType(
                CNEntityType.CNEntityTypeContacts
            ) { success, error ->
                error?.let {
                    debug(it.localizedDescription)
                    revokePermission(true)
                } ?: run {
                    timerHelper.isWaiting.value = false
                    if (success) grantPermission() else revokePermission(true)
                }
            }
        } else {
            revokePermission(true)
        }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        timerHelper.startMonitoring(interval)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        timerHelper.stopMonitoring()
    }
}

actual class ContactsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseContactsPermissionManagerBuilder {

    override fun create(contactsPermission: ContactsPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): ContactsPermissionManager {
        return DefaultContactsPermissionManager(context, contactsPermission, settings, coroutineScope)
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
