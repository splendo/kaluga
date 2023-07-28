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

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toUninitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.SerializableIdentifier
import com.splendo.kaluga.bluetooth.device.serializable
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.distance
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.info
import com.splendo.kaluga.bluetooth.rssi
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.bluetooth.updateRssi
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.coroutineContext

class DeviceDetails(value: Identifier) : SingleValueNavigationAction<SerializableIdentifier>(
    value.serializable,
    NavigationBundleSpecType.SerializedType(SerializableIdentifier.serializer()),
)

class BluetoothDeviceDetailViewModel(private val identifier: Identifier) : BaseLifecycleViewModel(), KoinComponent {

    companion object {
        private const val rssi_frequency = 1000L
    }

    private val bluetooth: Bluetooth by inject()
    private val device = bluetooth.scannedDevices()[identifier]

    val name = device.info().map { it.name ?: "bluetooth_no_name".localized() }.toInitializedObservable("", coroutineScope)
    val identifierString = identifier.stringValue
    val rssi = device.rssi().map { "rssi".localized().format(it) }.toInitializedObservable("", coroutineScope)
    val distance = device.distance().map { "distance".localized().format(it) }.toInitializedObservable("", coroutineScope)
    val state = device.state().map { deviceState ->
        when (deviceState) {
            is NotConnectableDeviceState -> ""
            is ConnectableDeviceState.Disconnecting -> "bluetooth_disconneting"
            is ConnectableDeviceState.Disconnected -> "bluetooth_disconnected"
            is ConnectableDeviceState.Connected.Discovering -> "bluetooth_discovering"
            is ConnectableDeviceState.Connected -> "bluetooth_connected"
            is ConnectableDeviceState.Connecting -> "bluetooth_connecting"
        }.localized()
    }.toInitializedObservable("", coroutineScope)
    private val servicesJob = Job(coroutineScope.coroutineContext[Job])
    val services = device.services().map { services ->
        servicesJob.cancelChildren()
        services.map { BluetoothServiceViewModel(bluetooth, identifier, it.uuid, CoroutineScope(coroutineScope.coroutineContext + servicesJob)) }
    }.toInitializedObservable(emptyList(), coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            while (true) {
                device.updateRssi()
                delay(rssi_frequency)
            }
        }
    }
}
