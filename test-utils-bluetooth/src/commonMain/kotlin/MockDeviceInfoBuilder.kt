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

import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.TxPower
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceStateImplRepo
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceImpl
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

@MockBuilderDsl
typealias ServiceUUIDsList = ArrayList<UUID>

fun ServiceUUIDsList.uuid(uuidString: String) = add(uuidFrom(uuidString))

@MockBuilderDsl
class MockDeviceInfoBuilder {

    var deviceName: String? = null
    var identifier: Identifier = randomIdentifier()
    var rssi: RSSI = 0
    var manufacturerId: Int? = null
    var manufacturerData: ByteArray? = null
    private val serviceUUIDs = ArrayList<UUID>()
    var serviceData: Map<UUID, ByteArray?> = emptyMap()
    var txPowerLevel: TxPower = Int.MIN_VALUE

    fun services(builder: ServiceUUIDsList.() -> Unit) = builder(serviceUUIDs)

    fun build() = DeviceInfoImpl(
        deviceName,
        identifier,
        rssi,
        MockAdvertisementData(
            deviceName,
            manufacturerId,
            manufacturerData,
            serviceUUIDs.toList(),
            serviceData,
            txPowerLevel,
        ),
    )
}

fun createMockDevice(
    identifier: Identifier,
    connectionSettings: ConnectionSettings = ConnectionSettings(
        reconnectionSettings = ConnectionSettings.ReconnectionSettings.Never,
    ),
    connectionManagerBuilder: (ConnectionSettings) -> DeviceConnectionManager,
    builder: MockDeviceInfoBuilder.() -> Unit,
    createDeviceStateFlow: (DeviceConnectionManager, CoroutineContext) -> ConnectableDeviceStateFlowRepo = { manager, coroutineContext ->
        ConnectableDeviceStateImplRepo(connectionSettings.reconnectionSettings, manager, coroutineContext)
    },
    coroutineScope: CoroutineScope,
) = DeviceImpl(
    identifier = identifier,
    initialDeviceInfo = MockDeviceInfoBuilder().apply(builder).build(),
    connectionSettings = connectionSettings,
    connectionManagerBuilder = connectionManagerBuilder,
    coroutineScope = coroutineScope,
    createDeviceStateFlow = createDeviceStateFlow,
)

fun createMockDevice(
    wrapper: DeviceWrapper,
    coroutineScope: CoroutineScope,
    connectionSettings: ConnectionSettings = ConnectionSettings(
        reconnectionSettings = ConnectionSettings.ReconnectionSettings.Never,
    ),
    connectionManagerBuilder: MockDeviceConnectionManager.Builder = MockDeviceConnectionManager.Builder(),
    createDeviceStateFlow: (DeviceConnectionManager, CoroutineContext) -> ConnectableDeviceStateFlowRepo = { manager, coroutineContext ->
        ConnectableDeviceStateImplRepo(connectionSettings.reconnectionSettings, manager, coroutineContext)
    },
    builder: MockDeviceInfoBuilder.() -> Unit,
) = createMockDevice(wrapper.identifier, connectionSettings, { connectionManagerBuilder.create(wrapper, it, coroutineScope) }, builder, createDeviceStateFlow, coroutineScope)
