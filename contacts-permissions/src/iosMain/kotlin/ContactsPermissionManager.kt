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

import co.touchlab.stately.freeze
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.AuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.AuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.base.requestAuthorizationStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.Contacts.CNAuthorizationStatus
import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNAuthorizationStatusDenied
import platform.Contacts.CNAuthorizationStatusNotDetermined
import platform.Contacts.CNAuthorizationStatusRestricted
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import kotlin.time.Duration

const val NSContactsUsageDescription = "NSContactsUsageDescription"

actual class DefaultContactsPermissionManager(
    private val bundle: NSBundle,
    contactsPermission: ContactsPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<ContactsPermission>(contactsPermission, settings, coroutineScope) {

    private class Provider() : AuthorizationStatusProvider {
        override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus = CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts).toAuthorizationStatus()
    }

    private val contactStore = CNContactStore()
    private val provider = Provider()

    private val permissionHandler = AuthorizationStatusHandler(sharedEvents, logTag, logger)
    private var timerHelper = PermissionRefreshScheduler(provider, permissionHandler, coroutineScope)

    override fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, NSContactsUsageDescription).isEmpty()) {
            permissionHandler.requestAuthorizationStatus(timerHelper, coroutineScope) {
                val deferred = CompletableDeferred<Boolean>()
                val callback = { success: Boolean, error: NSError? ->
                    error?.let { deferred.completeExceptionally(Throwable(it.localizedDescription)) } ?: deferred.complete(success)
                    Unit
                }.freeze()
                contactStore.requestAccessForEntityType(
                    CNEntityType.CNEntityTypeContacts,
                    callback
                )

                try {
                    if (deferred.await())
                        IOSPermissionsHelper.AuthorizationStatus.Authorized
                    else
                        IOSPermissionsHelper.AuthorizationStatus.Restricted
                } catch (t: Throwable) {
                    IOSPermissionsHelper.AuthorizationStatus.Restricted
                }
            }
        } else {
            val permissionHandler = permissionHandler
            launch {
                permissionHandler.emit(IOSPermissionsHelper.AuthorizationStatus.Restricted)
            }
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
