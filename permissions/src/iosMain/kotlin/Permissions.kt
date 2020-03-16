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
import platform.CoreBluetooth.CBCentralManager
import platform.Foundation.NSBundle
import platform.posix.remove

actual class Permissions(private val bundle: NSBundle) {

    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager(CBCentralManager())
    }

    actual open class Builder(private val bundle: NSBundle) {

        actual open fun build(): Permissions {
            return Permissions(bundle)
        }
    }

    companion object {
        const val TAG = "Permissions"

        fun checkDeclarationInPList(bundle: NSBundle, vararg permissionName: String): MutableList<String> {

            val missingPermissions = permissionName.toMutableList()
            missingPermissions.forEach { permission ->
                try {

                    val objectForInfoDictionaryKey = NSBundle.mainBundle.objectForInfoDictionaryKey(permission)

                    if (objectForInfoDictionaryKey == null) {
                        error(TAG, "$permission was not declared")
                    } else {
                        debug(TAG, "$permission was declared")
                        missingPermissions.remove(permission)
                    }

                } catch (error: Exception) {
                    error(TAG, error)
                }
            }

            return missingPermissions
        }
    }

}