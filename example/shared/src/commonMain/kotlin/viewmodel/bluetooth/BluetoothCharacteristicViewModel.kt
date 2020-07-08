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

import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.Identifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BluetoothCharacteristicViewModel(private val bluetooth: Bluetooth, private val deviceIdentifier: Identifier, private val serviceUUID: UUID, private val characteristicUUID: UUID) : BaseViewModel() {

    private val characteristic: Flow<Characteristic?> get() = bluetooth.devices()[deviceIdentifier].services()[serviceUUID].characteristics()[characteristicUUID]

    val uuid = characteristicUUID.uuidString
    val value = characteristic.value().map { it?.toHexString() ?: "" }.toObservable(coroutineScope)

    private val _descriptors = ConflatedBroadcastChannel<List<BluetoothDescriptorViewModel>>()
    val descriptors = _descriptors.toObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch { characteristic.flatMapLatest { characteristic -> characteristic?.let { flowOf(it) } ?: emptyFlow() }.first().readValue() }
        scope.launch {
            characteristic.descriptors().map { descriptors -> descriptors.map { BluetoothDescriptorViewModel(bluetooth, deviceIdentifier, serviceUUID, characteristicUUID, it.uuid) } }.collect {
                clearDescriptors()
                _descriptors.send(it)
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
        clearDescriptors()
    }

    private fun clearDescriptors() {
        _descriptors.valueOrNull?.forEach { it.onCleared() }
    }
}
