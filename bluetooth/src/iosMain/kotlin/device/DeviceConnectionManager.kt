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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.base.toNSData
import com.splendo.kaluga.base.typedList
import com.splendo.kaluga.bluetooth.DefaultServiceWrapper
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreBluetooth.*
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

internal actual class DeviceConnectionManager(private val cbCentralManager: CBCentralManager,
    private val peripheral: CBPeripheral,
    connectionSettings: ConnectionSettings,
    deviceHolder: DeviceHolder,
    stateRepo: StateRepo<DeviceState>,
    coroutineScope: CoroutineScope) : BaseDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo, coroutineScope), CoroutineScope by coroutineScope {

    class Builder(private val cbCentralManager: CBCentralManager, private val peripheral: CBPeripheral) : BaseDeviceConnectionManager.Builder {
        override fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>, coroutineScope: CoroutineScope): BaseDeviceConnectionManager {
            return DeviceConnectionManager(cbCentralManager, peripheral, connectionSettings, deviceHolder, stateRepo, coroutineScope)
        }
    }

    companion object {
        private const val TAG = "IOS Bluetooth DeviceConnectionManager"
    }

    private val discoveringServices = emptyList<CBUUID>().toMutableList()
    private val discoveringCharacteristics = emptyList<CBUUID>().toMutableList()

    @Suppress("CONFLICTING_OVERLOADS")
    private val peripheralDelegate = object : NSObject(), CBPeripheralDelegateProtocol {

        override fun peripheral(peripheral: CBPeripheral, didDiscoverDescriptorsForCharacteristic: CBCharacteristic, error: NSError?) {
            info(TAG, "Did Discover Descriptors for Characteristic ${didDiscoverDescriptorsForCharacteristic.UUID.UUIDString}")
            didDiscoverDescriptors(didDiscoverDescriptorsForCharacteristic)
        }

        override fun peripheral(peripheral: CBPeripheral, didUpdateNotificationStateForCharacteristic: CBCharacteristic, error: NSError?) {
            info(TAG, "Did Update Notification State for Characteristic ${didUpdateNotificationStateForCharacteristic.UUID.UUIDString}")
            when (val action = currentAction) {
                is DeviceAction.Notification -> {
                    if (action.characteristic.characteristic.UUID.toString() == didUpdateNotificationStateForCharacteristic.UUID.toString()) {
                        launch(MainQueueDispatcher) {
                            handleCurrentActionCompleted()
                        }
                    }
                }
            }
        }

        override fun peripheral(peripheral: CBPeripheral, didUpdateValueForCharacteristic: CBCharacteristic, error: NSError?) {
            info(TAG, "Did Update Value for Characteristic ${didUpdateValueForCharacteristic.UUID.UUIDString}")
            updateCharacteristic(didUpdateValueForCharacteristic)
        }

        override fun peripheral(peripheral: CBPeripheral, didWriteValueForCharacteristic: CBCharacteristic, error: NSError?) {
            info(TAG, "Did Write Value for Characteristic ${didWriteValueForCharacteristic.UUID.UUIDString}")
            updateCharacteristic(didWriteValueForCharacteristic)
        }

        override fun peripheral(peripheral: CBPeripheral, didUpdateValueForDescriptor: CBDescriptor, error: NSError?) {
            info(TAG, "Did Update Value for Descriptor ${didUpdateValueForDescriptor.UUID.UUIDString}")
            updateDescriptor(didUpdateValueForDescriptor)
        }

        override fun peripheral(peripheral: CBPeripheral, didWriteValueForDescriptor: CBDescriptor, error: NSError?) {
            info(TAG, "Did Write Value for Descriptor ${didWriteValueForDescriptor.UUID.UUIDString}")
            updateDescriptor(didWriteValueForDescriptor)
        }

        override fun peripheral(peripheral: CBPeripheral, didDiscoverCharacteristicsForService: CBService, error: NSError?) {
            info(TAG, "Did Discover Characteristics for Service ${didDiscoverCharacteristicsForService.UUID.UUIDString}")
            didDiscoverCharacteristic(didDiscoverCharacteristicsForService)
        }

        override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
            info(TAG, "Did Discover Services for Peripheral ${peripheral.identifier.UUIDString}")
            didDiscoverServices()
        }

        override fun peripheral(peripheral: CBPeripheral, didReadRSSI: NSNumber, error: NSError?) {
            info(TAG, "Did Read RSSI for Peripheral ${peripheral.identifier.UUIDString}")
            launch(MainQueueDispatcher) {
                handleNewRssi(didReadRSSI.intValue)
            }
        }

    }

    init {
        peripheral.delegate = peripheralDelegate
    }

    override suspend fun connect() {
        info(TAG, "Request Connect for Peripheral ${peripheral.identifier.UUIDString}")
        cbCentralManager.connectPeripheral(peripheral, null)
    }

    override suspend fun discoverServices() {
        info(TAG, "Discover Services for Peripheral ${peripheral.identifier.UUIDString}")
        discoveringServices.clear()
        discoveringCharacteristics.clear()
        peripheral.discoverServices(null)
    }

    override suspend fun disconnect() {
        info(TAG, "Request Disconnect for Peripheral ${peripheral.identifier.UUIDString}")
        cbCentralManager.cancelPeripheralConnection(peripheral)
    }

    override suspend fun readRssi() {
        info(TAG, "Read RSSI for Peripheral ${peripheral.identifier.UUIDString}")
        peripheral.readRSSI()
    }

    override suspend fun performAction(action: DeviceAction) {
        info(TAG, "Perform Action for Peripheral ${peripheral.identifier.UUIDString}")
        currentAction = action
        when(action) {
            is DeviceAction.Read.Characteristic -> action.characteristic.characteristic.readValue(peripheral)
            is DeviceAction.Read.Descriptor -> action.descriptor.descriptor.readValue(peripheral)
            is DeviceAction.Write.Characteristic -> {
                action.newValue?.toNSData()?.let {
                    action.characteristic.characteristic.writeValue(it, peripheral)
                }
            }
            is DeviceAction.Write.Descriptor -> {
                action.newValue?.toNSData()?.let {
                    action.descriptor.descriptor.writeValue(it, peripheral)
                }
            }
            is DeviceAction.Notification -> {
                val uuid = action.characteristic.uuid.uuidString
                if (action.enable) {
                    notifyingCharacteristics[uuid] = action.characteristic
                } else notifyingCharacteristics.remove(uuid)
                action.characteristic.characteristic.setNotificationValue(action.enable, peripheral)
            }
        }
    }

    private fun updateCharacteristic(characteristic: CBCharacteristic) {
        launch(MainQueueDispatcher) {
            handleUpdatedCharacteristic(characteristic.UUID)
        }
    }

    private fun updateDescriptor(descriptor: CBDescriptor) {
        launch(MainQueueDispatcher) {
            handleUpdatedDescriptor(descriptor.UUID)
        }
    }

    private fun didDiscoverServices() {
        discoveringServices.addAll(peripheral.services?.typedList<CBService>()?.map {
            peripheral.discoverCharacteristics(emptyList<CBUUID>(), it)
            it.UUID
        } ?: emptyList())

        checkScanComplete()
    }

    private fun didDiscoverCharacteristic(forService: CBService) {
        discoveringServices.remove(forService.UUID)
        discoveringCharacteristics.addAll(forService.characteristics?.typedList<CBCharacteristic>()?.map {
            peripheral.discoverDescriptorsForCharacteristic(it)
            it.UUID
        } ?: emptyList())
        checkScanComplete()
    }

    private fun didDiscoverDescriptors(forCharacteristic: CBCharacteristic) {
        discoveringCharacteristics.remove(forCharacteristic.UUID)
        checkScanComplete()
    }

    private fun checkScanComplete() {
        if (discoveringServices.isEmpty() && discoveringCharacteristics.isEmpty()) {
            launch(MainQueueDispatcher) {
                val services = peripheral.services?.typedList<CBService>()?.map { Service(DefaultServiceWrapper(it), stateRepo) } ?: emptyList()
                handleScanCompleted(services)
            }
        }
    }

}

