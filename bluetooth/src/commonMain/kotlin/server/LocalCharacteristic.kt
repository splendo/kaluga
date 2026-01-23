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
import com.splendo.kaluga.bluetooth.serialization.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Notifiable
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Permission
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
import kotlinx.serialization.SerializationStrategy

internal typealias Notify = suspend (characteristic: Notifiable, device: ConnectedDevice, value: ByteArray) -> Boolean
internal typealias BuildDescriptor = (
    uuid: UUID,
) -> LocalDescriptorDSL?

/**
 * A [Characteristic] available from a [BluetoothServer]
 * @property wrapper the [LocalCharacteristicWrapper] to access the platform characteristic
 */
sealed class LocalCharacteristic(val wrapper: LocalCharacteristicWrapper, override val service: LocalService) : Characteristic {

    /**
     * DSL for setting up a [LocalCharacteristic]
     */
    interface DSL {

        /**
         * Makes this [LocalCharacteristic] readable by a [ConnectedDevice]
         * Cannot be called if [readable], or [readableAlwaysSuccess] has been called before
         * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.READ_ENCRYPTION_REQUIRED].
         * Otherwise will add [Permission.READABLE]
         * @param onRead the function to call when reading from the characteristic.
         * This contains the [ConnectedDevice] and the offset of the data to read and should return a [GattResponse.ReadResponse]
         */
        fun readable(encrypted: Boolean = false, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)

        /**
         * Makes this [LocalCharacteristic] readable by a [ConnectedDevice] to always return [GattResponse.ReadSuccess]
         * Cannot be called if [readable], or [readableAlwaysSuccess] has been called before
         * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.READ_ENCRYPTION_REQUIRED].
         * Otherwise will add [Permission.READABLE]
         * @param onRead the function to call when reading from the characteristic.
         * This contains the [ConnectedDevice] and the offset of the data to read and should return the [ByteArray] being read.
         */
        fun readableAlwaysSuccess(encrypted: Boolean = false, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> ByteArray) {
            readable(encrypted) { device, offset ->
                GattResponse.ReadSuccess(onRead(this, device, offset))
            }
        }

        /**
         * Makes this [LocalCharacteristic] readable by a [ConnectedDevice] to always return [GattResponse.ReadSuccess]
         * Cannot be called if [readable], or [readableAlwaysSuccess] has been called before
         * @param T the type of the data being read
         * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.READ_ENCRYPTION_REQUIRED].
         * Otherwise will add [Permission.READABLE]
         * @param serializationStrategy the [SerializationStrategy] to use to encode the [T] to a [ByteArray]
         * @param bluetoothFormat the [BluetoothFormat] to use to encode the [T] to a [ByteArray]
         * @param onRead the function to call when reading from the characteristic.
         * This contains the [ConnectedDevice] and the offset of the data to read and should return the [T] being read.
         */
        fun <T> readableAlwaysSuccess(
            encrypted: Boolean = false,
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
            onRead: suspend LocalCharacteristic.(ConnectedDevice) -> T,
        ) {
            readable(encrypted) { device, offset ->
                GattResponse.ReadSuccess(onRead(device), offset, serializationStrategy, bluetoothFormat)
            }
        }

        /**
         * Makes this [LocalCharacteristic] writable by a [ConnectedDevice]
         * Cannot be called if [writable] or [writableAlwaysSuccess] has been called before
         * @param properties the [CharacteristicProperty.Writable] of the characteristic. Must not be empty
         * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [Permission.WRITABLE]
         * @param onWrite the function to call when reading from the characteristic.
         * This contains the [ConnectedDevice], the [ByteArray] to write and the offset of the data to write and should return a [GattResponse.WriteResponse]
         */
        fun writable(
            properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
            encrypted: Boolean = false,
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
        )

