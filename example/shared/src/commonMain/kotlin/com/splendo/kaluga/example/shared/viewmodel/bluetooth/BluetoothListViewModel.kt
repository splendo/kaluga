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

package com.splendo.kaluga.example.shared.viewmodel.bluetooth

import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.bluetooth.Bluetooth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BluetoothListViewModel(navigator: Navigator<DeviceDetails>) : NavigatingViewModel<DeviceDetails>(navigator), KoinComponent {

    private val bluetooth: Bluetooth by inject()
    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.toInitializedObservable(coroutineScope)

    val title = bluetooth.isEnabled
        .mapLatest { if (it) "Enabled" else "Disabled" }
        .toInitializedObservable("Initializing...", coroutineScope)

    private val _devices = MutableStateFlow(emptyList<BluetoothListDeviceViewModel>())
    val devices = _devices.toInitializedObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch { bluetooth.isScanning().collect { _isScanning.value = it } }
        scope.launch {
            bluetooth.devices().map { devices -> devices.map { BluetoothListDeviceViewModel(it.identifier, bluetooth, navigator) } }.collect { devices ->
                cleanDevices()
                _devices.value = devices.sortedByDescending { it.name.currentOrNull }
            }
        }
    }

    fun onScanPressed() {
        if (_isScanning.value) {
            bluetooth.stopScanning()
        } else {
            bluetooth.startScanning()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanDevices()
    }

    private fun cleanDevices() {
        _devices.value.forEach { it.onCleared() }
    }
}
