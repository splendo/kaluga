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

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.logging.ContextualLogger
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

interface Characteristic : Attribute {
    val service: Service
    val properties: Set<CharacteristicProperty>
    val descriptors: List<Descriptor>
}

/**
 * An [Attribute] of a Bluetooth Characteristic
 * @property wrapper the [CharacteristicWrapper] to access the platform characteristic
 * @param initialValue the initial [ByteArray] value of the characteristic
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param logger the [ContextualLogger] to use for logging.
 */
open class RemoteCharacteristic(
    val wrapper: CharacteristicWrapper,
    override val service: RemoteService,
    initialValue: ByteArray? = null,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    logger: ContextualLogger,
) : RemoteAttribute<DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(
    initialValue,
    emitNewAction,
    logger,
),
    Characteristic {

    private val isBusy = MutableStateFlow(false)
    private val _isNotifying = atomic(false)

    /**
     * If `true` this characteristic has been set to automatically provide updates to its value
     */
    var isNotifying: Boolean
        get() = _isNotifying.value
        set(value) {
            _isNotifying.value = value
        }

    /**
     * Enables notification or indication for this [Characteristic].
     *
     * Creates and puts [DeviceAction.Notification.Enable] into queue to be executed.
     * Sets [isNotifying] to `true` after action completed successfully.
     *
     * @return [DeviceAction] if action was added to the queue, or
     * `null` if notification is already enabled.
     * @see [disableNotification]
     * @see [isNotifying]
     */
    suspend fun enableNotification(): DeviceAction? {
        do {
            isBusy.first { !it }
            if (isNotifying) return null
        } while (!isBusy.compareAndSet(expect = false, update = true))

        val action = createNotificationAction(enabled = true)
        addAction(action)
        action.completedSuccessfully.invokeOnCompletion {
            if (it == null && action.completedSuccessfully.getCompleted()) {
                isNotifying = true
            }
            isBusy.compareAndSet(expect = true, update = false)
        }
        return action
    }

    /**
     * Disables notification or indication for this [Characteristic]
     *
     * Creates and puts [DeviceAction.Notification.Disable] into queue to be executed.
     * Sets [isNotifying] to `false` after action completed successfully.
     *
     * @return [DeviceAction] if action was added to the queue, or
     * `null` if notification is already disabled.
     * @see [enableNotification]
     * @see [isNotifying]
     */
    suspend fun disableNotification(): DeviceAction? {
        do {
            isBusy.first { !it }
            if (!isNotifying) return null
        } while (!isBusy.compareAndSet(expect = false, update = true))

        val action = createNotificationAction(enabled = false)
        addAction(action)
        action.completedSuccessfully.invokeOnCompletion {
            if (it == null && action.completedSuccessfully.getCompleted()) {
                isNotifying = false
            }
            isBusy.compareAndSet(expect = true, update = false)
        }
        return action
    }

    override val uuid = wrapper.uuid
    override val properties = wrapper.properties

    /**
     * The list of [Descriptor] associated with the characteristic
     */
    override val descriptors: List<RemoteDescriptor> = wrapper.descriptors.map {
        RemoteDescriptor(
            wrapper = it,
            characteristic = this,
            emitNewAction = emitNewAction,
            logger = logger.withAppendedContext("Descriptor" to it.uuid.uuidString),
        )
    }

    override fun createReadAction(): DeviceAction.Read.Characteristic = DeviceAction.Read.Characteristic(this)

    override fun createWriteAction(newValue: ByteArray): DeviceAction.Write.Characteristic = DeviceAction.Write.Characteristic(newValue, this)

    private fun createNotificationAction(enabled: Boolean): DeviceAction.Notification =
        if (enabled) DeviceAction.Notification.Enable(this) else DeviceAction.Notification.Disable(this)

    override fun getUpdatedValue(): ByteArray? = wrapper.value?.asBytes

    /**
     * Checks if the characteristic has a given [CharacteristicProperty]
     */
    fun hasProperty(property: CharacteristicProperty) = hasProperties(setOf(property))

    private fun hasProperties(properties: Set<CharacteristicProperty>) = wrapper.properties.containsAll(properties)
}

/**
 * Accessor to the platform level Bluetooth characteristic
 */
expect interface CharacteristicWrapper {
    /**
     * The [UUID] of the characteristic
     */
    val uuid: UUID

    val service: ServiceWrapper

    /**
     * The list of [DescriptorWrapper] of associated with the characteristic
     */
    val descriptors: List<DescriptorWrapper>

    /**
     * The current [Value] of the characteristic
     */
    val value: Value?

    /**
     * The integer representing all [CharacteristicProperty] of the characteristic
     */
    val properties: Set<CharacteristicProperty>
}

/**
 * The properties associated with a Bluetooth Characteristic
 * @param rawValue the raw value associated with the property
 */
sealed class CharacteristicProperty(val rawValue: Int, val encryptedValue: Int) {

    constructor(rawValue: Int) : this(rawValue, rawValue)

    companion object {
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
     * Characteristic is broadcastable
     */
    data object Broadcast : CharacteristicProperty(0x01)

    /**
     * Characteristic is readable
     */
    data object Read : CharacteristicProperty(0x02)

    sealed class Writable(rawValue: Int) : CharacteristicProperty(rawValue)

    /**
     * Characteristic can be written without response
     */
    data object WriteWithoutResponse : Writable(0x04)

    /**
     * Characteristic supports write with signature
     */
    data object SignedWrite : Writable(0x40)

    /**
     * Characteristic can be written
     */
    data object Write : Writable(0x08)

    sealed class Notifiable(rawValue: Int, encryptedValue: Int) : CharacteristicProperty(rawValue, encryptedValue)

    /**
     * Characteristic supports notification
     */
    data object Notify : Notifiable(0x10, 256)

    /**
     * Characteristic supports indication
     */
    data object Indicate : Notifiable(0x20, 512)

    /**
     * Characteristic has extended properties
     */
    data object ExtendedProperties : CharacteristicProperty(0x80)
}

fun Set<CharacteristicProperty>.rawValue(encrypted: Boolean): Int = fold(0) { acc, characteristicProperty ->
    if (encrypted) {
        acc or characteristicProperty.encryptedValue
    } else {
        acc or characteristicProperty.rawValue
    }
}
