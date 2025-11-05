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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.RemoteAttribute
import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.RemoteDescriptor
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.descriptors
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.includedServices
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.bluetooth.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.JvmName

interface RemoteAttributeBinding<T> {
    fun <Trigger, Response> Channel<Trigger>.consumeAsReadAndMutate(asValue: ByteArray.() -> Response, onRead: suspend T.(Response, Trigger) -> T)

    fun <Response> Channel<Unit>.consumeAsReadAndMutate(asValue: ByteArray.() -> Response, onRead: suspend T.(Response) -> T) = consumeAsReadAndMutate(asValue) { response, _ ->
        onRead(response)
    }

    fun <Trigger, Response> Channel<Trigger>.consumeAsRead(asValue: ByteArray.() -> Response, onRead: suspend T.(Response, Trigger) -> Unit) =
        consumeAsReadAndMutate(asValue) { response, trigger ->
            onRead(response, trigger)
            this
        }

    fun <Response> Channel<Unit>.consumeAsRead(asValue: ByteArray.() -> Response, onRead: suspend T.(Response) -> Unit) = consumeAsRead(asValue) { response, _ ->
        onRead(response)
    }

    fun <Trigger> Channel<Trigger>.consumeAsReadAndMutate(onRead: suspend T.(ByteArray, Trigger) -> T) = consumeAsReadAndMutate(asValue = { this }, onRead = onRead)

    fun Channel<Unit>.consumeAsReadAndMutate(onRead: suspend T.(ByteArray) -> T) = consumeAsReadAndMutate(asValue = { this }, onRead = onRead)

    fun <Trigger> Channel<Trigger>.consumeAsRead(onRead: suspend T.(ByteArray, Trigger) -> Unit) = consumeAsRead(asValue = { this }, onRead = onRead)

    fun Channel<Unit>.consumeAsRead(onRead: suspend T.(ByteArray) -> Unit) = consumeAsRead(asValue = { this }, onRead = onRead)

    fun <Trigger, Response> Flow<Trigger>.collectAsReadAndMutate(asValue: ByteArray.() -> Response, onRead: suspend T.(Response, Trigger) -> T)

    fun <Response> Flow<Unit>.collectAsReadAndMutate(asValue: ByteArray.() -> Response, onRead: suspend T.(Response) -> T) = collectAsReadAndMutate(asValue) { response, _ ->
        onRead(response)
    }

    fun <Trigger, Response> Flow<Trigger>.collectAsRead(asValue: ByteArray.() -> Response, onRead: suspend T.(Response, Trigger) -> Unit) =
        collectAsReadAndMutate(asValue) { response, trigger ->
            onRead(response, trigger)
            this
        }

    fun <Response> Flow<Unit>.collectAsRead(asValue: ByteArray.() -> Response, onRead: suspend T.(Response) -> Unit) = collectAsRead(asValue) { response, _ ->
        onRead(response)
    }

    fun <Trigger> Flow<Trigger>.collectAsReadAndMutate(onRead: suspend T.(ByteArray, Trigger) -> T) = collectAsReadAndMutate(asValue = { this }, onRead = onRead)

    fun Flow<Unit>.collectAsReadAndMutate(onRead: suspend T.(ByteArray) -> T) = collectAsReadAndMutate(asValue = { this }, onRead = onRead)

    fun <Trigger> Flow<Trigger>.collectAsRead(onRead: suspend T.(ByteArray, Trigger) -> Unit) = collectAsRead(asValue = { this }, onRead = onRead)

    fun Flow<Unit>.collectAsRead(onRead: suspend T.(ByteArray) -> Unit) = collectAsRead(asValue = { this }, onRead = onRead)

    fun <Data> Channel<Data>.consumeAsWriteAndMutate(asByte: Data.() -> ByteArray, onWrite: suspend T.(Data) -> T = { this })

    fun <Data> Channel<Data>.consumeAsWrite(asByte: Data.() -> ByteArray, onWrite: suspend T.(Data) -> Unit = {}) = consumeAsWriteAndMutate(asByte) {
        onWrite(it)
        this
    }

    fun Channel<ByteArray>.consumeAsWriteAndMutate(onWrite: suspend T.(ByteArray) -> T = { this }) = consumeAsWriteAndMutate({ this }, onWrite)

    fun Channel<ByteArray>.consumeAsWrite(onWrite: suspend T.(ByteArray) -> Unit = {}) = consumeAsWrite({ this }, onWrite)

    fun <Data> Flow<Data>.collectAsWriteAndMutate(asByte: Data.() -> ByteArray, onWrite: suspend T.(Data) -> T = { this })

    fun <Data> Flow<Data>.collectAsWrite(asByte: Data.() -> ByteArray, onWrite: suspend T.(Data) -> Unit = {}) = collectAsWriteAndMutate(asByte) {
        onWrite(it)
        this
    }

    fun Flow<ByteArray>.collectAsWriteAndMutate(onWrite: suspend T.(ByteArray) -> T = { this }) = collectAsWriteAndMutate({ this }, onWrite)

