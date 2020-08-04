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

package com.splendo.kaluga.example.shared.viewmodel.beacons

import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.beacons.Beacon
import com.splendo.kaluga.beacons.BeaconState
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.connect
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.disconnect
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.example.shared.viewmodel.featureList.Feature
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalStdlibApi
class BeaconsListBeaconViewModel(private val beacon: Beacon, navigator: Navigator<BeaconsListNavigation>) : NavigatingViewModel<BeaconsListNavigation>(navigator) {

    enum class ConnectButtonState {
        Connect,
        Disconnect
    }

    val _isFoldedOut = ConflatedBroadcastChannel(false)
    val isFoldedOut = _isFoldedOut.toObservable(coroutineScope)

    fun toggleFoldOut() {
        coroutineScope.launch {
            _isFoldedOut.send(!_isFoldedOut.value)
        }
    }

    fun onConnectPressed() {
    }

    fun onDisconnectPressed() {
    }

    fun onMorePressed() {
        navigator.navigate(BeaconsListNavigation(BeaconDetailsSpec().toBundle { specRow ->
            when (specRow) {
                is BeaconDetailsSpecRow.UUIDRow -> specRow.convertValue("fix me")
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

    public override fun onCleared() { super.onCleared() }
}
