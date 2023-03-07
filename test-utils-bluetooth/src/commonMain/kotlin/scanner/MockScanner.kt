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
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.EnableSensorAction
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import com.splendo.kaluga.test.bluetooth.MockBluetoothMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class MockScanner(
    override val isSupported: Boolean,
    override val events: MutableSharedFlow<Scanner.Event>,
    override val connectionEvents: MutableSharedFlow<Scanner.ConnectionEvent>,
    override val discoveryEvents: MutableSharedFlow<List<Scanner.DeviceDiscovered>>
) : Scanner {

    val startMonitoringPermissionsMock = ::startMonitoringPermissions.mock()
    override fun startMonitoringPermissions(): Unit = startMonitoringPermissionsMock.call()

    val stopMonitoringPermissionsMock = ::stopMonitoringPermissions.mock()
    override fun stopMonitoringPermissions(): Unit = stopMonitoringPermissionsMock.call()

    val startMonitoringHardwareEnabledMock = ::startMonitoringHardwareEnabled.mock()
    override fun startMonitoringHardwareEnabled(): Unit = startMonitoringHardwareEnabledMock.call()

    val stopMonitoringHardwareEnabledMock = ::stopMonitoringHardwareEnabled.mock()
    override fun stopMonitoringHardwareEnabled(): Unit = stopMonitoringHardwareEnabledMock.call()

    val isHardwareEnabledMock = ::isHardwareEnabled.mock()
    override suspend fun isHardwareEnabled(): Boolean = isHardwareEnabledMock.call()

    val requestEnableHardwareMock = ::requestEnableHardware.mock()
    override suspend fun requestEnableHardware(): Unit = requestEnableHardwareMock.call()

    val scanForDevicesMock = ::scanForDevices.mock()
    override suspend fun scanForDevices(filter: Set<UUID>): Unit = scanForDevicesMock.call(filter)

    val stopScanningMock = ::stopScanning.mock()
    override suspend fun stopScanning(): Unit = stopScanningMock.call()

    val generateEnableSensorsActionsMock = ::generateEnableSensorsActions.mock()
    override fun generateEnableSensorsActions(): List<EnableSensorAction> = generateEnableSensorsActionsMock.call()

    val retrievePairedDevicesMock = ::retrievePairedDevices.mock()
    override suspend fun retrievePairedDevices(withServices: Set<UUID>): Unit = retrievePairedDevicesMock.call(withServices)
}

/**
 * Mock implementation of [BaseScanner]
 * @param initialBluetoothEnabled Sets the initial enabled state of bluetooth
 * @param settings [BaseScanner.Settings] to configure this scanner
 * @param coroutineScope The [CoroutineScope] to run this Scanner on
 * @param setupMocks If `true` this will automatically configure the [createMock] to build [MockScanner]
 */
class MockBaseScanner(
    initialBluetoothEnabled: Boolean,
    settings: Settings,
    coroutineScope: CoroutineScope,
    override val isSupported: Boolean = true,
    setupMocks: Boolean = true
) : BaseScanner(
    settings,
    coroutineScope
) {

    /**
     * Mock implementation of [BaseScanner.Builder]
     * @param initialBluetoothEnabled Sets the initial enabled state of bluetooth
     * @param setupMocks If `true` this will automatically configure the [createMock] to build [MockScanner]
     */
    class Builder(initialBluetoothEnabled: Boolean, setupMocks: Boolean = true) : BaseScanner.Builder {

        /**
         * List of created [MockScanner]
         */
        val createdScanners = sharedMutableListOf<MockBaseScanner>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (settings, coroutineContext) ->
                    MockBaseScanner(initialBluetoothEnabled, settings, coroutineContext, setupMocks).also {
                        createdScanners.add(it)
                    }
                }
            }
        }

        override fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseScanner = createMock.call(settings, coroutineScope)
    }

    /**
     * Manages bluetooth enabled state
     */
    val isEnabled = MutableStateFlow(initialBluetoothEnabled)

    public override val bluetoothEnabledMonitor = MockBluetoothMonitor(isEnabled)

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoringPermissions]
     */
    val startMonitoringPermissionsMock = ::startMonitoringPermissions.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoringPermissions]
     */
    val stopMonitoringPermissionsMock = ::stopMonitoringPermissions.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoringHardwareEnabled]
     */
    val startMonitoringHardwareEnabledMock = ::startMonitoringHardwareEnabled.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoringHardwareEnabled]
     */
    val stopMonitoringHardwareEnabledMock = ::stopMonitoringHardwareEnabled.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [scanForDevices]
     */
    val didStartScanningMock = ::scanForDevices.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopScanning]
     */
    val didStopScanningMock = ::stopScanning.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [generateEnableSensorsActions]
     */
    val generateEnableSensorsActionsMock = ::generateEnableSensorsActions.mock()

    val pairedDeviceDiscoveredEvents = MutableSharedFlow<List<Scanner.DeviceDiscovered>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [pairedDevices]
     */
    val retrievePairedDeviceDiscoveredEventsMock = ::retrievePairedDeviceDiscoveredEvents.mock()

    init {
        if (setupMocks) {
            retrievePairedDeviceDiscoveredEventsMock.on().doExecuteSuspended {
                pairedDeviceDiscoveredEvents.first()
            }
        }
    }

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsMock.call()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsMock.call()
    }

    override fun startMonitoringHardwareEnabled() {
        super.startMonitoringHardwareEnabled()
        startMonitoringHardwareEnabledMock.call()
    }

    override fun stopMonitoringHardwareEnabled() {
        super.stopMonitoringHardwareEnabled()
        stopMonitoringHardwareEnabledMock.call()
    }

    override suspend fun didStartScanning(filter: Set<UUID>): Unit = didStartScanningMock.call(filter)

    override suspend fun didStopScanning(): Unit = didStopScanningMock.call()

    override fun generateEnableSensorsActions(): List<EnableSensorAction> = generateEnableSensorsActionsMock.call()

    override suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Set<UUID>): List<Scanner.DeviceDiscovered> = retrievePairedDeviceDiscoveredEventsMock.call(withServices)
}
