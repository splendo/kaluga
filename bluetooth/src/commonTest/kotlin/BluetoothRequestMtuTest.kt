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

import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BluetoothRequestMtuTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithService, BluetoothFlowTest.ServiceContext, Int?>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithService, scope: CoroutineScope) -> ServiceContext =
        { configuration, scope -> ServiceContext(configuration, scope) }
    override val flowFromTestContext: suspend ServiceContext.() -> Flow<Int?> = {
        flowOf(device).mtu()
    }

    @Test
    fun testRequestMtu() = testWithFlowAndTestContext(
        Configuration.DeviceWithService(),
    ) {
        val newMtu = 512

        mainAction {
            bluetooth.startScanning()
            scanDevice()
            connectDevice()
            device.state.filterIsInstance<ConnectableDeviceState.Connected.NoServices>().first().startDiscovering()
            discoverService()
            device.state.filterIsInstance<ConnectableDeviceState.Connected.Idle>().first()
        }
        test {
            assertNull(it)
        }
        mainAction {
            flowOf(device).requestMtu(newMtu)
            device.state.filterIsInstance<ConnectableDeviceState.Connected.HandlingAction>().first()
            connectionManager.handleCurrentAction()
        }

        test {
            assertEquals(newMtu, it)
        }
    }
}
