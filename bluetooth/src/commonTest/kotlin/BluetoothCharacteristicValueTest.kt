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

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.test.base.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.base.mock.verifyWithin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BluetoothCharacteristicValueTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithCharacteristic, BluetoothFlowTest.CharacteristicContext, ByteArray?>() {

    override val createTestContextWithConfiguration: suspend (Configuration.DeviceWithCharacteristic, CoroutineScope) -> CharacteristicContext = { configuration, scope ->
        CharacteristicContext(configuration, scope)
    }
    override val flowFromTestContext: suspend CharacteristicContext.() -> Flow<ByteArray?> = {
        bluetooth.scannedDevices()[device.identifier].services()[serviceUuid].characteristics()[characteristicUuid].value()
    }

    @Test
    fun testGetCharacteristicValue() = testWithFlowAndTestContext(
        Configuration.DeviceWithCharacteristic(),
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
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verifyWithin(value = captor)
            assertIs<DeviceAction.Notification.Enable>(captor.lastCaptured)
            yield()
            connectionManager.handleCurrentAction()
            connectionManager.notify(characteristicUuid, newValue)
        }
        test {
            assertEquals(newValue, it)
        }
        resetFlow()
        mainAction {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verifyWithin(value = captor, times = 2)
            assertIs<DeviceAction.Notification.Disable>(captor.lastCaptured)
        }
    }
}
