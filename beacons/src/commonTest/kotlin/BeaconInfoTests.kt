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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.beacons.Beacons
import com.splendo.kaluga.bluetooth.beacons.Eddystone
import com.splendo.kaluga.bluetooth.beacons.fullID
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class BeaconInfoTests {

    private companion object {
        private const val deviceName = "BEEC0NZ"
        private val deviceWrapper = createDeviceWrapper(deviceName = deviceName)
        private val uidFrame = dataOf(
            0x00, // Type
            0xdc, // Tx Power
            0xf7, 0x82, 0x6d, 0xa6, 0xbc, 0x5b, 0x71, 0xe0, 0x89, 0x3e, // Name Space ID
            0x4e, 0x41, 0x61, 0x46, 0x01, 0x02 // Instance ID
        )
        private val serviceData = mapOf(
            Eddystone.SERVICE_UUID to uidFrame
        )
        val settings = ConnectionSettings(
            ConnectionSettings.ReconnectionSettings.Limited(2)
        )
        val deviceInfo = DeviceInfoImpl(
            deviceWrapper,
            rssi = -78,
            MockAdvertisementData(
                name = deviceName,
                serviceData = serviceData
            )
        )
        val manager = object : BaseDeviceConnectionManager.Builder {
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
    }

    class BluetoothMock(
        private val devices: List<Device>,
        private val coroutineScope: CoroutineScope
    ) : BluetoothService {
        override fun startScanning(filter: Set<UUID>) { }
        override fun stopScanning() { }
        override fun devices() = flowOf(devices)
        override suspend fun isScanning() = flowOf(true).stateIn(coroutineScope)
        override val isEnabled = flowOf(true)
    }

    @Test
    fun testEmptyList() = runBlocking {
        val mock = BluetoothMock(emptyList(), this)
        val beacons = Beacons(mock)
        val found = beacons.beacons().firstOrNull() ?: fail("Expected Beacons list")
        assertTrue(found.isEmpty())
        val isInRange = beacons.isAnyInRange(listOf("123")).firstOrNull() ?: fail("Expected value")
        assertFalse(isInRange)
    }

    @Test
    fun testDiscoveredBeacon() = runBlocking {
        val device = Device(settings, deviceInfo, manager, this.coroutineContext)
        val mock = BluetoothMock(listOf(device), this)
        val beacons = Beacons(mock, timeoutMs = 1_000)
        val beacon = beacons.beacons().firstOrNull()?.firstOrNull() ?: fail("Expected Beacon")
        assertEquals(-78, beacon.RSSI)
        assertEquals("f7826da6bc5b71e0893e4e4161460102", beacon.fullID())

        val list = listOf("f7826da6bc5b71e0893e4e4161460102", "123", "456")
        val isInRange = beacons.isAnyInRange(list).firstOrNull() ?: fail("Expected value")
        assertTrue(isInRange)
    }
}
