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

package com.splendo.kaluga.permissions.bluetooth

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

/**
 * A [PermissionManager] for managing [Permission.Bluetooth]
 */
expect class BluetoothPermissionManager : PermissionManager<Permission.Bluetooth>

/**
 * A builder for creating a [BluetoothPermissionManager]
 */
expect class BluetoothPermissionManagerBuilder {
    /**
     * Creates a [BluetoothPermissionManager]
     * @param repo The [BluetoothPermissionStateRepo] associated with the [Permission.Bluetooth]
     */
    fun create(repo: BluetoothPermissionStateRepo): BluetoothPermissionManager
}

/**
 * A [PermissionStateRepo] for [Permission.Bluetooth]
 * @param builder The [BluetoothPermissionManagerBuilder] for creating the [BluetoothPermissionManager] associated with the permission
 */
class BluetoothPermissionStateRepo(builder: BluetoothPermissionManagerBuilder) : PermissionStateRepo<Permission.Bluetooth>() {

    override val permissionManager: PermissionManager<Permission.Bluetooth> = builder.create(this)
}