        /**
         * Makes this [LocalCharacteristic] writable by a [ConnectedDevice]
         * Cannot be called if [writable] or [writableAlwaysSuccess] has been called before
         * @param T the type of the data being written
         * @param properties the [CharacteristicProperty.Writable] of the characteristic. Must not be empty
         * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [Permission.WRITABLE]
         * @param deserializationStrategy the [DeserializationStrategy] to use to decode the [ByteArray] being written to an instance of [T]
         * @param onFailedToWrite the function to call when writing to the characteristic fails.
         * This contains the [ConnectedDevice] and the exception that caused deserialization to fail and should return a [GattResponse.WriteResponse]
         * @param onWrite the function to call when reading from the characteristic.
         * This contains the [ConnectedDevice], and the [T] to write and should return a [GattResponse.WriteResponse].
         * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
         */
        fun <T> writable(
            properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
            encrypted: Boolean = false,
            deserializationStrategy: DeserializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
            onFailedToWrite: suspend LocalCharacteristic.(ConnectedDevice, Exception) -> GattResponse.WriteResponse = { _, _ -> GattResponse.ApplicationError(0x80) },
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, T) -> GattResponse.WriteResponse,
        ) {
            val cache = mutableMapOf<ConnectedDevice, ByteArray>()
            writable(properties, encrypted) { device, value, offset ->
                val currentCache = cache.remove(device) ?: byteArrayOf()
                val valueToDeserialize = when (offset) {
                    0 -> {
                        value
                    }

                    currentCache.size -> {
                        currentCache + value
                    }

                    else -> null
                }
                valueToDeserialize?.let {
                    try {
                        onWrite(device, bluetoothFormat.decodeFromByteArray(deserializationStrategy, it))
                    } catch (_: ByteArrayEndedBeforeSerializationCompleted) {
                        cache[device] = valueToDeserialize
                        GattResponse.WriteSuccess
                    } catch (e: Exception) {
                        onFailedToWrite(device, e)
                    }
                } ?: GattResponse.InvalidOffset
            }
        }

        /**
         * Makes this [LocalCharacteristic] writable by a [ConnectedDevice] and always responds with [GattResponse.WriteSuccess]
         * Cannot be called if [writable] or [writableAlwaysSuccess] has been called before
         * @param properties the [CharacteristicProperty.Writable] of the characteristic. Must not be empty
         * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [Permission.WRITABLE]
         * @param onWrite the function to call when reading from the characteristic.
         * This contains the [ConnectedDevice], the [ByteArray] to write and the offset of the data to write
         */
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

