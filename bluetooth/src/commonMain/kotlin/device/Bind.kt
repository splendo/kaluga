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

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.RemoteAttribute
import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.RemoteDescriptor
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.descriptors
import com.splendo.kaluga.bluetooth.device.RemoteAttributeBinding.UnitReadBuilder
import com.splendo.kaluga.bluetooth.device.RemoteAttributeBinding.WriteBuilder
import com.splendo.kaluga.bluetooth.device.RemoteCharacteristicBinding.ObserveBuilder
import com.splendo.kaluga.bluetooth.discoveredServices
import com.splendo.kaluga.bluetooth.filterDiscovering
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.getOrNull
import com.splendo.kaluga.bluetooth.includedServices
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.startDiscovering
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.bluetooth.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import kotlin.jvm.JvmName

sealed interface RequiresServicesDiscoveredBinding<T, TransFormation> {
    interface NonMutating<T> : RequiresServicesDiscoveredBinding<T, Unit> {
        fun mutate(builder: Mutating<T>.() -> Unit)
    }

    interface Mutating<T> : RequiresServicesDiscoveredBinding<T, T>

    fun onAvailable(action: T.() -> TransFormation)
    fun onUnavailable(action: T.() -> TransFormation)
}

@Suppress("INAPPLICABLE_JVM_NAME")
interface RemoteAttributeBinding<T> {

    sealed interface ReadBuilder<T, Trigger, Response, Transformation> {

        interface NonMutating<T, Trigger, Response> : ReadBuilder<T, Trigger, Response, Unit> {
            fun mutate(builder: Mutating<T, Trigger, Response>.() -> Unit)
        }

        interface Mutating<T, Trigger, Response> : ReadBuilder<T, Trigger, Response, T>

        fun onRead(action: T.(Response, Trigger) -> Transformation)
        fun onFailedToRead(action: T.(Trigger, GattResponse.ReadError) -> Transformation)
    }

    sealed class UnitReadBuilder<T, Response, Transformation> protected constructor(private val builder: ReadBuilder<T, Unit, Response, Transformation>) {
        class NonMutating<T, Response> internal constructor(private val builder: ReadBuilder.NonMutating<T, Unit, Response>) : UnitReadBuilder<T, Response, Unit>(builder) {
            fun mutate(builder: Mutating<T, Response>.() -> Unit) = this.builder.mutate {
                Mutating(this).apply(builder)
            }
        }
        class Mutating<T, Response> internal constructor(private val builder: ReadBuilder.Mutating<T, Unit, Response>) : UnitReadBuilder<T, Response, T>(builder)

        fun onRead(action: T.(Response) -> Transformation) = builder.onRead { response, _ -> action(response) }
        fun onFailedToRead(action: T.(GattResponse.ReadError) -> Transformation) = builder.onFailedToRead { _, error -> action(error) }
    }

    sealed interface WriteBuilder<T, Data, Transformation> {

        interface NonMutating<T, Data> : WriteBuilder<T, Data, Unit> {
            fun mutate(builder: Mutating<T, Data>.() -> Unit)
        }

        interface Mutating<T, Data> : WriteBuilder<T, Data, T>

        fun onWrite(action: T.(Data) -> Transformation)
        fun onFailedToWrite(action: T.(Data, GattResponse.WriteError) -> Transformation)
    }

    fun <Trigger, Response> Channel<Trigger>.consumeToTriggerRead(asValue: ByteArray.() -> Response, builder: ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit)

    @JvmName("consumeUnitToTriggerRead")
    fun <Response> Channel<Unit>.consumeToTriggerRead(asValue: ByteArray.() -> Response, unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit) =
        consumeToTriggerRead(asValue, builder = {
            UnitReadBuilder.NonMutating(this).apply(unitBuilder)
        })

    fun <Response> Channel<Unit>.consumeToTriggerRead(
        deserializationStrategy: DeserializationStrategy<Response>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
    ) = consumeToTriggerRead({
        bluetoothFormat.decodeFromByteArray(deserializationStrategy, this)
    }, unitBuilder)

    fun <Trigger> Channel<Trigger>.consumeToTriggerRead(builder: ReadBuilder.NonMutating<T, Trigger, ByteArray>.() -> Unit) =
        consumeToTriggerRead(asValue = { this }, builder = builder)

