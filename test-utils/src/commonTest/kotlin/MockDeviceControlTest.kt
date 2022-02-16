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

import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.test.SimpleFlowTest
import com.splendo.kaluga.test.mock.bluetooth.MockDeviceControl
import com.splendo.kaluga.test.mock.bluetooth.device.randomIdentifier
import kotlinx.coroutines.delay

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
            println("1: $it")
            assertTrue(it is DeviceState.Connecting, "It should start connecting")
        }
    }
}