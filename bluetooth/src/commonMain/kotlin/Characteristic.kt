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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.utils.containsAny
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.logging.ContextualLogger
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.DeserializationStrategy

/**
 * A Characteristic is an [Attribute] provided by a [Service].
 * It is a value used in a service along with properties and configuration information about how the value is accessed and information about how the value is displayed or represented.
 * A Characteristic may contain one or more [Descriptor].
 */
interface Characteristic : Attribute {

    /**
     * The [Service] this characteristic belongs to
     */
    val service: Service

    /**
     * The set of [CharacteristicProperty] of this characteristic.
     */
    val properties: Set<CharacteristicProperty>

    /**
     * The list of [Descriptor] available for this characteristic
     */
    val descriptors: List<Descriptor>
}

/**
 * A [Characteristic] [RemoteAttribute] that is accessed remotely by a bluetooth client using [Bluetooth]
 * @property wrapper the [RemoteCharacteristicWrapper] to access the platform characteristic
 * @property service the [RemoteService] this characteristic belongs to
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param logger the [ContextualLogger] to use for logging.
 */
open class RemoteCharacteristic(
    val wrapper: RemoteCharacteristicWrapper,
    override val service: RemoteService,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    logger: ContextualLogger,
) : RemoteAttribute<DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(
    emitNewAction,
    logger,
),
    Characteristic {

    /**
     * Result from calling [RemoteCharacteristic.subscribe]
     */
    sealed class SubscriptionResult {

        /**
         * The subscription was successful
         * @property subscription the [Subscription] to use to remove the subscription
         */
        data class DidSubscribe(override val subscription: Subscription) : SubscriptionResult() {
            override val response: GattResponse.WriteResponse = GattResponse.WriteSuccess
        }

        /**
         * The subscription failed
         * @property response the [GattResponse.WriteError] that caused the subscription to fail
         */
        data class FailedToSubscribe(override val response: GattResponse.WriteError) : SubscriptionResult() {
            override val subscription: Subscription? = null
        }

        /**
         * The [Subscription] to use to remove the subscription if subscription succeeded.
         */
        abstract val subscription: Subscription?

        /**
         * The response of the Subscription attempt
         */
        abstract val response: GattResponse.WriteResponse
    }

    /**
     * A subscription to a [com.splendo.kaluga.bluetooth.RemoteCharacteristic]. Can be created using [RemoteCharacteristic.subscribe] if [Characteristic.properties] contains [CharacteristicProperty.Notifiable].
     * Call [unsubscribe] to remove the subscription. This will automatically stop notification if this is the last remaining subscription to the characteristic.
     */
    class Subscription internal constructor(internal val onUpdate: (ByteArray) -> Unit, private val onUnsubscribe: suspend Subscription.() -> DeviceAction.Notification?) {

        /**
         * Unsubscribes from the remote device.
         * This will automatically stop notification if this is the last remaining subscription to the characteristic.
         * @return the [GattResponse.WriteResponse] received by the unsubscribe action.
         */
        suspend fun unsubscribe(): GattResponse.WriteResponse = startUnsubscribe()?.response?.await() ?: GattResponse.WriteSuccess

        internal suspend fun startUnsubscribe() = onUnsubscribe()
    }

    private val isBusy = MutableStateFlow(false)
    private val _isNotifying = atomic(false)
    private val subscriptions = concurrentMutableListOf<Subscription>()
    private var lastKnownValue: ByteArray? = null

    /**
     * If `true` this characteristic has been set to automatically provide updates to its value
     */
    var isNotifying: Boolean
        get() = _isNotifying.value
        set(value) {
            _isNotifying.value = value
        }

    /**
     * Attempts to subscribe to the characteristic.
     * @param onUpdate called when the [ByteArray] value of the characteristic changes.
     * @return [SubscriptionResult.DidSubscribe] if subscription was successful, or [SubscriptionResult.FailedToSubscribe] if subscription failed.
     */
    suspend fun subscribe(onUpdate: (ByteArray) -> Unit): SubscriptionResult = when (
        val enableNotification =
            enableNotification()?.response?.await() ?: GattResponse.WriteSuccess
    ) {
        is GattResponse.WriteSuccess -> Subscription(
            onUpdate,
            { unsubscribe(this) },
        ).let { subscription ->
            lastKnownValue?.let {
                subscription.onUpdate(it)
            }
            subscriptions.add(subscription)
            SubscriptionResult.DidSubscribe(subscription)
        }
        is GattResponse.WriteError -> SubscriptionResult.FailedToSubscribe(enableNotification)
    }

    /**
     * Attempts to subscribe to the characteristic.
     * @param T the type of the value to be decoded from the [ByteArray] value of the characteristic.
     * @param deserializationStrategy the [DeserializationStrategy] to use to decode the [ByteArray] value of the characteristic.
     * @param bluetoothFormat the [BluetoothFormat] to use to decode the [ByteArray] value of the characteristic.
     * @param onUpdate called when the [T] value of the characteristic changes.
     * @return [SubscriptionResult.DidSubscribe] if subscription was successful, or [SubscriptionResult.FailedToSubscribe] if subscription failed.
     */
    suspend fun <T> subscribe(deserializationStrategy: DeserializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat, onUpdate: (T) -> Unit): SubscriptionResult =
        subscribe {
            onUpdate(
                bluetoothFormat.decodeFromByteArray(deserializationStrategy, it),
            )
        }

    /**
     * Attempts to subscribe to the characteristic.
     * @param T the type of the value to be decoded from the [ByteArray] value of the characteristic.
     * @param bluetoothFormat the [BluetoothFormat] to use to decode the [ByteArray] value of the characteristic.
     * @param onUpdate called when the [T] value of the characteristic changes.
     * @return [SubscriptionResult.DidSubscribe] if subscription was successful, or [SubscriptionResult.FailedToSubscribe] if subscription failed.
     */
    suspend inline fun <reified T> subscribe(bluetoothFormat: BluetoothFormat = BluetoothFormat, noinline onUpdate: (T) -> Unit) =
        subscribe(bluetoothFormat.serializer<T>(), bluetoothFormat, onUpdate)

    /**
     * Enables notification or indication for this [RemoteCharacteristic].
     *
     * Creates and puts [DeviceAction.Notification.Enable] into queue to be executed.
     * Sets [isNotifying] to `true` after action completed successfully.
     *
     * @return [DeviceAction.Notification.Enable] if action was added to the queue, or `null` if notification is already enabled.
     * @see [disableNotification]
     * @see [isNotifying]
     */
    suspend fun enableNotification() = createNotificationAction(true, DeviceAction.Notification.Enable(this))

    internal fun notify(value: ByteArray) {
        lastKnownValue = value
        subscriptions.forEach { it.onUpdate(value) }
    }

    private suspend fun unsubscribe(subscription: Subscription): DeviceAction.Notification? = if (subscriptions.remove(subscription) && subscriptions.isEmpty()) {
        lastKnownValue = null
        disableNotification()
    } else {
        null
    }

    /**
     * Disables notification or indication for this [com.splendo.kaluga.bluetooth.RemoteCharacteristic]
     *
     * Creates and puts [DeviceAction.Notification.Disable] into queue to be executed.
     * Sets [isNotifying] to `false` after action completed successfully.
     *
     * @return [DeviceAction.Notification.Disable] if action was added to the queue, or `null` if notification is already disabled.
     * @see [enableNotification]
     * @see [isNotifying]
     */
    suspend fun disableNotification() = createNotificationAction(
        false,
        DeviceAction.Notification.Disable(this),
    )

    private suspend fun <N : DeviceAction.Notification> createNotificationAction(expected: Boolean, action: N): N? {
        do {
            isBusy.first { !it }
            if (isNotifying == expected) return null
        } while (!isBusy.compareAndSet(expect = false, update = true))

        if (hasAnyProperty(setOf(CharacteristicProperty.Notify, CharacteristicProperty.Indicate))) {
            addAction(action)
            action.response.invokeOnCompletion {
                if (it == null && action.response.getCompleted() is GattResponse.WriteSuccess) {
                    isNotifying = expected
                }
                isBusy.compareAndSet(expect = true, update = false)
            }
        } else {
            action.complete(GattResponse.WriteNotPermitted)
            isBusy.compareAndSet(expect = true, update = false)
        }
        return action
    }

    override val uuid = wrapper.uuid
    override val properties = wrapper.properties

    /**
     * The list of [RemoteDescriptor] available for this characteristic
     */
    override val descriptors: List<RemoteDescriptor> = wrapper.descriptors.map {
        RemoteDescriptor(
            wrapper = it,
            characteristic = this,
            emitNewAction = emitNewAction,
            logger = logger.withAppendedContext("Descriptor" to it.uuid.uuidString),
        )
    }

    override fun createReadAction(): DeviceAction.Read.Characteristic = DeviceAction.Read.Characteristic(this).apply {
        if (!hasProperty(CharacteristicProperty.Read)) {
            complete(GattResponse.ReadNotPermitted)
        }
    }

    override fun createWriteAction(newValue: ByteArray): DeviceAction.Write.Characteristic = DeviceAction.Write.Characteristic(newValue, this).apply {
        if (!hasAnyProperty(setOf(CharacteristicProperty.Write, CharacteristicProperty.WriteWithoutResponse, CharacteristicProperty.SignedWrite))) {
            complete(GattResponse.WriteNotPermitted)
        }
    }

    /**
     * Checks if the characteristic has a given [CharacteristicProperty]
     */
    fun hasProperty(property: CharacteristicProperty) = hasProperties(setOf(property))

    private fun hasProperties(properties: Set<CharacteristicProperty>) = wrapper.properties.containsAll(properties)
    private fun hasAnyProperty(properties: Set<CharacteristicProperty>) = wrapper.properties.containsAny(properties)
}

