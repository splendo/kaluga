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

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo

actual class Scanner(permissions: Permissions,
                     connectionSettings: ConnectionSettings,
                     autoRequestPermission: Boolean,
                     autoEnableBluetooth: Boolean,
                     stateRepo: StateRepo<ScanningState>) : BaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, stateRepo) {

    class Builder() : BaseScanner.Builder {

        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableBluetooth: Boolean,
            scanningStateRepo: StateRepo<ScanningState>
        ): BaseScanner {
            return Scanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo)
        }
    }

    override suspend fun scanForDevices(filter: Set<UUID>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun stopScanning() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startMonitoringBluetooth() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopMonitoringBluetooth() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isBluetoothEnabled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun requestBluetoothEnable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}