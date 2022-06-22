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

import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.testBlockingAndCancelScope
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class PairingUtilsTests : BaseTest() {

    private object Mocks {

        const val NAME = "MockDevice"

        private val manager = object : BaseDeviceConnectionManager.Builder {
            override fun create(
                deviceWrapper: DeviceWrapper,
                bufferCapacity: Int,
                coroutineScope: CoroutineScope
            ) = MockDeviceConnectionManager(
                true,
                deviceWrapper,
                bufferCapacity,
                coroutineScope
            )
        }

        fun device(coroutineScope: CoroutineScope) = Device(
            ConnectionSettings(
                ConnectionSettings.ReconnectionSettings.Never
            ),
            DeviceInfoImpl(
                createDeviceWrapper(NAME),
                rssi = -78,
                MockAdvertisementData(NAME)
            ),
            manager,
            coroutineScope
        )
    }

    private val Device.mockManager
        get() = peekState().connectionManager as MockDeviceConnectionManager

    @Test
    fun unpairTest(): Unit = testBlockingAndCancelScope {
        val device = Mocks.device(this)
        val flow = flowOf(device)
        flow.unpair()
        device.mockManager.unpairMock.verify()
    }

    @Test
    fun pairTest(): Unit = testBlockingAndCancelScope {
        val device = Mocks.device(this)
        device.mockManager.reset()
        val flow = flowOf(device)

        val connectingJob = async {
            flow.connect()
        }

        delay(100.milliseconds) // TODO wait for correct state instead
        device.mockManager.handleConnect()
        connectingJob.await()

        flow.pair()
        device.mockManager.pairMock.verify()
    }
}
