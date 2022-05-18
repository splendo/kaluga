/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

actual class BluetoothPermissionManager(repo: PermissionStateRepo<BluetoothPermission>) : PermissionManager<BluetoothPermission>(repo) {

    override suspend fun requestPermission() {
        TODO("not implemented")
    }

    override suspend fun startMonitoring(interval: Long) {
        TODO("not implemented")
    }

    override suspend fun stopMonitoring() {
        TODO("not implemented")
    }
}

actual class BluetoothPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseBluetoothPermissionManagerBuilder {

    override fun create(repo: PermissionStateRepo<BluetoothPermission>): PermissionManager<BluetoothPermission> {
        return BluetoothPermissionManager(repo)
    }
}
