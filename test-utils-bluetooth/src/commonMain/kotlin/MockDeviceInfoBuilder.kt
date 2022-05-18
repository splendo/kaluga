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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlin.coroutines.CoroutineContext

@MockBuilderDsl
typealias ServiceUUIDsList = ArrayList<UUID>

fun ServiceUUIDsList.uuid(uuidString: String) = add(uuidFrom(uuidString))

@MockBuilderDsl
class MockDeviceInfoBuilder {

    var deviceName: String? = null
    var rssi: Int = 0
    var manufacturerId: Int? = null
    var manufacturerData: ByteArray? = null
    private val _serviceUUIDs = ArrayList<UUID>()
    private val serviceUUIDs get() = _serviceUUIDs.toList()
    var serviceData: Map<UUID, ByteArray?> = emptyMap()
    var txPowerLevel: Int = Int.MIN_VALUE

    fun services(builder: ServiceUUIDsList.() -> Unit) = builder(_serviceUUIDs)

    fun build() = DeviceInfoImpl(
        createDeviceWrapper(deviceName),
        rssi,
        MockAdvertisementData(
            deviceName,
            manufacturerId,
            manufacturerData,
            serviceUUIDs,
            serviceData,
            txPowerLevel
        )
    )
}

fun createMockDevice(
    coroutineContext: CoroutineContext,
    connectionSettings: ConnectionSettings = ConnectionSettings(
        ConnectionSettings.ReconnectionSettings.Never
    ),
    connectionBuilder: BaseDeviceConnectionManager.Builder = MockDeviceConnectionManager.Builder(),
    builder: MockDeviceInfoBuilder.() -> Unit
) = Device(
    connectionSettings = connectionSettings,
    connectionBuilder = connectionBuilder,
    initialDeviceInfo = MockDeviceInfoBuilder().apply(builder).build(),
    coroutineContext = coroutineContext
)
