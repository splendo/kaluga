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

package com.splendo.kaluga.example.feature.bluetooth.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.example.feature.bluetooth.base.BluetoothSpec
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.minutes

class BluetoothDeviceListViewModel(private val client: BluetoothClient) : ViewModel() {

    private val filter = setOf(BluetoothSpec.HeartRateService.UUID)

    val enabled: StateFlow<Boolean> = client.isEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // `isScanning()` is a `suspend fun` returning a `Flow<Boolean>`, so it is awaited in a coroutine.
    val scanning: StateFlow<Boolean> = flow { emitAll(client.isScanning()) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val pairedDevices: StateFlow<List<ConnectableDevice>> = client.pairedDevices(filter)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val scannedDevices: StateFlow<List<ConnectableDevice>> = client.devices()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** Toggle scanning with the chosen clean mode — stop if currently scanning, otherwise start. */
    fun toggleScanning(cleanMode: BluetoothClient.CleanMode) {
        if (scanning.value) {
            client.stopScanning(cleanMode)
        } else {
            client.startScanning(filter = filter, cleanMode = cleanMode)
        }
    }

    fun connect(device: ConnectableDevice, onConnected: () -> Unit) = viewModelScope.launch {
        try {
            withTimeout(5.minutes) {
                if (device.connect()) onConnected()
            }
        } catch (_: TimeoutCancellationException) {
        }
    }

    fun disconnect(device: ConnectableDevice) = viewModelScope.launch { device.disconnect() }
}
