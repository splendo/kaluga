/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions

import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import platform.Foundation.NSBundle

/**
 * Convenience class for checking permissions.
 */
class IOSPermissionsHelper {

    /**
     * Type of AuthorizationStatus that can be given by a permission.
     */
    enum class AuthorizationStatus {
        NotDetermined,
        Restricted,
        Denied,
        Authorized
    }

    companion object {

        private const val TAG = "Permissions"

        /**
         * Checks whether a set of declarations have been provided in the PList of a [NSBundle]
         * @param bundle The [NSBundle] to check
         * @param requiredDeclarationName List of declarations that should be present in the PList
         * @return The list of declarations not present in the PList. If empty all declarations where provided.
         */
        fun missingDeclarationsInPList(bundle: NSBundle, vararg requiredDeclarationName: String): List<String> {

            val missingDeclarations = requiredDeclarationName.toMutableList()
            missingDeclarations.forEach { declaration ->
                try {

                    val objectForInfoDictionaryKey = bundle.objectForInfoDictionaryKey(declaration)

                    if (objectForInfoDictionaryKey == null) {
                        error(TAG, "$declaration was not declared")
                    } else {
                        debug(TAG, "$declaration was declared")
                        missingDeclarations.remove(declaration)
                    }
                } catch (error: Exception) {
                    error(TAG, error)
                }
            }

            return missingDeclarations
        }

        /**
         * Maps an [AuthorizationStatus] to a [PermissionState]
         * @param authorizationStatus The [AuthorizationStatus] to map
         * @param permissionManager The [PermissionManager] associated with the [PermissionState]
         */
        fun <P : Permission> getPermissionState(authorizationStatus: AuthorizationStatus): PermissionState<P> {
            return when (authorizationStatus) {
                AuthorizationStatus.NotDetermined -> PermissionState.Denied.Requestable()
                AuthorizationStatus.Authorized -> PermissionState.Allowed()
                AuthorizationStatus.Denied, AuthorizationStatus.Restricted -> PermissionState.Denied.Locked()
            }
        }

        /**
         * Updates a [PermissionManager] with the [PermissionState] associated with a given [AuthorizationStatus]
         * @param authorizationStatus The [AuthorizationStatus] to update to
         * @param permissionManager The [PermissionManager] to update to the proper state.
         */
        fun <P : Permission> handleAuthorizationStatus(authorizationStatus: AuthorizationStatus, permissionManager: PermissionManager<P>) {
            return when (authorizationStatus) {
                AuthorizationStatus.NotDetermined -> permissionManager.revokePermission(false)
                AuthorizationStatus.Authorized -> permissionManager.grantPermission()
                AuthorizationStatus.Denied, AuthorizationStatus.Restricted -> permissionManager.revokePermission(true)
            }
        }
    }
}