    @JvmName("consumeUnitToTriggerRead")
    fun <Response> Channel<Unit>.consumeToTriggerRead(unitBuilder: UnitReadBuilder.NonMutating<T, ByteArray>.() -> Unit) = consumeToTriggerRead(builder = {
        UnitReadBuilder.NonMutating(this).apply(unitBuilder)
    })

    fun <Trigger, Response> Flow<Trigger>.collectToTriggerRead(asValue: ByteArray.() -> Response, builder: ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit)

    @JvmName("collectUnitToTriggerRead")
    fun <Response> Flow<Unit>.collectToTriggerRead(asValue: ByteArray.() -> Response, unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit) =
        collectToTriggerRead(asValue, builder = {
            UnitReadBuilder.NonMutating(this).apply(unitBuilder)
        })

    fun <Response> Flow<Unit>.collectToTriggerRead(
        deserializationStrategy: DeserializationStrategy<Response>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
    ) = collectToTriggerRead({
        bluetoothFormat.decodeFromByteArray(deserializationStrategy, this)
    }, unitBuilder)

    fun <Trigger> Flow<Trigger>.collectToTriggerRead(builder: ReadBuilder.NonMutating<T, Trigger, ByteArray>.() -> Unit) =
        collectToTriggerRead(asValue = { this }, builder = builder)

    @JvmName("collectUnitToTriggerRead")
    fun <Response> Flow<Unit>.collectToTriggerRead(unitBuilder: UnitReadBuilder.NonMutating<T, ByteArray>.() -> Unit) = collectToTriggerRead(builder = {
        UnitReadBuilder.NonMutating(this).apply(unitBuilder)
    })

    fun <Data> Channel<Data>.consumeToTriggerWrite(asByte: Data.() -> ByteArray, builder: WriteBuilder.NonMutating<T, Data>.() -> Unit)

    fun <Data, WriteData> Channel<Data>.consumeToTriggerWrite(
        serializationStrategy: SerializationStrategy<WriteData>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        mapper: Data.() -> WriteData,
        builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
    ) = consumeToTriggerWrite({ bluetoothFormat.encodeToByteArray(serializationStrategy, mapper()) }, builder)

    fun <Data> Channel<Data>.consumeToTriggerWrite(
        serializationStrategy: SerializationStrategy<Data>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
    ) = consumeToTriggerWrite(serializationStrategy, bluetoothFormat, { this }, builder)

    fun Channel<ByteArray>.consumeToTriggerWrite(builder: WriteBuilder.NonMutating<T, ByteArray>.() -> Unit) = consumeToTriggerWrite({ this }, builder)

    fun <Data> Flow<Data>.collectToTriggerWrite(asByte: Data.() -> ByteArray, builder: WriteBuilder.NonMutating<T, Data>.() -> Unit)

    fun <Data, WriteData> Flow<Data>.collectToTriggerWrite(
        serializationStrategy: SerializationStrategy<WriteData>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        mapper: Data.() -> WriteData,
        builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
    ) = collectToTriggerWrite({ bluetoothFormat.encodeToByteArray(serializationStrategy, mapper()) }, builder)

    fun <Data> Flow<Data>.collectToTriggerWrite(
        serializationStrategy: SerializationStrategy<Data>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
    ) = collectToTriggerWrite(serializationStrategy, bluetoothFormat, { this }, builder)

    fun Flow<ByteArray>.collectToTriggerWrite(builder: WriteBuilder.NonMutating<T, ByteArray>.() -> Unit) = collectToTriggerWrite({ this }, builder)
}

inline fun <reified Response, T> Channel<Unit>.consumeToTriggerRead(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
) = with(remoteAttributeBinding) {
    consumeToTriggerRead(bluetoothFormat.serializersModule.serializer<Response>(), bluetoothFormat, unitBuilder)
}

inline fun <reified Response, T> Flow<Unit>.collectToTriggerRead(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
) = with(remoteAttributeBinding) {
    collectToTriggerRead(bluetoothFormat.serializersModule.serializer<Response>(), bluetoothFormat, unitBuilder)
}

inline fun <Data, reified WriteData, T> Channel<Data>.consumeToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline mapper: Data.() -> WriteData,
    noinline builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
) = with(remoteAttributeBinding) {
    consumeToTriggerWrite(bluetoothFormat.serializersModule.serializer<WriteData>(), bluetoothFormat, mapper, builder)
}

