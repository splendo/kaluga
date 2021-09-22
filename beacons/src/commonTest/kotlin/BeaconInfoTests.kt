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
import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.base.utils.decodeHex
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.beacons.BeaconID
import com.splendo.kaluga.bluetooth.beacons.BeaconInfo
import com.splendo.kaluga.bluetooth.beacons.Beacons
import com.splendo.kaluga.bluetooth.beacons.Eddystone
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class BeaconInfoTests {

    private companion object {
        const val deviceName = "BEEC0NZ"
        val deviceWrapper = createDeviceWrapper(deviceName = deviceName)
        val uidFrame = dataOf(
            0x00, // Type
            0xdc, // Tx Power
            0xf7, 0x82, 0x6d, 0xa6, 0xbc, 0x5b, 0x71, 0xe0, 0x89, 0x3e, // Name Space ID
            0x4e, 0x41, 0x61, 0x46, 0x01, 0x02 // Instance ID
        )
        val serviceData = mapOf(
            Eddystone.SERVICE_UUID to uidFrame
        )

        val settings = ConnectionSettings(
            ConnectionSettings.ReconnectionSettings.Limited(2)
        )

        fun BeaconID.asByteArray() = dataOf(0x00, 0xdc) +
            namespace.decodeHex()!! +
            instance.decodeHex()!!

        fun makeDeviceInfo(name: String, id: BeaconID) = DeviceInfoImpl(
            createDeviceWrapper(name),
            rssi = -78,
            MockAdvertisementData(
                name,
                serviceData = mapOf(
                    Eddystone.SERVICE_UUID to id.asByteArray()
                )
            )
        )

        fun makeDevice(name: String, id: BeaconID, coroutineContext: CoroutineContext) = Device(
            settings,
            makeDeviceInfo(name, id),
            manager,
            coroutineContext
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
        private val coroutineScope: CoroutineScope
    ) : BluetoothService {

        val devices = MutableStateFlow(emptyList<Device>())

        override val isEnabled = flowOf(true)

        override fun startScanning(filter: Set<UUID>) { }
        override fun stopScanning() { }
        override fun devices() = devices.asStateFlow()
        override suspend fun isScanning() = flowOf(true).stateIn(coroutineScope)
    }

    @Test
    fun testEmptyList() = runBlocking {
        val mock = BluetoothMock(this)
        val beacons = Beacons(mock)
        val found = beacons.beacons().firstOrNull() ?: fail("Expected Beacons list")
        assertTrue(found.isEmpty())
        val isInRange = beacons.isAnyInRange(listOf("123")).firstOrNull() ?: fail("Expected value")
        assertFalse(isInRange)
    }

    @Test
    fun testDiscoveredBeacon() = runBlocking {
        val device = Device(settings, deviceInfo, manager, this.coroutineContext)
        val mock = BluetoothMock(this)
        val beacons = Beacons(mock, timeoutMs = 1_000)
        mock.devices.emit(listOf(device))
        val beacon = beacons.beacons().first { it.isNotEmpty() }.firstOrNull() ?: fail("Expected Beacon")
        assertEquals(-78, beacon.RSSI)
        assertEquals("f7826da6bc5b71e0893e4e4161460102", beacon.identifier)

        val list = listOf("f7826da6bc5b71e0893e4e4161460102", "123", "456")
        val isInRange = beacons.isAnyInRange(list).firstOrNull() ?: fail("Expected value")
        assertTrue(isInRange)
    }

    @Test
    fun testDiscoverSameBeacon() = runBlocking {
        val mock = BluetoothMock(this)
        val beacons = Beacons(mock, timeoutMs = 10_000)

        val beacon1 = BeaconID("f7826da6bc5b71e0893e", "4e4161460111")
        val device1 = makeDevice("beacon1", beacon1, this.coroutineContext)
        mock.devices.emit(listOf(device1))
        val discoveredSet1 = beacons.beacons().first { it.isNotEmpty() }
        assertEquals(1, discoveredSet1.size)

        delay(1_000)
        val beacon2 = BeaconID("f7826da6bc5b71e0893e", "4e4161460111")
        val device2 = makeDevice("beacon1", beacon2, this.coroutineContext)
        mock.devices.emit(listOf(device2))
        val discoveredSet2 = beacons.beacons().first()
        assertEquals(1, discoveredSet2.size)

        delay(1_000)
        val beacon3 = BeaconID("f7826da6bc5b71e0893e", "4e4161460111")
        val device3 = makeDevice("beacon1", beacon3, this.coroutineContext)
        mock.devices.emit(listOf(device3))
        val discoveredSet3 = beacons.beacons().first()
        assertEquals(1, discoveredSet3.size)

        // Last discovered last seen should be updated
        assertTrue {
            discoveredSet3.first().lastSeen.millisecondSinceEpoch > discoveredSet1.first().lastSeen.millisecondSinceEpoch
        }
    }

    @Test
    fun testMultipleBeacons() = runBlocking {
        val beacon1 = BeaconID("f7826da6bc5b71e0893e", "4e4161460111")
        val device1 = makeDevice("beacon1", beacon1, this.coroutineContext)
        val beacon2 = BeaconID("f7826da6bc5b71e0893e", "4e4161460222")
        val device2 = makeDevice("beacons2", beacon2, this.coroutineContext)
        val mock = BluetoothMock(this)
        val beacons = Beacons(mock, timeoutMs = 10_000)

        mock.devices.emit(listOf(device1))
        val discoveredSet1 = beacons.beacons().first { it.isNotEmpty() }
        assertEquals(1, discoveredSet1.size)

        // Discover device2 and lost device1
        mock.devices.emit(listOf(device2))
        val discoveredSet2 = beacons.beacons().first()
        // Should contain beacon1 and beacon2
        assertEquals(2, discoveredSet2.size)

        // If all lost, still keeps cached devices
        mock.devices.emit(emptyList())
        val discoveredSet3 = beacons.beacons().first()
        assertEquals(2, discoveredSet3.size)
    }

    @Test
    fun testLostDeviceOnTimeout() = runBlocking {
        val beacon1 = BeaconID("f7826da6bc5b71e0893e", "4e4161460111")
        val device1 = makeDevice("beacon1", beacon1, this.coroutineContext)
        val beacon2 = BeaconID("f7826da6bc5b71e0893e", "4e4161460222")
        val device2 = makeDevice("beacons2", beacon2, this.coroutineContext)
        val mock = BluetoothMock(this)
        val beacons = Beacons(mock, timeoutMs = 0)

        mock.devices.emit(listOf(device1))
        val discoveredSet1 = beacons.beacons().first { it.isNotEmpty() }
        assertEquals(1, discoveredSet1.size)

        // Discover device2 and lost device1
        mock.devices.emit(listOf(device2))
        val discoveredSet2 = beacons.beacons().first()
        // Should contain beacon2 only
        assertEquals(1, discoveredSet2.size)

        // If all lost after timeoutMs, should be empty list
        mock.devices.emit(emptyList())
        val discoveredSet3 = beacons.beacons().first()
        assertTrue(discoveredSet3.isEmpty())
    }

    @Test
    fun testMergeOldWithNew() {
        val beacon1 = BeaconInfo("NS123", BeaconID("NS", "123"), 1, 1, Date.epoch())
        val beacon2 = BeaconInfo("NS123", BeaconID("NS", "123"), 2, 2, Date.epoch())

        val newSet = (setOf(beacon1) + beacon2).toSet()
        assertEquals(1, newSet.size)
        assertEquals(1, newSet.first().txPower)
    }
}
