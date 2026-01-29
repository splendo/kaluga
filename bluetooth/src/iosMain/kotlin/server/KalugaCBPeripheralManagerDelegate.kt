/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.base.utils.toNSData
import com.splendo.kaluga.base.utils.typedList
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.asBytes
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBATTRequest
import platform.CoreBluetooth.CBCentral
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBPeripheralManagerDelegateProtocol
import platform.CoreBluetooth.CBPeripheralManagerStatePoweredOn
import platform.CoreBluetooth.CBService
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext

private const val TAG = "KalugaCBPeripheralManagerDelegate"
class KalugaCBPeripheralManagerDelegate(private val logger: Logger, handlingContext: CoroutineContext) :
    NSObject(),
    CBPeripheralManagerDelegateProtocol {

    data class ServiceAdded(val service: CBService, val success: Boolean)

    private val _isEnabled = MutableSharedFlow<Boolean>(replay = 1)
    val isEnabled = _isEnabled.asSharedFlow()

    private val _serviceAdded = MutableSharedFlow<ServiceAdded>(replay = 1)
    val serviceAdded = _serviceAdded.asSharedFlow()

    private var didStartAdvertising = CompletableDeferred<Boolean>()
    fun resetAdvertising(): Deferred<Boolean> {
        didStartAdvertising.complete(false)
        didStartAdvertising = CompletableDeferred()
        return didStartAdvertising
    }

    private var available = EmptyCompletableDeferred()
    fun resetAvailable(): Deferred<Unit> {
        if (!available.isCompleted) {
            available = CompletableDeferred()
        }
        return available
    }

    private val lock = reentrantLock()

    private val readActions = mutableMapOf<CBCharacteristic, suspend (ConnectedDevice, Int) -> GattResponse.ReadResponse>()
    private val writeActions = mutableMapOf<CBCharacteristic, suspend (ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>()
    private val subscribeActions = mutableMapOf<CBCharacteristic, (ConnectedDevice) -> Unit>()
    private val unsubscribeActions = mutableMapOf<CBCharacteristic, (ConnectedDevice) -> Unit>()

    private val handlingScope = CoroutineScope(handlingContext + CoroutineName("CBPeripheralManagerDelegate"))

    fun registerReadAction(characteristic: LocalCharacteristic, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse) = lock.withLock {
        val identifier = characteristic.wrapper.characteristic
        if (readActions.contains(identifier)) {
            logger.warn(TAG) { "Read action for $identifier was already set. Ignoring" }
        } else {
            readActions[identifier] = { device, offset -> characteristic.onRead(device, offset) }
        }
    }

    fun registerWriteAction(characteristic: LocalCharacteristic, onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse) = lock.withLock {
        val identifier = characteristic.wrapper.characteristic
        if (writeActions.contains(identifier)) {
            logger.warn(TAG) { "Write action for $identifier was already set. Ignoring" }
        } else {
            writeActions[identifier] = { device, offset, value -> characteristic.onWrite(device, offset, value) }
        }
    }

    fun registerSubscriptionActions(characteristic: LocalCharacteristic.Notifiable) = lock.withLock {
        val identifier = characteristic.wrapper.characteristic
        when {
            subscribeActions.contains(identifier) -> logger.warn(TAG) { "Subscribe action for $identifier was already set. Ignoring" }

            unsubscribeActions.contains(identifier) -> logger.warn(TAG) { "Unsubscribe action for $identifier was already set. Ignoring" }

            else -> {
                subscribeActions[identifier] = { device -> characteristic.subscribe(device) }
                unsubscribeActions[identifier] = { device -> characteristic.unsubscribe(device) }
            }
        }
    }

    fun removeService(service: CBService) {
        service.includedServices.orEmpty().typedList<CBService>().forEach(::removeService)
        lock.withLock {
            readActions -= readActions.keys.filter { it.service == service.UUID }.toSet()
            writeActions -= writeActions.keys.filter { it.service == service.UUID }.toSet()
            subscribeActions -= subscribeActions.keys.filter { it.service == service.UUID }.toSet()
        }
    }

    fun removeAllServices() = lock.withLock {
        readActions.clear()
        writeActions.clear()
        subscribeActions.clear()
    }

    override fun peripheralManagerDidUpdateState(peripheral: CBPeripheralManager) {
        _isEnabled.tryEmit(peripheral.state == CBPeripheralManagerStatePoweredOn)
    }

    override fun peripheralManager(peripheral: CBPeripheralManager, didAddService: CBService, error: NSError?) {
        _serviceAdded.tryEmit(ServiceAdded(didAddService, error == null))
    }

    @ObjCSignatureOverride
    override fun peripheralManager(peripheral: CBPeripheralManager, central: CBCentral, didSubscribeToCharacteristic: CBCharacteristic) {
        subscribeActions[didSubscribeToCharacteristic]?.let { onSubscribe ->
            handlingScope.launch {
                onSubscribe(ConnectedDevice(central))
            }
        }
    }

    @ObjCSignatureOverride
    override fun peripheralManager(peripheral: CBPeripheralManager, central: CBCentral, didUnsubscribeFromCharacteristic: CBCharacteristic) {
        unsubscribeActions[didUnsubscribeFromCharacteristic]?.let { onUnsubscribe ->
            handlingScope.launch {
                onUnsubscribe(ConnectedDevice(central))
            }
        }
    }

    override fun peripheralManager(peripheral: CBPeripheralManager, didReceiveReadRequest: CBATTRequest) {
        handlingScope.launch {
            val identifier = didReceiveReadRequest.characteristic
            logger.info(TAG) { "Device ${didReceiveReadRequest.central.identifier} attempting to read $identifier at ${didReceiveReadRequest.offset}" }
            val response = readActions[identifier]?.invoke(ConnectedDevice(didReceiveReadRequest.central), didReceiveReadRequest.offset.toInt()) ?: GattResponse.InvalidHandle
            if (response is GattResponse.ReadSuccess) {
                didReceiveReadRequest.setValue(response.value.toNSData())
            }

            if (peripheral.state == CBPeripheralManagerStatePoweredOn) {
                peripheral.respondToRequest(didReceiveReadRequest, response.statusCode.toLong())
            }
        }
    }

    override fun peripheralManager(peripheral: CBPeripheralManager, didReceiveWriteRequests: List<*>) {
        handlingScope.launch {
            val requests = didReceiveWriteRequests.typedList<CBATTRequest>()
            val responses = requests.map { writeRequest ->
                val identifier = writeRequest.characteristic
                val value = writeRequest.value?.asBytes ?: byteArrayOf()
                logger.info(TAG) { "Device ${writeRequest.central.identifier} wrote $value for $identifier at offset ${writeRequest.offset}" }
                writeActions[identifier]?.invoke(ConnectedDevice(writeRequest.central), value, writeRequest.offset.toInt()) ?: GattResponse.InvalidHandle
            }
            val response = responses.firstOrNull { it is GattResponse.Error } ?: GattResponse.WriteSuccess
            if (peripheral.state == CBPeripheralManagerStatePoweredOn) {
                peripheral.respondToRequest(requests.first(), response.statusCode.toLong())
            }
        }
    }

    override fun peripheralManagerDidStartAdvertising(peripheral: CBPeripheralManager, error: NSError?) {
        didStartAdvertising.complete(error == null)
    }

    override fun peripheralManagerIsReadyToUpdateSubscribers(peripheral: CBPeripheralManager) {
        available.complete()
    }
}