inline fun <reified Data, T> Channel<Data>.consumeToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
) = with(remoteAttributeBinding) {
    consumeToTriggerWrite(bluetoothFormat.serializersModule.serializer<Data>(), bluetoothFormat, builder)
}

inline fun <Data, reified WriteData, T> Flow<Data>.collectToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline mapper: Data.() -> WriteData,
    noinline builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
) = with(remoteAttributeBinding) {
    collectToTriggerWrite(bluetoothFormat.serializersModule.serializer<WriteData>(), bluetoothFormat, mapper, builder)
}

inline fun <reified Data, T> Flow<Data>.collectToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
) = with(remoteAttributeBinding) {
    collectToTriggerWrite(bluetoothFormat.serializersModule.serializer<Data>(), bluetoothFormat, builder)
}

sealed interface ConnectedDeviceBinding<T> {

    interface EnsuresAvailable<T> : ConnectedDeviceBinding<T> {

        fun service(uuid: UUID, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit)
        fun service(uuidString: String, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }

        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit)
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }
    }

    interface RequiresServicesDiscovered<T> :
        ConnectedDeviceBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> {

        fun service(uuid: UUID, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit)
        fun service(uuidString: String, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }

        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit)
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }
    }
}

interface RemoteServiceBinding<T> {

    interface EnsuresAvailable<T> : RemoteServiceBinding<T> {

        fun service(uuid: UUID, binding: EnsuresAvailable<T>.() -> Unit)
        fun service(uuidString: String, binding: EnsuresAvailable<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }
        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: EnsuresAvailable<R>.() -> Unit)
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: EnsuresAvailable<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }

        fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit)
        fun characteristic(uuidString: String, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit) {
            characteristic(uuidFrom(uuidString), binding)
        }

        fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<R>.() -> Unit)
        fun <R> R.bindCharacteristic(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<R>.() -> Unit) {
            bindCharacteristic(uuidFrom(uuidString), update, binding)
        }
    }
    interface RequiresServicesDiscovered<T> :
        RemoteServiceBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> {
        fun service(uuid: UUID, binding: RequiresServicesDiscovered<T>.() -> Unit)
        fun service(uuidString: String, binding: RequiresServicesDiscovered<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }
        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RequiresServicesDiscovered<R>.() -> Unit)
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RequiresServicesDiscovered<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }

        fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit)
        fun characteristic(uuidString: String, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            characteristic(uuidFrom(uuidString), binding)
        }

        fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<R>.() -> Unit)
        fun <R> R.bindCharacteristic(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            bindCharacteristic(uuidFrom(uuidString), update, binding)
        }
    }
}

sealed interface RemoteCharacteristicBinding<T> : RemoteAttributeBinding<T> {

    interface EnsuresServicesAvailable<T> : RemoteCharacteristicBinding<T> {
        fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding<T>.() -> Unit)
        fun descriptor(uuidString: String, binding: RemoteDescriptorBinding<T>.() -> Unit) {
            descriptor(uuidFrom(uuidString), binding)
        }

        fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding<R>.() -> Unit)
        fun <R> R.bindDescriptor(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding<R>.() -> Unit) {
            bindDescriptor(uuidFrom(uuidString), update, binding)
        }
    }

    interface RequiresServicesDiscovered<T> :
        RemoteCharacteristicBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> {
        fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit)
        fun descriptor(uuidString: String, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            descriptor(uuidFrom(uuidString), binding)
        }

        fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<R>.() -> Unit)
        fun <R> R.bindDescriptor(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            bindDescriptor(uuidFrom(uuidString), update, binding)
        }
    }

    sealed interface ObserveBuilder<T, Notification, Transformation> {
        interface NonMutating<T, Notification> : ObserveBuilder<T, Notification, Unit> {
            fun mutate(builder: Mutating<T, Notification>.() -> Unit)
        }
        interface Mutating<T, Notification> : ObserveBuilder<T, Notification, T>

        fun onNotification(action: suspend T.(Notification) -> Transformation)
    }

    fun <R> observe(asValue: ByteArray.() -> R, builder: ObserveBuilder.NonMutating<T, R>.() -> Unit)
    fun <R> observe(deserializationStrategy: DeserializationStrategy<R>, bluetoothFormat: BluetoothFormat = BluetoothFormat, builder: ObserveBuilder.NonMutating<T, R>.() -> Unit) =
        observe({ bluetoothFormat.decodeFromByteArray(deserializationStrategy, this) }, builder)
    fun observe(builder: ObserveBuilder.NonMutating<T, ByteArray>.() -> Unit) = observe(asValue = { this }, builder = builder)
}

