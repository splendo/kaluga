package com.splendo.kaluga.permissions

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

import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import platform.Foundation.NSBundle

class IOSPermissionsHelper {

    enum class AuthorizationStatus {
        NotDetermined,
        Restricted,
        Denied,
        Authorized
    }

    companion object {

        private const val TAG = "Permissions"

        fun missingDeclarationsInPList(bundle: NSBundle, vararg requiredDeclarationName: String): List<String> {

            val missingDeclarations = mutableListOf<String>()

            requiredDeclarationName.forEach { declaration ->
                try {

                    val objectForInfoDictionaryKey = bundle.objectForInfoDictionaryKey(declaration)

                    if (objectForInfoDictionaryKey == null) {
                        error(TAG, "$declaration was not declared")
                        missingDeclarations.add(declaration)
                    } else {
                        debug(TAG, "$declaration was declared")
                    }

                } catch (error: Exception) {
                    error(TAG, error)
                }
            }

            return missingDeclarations
        }

        fun <P: Permission> getPermissionState(authorizationStatus: AuthorizationStatus, permissionManager: PermissionManager<P>) : PermissionState<P> {
            return when (authorizationStatus) {
                AuthorizationStatus.NotDetermined -> PermissionState.Denied.Requestable(permissionManager)
                AuthorizationStatus.Authorized -> PermissionState.Allowed(permissionManager)
                AuthorizationStatus.Denied, AuthorizationStatus.Restricted -> PermissionState.Denied.Locked(permissionManager)
            }
        }

        fun <P: Permission> handleAuthorizationStatus(authorizationStatus: AuthorizationStatus, permissionManager: PermissionManager<P>) {
            return when (authorizationStatus) {
                AuthorizationStatus.NotDetermined -> permissionManager.revokePermission(false)
                AuthorizationStatus.Authorized -> permissionManager.grantPermission()
                AuthorizationStatus.Denied, AuthorizationStatus.Restricted -> permissionManager.revokePermission(true)
            }
        }

    }
}