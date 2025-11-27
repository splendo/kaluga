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

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Notifiable
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Static
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy

internal typealias Notify = suspend (characteristic: Notifiable, device: ConnectedDevice, value: ByteArray) -> Boolean
internal typealias BuildDescriptor = (
    uuid: UUID,
) -> LocalDescriptorDSL?

sealed class LocalCharacteristic(val wrapper: LocalCharacteristicWrapper, override val service: LocalService) : Characteristic {

    interface DSL {
        fun readable(encrypted: Boolean = false, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)

        fun readableAlwaysSuccess(encrypted: Boolean = false, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> ByteArray) {
            readable(encrypted) { device, offset ->
                GattResponse.ReadSuccess(onRead(this, device, offset))
            }
        }

        fun <T> readableAlwaysSuccess(
            encrypted: Boolean = false,
            onRead: suspend LocalCharacteristic.(ConnectedDevice) -> T,
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) {
            readableAlwaysSuccess(encrypted) { device, offset ->
                bluetoothFormat.encodeToByteArray(
                    serializationStrategy,
                    onRead(device),
                ).drop(offset).toByteArray()
            }
        }

        fun writable(
            properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
            encrypted: Boolean = false,
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
        )

        fun writableAlwaysSuccess(
            properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
            encrypted: Boolean = false,
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> Unit,
        ) {
            writable(properties, encrypted) { device, value, offset ->
                onWrite(device, value, offset)
                GattResponse.WriteSuccess
            }
        }

        fun <T> writableAlwaysSuccess(
            properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
            encrypted: Boolean = false,
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, T) -> Unit,
            serializationStrategy: DeserializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) {
            val cache = mutableMapOf<ConnectedDevice, ByteArray>()
            writableAlwaysSuccess(properties, encrypted) { device, value, offset ->
                val currentCache = cache[device] ?: byteArrayOf()
                val valueToDeserialize = when (offset) {
                    0 -> {
                        cache.remove(device)
                        value
                    }
                    currentCache.size -> {
                        currentCache + value
                    }
                    else -> null
                }
                valueToDeserialize?.let {
                    try {
                        onWrite(device, bluetoothFormat.decodeFromByteArray(serializationStrategy, it))
                    } catch (e: SerializationException) {
                        cache[device] = valueToDeserialize
                    }
                }
            }
        }

        fun notifiable(
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
            onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
        )

        fun descriptor(uuid: UUID, descriptor: LocalDescriptor.DSL.() -> Unit)
        fun descriptor(uuidString: String, descriptor: LocalDescriptor.DSL.() -> Unit) {
            descriptor(uuidFrom(uuidString), descriptor)
        }

        fun <T> Flow<T>.collectAsNotification(
            scope: CoroutineScope,
            started: SharingStarted,
            replay: Int = 0,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            toByteArray: T.() -> ByteArray,
        ) {
            val sharedFlow = shareIn(scope, started, replay)
            sharedFlow.collectAsNotification(scope, properties, encrypted, toByteArray)
        }

        fun <T> Flow<T>.collectAsNotification(
            scope: CoroutineScope,
            started: SharingStarted,
            replay: Int = 0,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) = collectAsNotification(
            scope,
            started,
            replay,
            properties,
            encrypted,
            { bluetoothFormat.encodeToByteArray(serializationStrategy, this) },
        )

        fun Flow<ByteArray>.collectAsNotification(
            scope: CoroutineScope,
            started: SharingStarted,
            replay: Int = 0,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
        ) = collectAsNotification(scope, started, replay, properties, encrypted, { this })

