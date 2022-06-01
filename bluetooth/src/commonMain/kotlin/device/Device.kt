/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.state.HotStateFlowRepo
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

typealias DeviceStateFlowRepo = StateRepo<DeviceState, MutableStateFlow<DeviceState>>

interface Device {
    val identifier: Identifier
    val info: Flow<DeviceInfo>
    val state: Flow<DeviceState>
    val mtu: Flow<Int>

    suspend fun connect(): Boolean
    suspend fun disconnect()
    fun rssiDidUpdate(rssi: Int)
    fun advertisementDataAndRssiDidUpdate(advertisementData: BaseAdvertisementData, rssi: Int)
}

class DeviceImpl(
    override val identifier: Identifier,
    initialDeviceInfo: DeviceInfoImpl,
    private val connectionSettings: ConnectionSettings,
    private val connectionManager: BaseDeviceConnectionManager,
    private val coroutineScope: CoroutineScope,
    private val createDeviceStateFlow: (BaseDeviceConnectionManager, CoroutineScope) -> DeviceStateFlowRepo
) : Device, CoroutineScope by coroutineScope {

    private val sharedInfo = MutableStateFlow(initialDeviceInfo)
    private val deviceStateRepo = MutableStateFlow<DeviceStateFlowRepo?>(null)
    override val info: Flow<DeviceInfo> = sharedInfo.asStateFlow()
    override val state: Flow<DeviceState> = deviceStateRepo.flatMapLatest { it ?: flowOf(DeviceStateImpl.Disconnected(connectionManager)) }
    override val mtu: Flow<Int> = connectionManager.mtuFlow

    init {
        launch {
            sharedInfo.map { it.advertisementData.isConnectable }.distinctUntilChanged().filterNot { it }.collect {
                connectionManager.disconnect()
                deviceStateRepo.value = null
            }
        }
    }

    override suspend fun connect(): Boolean = createDeviceStateRepoIfNotExisting()?.let {
        true
    } ?: false

    override suspend fun disconnect() {
        deviceStateRepo.value?.let {

        }
    }

    override fun rssiDidUpdate(rssi: Int) {
        sharedInfo.value = sharedInfo.value.copy(rssi = rssi)
    }

    override fun advertisementDataAndRssiDidUpdate(advertisementData: BaseAdvertisementData, rssi: Int) {
        sharedInfo.value = sharedInfo.value.copy(rssi = rssi, advertisementData = advertisementData)
    }

    private fun createDeviceStateRepoIfNotExisting(): DeviceStateFlowRepo? = deviceStateRepo.getAndUpdate { repo ->
        repo ?: if (sharedInfo.value.advertisementData.isConnectable) createDeviceStateFlow(connectionManager, coroutineScope) else null
    }
}
