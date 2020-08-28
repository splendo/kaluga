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
import com.splendo.kaluga.bluetooth.ServiceDataExtractor
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.state
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class BeaconService internal constructor(
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
        ): BeaconService {
            return BeaconService(bluetooth, extractor, coroutineScope)
        }
    }

    @ExperimentalCoroutinesApi
    fun beacons(): Flow<List<Beacon>> = bluetooth.devices().map { devices ->
        devices.mapNotNull { createBeaconWith(it.identifier) }
    }

    fun startMonitoring() = bluetooth.startScanning()

    fun stopMonitoring() = bluetooth.stopScanning()

    fun isMonitoring(): Flow<Boolean> = bluetooth.isScanning()

    private suspend fun createBeaconWith(identifier: Identifier): Beacon? {
        val device = bluetooth.devices()[identifier]
        val serviceData = device.state().map { it.advertisementData.serviceData }.first()
        val data = extractor.extract(serviceData,  Eddystone.ServiceUUIDFull)
        if (data != null) {
            return when (val frame = Eddystone.unpack(data)) {
                is Eddystone.Frame.UIDFrame -> Beacon(
                    BeaconInfoImpl(identifier, frame.uid, frame.txPower),
                    coroutineScope
                )
                else -> null
            }
        }
        return null
    }
}

operator fun Flow<List<Beacon>>.get(identifier: Identifier) = this.map { beacons ->
    beacons.firstOrNull { it.identifier == identifier }
}

fun Flow<Beacon?>.state(): Flow<BeaconState> = this.flatMapLatest { beacon ->
    beacon?.flow() ?: emptyFlow()
}
