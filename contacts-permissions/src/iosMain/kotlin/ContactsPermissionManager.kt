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

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.CurrentAuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.DefaultAuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.requestAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
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

private const val NS_CONTACTS_USAGE_DESCRIPTION = "NSContactsUsageDescription"

/**
 * The [BasePermissionManager] to use as a default for [ContactsPermission]
 * @param bundle the [NSBundle] the [ContactsPermission] is to be granted in
 * @param contactsPermission the [ContactsPermission] to manage.
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultContactsPermissionManager(
    private val bundle: NSBundle,
    contactsPermission: ContactsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope,
) : BasePermissionManager<ContactsPermission>(contactsPermission, settings, coroutineScope) {

    private class Provider : CurrentAuthorizationStatusProvider {
        override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus = CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts)
            .toAuthorizationStatus()
    }

    private val contactStore = CNContactStore()
    private val provider = Provider()

    private val permissionHandler = DefaultAuthorizationStatusHandler(eventChannel, logTag, logger)
    private var timerHelper = PermissionRefreshScheduler(provider, permissionHandler, coroutineScope)

    actual override fun requestPermissionDidStart() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NS_CONTACTS_USAGE_DESCRIPTION).isEmpty()) {
            permissionHandler.requestAuthorizationStatus(timerHelper, CoroutineScope(coroutineContext)) {
                val deferred = CompletableDeferred<Boolean>()
                contactStore.requestAccessForEntityType(
                    CNEntityType.CNEntityTypeContacts,
                ) { success, error ->
                    error?.let { deferred.completeExceptionally(Throwable(it.localizedDescription)) } ?: deferred.complete(success)
                    Unit
                }

                try {
                    if (deferred.await()) IOSPermissionsHelper.AuthorizationStatus.Authorized else IOSPermissionsHelper.AuthorizationStatus.Restricted
                } catch (t: Throwable) {
                    IOSPermissionsHelper.AuthorizationStatus.Restricted
                }
            }
        } else {
            val permissionHandler = permissionHandler
            permissionHandler.status(IOSPermissionsHelper.AuthorizationStatus.Restricted)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        timerHelper.startMonitoring(interval)
    }

    actual override fun monitoringDidStop() {
        timerHelper.stopMonitoring()
    }
}

/**
 * A [BaseContactsPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class ContactsPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseContactsPermissionManagerBuilder {

    actual override fun create(contactsPermission: ContactsPermission, settings: Settings, coroutineScope: CoroutineScope): ContactsPermissionManager {
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
                "Unknown ContactManagerAuthorization status={$this}",
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
