/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.beacons

import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class BeaconService (
    private val bluetooth: Bluetooth
) {

    fun startMonitoring() = bluetooth.startScanning()
    fun stopMonitoring() = bluetooth.stopScanning()
    suspend fun isMonitoring() = bluetooth.isScanning()

    fun beacons(): Flow<List<BeaconInfo>> = bluetooth.devices().map { devices ->
        devices.mapNotNull { createBeaconWith(it) }
    }

    fun isAnyInRange(beaconIds: List<String>) = beacons().map { list ->
        list.any { beaconIds.contains(it.fullID()) }
    }

    private suspend fun createBeaconWith(device: Device): BeaconInfo? {
        val serviceData = device.map { it.advertisementData.serviceData }.firstOrNull() ?: return null
        val data = serviceData[Eddystone.SERVICE_UUID] ?: return null
        val frame = Eddystone.unpack(data) ?: return null
        return BeaconInfo(device.identifier, frame.uid, frame.txPower)
    }
}

operator fun Flow<List<BeaconInfo>>.get(identifier: Identifier) = map { beacons ->
    beacons.firstOrNull { it.identifier == identifier }
}
