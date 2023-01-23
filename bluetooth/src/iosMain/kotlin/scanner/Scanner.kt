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

import com.splendo.kaluga.base.collections.concurrentMutableSetOf
import com.splendo.kaluga.base.utils.typedMap
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.DefaultCBPeripheralWrapper
import com.splendo.kaluga.bluetooth.device.DefaultDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.PairedAdvertisementData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerOptionShowPowerAlertKey
import platform.CoreBluetooth.CBCentralManagerScanOptionAllowDuplicatesKey
import platform.CoreBluetooth.CBCentralManagerScanOptionSolicitedServiceUUIDsKey
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_queue_create

actual class DefaultScanner internal constructor(
    settings: Settings,
    private val scanSettings: ScanSettings,
    coroutineScope: CoroutineScope
) : BaseScanner(settings, coroutineScope) {

    class Builder(private val scanSettings: ScanSettings = defaultScanOptions) : BaseScanner.Builder {

        override fun create(
            settings: Settings,
            coroutineScope: CoroutineScope,
        ): BaseScanner {
            return DefaultScanner(settings, scanSettings, coroutineScope)
        }
    }

    companion object {
        private val defaultScanOptions = ScanSettings.Builder().build()
    }

    @Suppress("CONFLICTING_OVERLOADS")
    private class PoweredOnCBCentralManagerDelegate(private val scanner: DefaultScanner, private val isEnabledCompleted: EmptyCompletableDeferred) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            if (central.state == CBCentralManagerStatePoweredOn) {
                isEnabledCompleted.complete()
            }
        }

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            scanner.discoverPeripheral(central, didDiscoverPeripheral, advertisementData.typedMap(), RSSI.intValue)
        }

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
            scanner.handleDeviceConnected(didConnectPeripheral.identifier)
        }

        override fun centralManager(central: CBCentralManager, didDisconnectPeripheral: CBPeripheral, error: NSError?) {
            scanner.handleDeviceDisconnected(didDisconnectPeripheral.identifier)
        }

        override fun centralManager(central: CBCentralManager, didFailToConnectPeripheral: CBPeripheral, error: NSError?) {
            scanner.handleDeviceDisconnected(didFailToConnectPeripheral.identifier)
        }
    }

    private val enabledQueue = dispatch_queue_create("ScannerMonitorEnabled", null)
    private val scanQueue = dispatch_queue_create("ScannerScanning", null)
    private val pairedDevicesQueue = dispatch_queue_create("ScannerPairedDevices", null)
    override val isSupported: Boolean = true
    private val centralManagersLock = Mutex()
    private val centralManagers = mutableListOf<CBCentralManager>()
    private val discoveringDelegates = mutableListOf<CBCentralManagerDelegateProtocol>()
    private val activeDelegates = concurrentMutableSetOf<CBCentralManagerDelegateProtocol>()
    override val bluetoothEnabledMonitor: BluetoothMonitor = BluetoothMonitor.Builder { CBCentralManager(null, enabledQueue, emptyMap<Any?, Any>()) }.create()

    private suspend fun scan(filter: UUID? = null) {
        centralManagersLock.withLock {
            val centralManager = CBCentralManager(null, scanQueue)
            centralManagers.add(centralManager)

            val awaitPoweredOn = EmptyCompletableDeferred()
            val delegate = PoweredOnCBCentralManagerDelegate(this, awaitPoweredOn)
            discoveringDelegates.add(delegate)
            centralManager.delegate = delegate
            awaitPoweredOn.await()
            centralManager.scanForPeripheralsWithServices(
                filter?.let { listOf(filter) },
                scanSettings.parse()
            )
        }
    }

    override suspend fun didStartScanning(filter: Set<UUID>) {
        if (filter.isEmpty()) {
            scan()
        } else {
            filter.forEach { scan(it) }
        }
    }

    override suspend fun didStopScanning() {
        centralManagersLock.withLock {
            centralManagers.forEach { centralManager ->
                if (centralManager.state == CBCentralManagerStatePoweredOn) {
                    centralManager.stopScan()
                }
            }
            discoveringDelegates.clear()
            centralManagers.clear()
        }
    }

    override fun generateEnableSensorsActions(): List<EnableSensorAction> {
        // Trigger Enable Bluetooth popup
        return listOfNotNull(
            if (!bluetoothEnabledMonitor.isServiceEnabled) {
                suspend {
                    val options =
                        mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to true)
                    CBCentralManager(null, enabledQueue, options)
                    bluetoothEnabledMonitor.isEnabled.first { it }
                }
            } else null
        )
    }

    override suspend fun retrievePairedDevices(withServices: Set<UUID>) {
        val awaitPoweredOn = EmptyCompletableDeferred()
        val delegate = PoweredOnCBCentralManagerDelegate(this, awaitPoweredOn)
        val centralManager = CBCentralManager(delegate, pairedDevicesQueue)
        awaitPoweredOn.await()
        val devices = centralManager
            .retrieveConnectedPeripheralsWithServices(withServices.toList())
            .filterIsInstance<CBPeripheral>()
            .map { peripheral ->
                activeDelegates.add(delegate)
                val deviceWrapper = DefaultCBPeripheralWrapper(peripheral)
                val deviceCreator: DeviceCreator = {
                    deviceWrapper to DefaultDeviceConnectionManager.Builder(centralManager, peripheral)
                }
                val serviceUUIDs: List<UUID> = peripheral.services
                    ?.filterIsInstance<CBService>()
                    ?.map { it.UUID }
                    ?: withServices.toList() // fallback to filter, as it *must* contain one of them

                Scanner.Event.DeviceDiscovered(
                    identifier = deviceWrapper.identifier,
                    rssi = Int.MIN_VALUE,
                    advertisementData = PairedAdvertisementData(deviceWrapper.name, serviceUUIDs),
                    deviceCreator = deviceCreator
                )
            }
        // We have to call even with empty list to clean up cached devices
        handlePairedDevices(withServices, devices)
    }

    private fun discoverPeripheral(central: CBCentralManager, peripheral: CBPeripheral, advertisementDataMap: Map<String, Any>, rssi: Int) {
        central.delegate?.let {
            activeDelegates.add(it)
        }

        val advertisementData = AdvertisementData(advertisementDataMap)
        val deviceWrapper = DefaultCBPeripheralWrapper(peripheral)
        handleDeviceDiscovered(deviceWrapper.identifier, rssi, advertisementData) {
            deviceWrapper to DefaultDeviceConnectionManager.Builder(central, peripheral)
        }
    }

    class ScanSettings private constructor(
        private val allowDuplicateKeys: Boolean,
        private val solicitedServiceUUIDsKey: List<UUID>?
    ) {

        fun parse(): Map<Any?, *> {
            val result: MutableMap<String, Any> =
                mutableMapOf(CBCentralManagerScanOptionAllowDuplicatesKey to allowDuplicateKeys)
            solicitedServiceUUIDsKey?.let {
                result[CBCentralManagerScanOptionSolicitedServiceUUIDsKey] = it
            }
            return result.toMap()
        }

        data class Builder(
            // https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionallowduplicateskey
            var allowDuplicateKeys: Boolean = true,
            // https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionsolicitedserviceuuidskey
            var solicitedServiceUUIDsKey: List<UUID>? = null,
        ) {
            fun allowDuplicateKeys(allow: Boolean) =
                apply { allowDuplicateKeys = allow }

            fun solicitedServiceUUIDsKey(keys: List<UUID>) =
                apply { solicitedServiceUUIDsKey = keys }

            fun build(): ScanSettings = ScanSettings(allowDuplicateKeys, solicitedServiceUUIDsKey)
        }
    }
}