/**
 * Accessor to the platform level Bluetooth characteristic
 */
expect interface RemoteCharacteristicWrapper {
    /**
     * The [UUID] of the characteristic
     */
    val uuid: UUID

    /**
     * The [RemoteServiceWrapper] this characteristic belongs to
     */
    val service: RemoteServiceWrapper

    /**
     * The list of [RemoteDescriptorWrapper] of associated with the characteristic
     */
    val descriptors: List<RemoteDescriptorWrapper>

    /**
     * The integer representing all [CharacteristicProperty] of the characteristic
     */
    val properties: Set<CharacteristicProperty>
}

/**
 * The properties associated with a Bluetooth Characteristic
 * A CharacteristicProperty determines how a [Characteristic] Value can be used, or how the [Descriptor] can be accessed.
 * @param rawValue the raw value associated with the property
 * @property encryptedValue the value associated with the property when encryption is used
 */
sealed class CharacteristicProperty(val rawValue: Int, val encryptedValue: Int) {

    constructor(rawValue: Int) : this(rawValue, rawValue)

    companion object {

        /**
         * Gets a [Set] of [CharacteristicProperty] from an [Int]
         */
        fun fromInt(properties: Int): Set<CharacteristicProperty> = setOf(
            Broadcast,
            Read,
            Write,
            WriteWithoutResponse,
            SignedWrite,
            Notify,
            Indicate,
            ExtendedProperties,
        ).filter {
            (properties and it.rawValue) != 0 || (properties and it.encryptedValue) != 0
        }.toSet()
    }