    fun Flow<ByteArray>.collectAsWrite(onWrite: suspend T.(ByteArray) -> Unit = {}) = collectAsWrite({ this }, onWrite)
}

interface ConnectedDeviceBinding<T> {
    fun service(uuid: UUID, binding: RemoteServiceBinding<T>.() -> Unit)
    fun service(uuidString: String, binding: RemoteServiceBinding<T>.() -> Unit) {
        service(uuidFrom(uuidString), binding)
    }

    fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteServiceBinding<R>.() -> Unit)
    fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteServiceBinding<R>.() -> Unit) {
        bindService(uuidFrom(uuidString), update, binding)
    }
}

interface RemoteServiceBinding<T> {

    fun service(uuid: UUID, binding: RemoteServiceBinding<T>.() -> Unit)
    fun service(uuidString: String, binding: RemoteServiceBinding<T>.() -> Unit) {
        service(uuidFrom(uuidString), binding)
    }

    fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteServiceBinding<R>.() -> Unit)
    fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteServiceBinding<R>.() -> Unit) {
        bindService(uuidFrom(uuidString), update, binding)
    }

    fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding<T>.() -> Unit)
    fun characteristic(uuidString: String, binding: RemoteCharacteristicBinding<T>.() -> Unit) {
        characteristic(uuidFrom(uuidString), binding)
    }

    fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding<R>.() -> Unit)
    fun <R> R.bindCharacteristic(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding<R>.() -> Unit) {
        bindCharacteristic(uuidFrom(uuidString), update, binding)
    }
}

interface RemoteCharacteristicBinding<T> : RemoteAttributeBinding<T> {

    fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding<T>.() -> Unit)
    fun descriptor(uuidString: String, binding: RemoteDescriptorBinding<T>.() -> Unit) {
        descriptor(uuidFrom(uuidString), binding)
    }

    fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding<R>.() -> Unit)
    fun <R> R.bindDescriptor(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding<R>.() -> Unit) {
        bindDescriptor(uuidFrom(uuidString), update, binding)
    }

    fun <R> observeAndMutate(asValue: ByteArray.() -> R, onNotification: suspend T.(R) -> T)
    fun <R> observe(asValue: ByteArray.() -> R, onNotification: suspend T.(R) -> Unit) = observeAndMutate(asValue) {
        onNotification(it)
        this
    }
    fun observeAndMutate(onNotification: suspend T.(ByteArray) -> T) = observeAndMutate(asValue = { this }, onNotification = onNotification)
    fun observe(onNotification: suspend T.(ByteArray) -> Unit) = observe(asValue = { this }, onNotification = onNotification)
}
interface RemoteDescriptorBinding<T> : RemoteAttributeBinding<T>

@JvmName("bindDevice")
fun <T> T.bind(device: Flow<ConnectableDevice?>, scope: CoroutineScope, binding: ConnectedDeviceBinding<T>.() -> Unit): StateFlow<T> = ConnectedDeviceBindingImpl(
    MutableStateFlow(this),
    device,
    scope,
).apply(binding).build()

@JvmName("bindService")
fun <T> T.bind(service: Flow<RemoteService?>, scope: CoroutineScope, binding: RemoteServiceBinding<T>.() -> Unit): StateFlow<T> = RemoteServiceBindingImpl(
    MutableStateFlow(this),
    service,
    scope,
).apply(binding).build()

@JvmName("bindCharacteristic")
fun <T> T.bind(characteristic: Flow<RemoteCharacteristic?>, scope: CoroutineScope, binding: RemoteCharacteristicBinding<T>.() -> Unit): StateFlow<T> =
    RemoteCharacteristicBindingImpl(MutableStateFlow(this), characteristic, scope).apply(binding).build()

@JvmName("bindDescriptor")
fun <T> T.bind(descriptor: Flow<RemoteDescriptor?>, scope: CoroutineScope, binding: RemoteDescriptorBinding<T>.() -> Unit): StateFlow<T> =
    RemoteDescriptorBindingImpl(MutableStateFlow(this), descriptor, scope).apply(binding).build()

