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

package com.splendo.kaluga.bluetooth.beacons

import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class BluetoothMock(
    private val coroutineScope: CoroutineScope
) : BluetoothService {

    val pairedDevices = MutableStateFlow(emptyList<Device>())
    val discoveredDevices = MutableStateFlow(emptyList<Device>())

    override val isEnabled = flowOf(true)

    override fun startScanning(filter: Set<UUID>) { }
    override fun stopScanning() { }
    override suspend fun pairedDevices(filter: Set<UUID>) = pairedDevices.first()
    override fun devices() = discoveredDevices.asStateFlow()
    override suspend fun isScanning() = flowOf(true).stateIn(coroutineScope)
}