    /**
     * [Characteristic] is broadcastable
     */
    data object Broadcast : CharacteristicProperty(0x01)

    /**
     * [Characteristic] is readable
     */
    data object Read : CharacteristicProperty(0x02)

    sealed class Writable(rawValue: Int) : CharacteristicProperty(rawValue)

    /**
     * [Characteristic] can be written without response
     */
    data object WriteWithoutResponse : Writable(0x04)

    /**
     * [Characteristic] supports write with signature
     */
    data object SignedWrite : Writable(0x40)

    /**
     * [Characteristic] can be written
     */
    data object Write : Writable(0x08)

    sealed class Notifiable(rawValue: Int, encryptedValue: Int) : CharacteristicProperty(rawValue, encryptedValue)

    /**
     * [Characteristic] supports notification
     */
    data object Notify : Notifiable(0x10, 256)

    /**
     * [Characteristic] supports indication
     */
    data object Indicate : Notifiable(0x20, 512)

    /**
     * [Characteristic] has extended properties
     */
    data object ExtendedProperties : CharacteristicProperty(0x80)
}

/**
 * Gets the Raw Int from a set of [CharacteristicProperty]
 * @param encrypted if `true` the [CharacteristicProperty.encryptedValue] value will be used, otherwise [CharacteristicProperty.rawValue]
 */
fun Set<CharacteristicProperty>.rawValue(encrypted: Boolean): Int = fold(0) { acc, characteristicProperty ->
    if (encrypted) {
        acc or characteristicProperty.encryptedValue
    } else {
        acc or characteristicProperty.rawValue
    }
}
