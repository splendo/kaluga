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

package com.splendo.kaluga.test.mock.bluetooth

import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.ServiceData
import com.splendo.kaluga.bluetooth.randomUUID
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import kotlin.coroutines.CoroutineContext

expect fun createDeviceWrapper(deviceName: String? = null): DeviceWrapper

expect fun createServiceWrapper(
    stateRepo: DeviceStateFlowRepo,
    uuid: UUID = randomUUID(),
    characteristics: List<Pair<UUID, List<UUID>>> = listOf(randomUUID() to listOf(randomUUID()))
): ServiceWrapper

interface CanUpdateMockValue {
    fun updateMockValue(value: ByteArray?)
}

fun createDeviceInfo(
    name: String = "DeviceName",
    rssi: Int = -78,
    serviceUUIDs: List<UUID> = emptyList(),
    serviceData: ServiceData = emptyMap()
) = DeviceInfoImpl(
    createDeviceWrapper(name),
    rssi,
    MockAdvertisementData(name, serviceUUIDs = serviceUUIDs, serviceData = serviceData)
)

fun createDevice(
    name: String = "DeviceName",
    rssi: Int = -78,
    serviceUUIDs: List<UUID> = emptyList(),
    serviceData: ServiceData = emptyMap(),
    connectionSettings: ConnectionSettings = ConnectionSettings(
        ConnectionSettings.ReconnectionSettings.Always
    ),
    connectionManagerBuilder: BaseDeviceConnectionManager.Builder = object : BaseDeviceConnectionManager.Builder {
        override fun create(
            connectionSettings: ConnectionSettings,
            deviceWrapper: DeviceWrapper,
            stateRepo: DeviceStateFlowRepo
        ): BaseDeviceConnectionManager {
            return create(connectionSettings, deviceWrapper, stateRepo)
        }
    },
    coroutineContext: CoroutineContext
) = Device(
    connectionSettings,
    createDeviceInfo(name, rssi = rssi, serviceUUIDs = serviceUUIDs, serviceData = serviceData),
    connectionManagerBuilder,
    coroutineContext
)
