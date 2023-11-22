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

package com.splendo.kaluga.bluetooth.beacons

import com.splendo.kaluga.base.utils.bytesOf
import com.splendo.kaluga.base.utils.decodeHex
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceImpl
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CoroutineScope

typealias ServiceData = Map<UUID, ByteArray?>

object BeaconMock {

    private fun BeaconID.asByteArray() = bytesOf(0x00, 0xdc) +
        namespace.decodeHex()!! +
        instance.decodeHex()!!

    fun mockGenericDevice(name: String, coroutineScope: CoroutineScope) = makeDevice(
        createDeviceWrapper(name),
        coroutineScope = coroutineScope,
    )

    fun mockBeaconDevice(id: String, coroutineScope: CoroutineScope): Device {
        val beaconId = BeaconID(
            namespace = id.substring(0..19),
            instance = id.substring(20),
        )
        return makeDevice(
            deviceWrapper = createDeviceWrapper("Beacon:" + beaconId.namespace + beaconId.instance),
            serviceData = mapOf(
                Eddystone.SERVICE_UUID to beaconId.asByteArray(),
            ),
            coroutineScope,
        )
    }

    private fun makeDevice(deviceWrapper: DeviceWrapper, serviceData: ServiceData = emptyMap(), coroutineScope: CoroutineScope) = DeviceImpl(
        deviceWrapper.identifier,
        makeDeviceInfo(deviceWrapper.name.orEmpty(), serviceData),
        settings,
        {
            MockDeviceConnectionManager(
                deviceWrapper = deviceWrapper,
                connectionSettings = it,
                coroutineScope = coroutineScope,
            )
        },
        coroutineScope,
    )

    private val settings = ConnectionSettings(
        reconnectionSettings = ConnectionSettings.ReconnectionSettings.Always,
    )

    private fun makeDeviceInfo(name: String, serviceData: ServiceData) = DeviceInfoImpl(
        createDeviceWrapper(name),
        rssi = -78,
        MockAdvertisementData(name, serviceData = serviceData),
    )
}
