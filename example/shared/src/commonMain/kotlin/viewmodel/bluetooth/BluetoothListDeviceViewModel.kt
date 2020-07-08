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
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.subjectOf
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.resources.formatted
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.flow.map

class BluetoothListDeviceViewModel(private val identifier: Identifier, private val bluetooth: Bluetooth, navigator: Navigator<BluetoothListNavigation>) : NavigatingViewModel<BluetoothListNavigation>(navigator) {

    enum class ConnectButtonState {
        Connect,
        Disconnect
    }

    private val device = bluetooth.devices()[identifier]

    val name = deviceStateObservable { it.advertisementData.name ?: "bluetooth_no_name".localized() }
    val identifierString = identifier.stringValue
    val rssi = deviceStateObservable { "rssi".localized().formatted(it.rssi) }
    val isTxPowerVisible = deviceStateObservable { it.advertisementData.txPowerLevel != Int.MIN_VALUE }
    val txPower = deviceStateObservable { if (it.advertisementData.txPowerLevel != Int.MIN_VALUE) "txPower".localized().formatted(it.advertisementData.txPowerLevel) else "" }

    val isConnectButtonVisible = deviceStateObservable { it.deviceInfo.advertisementData.isConnectible }
    val connectButtonState = deviceStateObservable {
        when (it) {
            is DeviceState.Disconnected, is DeviceState.Disconnecting -> ConnectButtonState.Connect
            else -> ConnectButtonState.Disconnect
        }
    }
    val isMoreButtonVisible = deviceStateObservable { it is DeviceState.Connected }

    val status = deviceStateObservable {
        when(it) {
            is DeviceState.Disconnecting -> "bluetooth_disconneting"
            is DeviceState.Disconnected -> "bluetooth_disconnected"
            is DeviceState.Connected -> "bluetooth_connected"
            is DeviceState.Connecting -> "bluetooth_connecting"
            is DeviceState.Reconnecting -> "bluetooth_reconnecting"
        }.localized()
    }
    val serviceUUIDs = deviceStateObservable { parseServiceUUIDs(it.advertisementData.serviceUUIDs) }
    val serviceData = deviceStateObservable { parseServiceData(it.advertisementData.serviceData) }
    val manufacturerId = deviceStateObservable { "bluetooth_manufacturer_id".localized().formatted(it.advertisementData.manufacturerId ?: -1) }
    val manufacturerData = deviceStateObservable { "bluetooth_manufacturer_data".localized().formatted(it.advertisementData.manufacturerData?.toHexString() ?: "") }

    val isFoldedOut = subjectOf(false, coroutineScope)

    private fun <T> deviceStateObservable(mapper: (DeviceState) -> T): Observable<T> = device.state().map { mapper(it) }.toObservable(coroutineScope)

    fun onMorePressed() {
        navigator.navigate(BluetoothListNavigation(DeviceDetailsSpec().toBundle { specRow ->
            when (specRow) {
                is DeviceDetailsSpecRow.UUIDRow -> specRow.convertValue(identifier.stringValue)
            }
        }))
    }

    private fun parseServiceUUIDs(uuids: List<UUID>): String {
        val uuidString = uuids.fold("") { result, next ->
            if (result.isEmpty())
                next.toString()
            else
                "$result, $next"
        }
        return "bluetooth_service_uuids".localized().formatted(uuidString)
    }

    private fun parseServiceData(data: Map<UUID, ByteArray?>): String {
        val dataString = data.entries.fold("") { result, next ->
            val nextString = "${next.key}: ${next.value?.toHexString() ?: ""}"
            if (result.isEmpty())
                nextString
            else
                "$result\n$nextString"
        }
        return "bluetooth_service_data".localized().formatted(dataString)
    }

    override public fun onCleared() { super.onCleared() }

}