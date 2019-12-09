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

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.log.warn
import com.splendo.kaluga.log.error

actual class Permissions constructor(private val context: Context) {

    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager(context)
    }

    actual open class Builder(private val context: Context = ApplicationHolder.applicationContext) {
        actual open fun build(): Permissions {
            return Permissions(this.context)
        }
    }

    companion object {
        fun requestPermissions(context: Context = ApplicationHolder.applicationContext, vararg permissions: String) {
            val intent = PermissionsActivity.intent(context, *permissions)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun checkPermissionsDeclaration(context: Context = ApplicationHolder.applicationContext, vararg requestedPermissionNames: String = emptyArray()): List<String> {
            val pm = context.packageManager

            val missingPermissions = requestedPermissionNames.toList().toMutableList()

            try {
                val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)

                var declaredPermissions: List<String> = mutableListOf()

                if (packageInfo != null && declaredPermissions.isEmpty()) {
                    declaredPermissions = packageInfo.requestedPermissions.toList()
                }

                if (declaredPermissions.isNotEmpty()) {
                    missingPermissions.toList().forEach { requestedPermissionName ->
                        if (declaredPermissions.contains(requestedPermissionName)) {
                            debug(TAG, "Permission $requestedPermissionName was declared in manifest")
                            missingPermissions.remove(requestedPermissionName)
                        } else {
                            warn(TAG, "Permission $requestedPermissionName was not declared in manifest")
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                error(TAG, e)
            }

            return missingPermissions
        }

        const val TAG = "Permissions"

    }

}