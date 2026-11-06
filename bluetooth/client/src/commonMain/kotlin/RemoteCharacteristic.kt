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

import com.splendo.kaluga.base.utils.containsAny
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.logging.Logger
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

/**
 * A [Characteristic] [RemoteAttribute] that is accessed remotely by a bluetooth client using [BluetoothClient]
 *
 * Once [invalidated] (the device disconnected) [isNotifying] is `false`, every subscription is dropped, the flows of
 * [value] complete, and every notification request answers with [GattResponse.DeviceUnavailable].
 */
open class RemoteCharacteristic internal constructor(
    val wrapper: RemoteCharacteristicWrapper,
    override val service: RemoteService,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    logger: ConnectionSettings.ConnectionLogger.CharacteristicLogger,
) : RemoteAttribute<DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(
    emitNewAction,
    logger,
),
    Characteristic {

    /**
     * Constructor
     * @property wrapper the [RemoteCharacteristicWrapper] to access the platform characteristic
     * @property service the [RemoteService] this characteristic belongs to
     * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
     * @param logger the [Logger] to use for logging.
     */
    constructor(
        wrapper: RemoteCharacteristicWrapper,
        service: RemoteService,
        emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
        logger: Logger,
    ) : this(
        wrapper,
        service,
        emitNewAction,
        ConnectionSettings.ConnectionLogger.CharacteristicLogger(logger),
    )

    /**
     * Result from calling [RemoteCharacteristic.subscribe]
     */
    sealed class SubscriptionResult {

        /**
         * The subscription was successful
         * @property subscription the [Subscription] to use to remove the subscription
         */
        data class DidSubscribe(override val subscription: Subscription) : SubscriptionResult() {
            override val response: GattResponse.WriteResponse = GattResponse.WriteSuccess.Acknowledged
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
    class Subscription internal constructor(internal val onUpdate: (ByteArray) -> Unit, private val onUnsubscribe: Subscription.() -> DeviceAction.Notification?) {

        /**
         * Unsubscribes from the remote device.
         * This will automatically stop notification if this is the last remaining subscription to the characteristic.
         * @return the [GattResponse.WriteResponse] received by the unsubscribe action, [GattResponse.DeviceUnavailable] if
         * the characteristic is [invalidated].
         */
        suspend fun unsubscribe(): GattResponse.WriteResponse = startUnsubscribe()?.response?.await() ?: GattResponse.WriteSuccess.Acknowledged

        internal fun startUnsubscribe() = onUnsubscribe()
    }

    /**
     * Completes with [RemoteService.invalidated] of the [service] this characteristic belongs to
     */
    override val invalidated: Deferred<Unit> = service.invalidated

    private val _isNotifying = MutableStateFlow(false)
    private val subscriptions = atomic(emptyList<Subscription>())
    private var lastKnownValue: ByteArray? = null

    /**
     * If `true` this characteristic has been set to automatically provide updates to its value. `false` once [invalidated].
     */
    val isNotifying: StateFlow<Boolean> = _isNotifying.asStateFlow()

    /**
     * Attempts to subscribe to the characteristic.
     * @param onUpdate called when the [ByteArray] value of the characteristic changes.
     * @return [SubscriptionResult.DidSubscribe] if subscription was successful, or [SubscriptionResult.FailedToSubscribe] if subscription failed,
     * with [GattResponse.DeviceUnavailable] if the device disconnected before it answered or the characteristic is [invalidated].
     */
    suspend fun subscribe(onUpdate: (ByteArray) -> Unit): SubscriptionResult = when (
        val enableNotification = enableNotification()
    ) {
        is GattResponse.WriteSuccess -> if (invalidated.isCompleted) {
            SubscriptionResult.FailedToSubscribe(GattResponse.DeviceUnavailable)
        } else {
            Subscription(
                onUpdate,
            ) { unsubscribe(this) }.let { subscription ->
                lastKnownValue?.let {
                    subscription.onUpdate(it)
                }
                subscriptions.update { it + subscription }
                SubscriptionResult.DidSubscribe(subscription)
            }
        }

        is GattResponse.WriteError -> SubscriptionResult.FailedToSubscribe(enableNotification)
    }

    /**
     * Attempts to subscribe to the characteristic.
     * @param T the type of the value to be decoded from the [ByteArray] value of the characteristic.
     * @param deserializationStrategy the [DeserializationStrategy] to use to decode the [ByteArray] value of the characteristic.
     * @param format the [BytesFormat] to use to decode the [ByteArray] value of the characteristic.
     * @param onUpdate called when the [T] value of the characteristic changes.
     * @return [SubscriptionResult.DidSubscribe] if subscription was successful, or [SubscriptionResult.FailedToSubscribe] if subscription failed.
     */
    suspend fun <T> subscribe(deserializationStrategy: DeserializationStrategy<T>, format: BytesFormat = BytesFormat, onUpdate: (T) -> Unit): SubscriptionResult = subscribe {
        onUpdate(
            format.decodeFromByteArray(deserializationStrategy, it),
        )
    }

    /**
     * Attempts to subscribe to the characteristic.
     * @param T the type of the value to be decoded from the [ByteArray] value of the characteristic.
     * @param format the [BytesFormat] to use to decode the [ByteArray] value of the characteristic.
     * @param onUpdate called when the [T] value of the characteristic changes.
     * @return [SubscriptionResult.DidSubscribe] if subscription was successful, or [SubscriptionResult.FailedToSubscribe] if subscription failed.
     */
    suspend inline fun <reified T> subscribe(format: BytesFormat = BytesFormat, noinline onUpdate: (T) -> Unit) = subscribe(format.serializer<T>(), format, onUpdate)

    /**
     * Enables notification or indication for this [RemoteCharacteristic].
     *
     * Creates and puts [DeviceAction.Notification.Enable] into queue to be executed.
     * Sets [isNotifying] to `true` after action completed successfully.
     *
     * @return [GattResponse.WriteResponse] indicating whether notification was enabled successfully. [GattResponse.DeviceUnavailable]
     * if the device disconnected before it answered or the characteristic is [invalidated], [GattResponse.Superseded] if a
     * disable request replaced this one before it reached the device.
     * @see [startDisableNotification]
     * @see [isNotifying]
     */
    suspend fun enableNotification() = startEnableNotification()?.response?.await() ?: GattResponse.WriteSuccess.Acknowledged

    /**
     * Starts to enable notification or indication for this [RemoteCharacteristic].
     *
     * Creates and puts [DeviceAction.Notification.Enable] into queue to be executed.
     * Sets [isNotifying] to `true` after action completed successfully.
     *
     * @return [DeviceAction.Notification.Enable] if action was added to the queue (already answered with
     * [GattResponse.DeviceUnavailable] if the characteristic is [invalidated]), or `null` if notification is already enabled.
     * @see [startDisableNotification]
     * @see [isNotifying]
     */
    fun startEnableNotification(): DeviceAction.Notification.Enable? {
        if (invalidated.isCompleted) {
            return DeviceAction.Notification.Enable(this).apply { completeUnavailable() }
        }
        var enableAction: DeviceAction.Notification.Enable? = null
        var supersededAction: DeviceAction.Notification? = null
        var shouldHandleAction = false
        currentNotificationAction.update { currentNotificationAction ->
            when {
                currentNotificationAction == null -> {
                    supersededAction = null
                    if (!_isNotifying.value) {
                        val action = DeviceAction.Notification.Enable(this)
                        enableAction = action
                        shouldHandleAction = true
                        NotificationActionQueue(action, null)
                    } else {
                        enableAction = null
                        shouldHandleAction = false
                        null
                    }
                }

                else -> when (val current = currentNotificationAction.current) {
                    is DeviceAction.Notification.Disable -> {
                        val next = currentNotificationAction.next
                        if (next is DeviceAction.Notification.Enable) {
                            enableAction = currentNotificationAction.next
                            supersededAction = null
                            shouldHandleAction = false
                            currentNotificationAction
                        } else {
                            val nextToDisable = DeviceAction.Notification.Enable(this)
                            enableAction = nextToDisable
                            supersededAction = currentNotificationAction.next
                            shouldHandleAction = false
                            NotificationActionQueue(current, nextToDisable)
                        }
                    }

                    is DeviceAction.Notification.Enable -> {
                        enableAction = current
                        shouldHandleAction = false
                        supersededAction = currentNotificationAction.next
                        NotificationActionQueue(current, null)
                    }
                }
            }
        }
        supersededAction?.complete(GattResponse.Superseded)
        enableAction?.takeIf { shouldHandleAction }?.handle()
        return enableAction
    }

    internal fun notify(value: ByteArray) {
        lastKnownValue = value
        val current = subscriptions.value
        for (index in 0 until current.size) {
            current[index].onUpdate(value)
        }
    }

    private fun unsubscribe(subscription: Subscription): DeviceAction.Notification? {
        if (invalidated.isCompleted) return DeviceAction.Notification.Disable(this).apply { completeUnavailable() }
        var removed = false
        val remaining = subscriptions.updateAndGet { current ->
            removed = subscription in current
            if (removed) current - subscription else current
        }
        return if (removed && remaining.isEmpty()) {
            lastKnownValue = null
            startDisableNotification()
        } else {
            null
        }
    }

    /**
     * Disables notification or indication for this [com.splendo.kaluga.bluetooth.RemoteCharacteristic]
     *
     * @return [GattResponse.WriteResponse] indicating whether notification was disabled successfully. [GattResponse.DeviceUnavailable]
     * if the device disconnected before it answered or the characteristic is [invalidated], [GattResponse.Superseded] if an
     * enable request replaced this one before it reached the device.
     * @see [startEnableNotification]
     * @see [isNotifying]
     */
    suspend fun disableNotification() = startDisableNotification()?.response?.await() ?: GattResponse.WriteSuccess.Acknowledged

    /**
     * Starts disabling notification or indication for this [com.splendo.kaluga.bluetooth.RemoteCharacteristic]
     *
     * Creates and puts [DeviceAction.Notification.Disable] into queue to be executed.
     * Sets [isNotifying] to `false` after action completed successfully.
     *
     * @return [DeviceAction.Notification.Disable] if action was added to the queue (already answered with
     * [GattResponse.DeviceUnavailable] if the characteristic is [invalidated]), or `null` if notification is already disabled.
     * @see [startEnableNotification]
     * @see [isNotifying]
     */
    fun startDisableNotification(): DeviceAction.Notification.Disable? {
        if (invalidated.isCompleted) {
            return DeviceAction.Notification.Disable(this).apply { completeUnavailable() }
        }
        var disableAction: DeviceAction.Notification.Disable? = null
        var supersededAction: DeviceAction.Notification? = null
        var shouldHandleAction = false
        currentNotificationAction.update { currentNotificationAction ->
            when {
                currentNotificationAction == null -> {
                    supersededAction = null
                    if (_isNotifying.value) {
                        val action = DeviceAction.Notification.Disable(this)
                        disableAction = action
                        shouldHandleAction = true
                        NotificationActionQueue(action, null)
                    } else {
                        disableAction = null
                        shouldHandleAction = false
                        null
                    }
                }

                else -> when (val current = currentNotificationAction.current) {
                    is DeviceAction.Notification.Enable -> {
                        val next = currentNotificationAction.next
                        if (next is DeviceAction.Notification.Disable) {
                            disableAction = currentNotificationAction.next
                            supersededAction = null
                            shouldHandleAction = false
                            currentNotificationAction
                        } else {
                            val nextToDisable = DeviceAction.Notification.Disable(this)
                            disableAction = nextToDisable
                            supersededAction = currentNotificationAction.next
                            shouldHandleAction = false
                            NotificationActionQueue(current, nextToDisable)
                        }
                    }

                    is DeviceAction.Notification.Disable -> {
                        disableAction = current
                        shouldHandleAction = false
                        supersededAction = currentNotificationAction.next
                        NotificationActionQueue(current, null)
                    }
                }
            }
        }
        supersededAction?.complete(GattResponse.Superseded)
        disableAction?.takeIf { shouldHandleAction }?.handle()
        return disableAction
    }

    private data class NotificationActionQueue(val current: DeviceAction.Notification, val next: DeviceAction.Notification?) {
        fun next() = next?.let { NotificationActionQueue(it, null) }
    }
    private val currentNotificationAction = atomic<NotificationActionQueue?>(null)

    init {
        invalidated.invokeOnCompletion { invalidate() }
    }

    private fun invalidate() {
        subscriptions.value = emptyList()
        lastKnownValue = null
        // Taking the queue first keeps the completion handler in handle() from starting the next action.
        currentNotificationAction.getAndSet(null)?.let { queue ->
            queue.current.completeUnavailable()
            queue.next?.completeUnavailable()
        }
        // Last, so an enable that succeeded concurrently cannot leave it true; see handle().
        _isNotifying.value = false
    }

    private fun DeviceAction.Notification.handle() {
        if (hasAnyProperty(setOf(CharacteristicProperty.Notify, CharacteristicProperty.Indicate))) {
            response.invokeOnCompletion { completedResponse ->
                if (completedResponse == null && response.getCompleted() is GattResponse.WriteSuccess && !invalidated.isCompleted) {
                    _isNotifying.value = this is DeviceAction.Notification.Enable
                    // invalidate() may have run between the check and the write.
                    if (invalidated.isCompleted) {
                        _isNotifying.value = false
                    }
                }
                currentNotificationAction.updateAndGet { it?.next() }?.current?.handle()
            }
            addAction(this)
        } else {
            complete(GattResponse.WriteNotPermitted)
            currentNotificationAction.updateAndGet { it?.next() }?.current?.handle()
        }
    }

    override val uuid = wrapper.uuid
    override val properties = wrapper.properties

    /**
     * The list of [RemoteDescriptor] available for this characteristic
     */
    override val descriptors: List<RemoteDescriptor> = wrapper.descriptors.mapNotNull {
        // The CCCD is owned by the notify/subscribe state machine; exposing it as a writable descriptor lets a
        // direct write desync the notifying state (and throws on iOS), so it is skipped. Use [subscribe]/notifications instead.
        if (it.uuid.uuidString == Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR.uuidString) {
            null
        } else {
            RemoteDescriptor(
                wrapper = it,
                characteristic = this,
                emitNewAction = emitNewAction,
                logger = logger[it.uuid],
            )
        }
    }

    override fun createReadAction(): DeviceAction.Read.Characteristic = DeviceAction.Read.Characteristic(this).apply {
        if (!hasProperty(CharacteristicProperty.Read)) {
            complete(GattResponse.ReadNotPermitted)
        }
    }

    override fun createWriteAction(newValue: ByteArray): DeviceAction.Write.Characteristic = createWriteAction(newValue, null)

    /**
     * Requests a write of [newValue] to the characteristic using the given [writeType] and suspends until the result is received.
     * The characteristic must support the property corresponding to [writeType], otherwise [GattResponse.WriteNotPermitted] is returned.
     * @param newValue the [ByteArray] to write
     * @param writeType the [WriteType] to use
     * @return the [GattResponse.WriteResponse] received from the device
     */
    suspend fun write(newValue: ByteArray, writeType: WriteType): GattResponse.WriteResponse = startWrite(newValue, writeType).response.await()

    /**
     * Creates and emits a [DeviceAction.Write.Characteristic] using the given [writeType]
     * @param newValue the [ByteArray] to write
     * @param writeType the [WriteType] to use
     * @return the [DeviceAction.Write.Characteristic] created. Its response is already set when the characteristic does
     * not permit [writeType] or is [invalidated]
     */
    fun startWrite(newValue: ByteArray, writeType: WriteType): DeviceAction.Write.Characteristic = createWriteAction(newValue, writeType).also(::addActionUnlessAnswered)

    /**
     * Attempts to write a value of [T] to the characteristic using the given [writeType] and suspends until the write completes.
     * @param T the type of the data object to write.
     * @param serializationStrategy the [SerializationStrategy] to use to serialize the value.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write.
     * @param writeType the [WriteType] to use
     * @return the [GattResponse.WriteResponse] received from the remote device.
     */
    suspend fun <T> write(serializationStrategy: SerializationStrategy<T>, format: BytesFormat = BytesFormat, newValue: T, writeType: WriteType): GattResponse.WriteResponse =
        write(format.encodeToByteArray(serializationStrategy, newValue), writeType)

    /**
     * Attempts to write a value of [T] to the characteristic using the given [writeType] and suspends until the write completes.
     * @param T the type of the data object to write.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write.
     * @param writeType the [WriteType] to use
     * @return the [GattResponse.WriteResponse] received from the remote device.
     */
    suspend inline fun <reified T> write(format: BytesFormat = BytesFormat, newValue: T, writeType: WriteType): GattResponse.WriteResponse =
        write(format.encodeToByteArray(format.serializer<T>(), newValue), writeType)

    /**
     * Creates and emits a [DeviceAction.Write.Characteristic] to write a given [T] using the given [writeType]
     * @param T the type of the data object to write.
     * @param serializationStrategy the [SerializationStrategy] to use to serialize the value.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write to the characteristic
     * @param writeType the [WriteType] to use
     * @return the [DeviceAction.Write.Characteristic] created
     */
    fun <T> startWrite(serializationStrategy: SerializationStrategy<T>, format: BytesFormat = BytesFormat, newValue: T, writeType: WriteType): DeviceAction.Write.Characteristic =
        startWrite(format.encodeToByteArray(serializationStrategy, newValue), writeType)

    /**
     * Creates and emits a [DeviceAction.Write.Characteristic] to write a given [T] using the given [writeType]
     * @param T the type of the data object to write.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write to the characteristic
     * @param writeType the [WriteType] to use
     * @return the [DeviceAction.Write.Characteristic] created
     */
    inline fun <reified T> startWrite(format: BytesFormat = BytesFormat, newValue: T, writeType: WriteType): DeviceAction.Write.Characteristic =
        startWrite(format.encodeToByteArray(format.serializer(), newValue), writeType)

    private fun createWriteAction(newValue: ByteArray, writeType: WriteType?): DeviceAction.Write.Characteristic =
        DeviceAction.Write.Characteristic(newValue, this, writeType).apply {
            val required = when (writeType) {
                WriteType.WithResponse -> setOf(CharacteristicProperty.Write)
                WriteType.WithoutResponse -> setOf(CharacteristicProperty.WriteWithoutResponse)
                null -> setOf(CharacteristicProperty.Write, CharacteristicProperty.WriteWithoutResponse, CharacteristicProperty.SignedWrite)
            }
            if (!hasAnyProperty(required)) {
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
 * A [RemoteCharacteristicWrapper] that can be implemented in common code.
 *
 * The platform [RemoteCharacteristicWrapper] interfaces add members only Kaluga's own platform connection managers
 * use. This class provides those, so a wrapper for another transport only implements the common members. See
 * [PortableRemoteServiceWrapper].
 */
expect abstract class PortableRemoteCharacteristicWrapper() : RemoteCharacteristicWrapper
