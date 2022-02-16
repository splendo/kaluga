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

package com.splendo.kaluga.test.mock.bluetooth

import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import kotlin.coroutines.CoroutineContext

class MockDeviceFactory(
    private val coroutineContext: CoroutineContext
) {

    private var _connectionManager: MockDeviceConnectionManager? = null
    internal val connectionManager: MockDeviceConnectionManager? get() = _connectionManager

    fun build(deviceInfo: DeviceInfo) = Device(
        connectionSettings = settings,
        initialDeviceInfo = deviceInfo.toImpl(),
        connectionBuilder = connectionBuilder,
        coroutineContext
    )

    private val settings = ConnectionSettings(
        ConnectionSettings.ReconnectionSettings.Never
    )

    private val connectionBuilder = object : BaseDeviceConnectionManager.Builder {
        override fun create(
            connectionSettings: ConnectionSettings,
            deviceWrapper: DeviceWrapper,
            stateRepo: DeviceStateFlowRepo
        ) = MockDeviceConnectionManager(
            connectionSettings,
            deviceWrapper,
            stateRepo
        ).also {
            _connectionManager = it
        }
    }

    private companion object {

        fun DeviceInfo.toImpl() = DeviceInfoImpl(
            deviceWrapper = createDeviceWrapper(deviceName = name, identifier = identifier),
            rssi = rssi,
            advertisementData = advertisementData
        )
    }
}