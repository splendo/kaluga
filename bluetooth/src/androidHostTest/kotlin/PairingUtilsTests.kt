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

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceImpl
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.testBlockingAndCancelScope
import com.splendo.kaluga.test.bluetooth.MockDeviceWrapper
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class PairingUtilsTests : BaseTest() {

    private class Mocks {

        companion object {
            const val NAME = "MockDevice"
        }

        val mockConnectionManager = CompletableDeferred<MockDeviceConnectionManager>()

        private val manager = object : DeviceConnectionManager.Builder {
            override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): MockDeviceConnectionManager {
                val connectionManager = MockDeviceConnectionManager(
                    true,
                    deviceWrapper,
                    settings,
                    coroutineScope,
                )
                mockConnectionManager.complete(connectionManager)
                return connectionManager
            }
        }

        fun device(coroutineScope: CoroutineScope) = DeviceImpl(
            NAME,
            DeviceInfoImpl(
                createDeviceWrapper(NAME),
                rssi = -78,
                MockAdvertisementData(NAME),
            ),
            ConnectionSettings(),
            { manager.create(MockDeviceWrapper(NAME, NAME, DeviceWrapper.BondState.NONE, true), ConnectionSettings(), coroutineScope) },
            coroutineScope,
        )
    }

    private val mocks = Mocks()

    @Test
    fun unpairTest(): Unit = testBlockingAndCancelScope {
        val device = mocks.device(this)
        val flow = flowOf(device)
        flow.unpair()
        mocks.mockConnectionManager.await().unpairMock.verify()
    }

    @Test
    fun pairTest(): Unit = testBlockingAndCancelScope {
        val device = mocks.device(this)
        val mockConnectionManager = mocks.mockConnectionManager.await()
        mockConnectionManager.reset()
        val flow = flowOf(device)

        val connectingJob = async {
            flow.connect()
        }

        delay(100.milliseconds) // TODO wait for correct state instead
        mockConnectionManager.handleConnect()
        connectingJob.await()

        flow.pair()
        mockConnectionManager.pairMock.verify()
    }
}
