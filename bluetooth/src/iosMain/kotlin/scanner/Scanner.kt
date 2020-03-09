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
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CompletableDeferred
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

    @Suppress("CONFLICTING_OVERLOADS")
    private val centralManagerDelegate = object : NSObject(), CBCentralManagerDelegateProtocol {

        internal var isCheckEnabledCompleted: CompletableDeferred<Boolean>? = null

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            discoverPeripheral(central, didDiscoverPeripheral, advertisementData.typedMap(), RSSI.intValue)
        }

        override fun centralManagerDidUpdateState(central: CBCentralManager) = mainContinuation {
            val isEnabled = central.state == CBCentralManagerStatePoweredOn
            if (central == checkEnabledCentralManager)
                isCheckEnabledCompleted?.complete(isEnabled)
            else if (central == mainCentralManager) {
                if (isEnabled) bluetoothEnabled() else bluetoothDisabled()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) = mainContinuation {
            val connectionManager = connectionManagerMap[didConnectPeripheral.identifier] ?: return@mainContinuation
            launch {
                connectionManager.handleConnect()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didDisconnectPeripheral: CBPeripheral, error: NSError?) = mainContinuation {
            val connectionManager = connectionManagerMap[didDisconnectPeripheral.identifier] ?: return@mainContinuation
            launch {
                connectionManager.handleDisconnect()
            }
        }.invoke()

        override fun centralManager(central: CBCentralManager, didFailToConnectPeripheral: CBPeripheral, error: NSError?) = mainContinuation {
            val connectionManager = connectionManagerMap[didFailToConnectPeripheral.identifier] ?: return@mainContinuation
            launch {
                connectionManager.handleDisconnect()
            }
        }.invoke()
    }

    private val mainCentralManager: CBCentralManager
    private val checkEnabledCentralManager: CBCentralManager
    private val centralManagers = emptyList<CBCentralManager>().toMutableList()
    private var connectionManagerMap = emptyMap<Identifier, BaseDeviceConnectionManager>().toMutableMap()

    init {
        val options = mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to autoEnableBluetooth)
        mainCentralManager = CBCentralManager(null, dispatch_get_main_queue(), options)
        checkEnabledCentralManager = CBCentralManager(null, dispatch_get_main_queue(), emptyMap<Any?, Any>())
    }

    override fun scanForDevices(filter: Set<UUID>) {
        connectionManagerMap.clear()

        if (filter.isEmpty()) {
            val centralManager = CBCentralManager(centralManagerDelegate, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            centralManager.scanForPeripheralsWithServices(null, null)
        }

        filter.forEach {
            val centralManager = CBCentralManager(centralManagerDelegate, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            centralManager.scanForPeripheralsWithServices(listOf(it), null)
        }
    }

    override fun stopScanning() {
        centralManagers.forEach {
            when(it.state) {
                CBCentralManagerStatePoweredOn -> it.stopScan()
            }
        }
        centralManagers.clear()
    }

    override fun startMonitoringBluetooth() {
        connectionManagerMap.clear()
        mainCentralManager.delegate = centralManagerDelegate
    }

    override fun stopMonitoringBluetooth() {
        connectionManagerMap.clear()
        mainCentralManager.delegate = null
    }

    override suspend fun isBluetoothEnabled(): Boolean {
        val completable = CompletableDeferred<Boolean>()
        centralManagerDelegate.isCheckEnabledCompleted = completable
        checkEnabledCentralManager.delegate = centralManagerDelegate
        return completable.await().also {
            checkEnabledCentralManager.delegate = null
            centralManagerDelegate.isCheckEnabledCompleted = null
        }
    }

    override suspend fun requestBluetoothEnable() {
        // No access to UIApplication.openSettingsURLString
        // We have to fallback to alert then?
    }

    private fun discoverPeripheral(central: CBCentralManager, peripheral: CBPeripheral, advertisementDataMap: Map<String, Any>, rssi: Int) {
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