        /**
         * Makes this [LocalCharacteristic] writable by a [ConnectedDevice] and always responds with [GattResponse.WriteSuccess]
         * Cannot be called if [writable] or [writableAlwaysSuccess] has been called before
         * @param T the type of the data being written
         * @param properties the [CharacteristicProperty.Writable] of the characteristic. Must not be empty
         * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [Permission.WRITABLE]
         * @param deserializationStrategy the [DeserializationStrategy] to use to decode the [ByteArray] being written to an instance of [T]
         * @param onWrite the function to call when reading from the characteristic.
         * This contains the [ConnectedDevice], and the [T] to write.
         * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
         */
        fun <T> writableAlwaysSuccess(
            properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
            encrypted: Boolean = false,
            deserializationStrategy: DeserializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, T) -> Unit,
        ) = writable(
            properties,
            encrypted,
            deserializationStrategy,
            bluetoothFormat,
            { _, _ -> GattResponse.WriteSuccess },
        ) { device, value ->
            onWrite(device, value)
            GattResponse.WriteSuccess
        }

        /**
         * Makes this [LocalCharacteristic] a [LocalCharacteristic.Notifiable]
         * This method can only be called once.
         * @param properties the [CharacteristicProperty.Notifiable] of the characteristic. Must not be empty
         * @param encrypted `true` if subscribing to the characteristic should be encrypted.
         * @param onSubscribe the function to call when subscribing to the characteristic. This contains the [ConnectedDevice] that subscribed
         * @param onUnsubscribe the function to call when unsubscribing from the characteristic. This contains the [ConnectedDevice] that unsubscribed
         */
        fun notifiable(
            properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
            encrypted: Boolean = false,
            onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
            onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
        )

        /**
         * Adds a [LocalDescriptor] to the characteristic being built
         * This is not supported on iOS and will be ignored there.
         * @param uuid the [UUID] of the [LocalDescriptor] to add
         * @param descriptor the [LocalDescriptor.DSL] to use to set up the [LocalDescriptor]
         */
        fun descriptor(uuid: UUID, descriptor: LocalDescriptor.DSL.() -> Unit)

        /**
         * Adds a [LocalDescriptor] to the characteristic being built
         * This is not supported on iOS and will be ignored there.
         * @param uuidString string of the [UUID] of the [LocalDescriptor] to add
         * @param descriptor the [LocalDescriptor.DSL] to use to set up the [LocalDescriptor]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if [uuidString] is not a valid [UUID]
         */
        fun descriptor(uuidString: String, descriptor: LocalDescriptor.DSL.() -> Unit) {
            descriptor(uuidFrom(uuidString), descriptor)
        }

        /**
         * Sets up notification to notify all [ConnectedDevice] of changes to this [LocalCharacteristic] whenever a [Trigger] fires
         * @param [Trigger] the type of the Trigger that will cause the notification.
         */
        class NotificationDSL<Trigger> internal constructor(
            val dsl: DSL,
            val onSubscribe: Notifiable.(ConnectedDevice, (Trigger.() -> ByteArray)) -> Unit,
            val onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
        ) {
            /**
             * Makes this [LocalCharacteristic] a [LocalCharacteristic.Notifiable]
             * and automatically sends a [ByteArray] notification upon [Trigger]
             * This method can only be called once.
             * @param properties the [CharacteristicProperty.Notifiable] of the characteristic. Must not be empty
             * @param encrypted `true` if subscribing to the characteristic should be encrypted.
             * @param toByteArray method to convert the [Trigger] to a [ByteArray]
             */
            fun triggerNotification(
                properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
                encrypted: Boolean = false,
                toByteArray: Trigger.() -> ByteArray,
            ) {
                dsl.notifiable(
                    properties,
                    encrypted,
                    onSubscribe = { device ->
                        onSubscribe(device, toByteArray)
                    },
                    onUnsubscribe = onUnsubscribe,
                )
            }

            /**
             * Makes this [LocalCharacteristic] a [LocalCharacteristic.Notifiable]
             * and automatically sends a [ByteArray] notification upon [Trigger]
             * This method can only be called once.
             * @param properties the [CharacteristicProperty.Notifiable] of the characteristic. Must not be empty
             * @param encrypted `true` if subscribing to the characteristic should be encrypted.
             * @param serializationStrategy the [SerializationStrategy] to use to encode the [Trigger] to a [ByteArray]
             * @param bluetoothFormat the [BluetoothFormat] to use to encode the [Trigger] to a [ByteArray]
             */
            fun triggerNotification(
                properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
                encrypted: Boolean = false,
                serializationStrategy: SerializationStrategy<Trigger>,
                bluetoothFormat: BluetoothFormat = BluetoothFormat,
            ) = triggerNotification(properties, encrypted) {
                bluetoothFormat.encodeToByteArray(serializationStrategy, this)
            }
        }

        /**
         * Collects a [Flow] of [T] and notifies any subscribed [ConnectedDevice] of any changes.
         * Results in a call to [notifiable] that may only be called once
         * @param T the type of the data being collected
         * @param scope the [CoroutineScope] to use to collect the [Flow]
         * @param started the [SharingStarted] to use to collect the [Flow]
         * @param replay the number of values to replay to new subscribers
         * @param notification the [NotificationDSL] to use to set up notification
         */
        fun <T> Flow<T>.collectTo(scope: CoroutineScope, started: SharingStarted, replay: Int = 0, notification: NotificationDSL<T>.() -> Unit) {
            val sharedFlow = shareIn(scope, started, replay)
            sharedFlow.collectTo(scope, notification)
        }

        /**
         * Collects a [SharedFlow] of [T] and notifies any subscribed [ConnectedDevice] of any changes.
         * Results in a call to [notifiable] that may only be called once
         * @param T the type of the data being collected
         * @param scope the [CoroutineScope] to use to collect the [Flow]
         * @param notification the [NotificationDSL] to use to set up notification
         */
        fun <T> SharedFlow<T>.collectTo(scope: CoroutineScope, notification: NotificationDSL<T>.() -> Unit) {
            val observingJobs = concurrentMutableMapOf<ConnectedDevice, Job>()
            NotificationDSL(
                this@DSL,
                onSubscribe = { device, toByteArray ->
                    observingJobs[device] = scope.launch {
                        map { it.toByteArray() }.collect { value ->
                            notify(device, value)
                        }
                    }
                },
                onUnsubscribe = { device ->
                    observingJobs.remove(device)?.cancel()
                },
            ).apply(notification)
        }

        /**
         * Collects a [StateFlow] of [T] and notifies any subscribed [ConnectedDevice] of any changes.
         * Results in a call to [notifiable] that may only be called once
         * @param T the type of the data being collected
         * @param scope the [CoroutineScope] to use to collect the [Flow]
         * @param notification the [NotificationDSL] to use to set up notification
         */
        fun <T> StateFlow<T>.collectTo(scope: CoroutineScope, notification: NotificationDSL<T>.() -> Unit) {
            val hasStarted = CompletableDeferred<Unit>()
            NotificationDSL(
                this@DSL,
                onSubscribe = { device, toByteArray ->
                    // We only know the Characteristic on first subscription, so this is the point at which to collect the state flow
                    if (hasStarted.complete(Unit)) {
                        scope.launch {
                            map { it.toByteArray() }.collect(this@NotificationDSL)
                        }
                    } else {
                        // If scope already launched, then the subscription will have missed the initial value. So report it immediately
                        scope.launch {
                            notify(device, value.toByteArray())
                        }
                    }
                },
                onUnsubscribe = {},
            ).apply(notification)
        }

        /**
         * Consumes a [ReceiveChannel] of [T] and notifies any subscribed [ConnectedDevice] of any changes.
         * Results in a call to [notifiable] that may only be called once
         * @param T the type of the data being collected
         * @param scope the [CoroutineScope] to use to collect the [Flow]
         * @param notification the [NotificationDSL] to use to set up notification
         */
        fun <T> ReceiveChannel<T>.consumeTo(scope: CoroutineScope, notification: NotificationDSL<T>.() -> Unit) {
            val hasStarted = CompletableDeferred<Unit>()
            NotificationDSL(
                this@DSL,
                onSubscribe = { device, toByteArray ->
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
            ).apply(notification)
        }
    }

    /**
     * The permissions this characteristic gives to [ConnectedDevice]
     */
    enum class Permission {

        /**
         * The characteristic can be read by a [ConnectedDevice]
         */
        READABLE,

        /**
         * The characteristic can be written to by a [ConnectedDevice]
         */
        WRITABLE,

        /**
         * The characteristic can be read by a [ConnectedDevice] if an encrypted connection has been established
         */
        READ_ENCRYPTION_REQUIRED,

        /**
         * The characteristic can be written to by a [ConnectedDevice] if an encrypted connection has been established
         */
        WRITE_ENCRYPTION_REQUIRED,
    }

    /**
     * A [LocalCharacteristic] that cannot be observed
     */
    class Static internal constructor(wrapper: LocalCharacteristicWrapper, service: LocalService, buildDescriptors: Static.() -> List<LocalDescriptor>) :
        LocalCharacteristic(wrapper, service) {
        override val descriptors: List<LocalDescriptor> = buildDescriptors().also { descriptors ->
            descriptors.forEach { wrapper.addDescriptor(it.wrapper) }
        }
    }

    /**
     * A [LocalCharacteristic] that can be observed
     */
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

        /**
         * A [StateFlow] of all [ConnectedDevice] that have subscribed to this [LocalCharacteristic.Notifiable]
         */
        val subscribedDevices = _subscribedDevices.asStateFlow()
        override val descriptors: List<LocalDescriptor> = buildDescriptors()

        /**
         * Notifies a [ConnectedDevice] that that the data changed to [value]
         * @param device the [ConnectedDevice] that should be notified
         * @param value the new data
         * @return `true` if the notification was successful, `false` otherwise
         */
        suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean = subscribedDevices.map { devices ->
            devices.find { it == device }
        }.distinctUntilChanged().transformLatest { device ->
            emit(device?.let { notify(this@Notifiable, device, value) } ?: false)
        }.first()

        /**
         * Notifies a [ConnectedDevice] that that the data changed to [notification]
         * @param Notification the type of the data to notify
         * @param device the [ConnectedDevice] that should be notified
         * @param notification the new data
         * @param toByteArray method to convert the [Notification] to a [ByteArray]
         * @return `true` if the notification was successful, `false` otherwise
         */
        suspend fun <Notification> notify(device: ConnectedDevice, notification: Notification, toByteArray: Notification.() -> ByteArray): Boolean =
            notify(device, notification.toByteArray())

        /**
         * Notifies a [ConnectedDevice] that that the data changed to [notification]
         * @param Notification the type of the data to notify
         * @param device the [ConnectedDevice] that should be notified
         * @param notification the new data
         * @param serializationStrategy the [SerializationStrategy] to use to encode the [Notification] to a [ByteArray]
         * @param bluetoothFormat the [BluetoothFormat] to use to encode the [Notification] to a [ByteArray]
         * @return `true` if the notification was successful, `false` otherwise
         */
        suspend fun <Notification> notify(
            device: ConnectedDevice,
            notification: Notification,
            serializationStrategy: SerializationStrategy<Notification>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ): Boolean = notify(device, bluetoothFormat.encodeToByteArray(serializationStrategy, notification))

        /**
         * Notifies a [ConnectedDevice] that that the data changed to [notification]
         * @param Notification the type of the data to notify
         * @param device the [ConnectedDevice] that should be notified
         * @param notification the new data
         * @param bluetoothFormat the [BluetoothFormat] to use to encode the [Notification] to a [ByteArray]
         * @return `true` if the notification was successful, `false` otherwise
         */
        suspend inline fun <reified Notification> notify(device: ConnectedDevice, notification: Notification, bluetoothFormat: BluetoothFormat = BluetoothFormat): Boolean =
            notify(device, bluetoothFormat.encodeToByteArray(bluetoothFormat.serializer(), notification))

        /**
         * Notifies all [ConnectedDevice] currently subscribed that that the data changed to [value]
         * @param value the new data
         * @return `true` if the notification was successfully sent to all [ConnectedDevice], `false` otherwise
         */
        suspend fun notifyAll(value: ByteArray): Boolean {
            var result = true
            for (device in subscribedDevices.value) {
                result = result or notify(device, value)
            }
            return result
        }

        /**
         * Notifies all [ConnectedDevice] currently subscribed that that the data changed to [notification]
         * @param Notification the type of the data to notify
         * @param notification the new data
         * @param toByteArray method to convert the [Notification] to a [ByteArray]
         * @return `true` if the notification was successfully sent to all [ConnectedDevice], `false` otherwise
         */
        suspend fun <Notification> notifyAll(notification: Notification, toByteArray: Notification.() -> ByteArray): Boolean = notifyAll(notification.toByteArray())

        /**
         * Notifies all [ConnectedDevice] currently subscribed that that the data changed to [notification]
         * @param Notification the type of the data to notify
         * @param notification the new data
         * @param serializationStrategy the [SerializationStrategy] to use to encode the [Notification] to a [ByteArray]
         * @param bluetoothFormat the [BluetoothFormat] to use to encode the [Notification] to a [ByteArray]
         * @return `true` if the notification was successfully sent to all [ConnectedDevice], `false` otherwise
         */
        suspend fun <Notification> notifyAll(
            notification: Notification,
            serializationStrategy: SerializationStrategy<Notification>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ): Boolean = notifyAll(bluetoothFormat.encodeToByteArray(serializationStrategy, notification))

        /**
         * Notifies all [ConnectedDevice] currently subscribed that that the data changed to [notification]
         * @param Notification the type of the data to notify
         * @param notification the new data
         * @param bluetoothFormat the [BluetoothFormat] to use to encode the [Notification] to a [ByteArray]
         * @return `true` if the notification was successfully sent to all [ConnectedDevice], `false` otherwise
         */
        suspend inline fun <reified Notification> notifyAll(notification: Notification, bluetoothFormat: BluetoothFormat = BluetoothFormat): Boolean =
            notifyAll(bluetoothFormat.encodeToByteArray(bluetoothFormat.serializer(), notification))

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

    /**
     * The list of [LocalDescriptor] available for this characteristic
     */
    abstract override val descriptors: List<LocalDescriptor>

    /**
     * The set of [Permission] of this characteristic.
     */
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