inline fun <reified R, T> RemoteCharacteristicBinding<T>.observe(
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline builder: ObserveBuilder.NonMutating<T, R>.() -> Unit,
) = observe(bluetoothFormat.serializersModule.serializer<R>(), bluetoothFormat, builder)

interface RemoteDescriptorBinding<T> : RemoteAttributeBinding<T> {
    interface RequiresServicesDiscovered<T> :
        RemoteDescriptorBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T>
}

@JvmName("bindDevice")
fun <T> T.bind(device: Flow<ConnectableDevice?>, scope: CoroutineScope, binding: ConnectedDeviceBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = ConnectedDeviceBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        device,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        device.startDiscovering().collect { discoveringState ->
            if (discoveringState != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

fun <T> T.bind(device: ConnectableDevice, scope: CoroutineScope, binding: ConnectedDeviceBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = ConnectedDeviceBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        flowOf(device),
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        device.filterDiscovering().collect { discoveringState ->
            if (discoveringState != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

fun <T> T.bind(
    deviceState: ConnectableDeviceState.Connected.DiscoveredServices,
    scope: CoroutineScope,
    binding: ConnectedDeviceBinding.EnsuresAvailable<T>.() -> Unit,
): StateFlow<T> = ConnectedDeviceBindingImpl.EnsuresServicesAvailable(
    MutableStateFlow(this),
    deviceState,
    scope,
).apply(binding).build()

@JvmName("bindService")
fun <T> T.bind(service: Flow<RemoteService?>, scope: CoroutineScope, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = RemoteServiceBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        service,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        service.collect { service ->
            if (service != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

fun <T> T.bind(service: RemoteService, scope: CoroutineScope, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit): StateFlow<T> =
    RemoteServiceBindingImpl.EnsuresServicesAvailable(
        MutableStateFlow(this),
        service,
        scope,
    ).apply(binding).build()

@JvmName("bindCharacteristic")
fun <T> T.bind(characteristic: Flow<RemoteCharacteristic?>, scope: CoroutineScope, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = RemoteCharacteristicBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        characteristic,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        characteristic.collect { service ->
            if (service != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

fun <T> T.bind(characteristic: RemoteCharacteristic, scope: CoroutineScope, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit) =
    RemoteCharacteristicBindingImpl.EnsuresServicesAvailable(
        MutableStateFlow(this),
        characteristic,
        scope,
    ).apply(binding).build()

@JvmName("bindDescriptor")
fun <T> T.bind(descriptor: Flow<RemoteDescriptor?>, scope: CoroutineScope, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = RemoteDescriptorBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        descriptor,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        descriptor.collect { service ->
            if (service != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

fun <T> T.bind(descriptor: RemoteDescriptor, scope: CoroutineScope, binding: RemoteDescriptorBinding<T>.() -> Unit): StateFlow<T> =
    RemoteDescriptorBindingImpl.EnsuresAvailable(MutableStateFlow(this), descriptor, scope).apply(binding).build()

private sealed class ConnectedDeviceBindingImpl<T>(protected val callingScope: MutableStateFlow<T>, protected val scope: CoroutineScope) : ConnectedDeviceBinding<T> {

    protected val bindingSubActions = mutableListOf<() -> Unit>()
    protected val observations = mutableListOf<suspend CoroutineScope.() -> Unit>()

    class EnsuresServicesAvailable<T>(callingScope: MutableStateFlow<T>, private val deviceState: ConnectableDeviceState.Connected.DiscoveredServices, scope: CoroutineScope) :
        ConnectedDeviceBindingImpl<T>(callingScope, scope),
        ConnectedDeviceBinding.EnsuresAvailable<T> {
        override fun service(uuid: UUID, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.EnsuresServicesAvailable(callingScope, deviceState.services[uuid], scope).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.EnsuresServicesAvailable(MutableStateFlow(this), deviceState.services[uuid], scope).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    class RequiresServicesDiscovered<T>(
        callingScope: MutableStateFlow<T>,
        private val device: Flow<ConnectableDevice?>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
        scope: CoroutineScope,
    ) : ConnectedDeviceBindingImpl<T>(callingScope, scope),
        ConnectedDeviceBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions) {
        override fun service(uuid: UUID, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.RequiresServicesDiscovered(
                callingScope,
                device.discoveredServices()[uuid],
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.RequiresServicesDiscovered(
                MutableStateFlow(this),
                device.discoveredServices()[uuid],
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    fun build(): StateFlow<T> {
        bindingSubActions.forEach { it() }
        observations.forEach {
            scope.launch(block = it)
        }

        return callingScope.asStateFlow()
    }
}

private sealed class RemoteServiceBindingImpl<T>(protected val callingScope: MutableStateFlow<T>, protected val scope: CoroutineScope) : RemoteServiceBinding<T> {

    protected val bindingSubActions = mutableListOf<() -> Unit>()
    protected val observations = mutableListOf<suspend CoroutineScope.() -> Unit>()

    class EnsuresServicesAvailable<T>(callingScope: MutableStateFlow<T>, private val service: RemoteService, scope: CoroutineScope) :
        RemoteServiceBindingImpl<T>(callingScope, scope),
        RemoteServiceBinding.EnsuresAvailable<T> {
        override fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.EnsuresServicesAvailable(callingScope, service.characteristics[uuid], scope).apply(binding)
            bindingSubActions += {
                characteristicBinding.build()
            }
        }

        override fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<R>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.EnsuresServicesAvailable(MutableStateFlow(this), service.characteristics[uuid], scope).apply(binding)
            observations += {
                characteristicBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun service(uuid: UUID, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit) {
            val serviceBinding = EnsuresServicesAvailable(callingScope, service.includedServices[uuid], scope).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit) {
            val serviceBinding = EnsuresServicesAvailable(MutableStateFlow(this), service.includedServices[uuid], scope).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    class RequiresServicesDiscovered<T>(
        callingScope: MutableStateFlow<T>,
        private val service: Flow<RemoteService?>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
        scope: CoroutineScope,
    ) : RemoteServiceBindingImpl<T>(callingScope, scope),
        RemoteServiceBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions) {

        override fun service(uuid: UUID, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val serviceBinding = RequiresServicesDiscovered(
                callingScope,
                service.includedServices().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val serviceBinding = RequiresServicesDiscovered(
                MutableStateFlow(this),
                service.includedServices().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.RequiresServicesDiscovered(
                callingScope,
                service.characteristics().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                characteristicBinding.build()
            }
        }

        override fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.RequiresServicesDiscovered(
                MutableStateFlow(this),
                service.characteristics().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                characteristicBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    fun build(): StateFlow<T> {
        bindingSubActions.forEach { it() }
        observations.forEach {
            scope.launch(block = it)
        }

        return callingScope.asStateFlow()
    }
}

private sealed class RemoteCharacteristicBindingImpl<T>(callingScope: MutableStateFlow<T>, getCharacteristic: suspend () -> RemoteCharacteristic?, scope: CoroutineScope) :
    RemoteAttributeBindingImpl<T, DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(
        callingScope,
        getCharacteristic,
        scope,
    ),
    RemoteCharacteristicBinding<T> {

    class EnsuresServicesAvailable<T>(private val callingScope: MutableStateFlow<T>, private val characteristic: RemoteCharacteristic, private val scope: CoroutineScope) :
        RemoteCharacteristicBindingImpl<T>(
            callingScope,
            { characteristic },
            scope,
        ),
        RemoteCharacteristicBinding.EnsuresServicesAvailable<T> {

        override fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding<T>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.EnsuresAvailable(callingScope, characteristic.descriptors[uuid], scope).apply(binding)
            bindingSubActions += {
                descriptorBinding.build()
            }
        }

        override fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T, binding: RemoteDescriptorBinding<R>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.EnsuresAvailable(MutableStateFlow(this), characteristic.descriptors[uuid], scope).apply(binding)
            observations += {
                descriptorBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun <R> observe(asValue: ByteArray.() -> R, builder: RemoteCharacteristicBinding.ObserveBuilder.NonMutating<T, R>.() -> Unit) {
            val builder = ObserverBuilder.NonMutating<T, R>().apply(builder)
            val onNotificationActions = builder.notificationActions()
            observations += {
                characteristic.value().collect { value ->
                    onNotificationActions.forEach { action ->
                        callingScope.update { it.action(value.asValue()) }
                    }
                }
            }
        }
    }

    class RequiresServicesDiscovered<T>(
        private val callingScope: MutableStateFlow<T>,
        private val characteristic: Flow<RemoteCharacteristic?>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
        private val scope: CoroutineScope,
    ) : RemoteCharacteristicBindingImpl<T>(
        callingScope,
        { characteristic.firstOrNull() },
        scope,
    ),
        RemoteCharacteristicBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions) {

        override fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.RequiresServicesDiscovered(
                callingScope,
                characteristic.descriptors().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                descriptorBinding.build()
            }
        }

        override fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.RequiresServicesDiscovered(
                MutableStateFlow(this),
                characteristic.descriptors().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                descriptorBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun <R> observe(asValue: ByteArray.() -> R, builder: RemoteCharacteristicBinding.ObserveBuilder.NonMutating<T, R>.() -> Unit) {
            val builder = ObserverBuilder.NonMutating<T, R>().apply(builder)
            val onNotificationActions = builder.notificationActions()
            observations += {
                characteristic.value().collect { value ->
                    onNotificationActions.forEach { action ->
                        callingScope.update { it.action(value.asValue()) }
                    }
                }
            }
        }
    }

    private sealed class ObserverBuilder<T, Notification> {

        protected val onNotificationActions = mutableListOf<suspend T.(Notification) -> T>()

        class NonMutating<T, Notification> :
            ObserverBuilder<T, Notification>(),
            RemoteCharacteristicBinding.ObserveBuilder.NonMutating<T, Notification> {
            override fun mutate(builder: RemoteCharacteristicBinding.ObserveBuilder.Mutating<T, Notification>.() -> Unit) {
                val mutating = Mutating<T, Notification>().apply(builder)
                onNotificationActions += mutating.notificationActions()
            }

            override fun onNotification(action: suspend T.(Notification) -> Unit) {
                onNotificationActions += { notification ->
                    action(notification)
                    this
                }
            }
        }

        class Mutating<T, Notification> :
            ObserverBuilder<T, Notification>(),
            RemoteCharacteristicBinding.ObserveBuilder.Mutating<T, Notification> {

            override fun onNotification(action: suspend T.(Notification) -> T) {
                onNotificationActions += action
            }
        }

        fun notificationActions() = onNotificationActions.toList()
    }

    // override fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding<T>.() -> Unit) {
    //     RemoteDescriptorBindingImpl(callingScope, characteristic.descriptors().getOrNull(uuid), scope).binding()
    // }
    //
    // override fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T, binding: RemoteDescriptorBinding<R>.() -> Unit) {
    //     val descriptorUpdates = bind(characteristic.descriptors().getOrNull(uuid), scope, binding)
    //     scope.launch {
    //         descriptorUpdates.collect { update -> callingScope.update { scope -> scope.update(update) } }
    //     }
    // }
    //
}

private sealed class RemoteDescriptorBindingImpl<T>(callingScope: MutableStateFlow<T>, getDescriptor: suspend () -> RemoteDescriptor?, scope: CoroutineScope) :
    RemoteAttributeBindingImpl<T, DeviceAction.Read.Descriptor, DeviceAction.Write.Descriptor>(
        callingScope,
        getDescriptor,
        scope,
    ) {

    class EnsuresAvailable<T>(callingScope: MutableStateFlow<T>, descriptor: RemoteDescriptor, scope: CoroutineScope) :
        RemoteDescriptorBindingImpl<T>(
            callingScope,
            { descriptor },
            scope,
        ),
        RemoteDescriptorBinding<T>

    class RequiresServicesDiscovered<T>(
        callingScope: MutableStateFlow<T>,
        descriptors: Flow<RemoteDescriptor?>,
        onServicesDiscoveredActions: MutableList<() -> Unit>,
        onServicesUndiscoveredActions: MutableList<() -> Unit>,
        scope: CoroutineScope,
    ) : RemoteDescriptorBindingImpl<T>(
        callingScope,
        { descriptors.firstOrNull() },
        scope,
    ),
        RemoteDescriptorBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions)
}

private abstract class RemoteAttributeBindingImpl<T, ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    private val callingScope: MutableStateFlow<T>,
    private val getAttribute: suspend () -> RemoteAttribute<ReadAction, WriteAction>?,
    private val scope: CoroutineScope,
) : RemoteAttributeBinding<T> {

    private sealed class ReadBuilder<T, Trigger, Response> {

        protected val onReadActions = mutableListOf<T.(Response, Trigger) -> T>()
        protected val onFailedToReadActions = mutableListOf<T.(Trigger, GattResponse.ReadError) -> T>()

        class NonMutating<T, Trigger, Response> :
            ReadBuilder<T, Trigger, Response>(),
            RemoteAttributeBinding.ReadBuilder.NonMutating<T, Trigger, Response> {
            override fun mutate(builder: RemoteAttributeBinding.ReadBuilder.Mutating<T, Trigger, Response>.() -> Unit) {
                val mutating = Mutating<T, Trigger, Response>().apply(builder)
                onReadActions += mutating.readActions()
                onFailedToReadActions += mutating.failedToReadActions()
            }

            override fun onRead(action: T.(Response, Trigger) -> Unit) {
                onReadActions += { response, trigger ->
                    action(response, trigger)
                    this
                }
            }

            override fun onFailedToRead(action: T.(Trigger, GattResponse.ReadError) -> Unit) {
                onFailedToReadActions += { trigger, error ->
                    action(trigger, error)
                    this
                }
            }
        }

        class Mutating<T, Trigger, Response> :
            ReadBuilder<T, Trigger, Response>(),
            RemoteAttributeBinding.ReadBuilder.Mutating<T, Trigger, Response> {

            override fun onRead(action: T.(Response, Trigger) -> T) {
                onReadActions += action
            }
            override fun onFailedToRead(action: T.(Trigger, GattResponse.ReadError) -> T) {
                onFailedToReadActions += action
            }
        }

        fun readActions() = onReadActions.toList()
        fun failedToReadActions() = onFailedToReadActions.toList()
    }

    private sealed class WriteBuilder<T, Data, Transformation> {

        protected val onWriteActions = mutableListOf<T.(Data) -> T>()
        protected val onFailedToWriteActions = mutableListOf<T.(Data, GattResponse.WriteError) -> T>()
        class NonMutating<T, Data> :
            WriteBuilder<T, Data, Unit>(),
            RemoteAttributeBinding.WriteBuilder.NonMutating<T, Data> {
            override fun mutate(builder: RemoteAttributeBinding.WriteBuilder.Mutating<T, Data>.() -> Unit) {
                val mutating = Mutating<T, Data>().apply(builder)
                onWriteActions += mutating.writeActions()
                onFailedToWriteActions += mutating.failedToWriteActions()
            }

            override fun onWrite(action: T.(Data) -> Unit) {
                onWriteActions += { data ->
                    action(data)
                    this
                }
            }

            override fun onFailedToWrite(action: T.(Data, GattResponse.WriteError) -> Unit) {
                onFailedToWriteActions += { data, error ->
                    action(data, error)
                    this
                }
            }
        }

        class Mutating<T, Data> :
            WriteBuilder<T, Data, T>(),
            RemoteAttributeBinding.WriteBuilder.Mutating<T, Data> {

            override fun onWrite(action: T.(Data) -> T) {
                onWriteActions += action
            }
            override fun onFailedToWrite(action: T.(Data, GattResponse.WriteError) -> T) {
                onFailedToWriteActions += action
            }
        }

        fun writeActions() = onWriteActions.toList()
        fun failedToWriteActions() = onFailedToWriteActions.toList()
    }

    protected val bindingSubActions = mutableListOf<() -> Unit>()
    protected val observations = mutableListOf<suspend CoroutineScope.() -> Unit>()

    override fun <Trigger, Response> Channel<Trigger>.consumeToTriggerRead(
        asValue: ByteArray.() -> Response,
        builder: RemoteAttributeBinding.ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit,
    ) {
        val builder = ReadBuilder.NonMutating<T, Trigger, Response>().apply(builder)
        val onReadActions = builder.readActions()
        val onFailedToReadActions = builder.failedToReadActions()
        observations += {
            consumeEach { trigger ->
                read(trigger, asValue, onReadActions, onFailedToReadActions)
            }
        }
    }

    override fun <Trigger, Response> Flow<Trigger>.collectToTriggerRead(
        asValue: ByteArray.() -> Response,
        builder: RemoteAttributeBinding.ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit,
    ) {
        val builder = ReadBuilder.NonMutating<T, Trigger, Response>().apply(builder)
        val onReadActions = builder.readActions()
        val onFailedToReadActions = builder.failedToReadActions()
        observations += {
            collect { trigger ->
                read(trigger, asValue, onReadActions, onFailedToReadActions)
            }
        }
    }

    override fun <Data> Channel<Data>.consumeToTriggerWrite(asByte: Data.() -> ByteArray, builder: RemoteAttributeBinding.WriteBuilder.NonMutating<T, Data>.() -> Unit) {
        val builder = WriteBuilder.NonMutating<T, Data>().apply(builder)
        val onWriteActions = builder.writeActions()
        val onFailedToWriteActions = builder.failedToWriteActions()
        observations += {
            consumeEach { value ->
                write(value, asByte, onWriteActions, onFailedToWriteActions)
            }
        }
    }

    override fun <Data> Flow<Data>.collectToTriggerWrite(asByte: Data.() -> ByteArray, builder: RemoteAttributeBinding.WriteBuilder.NonMutating<T, Data>.() -> Unit) {
        val builder = WriteBuilder.NonMutating<T, Data>().apply(builder)
        val onWriteActions = builder.writeActions()
        val onFailedToWriteActions = builder.failedToWriteActions()
        observations += {
            collect { value ->
                write(value, asByte, onWriteActions, onFailedToWriteActions)
            }
        }
    }

    private suspend fun <Trigger, Response> read(
        trigger: Trigger,
        asResponse: ByteArray.() -> Response,
        onReadActions: List<T.(Response, Trigger) -> T>,
        onFailedToReadActions: List<T.(Trigger, GattResponse.ReadError) -> T>,
    ) = when (val result = getAttribute()?.readValue()?.response?.await() ?: GattResponse.DeviceUnavailable) {
        is GattResponse.ReadSuccess -> onReadActions.forEach { action ->
            callingScope.update { it.action(result.value.asResponse(), trigger) }
        }
        is GattResponse.ReadError -> onFailedToReadActions.forEach { action ->
            callingScope.update { it.action(trigger, result) }
        }
    }

    private suspend fun <Data> write(
        value: Data,
        asByte: Data.() -> ByteArray,
        onWriteActions: List<T.(Data) -> T>,
        onFailedToWriteActions: List<T.(Data, GattResponse.WriteError) -> T>,
    ) = when (val result = getAttribute()?.writeValue(value.asByte())?.response?.await() ?: GattResponse.DeviceUnavailable) {
        is GattResponse.WriteSuccess -> onWriteActions.forEach { action ->
            callingScope.update { it.action(value) }
        }
        is GattResponse.WriteError -> onFailedToWriteActions.forEach { action ->
            callingScope.update { it.action(value, result) }
        }
    }
    fun build(): StateFlow<T> {
        bindingSubActions.forEach { it() }
        observations.forEach {
            scope.launch(block = it)
        }

        return callingScope.asStateFlow()
    }
}

internal sealed class RequiresServicesDiscoveredImpl<T> {
    class NonMutating<T>(
        private val callingScope: MutableStateFlow<T>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
    ) : RequiresServicesDiscoveredImpl<T>(),
        RequiresServicesDiscoveredBinding.NonMutating<T> {

        override fun mutate(builder: RequiresServicesDiscoveredBinding.Mutating<T>.() -> Unit) {
            val mutating = Mutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions).apply(builder)
        }

        override fun onAvailable(action: T.() -> Unit) {
            onServicesDiscoveredActions += {
                callingScope.value.action()
            }
        }

        override fun onUnavailable(action: T.() -> Unit) {
            onServicesUndiscoveredActions += {
                callingScope.value.action()
            }
        }
    }

    class Mutating<T>(
        private val callingScope: MutableStateFlow<T>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
    ) : RequiresServicesDiscoveredImpl<T>(),
        RequiresServicesDiscoveredBinding.Mutating<T> {
        override fun onAvailable(action: T.() -> T) {
            onServicesDiscoveredActions += {
                callingScope.update { it.action() }
            }
        }

        override fun onUnavailable(action: T.() -> T) {
            onServicesUndiscoveredActions += {
                callingScope.update { it.action() }
            }
        }
    }
}
