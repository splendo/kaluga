/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import kotlinx.coroutines.flow.StateFlow


interface BluetoothServerDSL {
    fun advertise(data: AdvertisementDataBuilder.() -> Unit)
    fun service(uuid: UUID, service: LocalServiceDSL.Primary.() -> Unit)
}

expect class BluetoothServer : AutoCloseable {

    val isAdvertising: StateFlow<Boolean>
    val services: List<LocalService>

    suspend fun advertise(data: AdvertisementDataBuilder.() -> Unit): Boolean
    fun stopAdvertising()

    suspend fun add(uuid: UUID, service: LocalServiceDSL.Primary.() -> Unit): LocalService?
    fun remove(service: LocalService)
    fun removeAllServices()
}

interface AdvertisementDataBuilder {
    var localName: String?
    fun serviceUUIDs(vararg uuid: UUID)
}

expect class ConnectedDevice : Device