/**
 * Accessor to the platform level Local Bluetooth characteristic
 * @param uuid the [UUID] of the characteristic
 * @param properties the [CharacteristicProperty] of the characteristic
 * @param permissions the [Permission] of the characteristic
 */
expect class LocalCharacteristicWrapper {
    val uuid: UUID
    val properties: Set<CharacteristicProperty>
    val permissions: Set<LocalCharacteristic.Permission>

    internal constructor(
        uuid: UUID,
        properties: Set<CharacteristicProperty>,
        encryptedNotification: Boolean,
        permissions: Set<LocalCharacteristic.Permission>,
    )

    /**
     * Adds a [LocalDescriptorWrapper] to the characteristic
     */
    fun addDescriptor(descriptor: LocalDescriptorWrapper)
}

/**
 * Makes this [LocalCharacteristic] readable by a [ConnectedDevice] to always return [GattResponse.ReadSuccess]
 * Cannot be called if [LocalCharacteristic.DSL.readable], or [LocalCharacteristic.DSL.readableAlwaysSuccess] has been called before
 * @param T the type of the data being read
 * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.READ_ENCRYPTION_REQUIRED].
 * Otherwise will add [Permission.READABLE]
 * @param bluetoothFormat the [BluetoothFormat] to use to encode the [T] to a [ByteArray]
 * @param onRead the function to call when reading from the characteristic.
 * This contains the [ConnectedDevice] and the offset of the data to read and should return the [T] being read.
 */
