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
import com.splendo.kaluga.bluetooth.WriteType
import com.splendo.kaluga.bluetooth.client.KalugaBluetoothPeripheralDelegateProtocol
import com.splendo.kaluga.bluetooth.client.KalugaBluetoothPeripheralWrapper
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.asBytes
import com.splendo.kaluga.bluetooth.dataValue
import com.splendo.kaluga.bluetooth.mtu
import com.splendo.kaluga.logging.debug
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
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

        // Write-without-response has no completion callback; if the outbound queue is full we wait this long
        // for peripheralIsReadyToSendWriteWithoutResponse before giving up.
        private val MAX_WRITE_WITHOUT_RESPONSE_WAIT = 500.milliseconds
    }

    private val discoveringMutex = Mutex()
    private val discoveringServices = mutableListOf<CBUUID>()
    private val discoveringCharacteristics = mutableListOf<CBUUID>()

    private val peripheralDelegate = object : NSObject(), KalugaBluetoothPeripheralDelegateProtocol {

        private var awaitingSendWriteWithoutResponse = CompletableDeferred<Unit>()

        fun resetAwaitingSendWriteWithoutResponse() {
            awaitingSendWriteWithoutResponse = CompletableDeferred()
        }

        suspend fun awaitSendWriteWithoutResponse() {
            awaitingSendWriteWithoutResponse.await()
        }

        override fun isReadyToSendWriteWithoutResponseFor(peripheral: CBPeripheral) {
            awaitingSendWriteWithoutResponse.complete(Unit)
        }

        override fun didDiscoverDescriptorsFor(characteristic: CBCharacteristic, peripheral: CBPeripheral, error: NSError?) {
            didDiscoverDescriptors(characteristic)
        }

        override fun didUpdateNotificationStateFor(characteristic: CBCharacteristic, peripheral: CBPeripheral, error: NSError?) {
            val action = currentAction
            if (action is DeviceAction.Notification && action.characteristic.wrapper.uuid == characteristic.UUID) {
                launch {
                    action.handleNotificationStateChanged(if (error == null) GattResponse.WriteSuccess else GattResponse.Error.from(error.code.toInt()))
                }
            }
        }

        override fun didUpdateValueForCharacteristic(characteristic: CBCharacteristic, peripheral: CBPeripheral, error: NSError?) {
            handleCharacteristicReadOrNotified(
                characteristic.UUID,
                if (error == null) GattResponse.ReadSuccess(characteristic.value?.asBytes ?: byteArrayOf()) else GattResponse.Error.from(error.code.toInt()),
            )
        }

        override fun didWriteValueForCharacteristic(characteristic: CBCharacteristic, peripheral: CBPeripheral, error: NSError?) {
            handleCharacteristicWritten(characteristic.UUID, if (error == null) GattResponse.WriteSuccess else GattResponse.Error.from(error.code.toInt()))
        }

        override fun didUpdateValueForDescriptor(descriptor: CBDescriptor, peripheral: CBPeripheral, error: NSError?) {
            handleDescriptorRead(
                descriptor.UUID,
                if (error == null) GattResponse.ReadSuccess(descriptor.dataValue?.asBytes ?: byteArrayOf()) else GattResponse.Error.from(error.code.toInt()),
            )
        }

        override fun didWriteValueForDescriptor(descriptor: CBDescriptor, peripheral: CBPeripheral, error: NSError?) {
            handleDescriptorWritten(descriptor.UUID, if (error == null) GattResponse.WriteSuccess else GattResponse.Error.from(error.code.toInt()))
        }

        override fun didDiscoverCharacteristicsFor(service: CBService, peripheral: CBPeripheral, error: NSError?) {
            didDiscoverCharacteristic(service)
        }

        override fun didDiscoverServicesFor(peripheral: CBPeripheral, error: NSError?) {
            didDiscoverServices()
        }

        override fun didReadWithRssi(RSSI: NSNumber, forPeripheral: CBPeripheral, error: NSError?) {
            launch {
                handleNewRssi(RSSI.intValue)
            }
        }
    }

    val wrapper = atomic<KalugaBluetoothPeripheralWrapper?>(null)

    actual override fun getCurrentState(): DeviceConnectionManager.State = when (peripheral.state) {
        CBPeripheralStateConnected -> DeviceConnectionManager.State.CONNECTED
        CBPeripheralStateConnecting -> DeviceConnectionManager.State.CONNECTING
        CBPeripheralStateDisconnected -> DeviceConnectionManager.State.DISCONNECTED
        CBPeripheralStateDisconnecting -> DeviceConnectionManager.State.DISCONNECTING
        else -> DeviceConnectionManager.State.DISCONNECTED
    }

    actual override fun connect() {
        wrapper.getAndUpdate {
            it?.unlink()
            KalugaBluetoothPeripheralWrapper.createByLinkingWithPeripheral(peripheral, peripheralDelegate)
        }
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
        wrapper.getAndUpdate {
            it?.unlink()
            null
        }
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
                val withResponse = when (action.writeType) {
                    WriteType.WithResponse -> true
                    WriteType.WithoutResponse -> false
                    null -> action.characteristic.hasProperty(CharacteristicProperty.Write)
                }
                if (withResponse) {
                    // With-response completes via didWriteValueForCharacteristic.
                    action.characteristic.wrapper.writeValue(action.newValue.toNSData(), peripheral, true)
                } else {
                    // Write-without-response has no didWriteValueForCharacteristic callback, so completion is
                    // derived from the send loop.
                    val wasReady = peripheral.canSendWriteWithoutResponse
                    peripheralDelegate.resetAwaitingSendWriteWithoutResponse()
                    val response = sendWriteWithoutResponse(
                        canSendNow = wasReady,
                        write = { action.characteristic.wrapper.writeValue(action.newValue.toNSData(), peripheral, false) },
                        awaitReady = {
                            withTimeoutOrNull(MAX_WRITE_WITHOUT_RESPONSE_WAIT) {
                                peripheralDelegate.awaitSendWriteWithoutResponse()
                                true
                            } ?: false
                        },
                    )
                    handleCharacteristicWritten(action.characteristic.uuid, response)
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
                // iOS negotiates the MTU automatically and exposes no request API; surface the actual ATT MTU.
                val mtu = peripheral.mtu
                debug(TAG) { "Reporting negotiated MTU $mtu" }
                handleNewMtu(GattResponse.MTUSuccess(mtu))
            }
        }
    }

    actual override suspend fun requestStartPairing(): PairingResult {
        // There is no iOS API to pair a peripheral; pairing happens implicitly on encrypted attribute access.
        return PairingResult.NOT_SUPPORTED
    }

    actual override suspend fun requestStartUnpairing(): PairingResult {
        // There is no iOS API to unpair a peripheral.
        return PairingResult.NOT_SUPPORTED
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
