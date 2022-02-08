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

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.test.SimpleFlowTest
import com.splendo.kaluga.test.mock.bluetooth.MockDeviceControl

import kotlin.test.Test
import kotlin.test.assertNotNull

class MockDeviceControlTest : SimpleFlowTest<Device>() {
    private val control = MockDeviceControl.build {
        deviceInfo {
            name = "Name"
        }
    }
    override val flow = suspend { control.mock }

    @Test
    fun testDiscover() = testWithFlow {
        action {
            control.discover()
        }
        test {
            assertNotNull(it, "It should receive discovered device")
        }
    }

}