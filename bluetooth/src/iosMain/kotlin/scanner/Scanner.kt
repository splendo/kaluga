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

import com.splendo.kaluga.base.mainContinuation
import com.splendo.kaluga.base.typedMap
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceHolder
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerOptionShowPowerAlertKey
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import kotlin.device.DefaultCBPeripheralWrapper

actual class Scanner internal constructor(permissions: Permissions,
                                          private val connectionSettings: ConnectionSettings,
                                          autoRequestPermission: Boolean,
                                          autoEnableBluetooth: Boolean,
                                          stateRepo: StateRepo<ScanningState>)
    : BaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, stateRepo)  {

    class Builder() : BaseScanner.Builder {

        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableBluetooth: Boolean,
            scanningStateRepo: StateRepo<ScanningState>
        ): BaseScanner {
            return Scanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo)
        }

    }

    companion object {
        private const val TAG = "IOS Bluetooth Scanner"
    }

    private val mainCBCentralManagerDelegate = object : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) = mainContinuation {
            val isEnabled = central.state == CBCentralManagerStatePoweredOn
            if (isEnabled) bluetoothEnabled() else bluetoothDisabled()
        }.invoke()
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
            info(TAG, "Powered On Did Update State")
            if (central.state == CBCentralManagerStatePoweredOn) {
                isEnabledCompleted.complete()
            }

        }.invoke()

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            info(TAG, "Did Discover Peripheral ${didDiscoverPeripheral.identifier.UUIDString}")
            scanner.discoverPeripheral(central, didDiscoverPeripheral, advertisementData.typedMap(), RSSI.intValue)
        }

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) = mainContinuation {
            info(TAG, "Did Connect Peripheral ${didConnectPeripheral.identifier.UUIDString}")
            val connectionManager = scanner.connectionManagerMap[didConnectPeripheral.identifier] ?: return@mainContinuation
            scanner.launch {
                connectionManager.handleConnect()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didDisconnectPeripheral: CBPeripheral, error: NSError?) = mainContinuation {
            info(TAG, "Did Disconnect Peripheral ${didDisconnectPeripheral.identifier.UUIDString}")
            val connectionManager = scanner.connectionManagerMap[didDisconnectPeripheral.identifier] ?: return@mainContinuation
            scanner.launch {
                connectionManager.handleDisconnect()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didFailToConnectPeripheral: CBPeripheral, error: NSError?) = mainContinuation {
            info(TAG, "Did Fail to Connect to Peripheral ${didFailToConnectPeripheral.identifier.UUIDString}")
            val connectionManager = scanner.connectionManagerMap[didFailToConnectPeripheral.identifier] ?: return@mainContinuation
            scanner.launch {
                connectionManager.handleDisconnect()
            }
        }.invoke()
        
    }

    private lateinit var mainCentralManager: CBCentralManager
    private lateinit var checkEnabledCentralManager: CBCentralManager
    private val centralManagers = emptyList<CBCentralManager>().toMutableList()
    private var connectionManagerMap = emptyMap<Identifier, BaseDeviceConnectionManager>().toMutableMap()
    private val discoveringDelegates = mutableListOf<CBCentralManagerDelegateProtocol>()
    private val activeDelegates = mutableSetOf<CBCentralManagerDelegateProtocol>()

    private fun initMainManagers() {
        if (!::mainCentralManager.isInitialized) {
            mainCentralManager = CBCentralManager(null, dispatch_get_main_queue(), emptyMap<Any?, Any>())
        }
        if (!::checkEnabledCentralManager.isInitialized) {
            checkEnabledCentralManager = CBCentralManager(null, dispatch_get_main_queue(), emptyMap<Any?, Any>())
        }
    }

    override suspend fun scanForDevices(filter: Set<UUID>) {
        connectionManagerMap.clear()
        info(TAG, "Scan For Devices")
        if (filter.isEmpty()) {
            val centralManager = CBCentralManager(null, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            val awaitPoweredOn = EmptyCompletableDeferred()
            val delegate = PoweredOnCBCentralManagerDelegate( this, awaitPoweredOn)
            discoveringDelegates.add(delegate)
            centralManager.delegate = delegate
            awaitPoweredOn.await()
            centralManager.scanForPeripheralsWithServices(null, null)
        }

        filter.forEach {
            val centralManager = CBCentralManager(null, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            val awaitPoweredOn = EmptyCompletableDeferred()
            val delegate = PoweredOnCBCentralManagerDelegate( this, awaitPoweredOn)
            discoveringDelegates.add(delegate)
            centralManager.delegate = delegate
            awaitPoweredOn.await()
            centralManager.scanForPeripheralsWithServices(listOf(it), null)
        }
    }

    override suspend fun stopScanning() {
        info(TAG, "Stop Scanning")
        centralManagers.forEach {centralManager ->
            if (centralManager.state == CBCentralManagerStatePoweredOn) {
                centralManager.stopScan()
            }
        }
        discoveringDelegates.clear()
        centralManagers.clear()
    }

    override fun startMonitoringBluetooth() {
        initMainManagers()
        discoveringDelegates.clear()
        activeDelegates.clear()
        connectionManagerMap.clear()
        mainCentralManager.delegate = mainCBCentralManagerDelegate
    }

    override fun stopMonitoringBluetooth() {
        initMainManagers()
        discoveringDelegates.clear()
        activeDelegates.clear()
        connectionManagerMap.clear()
        mainCentralManager.delegate = null
    }

    override suspend fun isBluetoothEnabled(): Boolean {
        initMainManagers()
        val completable = CompletableDeferred<Boolean>()
        val delegate = EnabledCBCentralManagerDelegate(completable)
        checkEnabledCentralManager.delegate = delegate
        return completable.await().also {
            checkEnabledCentralManager.delegate = null
        }
    }

    override suspend fun requestBluetoothEnable() {
        // Trigger Enable Bluetooth popup
        val options = mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to autoEnableBluetooth)
        CBCentralManager(null, dispatch_get_main_queue(), options)
    }

    private fun discoverPeripheral(central: CBCentralManager, peripheral: CBPeripheral, advertisementDataMap: Map<String, Any>, rssi: Int) {
        initMainManagers()

        central.delegate?.let {
            activeDelegates.add(it)
        }

        val advertisementData = AdvertisementData(advertisementDataMap)
        val deviceHolder = DeviceHolder(DefaultCBPeripheralWrapper(peripheral))
        handleDeviceDiscovered(deviceHolder.identifier, advertisementData) {
            val deviceInfo = DeviceInfoImpl(deviceHolder, rssi, advertisementData)
            Device(connectionSettings, deviceInfo, DeviceConnectionManager.Builder(central, peripheral)).also {
                connectionManagerMap[it.identifier] = it.deviceConnectionManager
            }
        }
    }


}