        fun <T> SharedFlow<T>.collectAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            toByteArray: T.() -> ByteArray,
        ) {
            val observingJobs = concurrentMutableMapOf<ConnectedDevice, Job>()
            notifiable(
                properties,
                encrypted,
                onSubscribe = { device ->
                    observingJobs[device] = scope.launch {
                        map { it.toByteArray() }.collect { value ->
                            notify(device, value)
                        }
                    }
                },
                onUnsubscribe = { device ->
                    observingJobs.remove(device)?.cancel()
                },
            )
        }

        fun <T> SharedFlow<T>.collectAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) = collectAsNotification(
            scope,
            properties,
            encrypted,
            { bluetoothFormat.encodeToByteArray(serializationStrategy, this) },
        )

        fun SharedFlow<ByteArray>.collectAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
        ) = collectAsNotification(scope, properties, encrypted, { this })

        fun <T> StateFlow<T>.collectAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            toByteArray: T.() -> ByteArray,
        ) {
            val hasStarted = CompletableDeferred<Unit>()
            notifiable(
                properties,
                encrypted,
                onSubscribe = { device ->
                    // We only know the Characteristic on first subscription, so this is the point at which to collect the state flow
                    if (hasStarted.complete(Unit)) {
                        scope.launch {
                            map { it.toByteArray() }.collect(this@notifiable)
                        }
                    } else {
                        // If scope already launched, then the subscription will have missed the initial value. So report it immediately
                        scope.launch {
                            notify(device, value.toByteArray())
                        }
                    }
                },
                onUnsubscribe = {},
            )
        }

        fun <T> StateFlow<T>.collectAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) = collectAsNotification(
            scope,
            properties,
            encrypted,
            { bluetoothFormat.encodeToByteArray(serializationStrategy, this) },
        )

        fun StateFlow<ByteArray>.collectAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
        ) = collectAsNotification(scope, properties, encrypted, { this })

        fun <T> ReceiveChannel<T>.consumeAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            toByteArray: T.() -> ByteArray,
        ) {
            val hasStarted = CompletableDeferred<Unit>()
            notifiable(
                properties,
                encrypted,
                onSubscribe = { device ->
                    // We only know the Characteristic on first subscription, so this is the point at which to collect the state flow
                    if (hasStarted.complete(Unit)) {
                        scope.launch {
                            consumeEach { value ->
                                notify(device, value.toByteArray())
                            }
                        }
                    }
                },
                onUnsubscribe = {},
            )
        }

        fun <T> ReceiveChannel<T>.consumeAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) = consumeAsNotification(
            scope,
            properties,
            encrypted,
            { bluetoothFormat.encodeToByteArray(serializationStrategy, this) },
        )

        fun ReceiveChannel<ByteArray>.consumeAsNotification(
            scope: CoroutineScope,
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
        ) = consumeAsNotification(scope, properties, encrypted, { this })
    }

    enum class Permission {
        READABLE,
        WRITABLE,
        READ_ENCRYPTION_REQUIRED,
        WRITE_ENCRYPTION_REQUIRED,
    }

    class Static internal constructor(wrapper: LocalCharacteristicWrapper, service: LocalService, buildDescriptors: Static.() -> List<LocalDescriptor>) :
        LocalCharacteristic(wrapper, service) {
        override val descriptors: List<LocalDescriptor> = buildDescriptors().also { descriptors ->
            descriptors.forEach { wrapper.addDescriptor(it.wrapper) }
        }
    }

    class Notifiable internal constructor(
        wrapper: LocalCharacteristicWrapper,
        service: LocalService,
        private val notify: Notify,
        private val onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
        private val onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
        buildDescriptors: Notifiable.() -> List<LocalDescriptor>,
    ) : LocalCharacteristic(wrapper, service),
        FlowCollector<ByteArray> {

        private val _subscribedDevices = MutableStateFlow(emptyList<ConnectedDevice>())
        val subscribedDevices = _subscribedDevices.asStateFlow()
        override val descriptors: List<LocalDescriptor> = buildDescriptors()
        suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean = subscribedDevices.map { devices ->
            devices.find { it == device }
        }.distinctUntilChanged().transformLatest { device ->
            emit(device?.let { notify(this@Notifiable, device, value) } ?: false)
        }.first()

        suspend fun notifyAll(value: ByteArray): Boolean {
            var result = true
            for (device in subscribedDevices.value) {
                result = result or notify(device, value)
            }
            return result
        }

        override suspend fun emit(value: ByteArray) {
            notifyAll(value)
        }

        internal fun subscribe(device: ConnectedDevice) {
            _subscribedDevices.update { it + device }
            onSubscribe(device)
        }
        internal fun unsubscribe(device: ConnectedDevice) {
            _subscribedDevices.update { it - device }
            onUnsubscribe(device)
        }

        internal fun unsubscribeAll() {
            subscribedDevices.value.forEach(::unsubscribe)
        }
    }

    override val uuid: UUID = wrapper.uuid
    override val properties: Set<CharacteristicProperty> = wrapper.properties

    abstract override val descriptors: List<LocalDescriptor>
    val permissions: Set<Permission> = wrapper.permissions
}

