/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.scanner.Filter
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class MockBluetoothService(
    val discoveredDevicesFlow: MutableStateFlow<List<Device>> = MutableStateFlow(emptyList()),
    val filteredDevicesFlow: MutableStateFlow<Map<Filter, List<Device>>> = MutableStateFlow(emptyMap()),
    val pairedDevicesFlow: MutableStateFlow<Map<Set<UUID>, List<Device>>> = MutableStateFlow(emptyMap()),
    override val isEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true),
    setupMocks: Boolean = true,
) : BluetoothService {

    private val isScanningState = MutableStateFlow(false)

    val startScanningMock = this::startScanning.mock()
    val stopScanningMock = this::stopScanning.mock()
    val isScanningMock = this::isScanning.mock()

    val allDevicesMock = this::allDevices.mock()
    val scannedDevicesMock = this::scannedDevices.mock()
    val devicesMock = this::devices.mock()
    val pairedDevicesMock = this::pairedDevices.mock()

    var currentFilter = MutableStateFlow<Filter>(emptySet())

    init {
        if (setupMocks) {
            startScanningMock.on().doExecute {
                isScanningState.value = true
            }
            stopScanningMock.on().doExecute {
                isScanningState.value = false
            }
            allDevicesMock.on().doReturn(discoveredDevicesFlow)
            scannedDevicesMock.on().doExecute { (uuids) ->
                filteredDevicesFlow.map { it[uuids] ?: emptyList() }
            }
            devicesMock.on().doExecute { filteredDevicesFlow.combine(currentFilter) { filteredDevices, filter -> filteredDevices[filter] ?: emptyList() } }
            pairedDevicesMock.on().doExecute { (uuids) ->
                pairedDevicesFlow.map { it[uuids] ?: emptyList() }
            }
            isScanningMock.on().doReturn(isScanningState)
        }
    }

    override fun startScanning(filter: Filter, cleanMode: BluetoothService.CleanMode, connectionSettings: ConnectionSettings?) {
        currentFilter.value = filter
        startScanningMock.call(filter, cleanMode, connectionSettings)
    }

    override fun stopScanning(cleanMode: BluetoothService.CleanMode): Unit = stopScanningMock.call(cleanMode)
    override fun pairedDevices(filter: Filter, removeForAllPairedFilters: Boolean, connectionSettings: ConnectionSettings?): Flow<List<Device>> =
        pairedDevicesMock.call(filter, removeForAllPairedFilters, connectionSettings)

    override fun scannedDevices(filter: Filter): Flow<List<Device>> = scannedDevicesMock.call(filter)
    override fun allDevices(): Flow<List<Device>> = allDevicesMock.call()
    override fun devices(): Flow<List<Device>> = devicesMock.call()
    override suspend fun isScanning(): Flow<Boolean> = isScanningMock.call()
}
