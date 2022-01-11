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

package com.splendo.kaluga.bluetooth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BluetoothMultipleCharacteristicValuesTest : BluetoothFlowTest<ByteArray?>() {

    override val flow = suspend {
        setup(Setup.CHARACTERISTIC)
        bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].value()
    }

    @Test
    fun testGetMultipleCharacteristicValues() = testWithFlow {

        val newValue = "Test".encodeToByteArray()

        scanDevice()
        bluetooth.startScanning()

        test {
            assertEquals(null, it)
        }
        action {
            connectDevice(device)
            connectionManager.discoverServicesCompleted.get().await()
            discoverService(service, device)
            characteristic.writeValue(newValue)
        }
        test {
            assertTrue(newValue contentEquals it)
        }
    }

    override fun createServiceWrapper() =
        com.splendo.kaluga.test.mock.bluetooth.createServiceWrapper(
            connectionManager.stateRepo,
            uuid = randomUUID(),
            characteristics = listOf(randomUUID() to listOf(randomUUID()))
        )
}