inline fun <reified T : Any> LocalCharacteristic.DSL.readableAlwaysSuccess(
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onRead: suspend LocalCharacteristic.(ConnectedDevice) -> T,
) = readableAlwaysSuccess(encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onRead)

/**
 * Makes this [LocalCharacteristic] writable by a [ConnectedDevice]
 * Cannot be called if [writable] has been called before
 * @param T the type of the data being written
 * @param properties the [CharacteristicProperty.Writable] of the characteristic. Must not be empty
 * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.WRITE_ENCRYPTION_REQUIRED].
 * Otherwise will add [Permission.WRITABLE]
 * @param onFailedToWrite the function to call when writing to the characteristic fails.
 * This contains the [ConnectedDevice] and the exception that caused deserialization to fail and should return a [GattResponse.WriteResponse]
 * @param onWrite the function to call when reading from the characteristic.
 * This contains the [ConnectedDevice], and the [T] to write and should return a [GattResponse.WriteResponse].
 * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
 */
inline fun <reified T : Any> LocalCharacteristic.DSL.writable(
    properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onFailedToWrite: suspend LocalCharacteristic.(ConnectedDevice, Exception) -> GattResponse.WriteResponse = { _, _ -> GattResponse.ApplicationError(0x80) },
    noinline onWrite: suspend LocalCharacteristic.(ConnectedDevice, T) -> GattResponse.WriteResponse,
) = writable(properties, encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onFailedToWrite, onWrite)

