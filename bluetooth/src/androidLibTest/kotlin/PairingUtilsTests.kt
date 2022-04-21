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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.test.BaseTest
import com.splendo.kaluga.test.mock.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class PairingUtilsTests : BaseTest() {

    private object Mocks {

        const val NAME = "MockDevice"

        private val manager = object : BaseDeviceConnectionManager.Builder {
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

        fun device(coroutineContext: CoroutineContext) = Device(
            ConnectionSettings(
                ConnectionSettings.ReconnectionSettings.Never
            ),
            DeviceInfoImpl(
                createDeviceWrapper(NAME),
                rssi = -78,
                MockAdvertisementData(NAME)
            ),
            manager,
            coroutineContext
        )
    }

    private val Device.mockManager
        get() = peekState().connectionManager as MockDeviceConnectionManager

    @Test
    fun unpairTest(): Unit = runBlocking {
        val device = Mocks.device(coroutineContext)
        val flow = flowOf(device)
        flow.unpair()
        assert(device.mockManager.unpairCompleted.get().isCompleted)
    }

    @Test
    fun pairTest(): Unit = runBlocking {
        val device = Mocks.device(coroutineContext)
        device.mockManager.reset()
        val flow = flowOf(device)

        val connectingJob = async {
            flow.connect()
        }
        device.mockManager.connectCompleted.get().await()
        device.mockManager.handleConnect()
        connectingJob.await()

        flow.pair()
        assert(device.mockManager.pairCompleted.get().isCompleted)
    }
}
