/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.TestPeripheral.Command
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Scans for the peripheral and for its beacon, a second simulated device that only advertises: a service UUID and
 * service data in the advertisement, its name and manufacturer data in the scan response.
 */
class PeripheralScanTest : PeripheralTest() {

    @Test
    fun advertisementFields() = peripheralTest {
        withBeacon {
            val beacon = scan(setOf(TestPeripheral.beaconService)).single()
            // Until the scan response arrives the advertisement has no name.
            val advertisement = within(10.seconds, "scan response") { beacon.info.first { it.advertisementData.name != null }.advertisementData }
            assertEquals(TestPeripheral.BEACON_NAME, advertisement.name)
            assertEquals(listOf(TestPeripheral.beaconService), advertisement.serviceUUIDs)
            assertEquals(setOf(TestPeripheral.beaconServiceDataUuid), advertisement.serviceData.keys)
            assertContentEquals(TestPeripheral.beaconServiceData, advertisement.serviceData[TestPeripheral.beaconServiceDataUuid])
            assertEquals(TestPeripheral.BEACON_COMPANY, advertisement.manufacturerId)
            // The company identifier stays in front of the data, little-endian, as it was advertised.
            assertContentEquals(byteArrayOf(0xFF.toByte(), 0xFF.toByte()) + TestPeripheral.beaconManufacturerData, advertisement.manufacturerData)
            assertFalse(advertisement.isConnectable)
        }
    }

    @Test
    fun filterByServiceUuid() = peripheralTest {
        withBeacon {
            val beacons = scan(setOf(TestPeripheral.beaconService))
            assertEquals(listOf(TestPeripheral.BEACON_NAME), beacons.map { it.info.value.name })
            disconnect()
            val peripherals = scan(setOf(TestPeripheral.service))
            assertEquals(listOf(device.identifier), peripherals.map { it.identifier })
            assertTrue(peripherals.none { it.identifier == beacons.single().identifier })
        }
    }

    @Test
    fun deviceThatStopsAdvertisingStaysListed() = peripheralTest {
        withBeacon {
            val beacon = scan(setOf(TestPeripheral.beaconService), stopAfter = false).single()
            try {
                // The beacon may be listed from an earlier scan, so a fresh advertisement shows this scan is running.
                within(10.seconds, "fresh advertisement") { beacon.info.drop(1).first() }
                assertTrue(advertisementsOf(beacon, 3.seconds) > 0, "no advertisements while the beacon advertises")
                send(Command.StopBeacon)
                // Gives an advertisement already on its way time to arrive.
                delay(1000)
                assertEquals(0, advertisementsOf(beacon, 3.seconds), "advertisements after the beacon stopped")
                // The client has no notion of a lost device: it stays in the list with its last advertisement.
                assertEquals(listOf(beacon), client.scannedDevices(setOf(TestPeripheral.beaconService)).first())
                assertEquals(TestPeripheral.BEACON_NAME, beacon.info.value.name)
            } finally {
                client.stopScanning(BluetoothClient.CleanMode.RETAIN_ALL)
            }
        }
    }

    private suspend fun PeripheralSession.withBeacon(block: suspend PeripheralSession.Attributes.() -> Unit) {
        val attributes = connectAndDiscover()
        attributes.send(Command.StartBeacon)
        try {
            attributes.block()
        } finally {
            // Connects again in case the block disconnected.
            connectAndDiscover().send(Command.StopBeacon)
        }
    }

    private suspend fun PeripheralSession.scan(filter: Set<UUID>, stopAfter: Boolean = true): List<ConnectableDevice> {
        platform.awaitScanStart()
        client.startScanning(filter, BluetoothClient.CleanMode.RETAIN_ALL, connectionSettings)
        return try {
            within(30.seconds, "scan for $filter") { client.scannedDevices(filter).first { it.isNotEmpty() } }
        } finally {
            if (stopAfter) client.stopScanning(BluetoothClient.CleanMode.RETAIN_ALL)
        }
    }

    private suspend fun PeripheralSession.advertisementsOf(device: ConnectableDevice, duration: Duration): Int {
        val seen = mutableListOf<DeviceInfo>()
        val collector = scope.launch { device.info.collect { seen += it } }
        delay(duration)
        collector.cancel()
        // The state flow replays the info it held when collection started.
        return (seen.size - 1).also { log("${device.identifier} advertised $it times in $duration") }
    }
}
