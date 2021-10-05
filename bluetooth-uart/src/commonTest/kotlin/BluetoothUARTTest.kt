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

package com.splendo.kaluga.bluetooth.uart

import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.test.mock.bluetooth.createDevice
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BluetoothUARTTest : BluetoothUARTFlowTest() {

    private val builder = object : BaseDeviceConnectionManager.Builder {
        override fun create(
            connectionSettings: ConnectionSettings,
            deviceWrapper: DeviceWrapper,
            stateRepo: DeviceStateFlowRepo
        ) = MockDeviceConnectionManager(
            connectionSettings,
            deviceWrapper,
            stateRepo
        )
    }

    @Test
    fun testHasUARTFalse() = testWithFlow {

        test {
            assertTrue(it.isEmpty())
        }

        action {
            discoverDevices(
                createDevice(
                    name = "Non-UART Device",
                    connectionManagerBuilder = builder,
                    coroutineContext = scope.coroutineContext
                )
            )
        }

        test {
            assertFalse(it.first().hasUART().first())
        }
    }

    @Test
    fun testHasUARTTrue() = testWithFlow {

        test {
            assertTrue(it.isEmpty())
        }

        action {
            discoverDevices(
                createDevice(
                    name = "UART Device",
                    serviceUUIDs = listOf(uuidFrom(BluetoothUART.UART_SERVICE_UUID)),
                    connectionManagerBuilder = builder,
                    coroutineContext = scope.coroutineContext
                )
            )
        }

        test {
            assertTrue(it.first().hasUART().first())
        }
    }
}