internal class LocalCharacteristicDSL(
    val uuid: UUID,
    private val notify: Notify,
    private val registerCharacteristicReadAction: LocalCharacteristicRegisterReadAction,
    private val registerCharacteristicWriteAction: LocalCharacteristicRegisterWriteAction,
    private val registerSubscriptionActions: NotifiableRegisterSubscription,
    private val buildDescriptor: BuildDescriptor,
) : LocalCharacteristic.DSL {

    val properties = mutableSetOf<CharacteristicProperty>()
    var encryptedNotification = false
    val permissions = mutableSetOf<LocalCharacteristic.Permission>()

    private var readAction: (suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)? = null
    private var writeAction: (suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)? = null
    private var subscriptionActions: Pair<Notifiable.(ConnectedDevice) -> Unit, Notifiable.(ConnectedDevice) -> Unit>? = null
    protected val descriptorBuilders = mutableListOf<LocalDescriptorDSL>()

    override fun readable(encrypted: Boolean, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse) {
        require(readAction == null) { "Read already set" }
        properties.add(CharacteristicProperty.Read)
        permissions.add(if (encrypted) LocalCharacteristic.Permission.READ_ENCRYPTION_REQUIRED else LocalCharacteristic.Permission.READABLE)
        readAction = onRead
    }

    override fun writable(
        properties: Set<CharacteristicProperty.Writable>,
        encrypted: Boolean,
        onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
    ) {
        require(writeAction == null) { "Write already set" }
        require(properties.isNotEmpty()) { "properties cannot be empty" }
        this.properties.addAll(properties)
        permissions.add(if (encrypted) LocalCharacteristic.Permission.WRITE_ENCRYPTION_REQUIRED else LocalCharacteristic.Permission.WRITABLE)
        writeAction = onWrite
    }

    override fun notifiable(
        properties: Set<CharacteristicProperty.Notifiable>,
        encrypted: Boolean,
        onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
        onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
    ) {
        require(subscriptionActions == null) { "Notifiable already set" }
        this.properties.addAll(properties)
        encryptedNotification = encrypted
        subscriptionActions = onSubscribe to onUnsubscribe
    }

    override fun descriptor(uuid: UUID, descriptor: LocalDescriptor.DSL.() -> Unit) {
        require(descriptorBuilders.none { it.uuid == uuid }) { "Descriptor $uuid already declared" }
        buildDescriptor(uuid)?.let {
            descriptorBuilders.add(it.apply(descriptor))
        }
    }

    fun build(forService: LocalService): LocalCharacteristic {
        val characteristic = subscriptionActions?.let { (onSubscribe, onUnsubscribe) ->
            Notifiable(
                LocalCharacteristicWrapper(uuid, properties, encryptedNotification, permissions),
                forService,
                notify,
                onSubscribe,
                onUnsubscribe,
            ) {
                forService.wrapper.addCharacteristic(wrapper)
                registerSubscriptionActions(encryptedNotification)
                descriptorBuilders.mapNotNull { descriptorBuilder ->
                    descriptorBuilder.build(this).takeIf { it.uuid != Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR }
                }
            }
        } ?: Static(
            LocalCharacteristicWrapper(uuid, properties, false, permissions),
            forService,
        ) {
            forService.wrapper.addCharacteristic(wrapper)
            descriptorBuilders.map { it.build(this) }
        }

        readAction?.let { onRead ->
            registerCharacteristicReadAction(characteristic, onRead)
        }
        writeAction?.let { onWrite ->
            registerCharacteristicWriteAction(characteristic, onWrite)
        }

        return characteristic
    }
}

expect class LocalCharacteristicWrapper {
    val uuid: UUID
    val properties: Set<CharacteristicProperty>
    val permissions: Set<LocalCharacteristic.Permission>

    constructor(
        uuid: UUID,
        properties: Set<CharacteristicProperty>,
        encryptedNotification: Boolean,
        permissions: Set<LocalCharacteristic.Permission>,
    )

    fun addDescriptor(descriptor: LocalDescriptorWrapper)
}
