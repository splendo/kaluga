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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Beacons internal constructor(
    private val bluetooth: Bluetooth,
    private val extractor: ServiceDataExtractor,
    private val coroutineScope: CoroutineScope
) {

    companion object {
        private const val LOG_TAG = "Kaluga Beacons"

        fun create(
            bluetooth: Bluetooth,
            extractor: ServiceDataExtractor,
            coroutineScope: CoroutineScope = MainScope()
        ): Beacons {
            return Beacons(bluetooth, extractor, coroutineScope)
        }
    }

    @ExperimentalCoroutinesApi
    fun beacons(): Flow<List<Beacon>> = bluetooth.devices().map { list ->
        list.mapNotNull { createBeaconFrom(it) }
    }

    fun startMonitoring() = bluetooth.startScanning()

    fun stopMonitoring() = bluetooth.stopScanning()

    fun isMonitoring(): Flow<Boolean> = bluetooth.isScanning()

    private fun createBeaconFrom(device: Device): Beacon? {
        val data = extractor.extract(Eddystone.ServiceUUID, device.peekState().advertisementData.serviceData)
        if (data != null) {
            return Eddystone.unpackUIDFrame(data, coroutineScope)
        }
        return null
    }
}
