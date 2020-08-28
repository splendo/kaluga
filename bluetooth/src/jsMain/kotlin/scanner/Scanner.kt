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

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.base.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope

actual class Scanner(
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermission: Boolean,
    autoEnableBluetooth: Boolean,
    stateRepo: StateRepo<ScanningState>,
    coroutineScope: CoroutineScope
) : BaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, stateRepo, coroutineScope) {

    class Builder : BaseScanner.Builder {

        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableBluetooth: Boolean,
            scanningStateRepo: StateRepo<ScanningState>,
            coroutineScope: CoroutineScope
        ): BaseScanner {
            return Scanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo, coroutineScope)
        }
    }

    override suspend fun scanForDevices(filter: Set<UUID>) {}

    override suspend fun stopScanning() {}

    override fun startMonitoringBluetooth() {}

    override fun stopMonitoringBluetooth() {}

    override suspend fun isBluetoothEnabled(): Boolean = false

    override suspend fun requestBluetoothEnable() {}
}
