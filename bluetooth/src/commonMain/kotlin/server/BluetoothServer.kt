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
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.permissions.base.Permissions
import kotlinx.coroutines.flow.StateFlow

interface BluetoothServerDSL {
    fun advertise(data: AdvertisementDataBuilder.() -> Unit)
    fun service(uuid: UUID, service: LocalServiceDSL.Primary.() -> Unit)
}

enum class ServerState {
    UNAVAILABLE,
    AWAITING_PERMISSIONS,
    AWAITING_BLUETOOTH_ENABLED,
    AVAILABLE,
    CLOSED,
}

data class ServerSettings(
    val permissions: Permissions,
    val autoRequestPermission: Boolean = true,
    val autoEnableBluetooth: Boolean = true,
    val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
)

expect class BluetoothServer : AutoCloseable {

    val state: StateFlow<ServerState>

    val isAdvertising: StateFlow<Boolean>
    val services: StateFlow<List<LocalService>>

    suspend fun advertise(data: AdvertisementDataBuilder.() -> Unit): Boolean
    fun stopAdvertising()

    suspend fun add(uuid: UUID, service: LocalServiceDSL.Primary.() -> Unit): LocalService?
    fun remove(service: LocalService)
    fun removeAllServices()
    override fun close()
}

interface AdvertisementDataBuilder {
    var localName: String?
    fun serviceUUIDs(vararg uuid: UUID)
}

expect class ConnectedDevice : Device {
    override val identifier: Identifier
}
