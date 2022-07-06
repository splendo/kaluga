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

import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.observable.UninitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toUninitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.connect
import com.splendo.kaluga.bluetooth.advertisement
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.disconnect
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.rssi
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BluetoothListDeviceViewModel(private val identifier: Identifier, bluetooth: Bluetooth, navigator: Navigator<BluetoothListNavigation>) : NavigatingViewModel<BluetoothListNavigation>(navigator) {

    enum class ConnectButtonState {
        Connect,
        Disconnect
    }

    private val device = bluetooth.devices()[identifier]

    val name = advertisementObservable { it.name ?: "bluetooth_no_name".localized() }
    val identifierString = identifier.stringValue
    val rssi = device.rssi().map { "rssi".localized().format(it) }.toUninitializedObservable(coroutineScope)
    val isTxPowerVisible = advertisementObservable { it.txPowerLevel != Int.MIN_VALUE }
    val txPower = advertisementObservable { if (it.txPowerLevel != Int.MIN_VALUE) "txPower".localized().format(it.txPowerLevel) else "" }

    val isConnectButtonVisible = deviceStateObservable { it !is NotConnectableDeviceState }
    val connectButtonState = deviceStateObservable {
        when (it) {
            is ConnectableDeviceState.Disconnected, is ConnectableDeviceState.Disconnecting -> ConnectButtonState.Connect
            else -> ConnectButtonState.Disconnect
        }
    }
    val isMoreButtonVisible = deviceStateObservable { it is ConnectableDeviceState.Connected }

    val status = deviceStateObservable {
        when (it) {
            is NotConnectableDeviceState -> ""
            is ConnectableDeviceState.Disconnecting -> "bluetooth_disconneting"
            is ConnectableDeviceState.Disconnected -> "bluetooth_disconnected"
            is ConnectableDeviceState.Connected -> "bluetooth_connected"
            is ConnectableDeviceState.Connecting -> "bluetooth_connecting"
            is ConnectableDeviceState.Reconnecting -> "bluetooth_reconnecting"
        }.localized()
    }
    val serviceUUIDs = advertisementObservable { parseServiceUUIDs(it.serviceUUIDs) }
    val serviceData = advertisementObservable { parseServiceData(it.serviceData) }
    val manufacturerId = advertisementObservable { "bluetooth_manufacturer_id".localized().format(it.manufacturerId ?: -1) }
    val manufacturerData = advertisementObservable { "bluetooth_manufacturer_data".localized().format(it.manufacturerData?.toHexString() ?: "") }

    val _isFoldedOut = MutableStateFlow(false)
    val isFoldedOut = _isFoldedOut.toInitializedObservable(coroutineScope)

    private fun <T> deviceStateObservable(mapper: (DeviceState) -> T): UninitializedObservable<T> = device.state().map { mapper(it) }.toUninitializedObservable(coroutineScope)
    private fun <T> advertisementObservable(mapper: (BaseAdvertisementData) -> T): UninitializedObservable<T> = device.advertisement().map { mapper(it) }.toUninitializedObservable(coroutineScope)

    fun toggleFoldOut() = coroutineScope.launch {
        _isFoldedOut.value = !_isFoldedOut.value
    }

    fun onConnectPressed() = coroutineScope.launch {
        device.connect()
    }

    fun onDisconnectPressed() = coroutineScope.launch {
        device.disconnect()
    }

    fun onMorePressed() =
        navigator.navigate(
            BluetoothListNavigation(
                DeviceDetailsSpec().toBundle { specRow ->
                    when (specRow) {
                        is DeviceDetailsSpecRow.UUIDRow -> specRow.convertValue(identifier.stringValue)
                    }
                }
            )
        )

    private fun parseServiceUUIDs(uuids: List<UUID>): String {
        val uuidString = uuids.fold("") { result, next ->
            if (result.isEmpty())
                next.toString()
            else
                "$result, $next"
        }
        return "bluetooth_service_uuids".localized().format(uuidString)
    }

    private fun parseServiceData(data: Map<UUID, ByteArray?>): String {
        val dataString = data.entries.fold("") { result, next ->
            val nextString = "${next.key}: ${next.value?.toHexString() ?: ""}"
            if (result.isEmpty())
                nextString
            else
                "$result\n$nextString"
        }
        return "bluetooth_service_data".localized().format(dataString)
    }

    public override fun onCleared() {
        super.onCleared()
    }
}
