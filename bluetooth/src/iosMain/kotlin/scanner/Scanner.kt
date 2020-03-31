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
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreBluetooth.*
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

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

    private class MainCBCentralManagerDelegate(private val scanner: Scanner) : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) = mainContinuation {
            val isEnabled = central.state == CBCentralManagerStatePoweredOn
            if (isEnabled) scanner.bluetoothEnabled() else scanner.bluetoothDisabled()
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
            info("IOS Scanner", "Powered On Did Update State")
            if (central.state == CBCentralManagerStatePoweredOn) {
                isEnabledCompleted.complete()
            }

        }.invoke()

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            info("IOS Scanner", "Did Discover")
            scanner.discoverPeripheral(central, didDiscoverPeripheral, advertisementData.typedMap(), RSSI.intValue)
        }

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) = mainContinuation {
            info("IOS Scanner", "Did Connect Peripheral")
            val connectionManager = scanner.connectionManagerMap[didConnectPeripheral.identifier] ?: return@mainContinuation
            scanner.launch {
                connectionManager.handleConnect()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didDisconnectPeripheral: CBPeripheral, error: NSError?) = mainContinuation {
            info("IOS Scanner", "Did Disconnect Peripheral")
            val connectionManager = scanner.connectionManagerMap[didDisconnectPeripheral.identifier] ?: return@mainContinuation
            scanner.launch {
                connectionManager.handleDisconnect()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didFailToConnectPeripheral: CBPeripheral, error: NSError?) = mainContinuation {
            info("IOS Scanner", "Did Fail to Connect to Peripheral")
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
        
        if (filter.isEmpty()) {
            info("IOS Scanner", "Start Scanning Empty Filter")
            val centralManager = CBCentralManager(null, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            val awaitPoweredOn = EmptyCompletableDeferred()
            centralManager.delegate = PoweredOnCBCentralManagerDelegate(this, awaitPoweredOn)
            awaitPoweredOn.await()
            info("IOS Scanner", "Start Scanning Powered On")
            centralManager.scanForPeripheralsWithServices(null, null)
        }

        filter.forEach {
            val centralManager = CBCentralManager(null, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            val awaitPoweredOn = EmptyCompletableDeferred()
            centralManager.delegate = PoweredOnCBCentralManagerDelegate(this, awaitPoweredOn)
            awaitPoweredOn.await()
            info("IOS Scanner", "Start Scanning Filter: $it Powered On")
            centralManager.scanForPeripheralsWithServices(listOf(it), null)
        }
    }

    override suspend fun stopScanning() {
        centralManagers.forEach {centralManager ->
            val awaitPoweredOn = EmptyCompletableDeferred()
            centralManager.delegate = PoweredOnCBCentralManagerDelegate(this, awaitPoweredOn)
            awaitPoweredOn.await()
            centralManager.stopScan()
        }
        centralManagers.clear()
    }

    override fun startMonitoringBluetooth() {
        initMainManagers()
        connectionManagerMap.clear()
        mainCentralManager.delegate = MainCBCentralManagerDelegate(this)
    }

    override fun stopMonitoringBluetooth() {
        initMainManagers()
        connectionManagerMap.clear()
        mainCentralManager.delegate = null
    }

    override suspend fun isBluetoothEnabled(): Boolean {
        initMainManagers()
        val completable = CompletableDeferred<Boolean>()
        checkEnabledCentralManager.delegate = EnabledCBCentralManagerDelegate(completable)
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
        if (central == mainCentralManager || central == checkEnabledCentralManager)
            return

        val advertisementData = AdvertisementData(advertisementDataMap)
        val deviceHolder = DeviceHolder(peripheral, central)
        handleDeviceDiscovered(deviceHolder.identifier, advertisementData) {
            val deviceInfo = DeviceInfoImpl(deviceHolder, rssi, advertisementData)
            Device(connectionSettings, deviceInfo, DeviceConnectionManager.Builder(central)).also {
                connectionManagerMap[it.identifier] = it.deviceConnectionManager
            }
        }
    }


}