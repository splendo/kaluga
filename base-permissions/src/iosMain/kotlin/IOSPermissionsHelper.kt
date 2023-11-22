/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions.base

import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper.AuthorizationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import platform.Foundation.NSBundle

/**
 * Convenience class for checking permissions.
 */
class IOSPermissionsHelper {

    /**
     * Type of AuthorizationStatus that can be given by a permission.
     */
    enum class AuthorizationStatus {
        /**
         * The status of Authorization has not yet been determined by the user and can thus be requested.
         */
        NotDetermined,

        /**
         * The Authorization status cannot be granted because it is restricted
         */
        Restricted,

        /**
         * The Authorization status cannot be granted because the user has denied it
         */
        Denied,

        /**
         * The Authorization status has been granted
         */
        Authorized,
    }

    companion object {

        private const val TAG = "Permissions"

        /**
         * Checks whether a set of declarations have been provided in the PList of a [NSBundle]
         * @param bundle The [NSBundle] to check
         * @param requiredDeclarationName List of declarations that should be present in the PList
         * @return The list of declarations not present in the PList. If empty all declarations where provided.
         */
        fun missingDeclarationsInPList(bundle: NSBundle, vararg requiredDeclarationName: String) = requiredDeclarationName.mapNotNull { declaration ->
            try {
                if (bundle.objectForInfoDictionaryKey(declaration) == null) {
                    error(TAG, "$declaration was not declared")
                    declaration
                } else {
                    debug(TAG, "$declaration was declared")
                    null
                }
            } catch (error: Exception) {
                error(TAG, error)
                null
            }
        }
    }
}

/**
 * Handles changes to [AuthorizationStatus]
 */
interface AuthorizationStatusHandler {
    fun status(status: AuthorizationStatus)
}

/**
 * Default implementation of [AuthorizationStatusHandler].
 * Maps [AuthorizationStatus] to a [PermissionManager.Event] and sends this to a channel
 * @param eventChannel the [SendChannel] that should emit the [PermissionManager.Event]
 * @param logTag the tag wto use for logging
 * @param logger the [Logger] to use for logging
 */
class DefaultAuthorizationStatusHandler(
    private val eventChannel: SendChannel<PermissionManager.Event>,
    private val logTag: String,
    private val logger: Logger,
) : AuthorizationStatusHandler {

    override fun status(status: AuthorizationStatus) {
        when (status) {
            AuthorizationStatus.NotDetermined -> {
                logger.info(logTag) { "Permission Revoked" }
                tryAndEmitEvent(PermissionManager.Event.PermissionDenied(false))
            }
            AuthorizationStatus.Authorized -> {
                logger.info(logTag) { "Permission Granted" }
                tryAndEmitEvent(PermissionManager.Event.PermissionGranted)
            }
            AuthorizationStatus.Denied, AuthorizationStatus.Restricted -> {
                logger.info(logTag) { "Permission Locked" }
                tryAndEmitEvent(PermissionManager.Event.PermissionDenied(true))
            }
        }
    }

    private fun tryAndEmitEvent(event: PermissionManager.Event) {
        eventChannel.trySend(event)
    }
}

/**
 * Pauses a [PermissionRefreshScheduler] while requesting an [AuthorizationStatus] and calls [AuthorizationStatusHandler.status] once the request has been completed.
 * @param timerHelper the [PermissionRefreshScheduler] to pause.
 * @param coroutineScope the [CoroutineScope] on which to request the [AuthorizationStatus]
 * @param request the method for requesting the [AuthorizationStatus]
 */
fun AuthorizationStatusHandler.requestAuthorizationStatus(
    timerHelper: PermissionRefreshScheduler? = null,
    coroutineScope: CoroutineScope,
    request: suspend () -> AuthorizationStatus,
) {
    coroutineScope.launch {
        timerHelper?.waitingLock?.withLock {
            request()
        }?.let { status(it) }
    }
}
