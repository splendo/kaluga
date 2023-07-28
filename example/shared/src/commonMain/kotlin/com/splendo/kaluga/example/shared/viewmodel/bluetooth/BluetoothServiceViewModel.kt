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

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.bluetooth.uuidString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BluetoothServiceViewModel(private val bluetooth: Bluetooth, private val deviceIdentifier: Identifier, private val serviceUUID: UUID, private val coroutineScope: CoroutineScope) {

    private val service: Flow<Service?> get() = bluetooth.scannedDevices()[deviceIdentifier].services()[serviceUUID]

    val uuid = serviceUUID.uuidString
    private val characteristicsJob = Job(coroutineScope.coroutineContext[Job])
    val characteristics =  service.characteristics().map { characteristics ->
        characteristicsJob.cancelChildren()
        characteristics.map {
            BluetoothCharacteristicViewModel(bluetooth, deviceIdentifier, serviceUUID, it.uuid, CoroutineScope(coroutineScope.coroutineContext + characteristicsJob))
        }
    }.toInitializedObservable(emptyList(), coroutineScope)
}
