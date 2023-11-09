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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.base.utils.typedMap
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultCBPeripheralWrapper
import com.splendo.kaluga.bluetooth.device.DefaultDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.PairedAdvertisementData
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner.ScanSettings
import kotlinx.coroutines.CoroutineDispatcher
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

/**
 * A default implementation of [BaseScanner]
 * @param settings the [BaseScanner.Settings] to configure this scanner
 * @param scanSettings the [ScanSettings] to configure this scanner
 * @param coroutineScope the [CoroutineScope] this scanner runs on
 * @param scanningDispatcher the [CoroutineDispatcher] to which scanning should be dispatched. It is recommended to make this a dispatcher that can handle high frequency of events
 */
actual class DefaultScanner internal constructor(
    settings: Settings,
    private val scanSettings: ScanSettings,
    coroutineScope: CoroutineScope,
    scanningDispatcher: CoroutineDispatcher = com.splendo.kaluga.bluetooth.scanner.scanningDispatcher,
) : BaseScanner(settings, coroutineScope, scanningDispatcher) {

    /**
     * Builder for creating a [DefaultScanner]
     * @param scanSettings the [ScanSettings] to configure the scanner
     */
    class Builder(private val scanSettings: ScanSettings = ScanSettings.defaultScanOptions) : BaseScanner.Builder {

        override fun create(settings: Settings, coroutineScope: CoroutineScope, scanningDispatcher: CoroutineDispatcher): BaseScanner {
            return DefaultScanner(settings, scanSettings, coroutineScope, scanningDispatcher)
        }
    }

    /**
     * Settings to configure a [DefaultScanner]
     * @param allowDuplicateKeys if `true` a new discovery event will be sent each time advertisement data is received. Otherwise multiple discoveries will be grouped into a single discovery event
     * @param solicitedServiceUUIDsKey when not empty the scanner will also scan for peripherals soliciting any services matching the [UUID]
     */
    class ScanSettings private constructor(
        private val allowDuplicateKeys: Boolean,
        private val solicitedServiceUUIDsKey: List<UUID>?,
    ) {

        companion object {
            internal val defaultScanOptions = Builder().build()
        }

        internal fun parse(): Map<Any?, *> = buildMap {
            this[CBCentralManagerScanOptionAllowDuplicatesKey] = allowDuplicateKeys
            if (solicitedServiceUUIDsKey?.isNotEmpty() == true) {
                this[CBCentralManagerScanOptionSolicitedServiceUUIDsKey] = solicitedServiceUUIDsKey
            }
        }

        /**
         * Builder for creating [ScanSettings]
         */
        class Builder {

            // https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionallowduplicateskey
            private var allowDuplicateKeys: Boolean = true

            // https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionsolicitedserviceuuidskey
            private var solicitedServiceUUIDsKey: List<UUID>? = null

            /**
             * Sets whether a new discovery event will be sent each time advertisement data is received.
             * @param allow if `true` a new discovery event will be sent each time advertisement data is received. Otherwise multiple discoveries will be grouped into a single discovery event
             * @return the [Builder]
             */
            fun allowDuplicateKeys(allow: Boolean) = apply { allowDuplicateKeys = allow }

            /**
             * Sets the list of [UUID] corresponding with services the scanner will also scan for
             * @param keys when not empty the scanner will also scan for peripherals soliciting any services matching the [UUID]
             * @return the [Builder]
             */
            fun solicitedServiceUUIDsKey(keys: List<UUID>) = apply { solicitedServiceUUIDsKey = keys }

            /**
             * Creates [ScanSettings]
             * @return the created [ScanSettings]
             */
            fun build(): ScanSettings = ScanSettings(allowDuplicateKeys, solicitedServiceUUIDsKey)
        }
    }

    @Suppress("CONFLICTING_OVERLOADS")
    private class PoweredOnCBCentralManagerDelegate(
        private val scanner: DefaultScanner,
        private val isEnabledCompleted: EmptyCompletableDeferred,
    ) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            if (central.state == CBCentralManagerStatePoweredOn) {
                isEnabledCompleted.complete()
            }
        }

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            scanner.discoverPeripheral(
                central,
                didDiscoverPeripheral,
                advertisementData.typedMap(),
                RSSI.intValue,
            )
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
    override val isSupported: Boolean = true
    override val bluetoothEnabledMonitor: BluetoothMonitor = BluetoothMonitor.Builder {
        CBCentralManager(null, enabledQueue, emptyMap<Any?, Any>())
    }.create()

    private var centralManager: CBCentralManager? = null
    private var centralManagerDelegate: CBCentralManagerDelegateProtocol? = null
    private val centralManagerMutex = Mutex()

    private suspend fun getOrCreateCentralManager(): CBCentralManager {
        return centralManager ?: centralManagerMutex.withLock {
            centralManager ?: createCentralManager().also {
                centralManager = it
            }
        }
    }

    private suspend fun createCentralManager(): CBCentralManager {
        val awaitPoweredOn = EmptyCompletableDeferred()
        val delegate = PoweredOnCBCentralManagerDelegate(this, awaitPoweredOn)
        centralManagerDelegate = delegate
        val manager = CBCentralManager(delegate, scanQueue)
        awaitPoweredOn.await()
        return manager
    }

    private suspend fun scan(filter: Filter) {
        val centralManager = getOrCreateCentralManager()
        centralManager.scanForPeripheralsWithServices(
            filter.takeIf { it.isNotEmpty() }?.toList(),
            scanSettings.parse(),
        )
    }

    override suspend fun didStartScanning(filter: Filter) {
        scan(filter)
    }

    override suspend fun didStopScanning() {
        getOrCreateCentralManager().stopScan()
    }

    override fun generateEnableSensorsActions(): List<EnableSensorAction> {
        // Trigger Enable Bluetooth popup
        return listOfNotNull(
            if (!bluetoothEnabledMonitor.isServiceEnabled) {
                suspend {
                    // We want it to ask for permissions when the state machine requests it but NOT if the scanning starts
                    val options = mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to true)
                    CBCentralManager(null, scanQueue, options)
                    bluetoothEnabledMonitor.isEnabled.first { it }
                }
            } else {
                null
            },
        )
    }

    override suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Filter, connectionSettings: ConnectionSettings?): List<Scanner.DeviceDiscovered> {
        val centralManager = getOrCreateCentralManager()
        return centralManager
            .retrieveConnectedPeripheralsWithServices(withServices.toList())
            .filterIsInstance<CBPeripheral>()
            .map { peripheral ->
                val deviceWrapper = DefaultCBPeripheralWrapper(peripheral)
                val serviceUUIDs: List<UUID> = peripheral.services
                    ?.filterIsInstance<CBService>()
                    ?.map { it.UUID }
                    ?: withServices.toList() // fallback to filter, as it *must* contain one of them
                val advertisementData = PairedAdvertisementData(deviceWrapper.name, serviceUUIDs)

                Scanner.DeviceDiscovered(
                    identifier = deviceWrapper.identifier,
                    rssi = Int.MIN_VALUE,
                    advertisementData = advertisementData,
                    deviceCreator = getDeviceBuilder(
                        deviceWrapper,
                        Int.MIN_VALUE,
                        advertisementData,
                        DefaultDeviceConnectionManager.Builder(
                            centralManager,
                            peripheral,
                        ),
                        connectionSettings,
                    ),
                )
            }
    }

    private fun discoverPeripheral(central: CBCentralManager, peripheral: CBPeripheral, advertisementDataMap: Map<String, Any>, rssi: Int) {
        val advertisementData = AdvertisementData(advertisementDataMap)
        val deviceWrapper = DefaultCBPeripheralWrapper(peripheral)
        handleDeviceDiscovered(deviceWrapper, rssi, advertisementData, DefaultDeviceConnectionManager.Builder(central, peripheral))
    }
}