private abstract class RemoteAttributeBindingImpl<T, ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    private val callingScope: MutableStateFlow<T>,
    private val attribute: Flow<RemoteAttribute<ReadAction, WriteAction>?>,
    private val scope: CoroutineScope,
) : RemoteAttributeBinding<T> {
    override fun <Trigger, Response> Channel<Trigger>.consumeAsReadAndMutate(asValue: ByteArray.() -> Response, onRead: suspend T.(Response, Trigger) -> T) {
        scope.launch {
            consumeEach { trigger ->
                val attribute = attribute.filterNotNull().first()
                val result = attribute.readValue()
                    .completedSuccessfully.await()
                when (result) {
                    is DeviceAction.Read.Result.Success -> callingScope.update { it.onRead(result.value.asValue(), trigger) }
                    is DeviceAction.Read.Result.Failure -> {}
                }
            }
        }
    }

    override fun <Trigger, Response> Flow<Trigger>.collectAsReadAndMutate(asValue: ByteArray.() -> Response, onRead: suspend T.(Response, Trigger) -> T) {
        scope.launch {
            collect { trigger ->
                val attribute = attribute.filterNotNull().first()
                val result = attribute.readValue()
                    .completedSuccessfully.await()
                when (result) {
                    is DeviceAction.Read.Result.Success -> callingScope.update { it.onRead(result.value.asValue(), trigger) }
                    is DeviceAction.Read.Result.Failure -> {}
                }
            }
        }
    }

    override fun <R> Channel<R>.consumeAsWriteAndMutate(asByte: R.() -> ByteArray, onWrite: suspend T.(R) -> T) {
        scope.launch {
            consumeEach { value ->
                val didComplete = attribute.filterNotNull().first().writeValue(value.asByte()).completedSuccessfully.await()
                if (didComplete) {
                    callingScope.update { it.onWrite(value) }
                }
            }
        }
    }

    override fun <R> Flow<R>.collectAsWriteAndMutate(asByte: R.() -> ByteArray, onWrite: suspend T.(R) -> T) {
        scope.launch {
            collect { value ->
                val didComplete = attribute.filterNotNull().first().writeValue(value.asByte()).completedSuccessfully.await()
                if (didComplete) {
                    callingScope.update { it.onWrite(value) }
                }
            }
        }
    }

    fun build(): StateFlow<T> = callingScope.asStateFlow()
}

private class RemoteServiceBindingImpl<T>(private val callingScope: MutableStateFlow<T>, private val service: Flow<RemoteService?>, private val scope: CoroutineScope) :
    RemoteServiceBinding<T> {
    override fun service(uuid: UUID, binding: RemoteServiceBinding<T>.() -> Unit) {
        RemoteServiceBindingImpl(callingScope, service, scope).binding()
    }

    override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding<R>.() -> Unit) {
        val serviceUpdates = bind(service.includedServices()[uuid], scope, binding)
        scope.launch { serviceUpdates.collect { update -> callingScope.update { scope -> scope.update(update) } } }
    }

    override fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding<T>.() -> Unit) {
        RemoteCharacteristicBindingImpl(callingScope, service.characteristics()[uuid], scope).binding()
    }

    override fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T, binding: RemoteCharacteristicBinding<R>.() -> Unit) {
        val characteristicUpdates = bind(service.characteristics()[uuid], scope, binding)
        scope.launch { characteristicUpdates.collect { update -> callingScope.update { scope -> scope.update(update) } } }
    }

    fun build(): StateFlow<T> = callingScope.asStateFlow()
}

private class RemoteCharacteristicBindingImpl<T>(
    private val callingScope: MutableStateFlow<T>,
    private val characteristic: Flow<RemoteCharacteristic?>,
    private val scope: CoroutineScope,
) : RemoteAttributeBindingImpl<T, DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(
    callingScope,
    characteristic,
    scope,
),
    RemoteCharacteristicBinding<T> {
    override fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding<T>.() -> Unit) {
        RemoteDescriptorBindingImpl(callingScope, characteristic.descriptors()[uuid], scope).binding()
    }

    override fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T, binding: RemoteDescriptorBinding<R>.() -> Unit) {
        val descriptorUpdates = bind(characteristic.descriptors()[uuid], scope, binding)
        scope.launch {
            descriptorUpdates.collect { update -> callingScope.update { scope -> scope.update(update) } }
        }
    }

    override fun <R> observeAndMutate(asValue: ByteArray.() -> R, onNotification: suspend T.(R) -> T) {
        scope.launch {
            characteristic.value().collect { value ->
                if (value != null) {
                    callingScope.update { it.onNotification(value.asValue()) }
                }
            }
        }
    }
}

private class ConnectedDeviceBindingImpl<T>(private val callingScope: MutableStateFlow<T>, private val device: Flow<ConnectableDevice?>, private val scope: CoroutineScope) :
    ConnectedDeviceBinding<T> {
    override fun service(uuid: UUID, binding: RemoteServiceBinding<T>.() -> Unit) {
        RemoteServiceBindingImpl(callingScope, device.services()[uuid], scope).binding()
    }

    override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding<R>.() -> Unit) {
        val serviceUpdate = bind(device.services()[uuid], scope, binding)
        scope.launch {
            serviceUpdate.collect { update -> callingScope.update { scope -> scope.update(update) } }
        }
    }

    fun build() = callingScope.asStateFlow()
}

private class RemoteDescriptorBindingImpl<T>(callingScope: MutableStateFlow<T>, descriptor: Flow<RemoteDescriptor?>, scope: CoroutineScope) :
    RemoteAttributeBindingImpl<T, DeviceAction.Read.Descriptor, DeviceAction.Write.Descriptor>(
        callingScope,
        descriptor,
        scope,
    ),
    RemoteDescriptorBinding<T>
