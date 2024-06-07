/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.firstInstance
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.milliseconds

class MockDeviceTest {

    @Test
    fun mock_device_connect_disconnect(): Unit = runBlocking {
        val device = buildMockDevice(coroutineContext) {
            identifier = randomIdentifier()
            services {
                add(uuidFrom("2345"))
            }
            connectionDelay = 100.milliseconds
        }
        withTimeout(500.milliseconds) {
            device.connect()
            assertNotNull(device.state.firstInstance<ConnectableDeviceState.Connected>())
        }
        withTimeout(500.milliseconds) {
            device.disconnect()
            assertNotNull(device.state.firstInstance<ConnectableDeviceState.Disconnected>())
        }
    }
}
