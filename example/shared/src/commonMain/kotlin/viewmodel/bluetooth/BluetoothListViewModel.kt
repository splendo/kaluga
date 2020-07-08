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

package com.splendo.kaluga.example.shared.viewmodel.bluetooth

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.bluetooth.Bluetooth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BluetoothListNavigation(bundle: NavigationBundle<DeviceDetailsSpecRow<*>>): NavigationAction<DeviceDetailsSpecRow<*>>(bundle)

class BluetoothListViewModel(private val bluetooth: Bluetooth, navigator: Navigator<BluetoothListNavigation>) : NavigatingViewModel<BluetoothListNavigation>(navigator) {

    private val _isScanning = ConflatedBroadcastChannel<Boolean>()
    val isScanning = _isScanning.toObservable(coroutineScope)

    private val _devices = ConflatedBroadcastChannel<List<BluetoothListDeviceViewModel>>()
    val devices = _devices.toObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch { bluetooth.isScanning().collect { _isScanning.send(it) } }
        scope.launch { bluetooth.devices().map { devices -> devices.map { BluetoothListDeviceViewModel(it.identifier, bluetooth, navigator) } }.collect { devices ->
            cleanDevices()
            _devices.send(devices)
        } }
    }

    fun onScanPressed() {
        if (_isScanning.valueOrNull == true) {
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
        _devices.valueOrNull?.forEach { it.onCleared() }
    }
}
