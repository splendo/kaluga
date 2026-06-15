/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.server

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.min

internal typealias SendResponse = (device: BluetoothDevice, requestId: Int, status: Int, offset: Int, data: ByteArray?) -> Unit

internal class KalugaBluetoothGattServerCallback(private val logger: Logger, handlingContext: CoroutineContext) : BluetoothGattServerCallback() {

    companion object Companion {
        const val TAG = "KalugaBluetoothGattServerCallback"
        private const val MTU_HEADER_SIZE = 3
        const val DEFAULT_MTU_SIZE = 23
    }

    data class ServiceAdded(val service: BluetoothGattService, val success: Boolean)
    data class NotificationSent(val device: BluetoothDevice, val success: Boolean)

    private val _serviceAdded = MutableSharedFlow<ServiceAdded>(replay = 1)
    val serviceAdded = _serviceAdded.asSharedFlow()

    private val _notificationSent = MutableSharedFlow<NotificationSent>(replay = 1)
    val notificationSent = _notificationSent.asSharedFlow()

    private var sendResponse: SendResponse? = null

    private val mtu = mutableMapOf<String, Int>()
    private val pendingWrites = concurrentMutableMapOf<String, Map<AttributeIdentity, ByteArray>>()

    private val readActions = mutableMapOf<AttributeIdentity, suspend (ConnectedDevice, Int) -> GattResponse.ReadResponse>()
    private val writeActions = mutableMapOf<AttributeIdentity, suspend (ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>()

    private val handlingScope = CoroutineScope(handlingContext + CoroutineName("BluetoothServerCallback"))

    fun registerReadAction(characteristic: LocalCharacteristic, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse) {
        registerReadAction(characteristic.wrapper.identity) { device, offset ->
            characteristic.onRead(device, offset)
        }
    }

    fun registerReadAction(descriptor: LocalDescriptor, onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse) {
        registerReadAction(descriptor.wrapper.identity) { device, offset ->
            descriptor.onRead(device, offset)
        }
    }

    fun registerWriteAction(characteristic: LocalCharacteristic, onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse) {
        registerWriteAction(characteristic.wrapper.identity) { device, value, offset ->
            characteristic.onWrite(device, value, offset)
        }
    }

    fun registerWriteAction(descriptor: LocalDescriptor, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse) {
        registerWriteAction(descriptor.wrapper.identity) { device, value, offset ->
            descriptor.onWrite(device, value, offset)
        }
    }

    fun registerSendResponse(sendResponse: SendResponse) {
        this.sendResponse = sendResponse
    }

    fun removeService(service: LocalService) {
        service.includedServices.forEach { removeService(it) }
        service.characteristics.forEach { characteristic ->
            readActions.remove(characteristic.wrapper.identity)
            writeActions.remove(characteristic.wrapper.identity)
            characteristic.descriptors.forEach { descriptor ->
                readActions.remove(descriptor.wrapper.identity)
                writeActions.remove(descriptor.wrapper.identity)
            }
        }
    }

    fun reset() {
        sendResponse = null
        mtu.clear()
        pendingWrites.clear()
    }
    fun removeAllServices() {
        readActions.clear()
        writeActions.clear()
    }

    override fun onCharacteristicReadRequest(device: BluetoothDevice, requestId: Int, offset: Int, characteristic: BluetoothGattCharacteristic) {
        handleReadAction(device, GattCharacteristicIdentity(characteristic), requestId, offset)
    }

    override fun onCharacteristicWriteRequest(
        device: BluetoothDevice,
        requestId: Int,
        characteristic: BluetoothGattCharacteristic,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray,
    ) {
        handleWriteAction(device, GattCharacteristicIdentity(characteristic), requestId, offset, value, preparedWrite, responseNeeded)
    }

    override fun onDescriptorReadRequest(device: BluetoothDevice, requestId: Int, offset: Int, descriptor: BluetoothGattDescriptor) {
        handleReadAction(device, GattDescriptorIdentity(descriptor), requestId, offset)
    }

    override fun onDescriptorWriteRequest(
        device: BluetoothDevice,
        requestId: Int,
        descriptor: BluetoothGattDescriptor,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray,
    ) {
        handleWriteAction(device, GattDescriptorIdentity(descriptor), requestId, offset, value, preparedWrite, responseNeeded)
    }

    override fun onExecuteWrite(device: BluetoothDevice, requestId: Int, execute: Boolean) {
        handlingScope.launch {
            val response = if (execute) {
                pendingWrites[device.address]?.entries.orEmpty().map { (identifier, value) ->
                    writeActions[identifier]?.let { writeAction ->
                        writeAction(DefaultConnectedDevice(device), value, 0)
                    }
                }.firstOrNull { it is GattResponse.Error } ?: GattResponse.WriteSuccess
            } else {
                GattResponse.WriteSuccess
            }
            pendingWrites.remove(device.address)
            sendResponse?.invoke(device, requestId, response.statusCode, 0, null)
        }
    }

    override fun onMtuChanged(device: BluetoothDevice, mtu: Int) {
        logger.info(TAG) { "Device ${device.address} set mtu to $mtu" }
        this.mtu[device.address] = mtu
    }

    override fun onNotificationSent(device: BluetoothDevice, status: Int) {
        logger.info(TAG) { "Sent notification to Device ${device.address}. Status $status" }
        _notificationSent.tryEmit(NotificationSent(device, status == BluetoothGatt.GATT_SUCCESS))
    }

    override fun onServiceAdded(status: Int, service: BluetoothGattService) {
        logger.info(TAG) { "Added service ${service.uuid} with status $status" }
        _serviceAdded.tryEmit(ServiceAdded(service, status == BluetoothGatt.GATT_SUCCESS))
    }

    override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
        when (newState) {
            BluetoothProfile.STATE_CONNECTED if (status == BluetoothGatt.GATT_SUCCESS) -> {
                logger.info(TAG) { "Device ${device.address} connected" }
                mtu[device.address] = DEFAULT_MTU_SIZE
            }

            BluetoothProfile.STATE_DISCONNECTED -> {
                logger.info(TAG) { "Device ${device.address} disconnected" }
                mtu.remove(device.address)
            }

            else -> logger.warn(TAG) { "Device ${device.address} connection changed to $newState with unexpected status $status" }
        }
    }

