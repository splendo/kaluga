/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.bluetooth.scanner

import co.touchlab.stately.collections.sharedMutableListOf
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.EnableSensorAction
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateFlowRepo
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import com.splendo.kaluga.test.bluetooth.MockBluetoothMonitor
import kotlinx.coroutines.flow.MutableStateFlow

class MockScanner(
    initialBluetoothEnabled: Boolean,
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermissions: Boolean,
    autoEnableBluetooth: Boolean,
    stateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
    override val isSupported: Boolean = true
) : BaseScanner(
    permissions,
    connectionSettings,
    autoRequestPermissions,
    autoEnableBluetooth,
    stateRepo
) {

    class Builder(initialBluetoothEnabled: Boolean, setupMocks: Boolean = true) : BaseScanner.Builder {

        val createdScanners = sharedMutableListOf<MockScanner>()
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (permissions, connectionSettings, autoRequestPermission, autoEnableSensors, scanningStateRepo) ->
                    MockScanner(initialBluetoothEnabled, permissions, connectionSettings, autoRequestPermission, autoEnableSensors, scanningStateRepo).also {
                        createdScanners.add(it)
                    }
                }
            }
        }

        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableSensors: Boolean,
            scanningStateRepo: ScanningStateFlowRepo
        ): BaseScanner = createMock.call(permissions, connectionSettings, autoRequestPermission, autoEnableSensors, scanningStateRepo)
    }

    val isEnabled = MutableStateFlow(initialBluetoothEnabled)

    public override val bluetoothEnabledMonitor = MockBluetoothMonitor(isEnabled)

    val startMonitoringPermissionsMock = ::startMonitoringPermissions.mock()
    val stopMonitoringPermissionsMock = ::stopMonitoringPermissions.mock()
    val startMonitoringSensorsMock = ::startMonitoringSensors.mock()
    val stopMonitoringSensorsMock = ::stopMonitoringSensors.mock()
    val scanForDevicesMock = ::scanForDevices.mock()
    val stopScanningMock = ::stopScanning.mock()
    val generateEnableSensorsActionsMock = ::generateEnableSensorsActions.mock()

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsMock.call()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsMock.call()
    }

    override fun startMonitoringSensors() {
        super.startMonitoringSensors()
        startMonitoringSensorsMock.call()
    }

    override fun stopMonitoringSensors() {
        super.stopMonitoringSensors()
        stopMonitoringSensorsMock.call()
    }

    override suspend fun scanForDevices(filter: Set<UUID>): Unit = scanForDevicesMock.call(filter)

    override suspend fun stopScanning(): Unit = stopScanningMock.call()

    override fun generateEnableSensorsActions(): List<EnableSensorAction> = generateEnableSensorsActionsMock.call()
}
