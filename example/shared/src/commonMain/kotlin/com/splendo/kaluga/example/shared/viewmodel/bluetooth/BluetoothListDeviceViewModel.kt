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
import com.splendo.kaluga.architecture.observable.BaseInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.advertisement
import com.splendo.kaluga.bluetooth.connect
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.disconnect
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.rssi
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.minutes

class BluetoothListDeviceViewModel(
    private val identifier: Identifier,
    bluetooth: Bluetooth,
    private val coroutineScope: CoroutineScope,
    private val navigator: Navigator<DeviceDetails>,
) {

    private val device = bluetooth.allDevices()[identifier]

    val name = advertisementObservable("bluetooth_no_name".localized()) { it.name ?: "bluetooth_no_name".localized() }
    val identifierString = identifier.stringValue
    val rssi = device.rssi().map { "rssi".localized().format(it) }.toInitializedObservable("", coroutineScope)
    val isTxPowerVisible = advertisementObservable(false) { it.txPowerLevel != Int.MIN_VALUE }
    val txPower = advertisementObservable("") { if (it.txPowerLevel != Int.MIN_VALUE) "txPower".localized().format(it.txPowerLevel) else "" }

    val isConnectButtonVisible = deviceStateObservable(false) { it !is NotConnectableDeviceState }
    val connectButton: BaseInitializedObservable<KalugaButton> = deviceStateObservable(KalugaButton.Plain("", ButtonStyles.default) {}) {
        when (it) {
            is ConnectableDeviceState.Disconnected, is ConnectableDeviceState.Disconnecting -> KalugaButton.Plain("Connect", ButtonStyles.default) { onConnectPressed() }
            else -> KalugaButton.Plain("Disconnect", ButtonStyles.default) { onDisconnectPressed() }
        }
    }
    val isMoreButtonVisible = deviceStateObservable(false) { it is ConnectableDeviceState.Connected }

    val status = deviceStateObservable("") {
        when (it) {
            is NotConnectableDeviceState -> ""
            is ConnectableDeviceState.Disconnecting -> "bluetooth_disconneting"
            is ConnectableDeviceState.Disconnected -> "bluetooth_disconnected"
            is ConnectableDeviceState.Connected -> "bluetooth_connected"
            is ConnectableDeviceState.Connecting -> "bluetooth_connecting"
        }.localized()
    }
    val serviceUUIDs = advertisementObservable("") { parseServiceUUIDs(it.serviceUUIDs) }
    val serviceData = advertisementObservable("") { parseServiceData(it.serviceData) }
    val manufacturerId = advertisementObservable("") { "bluetooth_manufacturer_id".localized().format(it.manufacturerId ?: -1) }
    val manufacturerData = advertisementObservable("") { "bluetooth_manufacturer_data".localized().format(it.manufacturerData?.toHexString() ?: "") }

    private val _isFoldedOut = MutableStateFlow(false)
    val isFoldedOut = _isFoldedOut.toInitializedObservable(coroutineScope)

    private fun <T> deviceStateObservable(initialValue: T, mapper: (DeviceState) -> T): BaseInitializedObservable<T> = device.state()
        .map { mapper(it) }
        .toInitializedObservable(initialValue, coroutineScope)
    private fun <T> advertisementObservable(initialValue: T, mapper: (BaseAdvertisementData) -> T): BaseInitializedObservable<T> = device.advertisement().map {
        mapper(it)
    }.toInitializedObservable(initialValue, coroutineScope)

    fun toggleFoldOut() = coroutineScope.launch {
        _isFoldedOut.value = !_isFoldedOut.value
    }

    private fun onConnectPressed() = coroutineScope.launch {
        try {
            withTimeout(5.minutes) {
                device.connect()
            }
        } catch (_: TimeoutCancellationException) {}
    }

    private fun onDisconnectPressed() = coroutineScope.launch {
        device.disconnect()
    }

    fun onMorePressed() = navigator.navigate(DeviceDetails(identifier))

    private fun parseServiceUUIDs(uuids: List<UUID>): String {
        val uuidString = uuids.fold("") { result, next ->
            if (result.isEmpty()) next.toString() else "$result, $next"
        }
        return "bluetooth_service_uuids".localized().format(uuidString)
    }

    private fun parseServiceData(data: Map<UUID, ByteArray?>): String {
        val dataString = data.entries.fold("") { result, next ->
            val nextString = "${next.key}: ${next.value?.toHexString() ?: ""}"
            if (result.isEmpty()) nextString else "$result\n$nextString"
        }
        return "bluetooth_service_data".localized().format(dataString)
    }
}
