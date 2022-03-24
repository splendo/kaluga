/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.scanner

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.collections.sharedMutableSetOf
import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.base.typedMap
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultCBPeripheralWrapper
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.permissions.Permissions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerOptionShowPowerAlertKey
import platform.CoreBluetooth.CBCentralManagerScanOptionAllowDuplicatesKey
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class Scanner internal constructor(
    permissions: Permissions,
    private val connectionSettings: ConnectionSettings,
    autoRequestPermission: Boolean,
    autoEnableSensors: Boolean,
    stateRepo: ScanningStateFlowRepo,
) : BaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableSensors, stateRepo) {

    class Builder : BaseScanner.Builder {

        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableSensors: Boolean,
            scanningStateRepo: ScanningStateFlowRepo,
        ): BaseScanner {
            return Scanner(permissions, connectionSettings, autoRequestPermission, autoEnableSensors, scanningStateRepo)
        }
    }

    companion object {
        private const val TAG = "IOS Bluetooth Scanner"
    }

    private class EnabledCBCentralManagerDelegate(private val isCheckEnabledCompleted: CompletableDeferred<Boolean>) : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) = mainContinuation {
            val isEnabled = central.state == CBCentralManagerStatePoweredOn
            isCheckEnabledCompleted.complete(isEnabled)
        }.invoke()
    }

    @Suppress("CONFLICTING_OVERLOADS")
    private class PoweredOnCBCentralManagerDelegate(private val scanner: Scanner, private val isEnabledCompleted: EmptyCompletableDeferred) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManagerDidUpdateState(central: CBCentralManager) = mainContinuation {
            if (central.state == CBCentralManagerStatePoweredOn) {
                isEnabledCompleted.complete()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            scanner.discoverPeripheral(central, didDiscoverPeripheral, advertisementData.typedMap(), RSSI.intValue)
        }

        fun handlePeripheral(didConnectPeripheral: CBPeripheral, block: suspend BaseDeviceConnectionManager.() -> Unit) = mainContinuation {
            scanner.stateRepo.launchUseState { scannerState ->
                if (scannerState is ScanningState.Initialized.Enabled)
                    scannerState.discovered.devices.find { it.identifier == didConnectPeripheral.identifier }
                        ?.let { device ->
                            block(device.peekState().connectionManager)
                        }
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
            handlePeripheral(didConnectPeripheral) { handleConnect() }
        }

        override fun centralManager(central: CBCentralManager, didDisconnectPeripheral: CBPeripheral, error: NSError?) {
            handlePeripheral(didDisconnectPeripheral) { handleDisconnect() }
        }

        override fun centralManager(central: CBCentralManager, didFailToConnectPeripheral: CBPeripheral, error: NSError?) {
            handlePeripheral(didFailToConnectPeripheral) { handleDisconnect() }
        }
    }

    override val isSupported: Boolean = true
    private lateinit var mainCentralManager: CBCentralManager
    private lateinit var checkEnabledCentralManager: CBCentralManager
    private val centralManagers = sharedMutableListOf<CBCentralManager>()
    private val discoveringDelegates = sharedMutableListOf<CBCentralManagerDelegateProtocol>()
    private val activeDelegates = sharedMutableSetOf<CBCentralManagerDelegateProtocol>()
    override val bluetoothEnabledMonitor: BluetoothMonitor by lazy {
        initMainManagersIfNeeded()
        BluetoothMonitor.Builder(mainCentralManager).create()
    }

    private fun initMainManagersIfNeeded() {
        if (!::mainCentralManager.isInitialized) {
            mainCentralManager = CBCentralManager(null, dispatch_get_main_queue(), emptyMap<Any?, Any>())
        }
        if (!::checkEnabledCentralManager.isInitialized) {
            checkEnabledCentralManager = CBCentralManager(null, dispatch_get_main_queue(), emptyMap<Any?, Any>())
        }
    }

    private suspend fun scan(filter: UUID? = null) {
        val centralManager = CBCentralManager(null, dispatch_get_main_queue())
        centralManagers.add(centralManager)
        val awaitPoweredOn = EmptyCompletableDeferred()
        val delegate = PoweredOnCBCentralManagerDelegate(this, awaitPoweredOn)
        discoveringDelegates.add(delegate)
        centralManager.delegate = delegate
        awaitPoweredOn.await()
        centralManager.scanForPeripheralsWithServices(filter?.let { listOf(filter) }, mapOf(CBCentralManagerScanOptionAllowDuplicatesKey to true))
    }

    override suspend fun scanForDevices(filter: Set<UUID>) =
        if (filter.isEmpty())
            scan()
        else
            filter.forEach { scan(it) }

    override suspend fun stopScanning() {
        centralManagers.forEach { centralManager ->
            if (centralManager.state == CBCentralManagerStatePoweredOn) {
                centralManager.stopScan()
            }
        }
        discoveringDelegates.clear()
        centralManagers.clear()
    }

    override fun generateEnableSensorsActions(): List<EnableSensorAction> {
        // Trigger Enable Bluetooth popup
        return listOfNotNull(
            if (!bluetoothEnabledMonitor.isServiceEnabled) {
                suspend {
                    val options =
                        mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to true)
                    CBCentralManager(null, dispatch_get_main_queue(), options)
                    bluetoothEnabledMonitor.isEnabled.first { it }
                }
            } else null
        )
    }

    private fun discoverPeripheral(central: CBCentralManager, peripheral: CBPeripheral, advertisementDataMap: Map<String, Any>, rssi: Int) {
        initMainManagersIfNeeded()

        central.delegate?.let {
            activeDelegates.add(it)
        }

        val advertisementData = AdvertisementData(advertisementDataMap)
        val deviceWrapper = DefaultCBPeripheralWrapper(peripheral)
        handleDeviceDiscovered(deviceWrapper.identifier, rssi, advertisementData) {
            val deviceInfo = DeviceInfoImpl(deviceWrapper, rssi, advertisementData)
            Device(
                connectionSettings,
                deviceInfo,
                DeviceConnectionManager.Builder(central, peripheral),
                coroutineContext
            )
        }
    }
}
