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

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.resources.formatted
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DeviceDetailsSpec : NavigationBundleSpec<DeviceDetailsSpecRow<*>>(setOf(DeviceDetailsSpecRow.UUIDRow))

sealed class DeviceDetailsSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    object UUIDRow : DeviceDetailsSpecRow<String>(NavigationBundleSpecType.StringType)
}

class BluetoothDeviceDetailViewModel(private val bluetooth: Bluetooth, private val identifier: Identifier) : BaseViewModel() {

    companion object {
        private const val rssi_frequency = 1000L
    }

    private val device = bluetooth.devices()[identifier]

    val name = device.info().map { it.name ?: "bluetooth_no_name".localized() }.toObservable(coroutineScope)
    val rssi = device.rssi().map { "rssi".localized().formatted(it) }.toObservable(coroutineScope)
    val distance = device.distance().map { "distance".localized().formatted(it) }.toObservable(coroutineScope)
    val state = device.state().map { deviceState ->
        when(deviceState) {
            is DeviceState.Disconnecting -> "bluetooth_disconneting"
            is DeviceState.Disconnected -> "bluetooth_disconnected"
            is DeviceState.Connected.Discovering -> "bluetooth_discovering"
            is DeviceState.Connected -> "bluetooth_connected"
            is DeviceState.Connecting -> "bluetooth_connecting"
            is DeviceState.Reconnecting -> "bluetooth_reconnecting"
        }.localized()
    }.toObservable(coroutineScope)
    val _services = ConflatedBroadcastChannel<List<BluetoothServiceViewModel>>()
    val services = _services.toObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            device.services().map { services -> services.map { BluetoothServiceViewModel(bluetooth, identifier, it.uuid) } }.collect {
                cleanServices()
                _services.send(it)
            }
        }

        scope.launch {
            while (true) {
                device.updateRssi()
                delay(rssi_frequency)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanServices()
    }

    private fun cleanServices() {
        _services.valueOrNull?.forEach { it.onCleared() }
    }
}
