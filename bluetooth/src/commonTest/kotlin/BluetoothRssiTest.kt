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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothRssiTest: BluetoothFlowTest<Int>() {

    override val flow: suspend () -> Flow<Int> = {
        setup(Setup.DEVICE)
        bluetooth.devices()[device.identifier].rssi()
    }

    @Test
    fun testRssi() = testWithFlow {
        val newRssi = -42
        launch {
            scanDevice(device, deviceWrapper)
        }
        bluetooth.startScanning()
        test {
            assertEquals(initialRssi, it)
        }
        action {
            connectDevice(device, connectionManager, this@BluetoothRssiTest)
            bluetooth.devices()[device.identifier].updateRssi()
            connectionManager.readRssiCompleted.await()
            device.filter { it is DeviceState.Connected }.first()
            connectionManager.handleNewRssi(newRssi)
        }
        test {
            assertEquals(newRssi, it)
        }

        resetFlow()

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner().stopMonitoringPermissions.await()
//        mockBaseScanner().stopMonitoringBluetoothCompleted.await()
    }
}