/**
 * Makes this [LocalCharacteristic] writable by a [ConnectedDevice] and always responds with [GattResponse.WriteSuccess]
 * Cannot be called if [writable] has been called before
 * @param T the type of the data being written
 * @param properties the [CharacteristicProperty.Writable] of the characteristic. Must not be empty
 * @param encrypted `true` if reading from the characteristic should be encrypted. This will result in [Permission.WRITE_ENCRYPTION_REQUIRED].
 * Otherwise will add [Permission.WRITABLE]
 * @param onWrite the function to call when reading from the characteristic.
 * This contains the [ConnectedDevice], and the [T] to write.
 * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
 */
inline fun <reified T : Any> LocalCharacteristic.DSL.writableAlwaysSuccess(
    properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onWrite: suspend LocalCharacteristic.(ConnectedDevice, T) -> Unit,
) = writableAlwaysSuccess(properties, encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onWrite)

/**
 * Makes this [LocalCharacteristic] a [LocalCharacteristic.Notifiable]
 * and automatically sends the [ByteArray] as a notification
 * This method can only be called once.
 * @param properties the [CharacteristicProperty.Notifiable] of the characteristic. Must not be empty
 * @param encrypted `true` if subscribing to the characteristic should be encrypted.
 */
fun LocalCharacteristic.DSL.NotificationDSL<ByteArray>.triggerNotification(
    properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
    encrypted: Boolean = false,
) = triggerNotification(properties, encrypted) { this }

/**
 * Makes this [LocalCharacteristic] a [LocalCharacteristic.Notifiable]
 * and automatically sends a [ByteArray] notification upon [Trigger]
 * This method can only be called once.
 * @param Trigger the type of the data being collected
 * @param properties the [CharacteristicProperty.Notifiable] of the characteristic. Must not be empty
 * @param encrypted `true` if subscribing to the characteristic should be encrypted.
 * @param bluetoothFormat the [BluetoothFormat] to use to encode the [Trigger] to a [ByteArray]
 */
inline fun <reified Trigger> LocalCharacteristic.DSL.NotificationDSL<Trigger>.triggerNotification(
    properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
) = triggerNotification(properties, encrypted, bluetoothFormat.serializer<Trigger>(), bluetoothFormat)
