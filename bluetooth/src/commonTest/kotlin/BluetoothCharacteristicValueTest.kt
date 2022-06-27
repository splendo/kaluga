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

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.test.base.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.yieldMultiple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BluetoothCharacteristicValueTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithCharacteristic, BluetoothFlowTest.CharacteristicContext, ByteArray?>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithCharacteristic, scope: CoroutineScope) -> CharacteristicContext = { configuration, scope -> CharacteristicContext(configuration, scope) }
    override val flowFromTestContext: suspend CharacteristicContext.() -> Flow<ByteArray?> = {
        bluetooth.devices()[device.identifier].services()[serviceUuid].characteristics()[characteristicUuid].value()
    }

    @Test
    fun testGetCharacteristicValue() = testWithFlowAndTestContext(
        Configuration.DeviceWithCharacteristic()
    ) {

        val newValue = "Test".encodeToByteArray()

        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }
        test {
            assertEquals(null, it)
        }
        mainAction {
            connectDevice()
            discoverService()
            yieldMultiple(5)
            characteristic.writeValue(newValue)
            yieldMultiple(2)
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Write.Characteristic>(captor.lastCaptured)
            connectionManager.handleCurrentAction()
        }
        test {
            assertTrue(newValue contentEquals it)
        }
    }
}
