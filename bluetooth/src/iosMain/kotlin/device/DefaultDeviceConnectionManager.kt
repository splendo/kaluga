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

import com.splendo.kaluga.base.utils.toNSData
import com.splendo.kaluga.base.utils.typedList
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.DefaultServiceWrapper
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.asBytes
import com.splendo.kaluga.bluetooth.dataValue
import com.splendo.kaluga.logging.debug
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBPeripheralStateConnected
import platform.CoreBluetooth.CBPeripheralStateConnecting
import platform.CoreBluetooth.CBPeripheralStateDisconnected
import platform.CoreBluetooth.CBPeripheralStateDisconnecting
import platform.CoreBluetooth.CBService
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

internal actual class DefaultDeviceConnectionManager(
    private val cbCentralManager: CBCentralManager,
    private val peripheral: CBPeripheral,
    deviceWrapper: DeviceWrapper,
    settings: ConnectionSettings,
    coroutineScope: CoroutineScope,
) : BaseDeviceConnectionManager(deviceWrapper, settings, coroutineScope) {

    class Builder(private val cbCentralManager: CBCentralManager, private val peripheral: CBPeripheral) : DeviceConnectionManager.Builder {
        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): DefaultDeviceConnectionManager =
            DefaultDeviceConnectionManager(
                cbCentralManager,
                peripheral,
                deviceWrapper,
                settings,
                coroutineScope,
            )
    }

    companion object {
        private const val TAG = "IOS Bluetooth DeviceConnectionManager"
    }

    private val discoveringMutex = Mutex()
    private val discoveringServices = mutableListOf<CBUUID>()
    private val discoveringCharacteristics = mutableListOf<CBUUID>()

    private val peripheralDelegate = object : NSObject(), CBPeripheralDelegateProtocol {

        @ObjCSignatureOverride
        override fun peripheral(peripheral: CBPeripheral, didDiscoverDescriptorsForCharacteristic: CBCharacteristic, error: NSError?) {
            didDiscoverDescriptors(didDiscoverDescriptorsForCharacteristic)
        }

        @ObjCSignatureOverride
        override fun peripheral(peripheral: CBPeripheral, didUpdateNotificationStateForCharacteristic: CBCharacteristic, error: NSError?) {
            val action = currentAction
            if (action is DeviceAction.Notification && action.characteristic.wrapper.uuid == didUpdateNotificationStateForCharacteristic.UUID) {
                launch {
                    action.handleNotificationStateChanged(if (error == null) GattResponse.WriteSuccess else GattResponse.Error.from(error.code.toInt()))
                }
            }
        }

        @ObjCSignatureOverride
        override fun peripheral(peripheral: CBPeripheral, didUpdateValueForCharacteristic: CBCharacteristic, error: NSError?) {
            handleCharacteristicReadOrNotified(
                didUpdateValueForCharacteristic.UUID,
                if (error == null) GattResponse.ReadSuccess(didUpdateValueForCharacteristic.value?.asBytes ?: byteArrayOf()) else GattResponse.Error.from(error.code.toInt()),
            )
        }

        @ObjCSignatureOverride
        override fun peripheral(peripheral: CBPeripheral, didWriteValueForCharacteristic: CBCharacteristic, error: NSError?) {
            handleCharacteristicWritten(didWriteValueForCharacteristic.UUID, if (error == null) GattResponse.WriteSuccess else GattResponse.Error.from(error.code.toInt()))
        }

        @ObjCSignatureOverride
        override fun peripheral(peripheral: CBPeripheral, didUpdateValueForDescriptor: CBDescriptor, error: NSError?) {
            handleDescriptorRead(
                didUpdateValueForDescriptor.UUID,
                if (error == null) GattResponse.ReadSuccess(didUpdateValueForDescriptor.dataValue?.asBytes ?: byteArrayOf()) else GattResponse.Error.from(error.code.toInt()),
            )
        }

        @ObjCSignatureOverride
        override fun peripheral(peripheral: CBPeripheral, didWriteValueForDescriptor: CBDescriptor, error: NSError?) {
            handleDescriptorWritten(didWriteValueForDescriptor.UUID, if (error == null) GattResponse.WriteSuccess else GattResponse.Error.from(error.code.toInt()))
        }

        override fun peripheral(peripheral: CBPeripheral, didDiscoverCharacteristicsForService: CBService, error: NSError?) {
            didDiscoverCharacteristic(didDiscoverCharacteristicsForService)
        }

        override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
            didDiscoverServices()
        }

        override fun peripheral(peripheral: CBPeripheral, didReadRSSI: NSNumber, error: NSError?) {
            launch {
                handleNewRssi(didReadRSSI.intValue)
            }
        }
    }

    actual override fun getCurrentState(): DeviceConnectionManager.State = when (peripheral.state) {
        CBPeripheralStateConnected -> DeviceConnectionManager.State.CONNECTED
        CBPeripheralStateConnecting -> DeviceConnectionManager.State.CONNECTING
        CBPeripheralStateDisconnected -> DeviceConnectionManager.State.DISCONNECTED
        CBPeripheralStateDisconnecting -> DeviceConnectionManager.State.DISCONNECTING
        else -> DeviceConnectionManager.State.DISCONNECTED
    }

    actual override fun connect() {
        peripheral.delegate = peripheralDelegate
        cbCentralManager.connectPeripheral(peripheral, null)
    }

    actual override suspend fun discoverServices() {
        discoveringMutex.withLock {
            discoveringServices.clear()
            discoveringCharacteristics.clear()
            peripheral.discoverServices(null)
        }
    }

    actual override fun disconnect() {
        val state = getCurrentState()
        cbCentralManager.cancelPeripheralConnection(peripheral)
        peripheral.delegate = null
        if (state != DeviceConnectionManager.State.CONNECTED) {
            handleDisconnect()
        }
    }

    override suspend fun readRssi() {
        peripheral.readRSSI()
    }

    actual override suspend fun didStartPerformingAction(action: DeviceAction<*>) {
        currentAction = action
        when (action) {
            is DeviceAction.Read.Characteristic -> action.characteristic.wrapper.readValue(peripheral)

            is DeviceAction.Read.Descriptor -> action.descriptor.wrapper.readValue(peripheral)

            is DeviceAction.Write.Characteristic -> {
                val withResponse = action.characteristic.hasProperty(CharacteristicProperty.Write) ||
                    !action.characteristic.hasProperty(CharacteristicProperty.WriteWithoutResponse)
                action.characteristic.wrapper.writeValue(action.newValue.toNSData(), peripheral, withResponse)
                if (!withResponse) {
                    handleCharacteristicWritten(action.characteristic.uuid, GattResponse.WriteSuccess)
                }
            }

            is DeviceAction.Write.Descriptor -> {
                action.descriptor.wrapper.writeValue(action.newValue.toNSData(), peripheral)
            }

            is DeviceAction.Notification.Enable -> {
                action.characteristic.wrapper.setNotificationValue(true, peripheral)
            }

            is DeviceAction.Notification.Disable -> {
                action.characteristic.wrapper.setNotificationValue(false, peripheral)
            }

            is DeviceAction.RequestMtu -> {
                val max = peripheral.maximumWriteValueLengthForType(CBCharacteristicWriteWithResponse)
                debug(TAG) { "maximumWriteValueLengthForType(CBCharacteristicWriteWithResponse) = $max" }
                // Update MTU to current known value, set succeeded to false, because we can't request MTU change from iOS
                handleNewMtu(GattResponse.MTUNotPermitted(max.toInt()))
            }
        }
    }

    actual override suspend fun requestStartPairing() {
        // There is no iOS API to pair peripheral
    }

    actual override suspend fun requestStartUnpairing() {
        // There is no iOS API to unpair peripheral
    }

    private fun didDiscoverServices() {
        launch {
            discoveringMutex.withLock {
                discoveringServices.addAll(
                    peripheral.services?.typedList<CBService>()?.map {
                        peripheral.discoverCharacteristics(emptyList<CBUUID>(), it)
                        it.UUID
                    } ?: emptyList(),
                )
            }

            checkScanComplete()
        }
    }

    private fun didDiscoverCharacteristic(forService: CBService) {
        launch {
            discoveringMutex.withLock {
                discoveringServices.remove(forService.UUID)
                discoveringCharacteristics.addAll(
                    forService.characteristics?.typedList<CBCharacteristic>()?.map {
                        peripheral.discoverDescriptorsForCharacteristic(it)
                        it.UUID
                    } ?: emptyList(),
                )
            }
            checkScanComplete()
        }
    }

    private fun didDiscoverDescriptors(forCharacteristic: CBCharacteristic) {
        launch {
            discoveringMutex.withLock {
                discoveringCharacteristics.remove(forCharacteristic.UUID)
            }
            checkScanComplete()
        }
    }

    private fun checkScanComplete() {
        if (discoveringServices.isEmpty() && discoveringCharacteristics.isEmpty()) {
            val services = peripheral.services?.typedList<CBService>()?.map { DefaultServiceWrapper(it) } ?: emptyList()
            handleDiscoverCompleted(services)
        }
    }
}
