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

package com.splendo.kaluga.test.bluetooth.scanner

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.EnableSensorAction
import com.splendo.kaluga.bluetooth.scanner.Filter
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import com.splendo.kaluga.test.bluetooth.MockBluetoothMonitor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

class MockScanner(
    override val isSupported: Boolean,
    override val events: MutableSharedFlow<Scanner.Event>,
    override val connectionEvents: MutableSharedFlow<Scanner.ConnectionEvent>,
    override val discoveryEvents: MutableSharedFlow<List<Scanner.DeviceDiscovered>>,
) : Scanner {

    val startMonitoringPermissionsMock = ::startMonitoringPermissions.mock()
    override suspend fun startMonitoringPermissions(): Unit = startMonitoringPermissionsMock.call()

    val stopMonitoringPermissionsMock = ::stopMonitoringPermissions.mock()
    override suspend fun stopMonitoringPermissions(): Unit = stopMonitoringPermissionsMock.call()

    val startMonitoringHardwareEnabledMock = ::startMonitoringHardwareEnabled.mock()
    override suspend fun startMonitoringHardwareEnabled(): Unit = startMonitoringHardwareEnabledMock.call()

    val stopMonitoringHardwareEnabledMock = ::stopMonitoringHardwareEnabled.mock()
    override suspend fun stopMonitoringHardwareEnabled(): Unit = stopMonitoringHardwareEnabledMock.call()

    val isHardwareEnabledMock = ::isHardwareEnabled.mock()
    override suspend fun isHardwareEnabled(): Boolean = isHardwareEnabledMock.call()

    val requestEnableHardwareMock = ::requestEnableHardware.mock()
    override suspend fun requestEnableHardware(): Unit = requestEnableHardwareMock.call()

    val scanForDevicesMock = ::scanForDevices.mock()
    override suspend fun scanForDevices(filter: Filter, connectionSettings: ConnectionSettings?): Unit = scanForDevicesMock.call(filter, connectionSettings)

    val stopScanningMock = ::stopScanning.mock()
    override suspend fun stopScanning(): Unit = stopScanningMock.call()

    val retrievePairedDevicesMock = ::retrievePairedDevices.mock()
    override suspend fun retrievePairedDevices(withServices: Filter, removeForAllPairedFilters: Boolean, connectionSettings: ConnectionSettings?): Unit =
        retrievePairedDevicesMock.call(withServices, removeForAllPairedFilters, connectionSettings)

    val cancelRetrievingPairedDevicesMock = ::cancelRetrievingPairedDevices.mock()
    override fun cancelRetrievingPairedDevices(): Unit = cancelRetrievingPairedDevicesMock.call()
}

/**
 * Mock implementation of [BaseScanner]
 * @param initialBluetoothEnabled Sets the initial enabled state of bluetooth
 * @param settings [BaseScanner.Settings] to configure this scanner
 * @param coroutineScope The [CoroutineScope] to run this Scanner on
 * @param scanningDispatcher the [CoroutineDispatcher] to which scanning should be dispatched. It is recommended to make this a dispatcher that can handle high frequency of events
 * @param isSupported Indicates whether the system supports Bluetooth scanning
 */
class MockBaseScanner(
    initialBluetoothEnabled: Boolean,
    settings: Settings,
    coroutineScope: CoroutineScope,
    scanningDispatcher: CoroutineDispatcher,
    override val isSupported: Boolean = true,
) : BaseScanner(
    settings,
    coroutineScope,
    scanningDispatcher,
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
        val createdScanners = concurrentMutableListOf<MockBaseScanner>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (settings, coroutineContext, coroutineDispatcher) ->
                    MockBaseScanner(initialBluetoothEnabled, settings, coroutineContext, coroutineDispatcher, setupMocks).also {
                        createdScanners.add(it)
                    }
                }
            }
        }

        override fun create(settings: Settings, coroutineScope: CoroutineScope, scanningDispatcher: CoroutineDispatcher): BaseScanner =
            createMock.call(settings, coroutineScope, scanningDispatcher)
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
    val didStartScanningMock = ::didStartScanning.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopScanning]
     */
    val didStopScanningMock = ::didStopScanning.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [generateEnableSensorsActions]
     */
    val generateEnableSensorsActionsMock = ::generateEnableSensorsActions.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [pairedDevices]
     */
    val retrievePairedDeviceDiscoveredEventsMock = ::retrievePairedDeviceDiscoveredEvents.mock()

    override suspend fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsMock.call()
    }

    override suspend fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsMock.call()
    }

    override suspend fun startMonitoringHardwareEnabled() {
        super.startMonitoringHardwareEnabled()
        startMonitoringHardwareEnabledMock.call()
    }

    override suspend fun stopMonitoringHardwareEnabled() {
        super.stopMonitoringHardwareEnabled()
        stopMonitoringHardwareEnabledMock.call()
    }

    override suspend fun didStartScanning(filter: Set<UUID>): Unit = didStartScanningMock.call(filter)

    override suspend fun didStopScanning(): Unit = didStopScanningMock.call()

    override fun generateEnableSensorsActions(): List<EnableSensorAction> = generateEnableSensorsActionsMock.call()

    public override fun handleDeviceDiscovered(deviceWrapper: DeviceWrapper, rssi: RSSI, advertisementData: BaseAdvertisementData, deviceCreator: (CoroutineContext) -> Device) {
        super.handleDeviceDiscovered(deviceWrapper, rssi, advertisementData, deviceCreator)
    }

    override suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Filter, connectionSettings: ConnectionSettings?): List<Scanner.DeviceDiscovered> =
        retrievePairedDeviceDiscoveredEventsMock.call(withServices, connectionSettings)
}