    private fun handleReadAction(device: BluetoothDevice, identifier: AttributeIdentity, requestId: Int, offset: Int) {
        handlingScope.launch {
            logger.info(TAG) { "Device ${device.address} attempting to read $identifier at $offset" }
            val (response, data) = readActions[identifier]?.let { readAction ->
                val response = readAction(DefaultConnectedDevice(device), offset)
                response to when (response) {
                    is GattResponse.ReadSuccess -> {
                        val sizeToSend = (mtu[device.address] ?: DEFAULT_MTU_SIZE) - MTU_HEADER_SIZE
                        response.value.sliceArray(0..<min(response.value.size, sizeToSend))
                    }

                    else -> null
                }
            } ?: (GattResponse.InvalidHandle to null)

            sendResponse?.invoke(device, requestId, response.statusCode, offset, data)
        }
    }

    private fun handleWriteAction(
        device: BluetoothDevice,
        identifier: AttributeIdentity,
        requestId: Int,
        offset: Int,
        value: ByteArray,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
    ) {
        handlingScope.launch {
            val response = if (preparedWrite) {
                logger.info(TAG) { "Device ${device.address} wrote $value (pending) for $identifier" }
                pendingWrites.synchronized {
                    val pendingWritesForDevice = getOrElse(device.address) { emptyMap() }.toMutableMap()
                    pendingWritesForDevice[identifier] = (pendingWritesForDevice[identifier] ?: byteArrayOf()) + value
                    put(device.address, pendingWritesForDevice)
                }
                GattResponse.WriteSuccess
            } else {
                logger.info(TAG) { "Device ${device.address} wrote $value for $identifier at offset $offset" }
                writeActions[identifier]?.let { writeAction ->
                    writeAction(DefaultConnectedDevice(device), value, offset)
                } ?: GattResponse.InvalidHandle
            }

            if (responseNeeded) {
                sendResponse?.invoke(device, requestId, response.statusCode, offset, null)
            }
        }
    }

    private fun registerReadAction(identifier: AttributeIdentity, onRead: suspend (ConnectedDevice, Int) -> GattResponse.ReadResponse) {
        if (readActions.putIfAbsent(identifier, onRead) != null) {
            logger.warn(TAG) { "Read action for $identifier was already set. Ignoring" }
        }
    }
    private fun registerWriteAction(identifier: AttributeIdentity, onWrite: suspend (ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse) {
        if (writeActions.putIfAbsent(identifier, onWrite) != null) {
            logger.warn(TAG) { "Write action for $identifier was already set. Ignoring" }
        }
    }
}
