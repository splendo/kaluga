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

import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.randomUUID
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.test.SimpleFlowTest
import com.splendo.kaluga.test.mock.bluetooth.MockDeviceControl
import com.splendo.kaluga.test.mock.bluetooth.MockServiceAdvertisingData
import com.splendo.kaluga.test.mock.bluetooth.device.randomIdentifier
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MockDeviceControlTest : SimpleFlowTest<DeviceState>() {
    companion object {
        val deviceId = randomIdentifier()
    }

    private val control = MockDeviceControl.build {
        deviceInfo {
            identifier = deviceId
        }
    }
    override val flow = suspend { control.mock.state() }

    @Test
    fun testDiscoverDevice() = testWithFlow {
        action {
            control.discover()
        }
        test {
            assertNotNull(it, "It should received discovered device")
            assertTrue(it is DeviceState.Disconnected, "It should receive disconnected device")
            assertEquals(deviceId, it.identifier, "It should discover device with correct identifier")
        }
    }

    @Test
    fun testConnect() = testWithFlow {
        control.discover()
        action {
            control.connect()
        }
        test {
            assertTrue(it is DeviceState.Connected, "It should connect. Actual state: $it")
        }
    }

    @Test
    fun testDisconnect() = testWithFlow {
        control.discover()
        control.connect()
        action {
            control.disconnect()
        }
        test {
            assertTrue(it is DeviceState.Disconnected, "It should disconnect. Actual state: $it")
        }
    }

    @Test
    fun testDiscoverServices() = testWithFlow {
        val firstUUID = randomUUID()
        val secondUUID = randomUUID()
        val firstService = MockServiceAdvertisingData(firstUUID)
        val secondService = MockServiceAdvertisingData(secondUUID)

        action {
            control.simulate(serviceAdvertisingData = firstService)
        }
        test {
            assertNotNull(control.mock.services()[firstUUID].first(), "It should discover service")
        }

        action {
            control.simulate(serviceAdvertisingData = secondService)
        }
        test {
            assertNotNull(control.mock.services()[firstUUID].first(), "It should discover the first added service")
            assertNotNull(control.mock.services()[secondUUID].first(), "It should discover the second added service")
        }
    }

    @Test
    fun testWriteToCharacteristic() = testWithFlow {
        val serviceUUID = randomUUID()
        val characteristicUUID = randomUUID()

        val data = byteArrayOf(0x01, 0x0, 0xf, 0x4)
        action {
            val characteristic = control.mock.services()[serviceUUID].characteristics()[characteristicUUID].first()
            characteristic?.writeValue(data)
        }
    }

    @Test
    fun testReset() = testWithFlow {
        control.simulate(serviceAdvertisingData = MockServiceAdvertisingData(randomUUID()))

        action {
            control.reset()
        }
        test {
            assertTrue(control.mock.services().first().isEmpty(), "It should clean discovered service")
        }
    }
}
