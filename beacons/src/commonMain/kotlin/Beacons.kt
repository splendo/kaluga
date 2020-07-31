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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Beacons internal constructor(private val bluetooth: Bluetooth) {

    interface Builder {
        fun create(bluetooth: Bluetooth): Beacons
    }

    companion object {
        private const val LOG_TAG = "Kaluga Beacons"
    }

    @ExperimentalCoroutinesApi
    fun beacons(): Flow<List<Beacon>> = bluetooth.devices().map { list ->
        list.mapNotNull { device ->
            Beacon.init(device.identifier, device.peekState().advertisementData.serviceData)
        }
    }
}
