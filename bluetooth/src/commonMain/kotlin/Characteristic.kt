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
import com.splendo.kaluga.logging.Logger
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

/**
 * An [Attribute] of a Bluetooth Characteristic
 * @property wrapper the [CharacteristicWrapper] to access the platform characteristic
 * @param initialValue the initial [ByteArray] value of the characteristic
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param parentLogTag the log tag used to modify the log tag of this characteristic
 * @param logger the [Logger] to use for logging.
 */
open class Characteristic(
    val wrapper: CharacteristicWrapper,
    initialValue: ByteArray? = null,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    parentLogTag: String,
    logger: Logger,
) : Attribute<DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(
    initialValue,
    emitNewAction,
    "$parentLogTag Characteristic",
    logger,
) {

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

    /**
     * The list of [Descriptor] associated with the characteristic
     */
    val descriptors: List<Descriptor> = wrapper.descriptors.map { Descriptor(it, emitNewAction = emitNewAction, parentLogTag = logTag, logger = logger) }

    override fun createReadAction(): DeviceAction.Read.Characteristic {
        return DeviceAction.Read.Characteristic(this)
    }

    override fun createWriteAction(newValue: ByteArray): DeviceAction.Write.Characteristic {
        return DeviceAction.Write.Characteristic(newValue, this)
    }

    private fun createNotificationAction(enabled: Boolean): DeviceAction.Notification {
        return if (enabled) DeviceAction.Notification.Enable(this) else DeviceAction.Notification.Disable(this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return wrapper.value?.asBytes
    }

    /**
     * Checks if the characteristic has a given [CharacteristicProperties]
     */
    fun hasProperty(property: CharacteristicProperties) = hasProperties(listOf(property))

    private fun hasProperties(properties: List<CharacteristicProperties>) = wrapper
        .containsAnyOf(*properties.map(CharacteristicProperties::rawValue).toIntArray())
}

/**
 * Accessor to the platform level Bluetooth characteristic
 */
expect interface CharacteristicWrapper {
    /**
     * The [UUID] of the characteristic
     */
    val uuid: UUID

    /**
     * The list of [DescriptorWrapper] of associated with the characteristic
     */
    val descriptors: List<DescriptorWrapper>

    /**
     * The current [Value] of the characteristic
     */
    val value: Value?

    /**
     * The integer representing all [CharacteristicProperties] of the characteristic
     */
    val properties: Int
}

/**
 * Checks whether [CharacteristicWrapper.properties] contains any properties in [property]
 * @param property the list of properties to check
 * @return `tre` if [CharacteristicWrapper.properties] contains at least one of [property]
 */
fun CharacteristicWrapper.containsAnyOf(vararg property: Int) = if (property.isNotEmpty()) {
    properties and property.reduce { acc, i -> acc.or(i) } != 0
} else {
    false
}

/**
 * The properties associated with a Bluetooth Characteristic
 * @param rawValue the raw value associated with the property
 */
sealed class CharacteristicProperties(val rawValue: Int) {

    /**
     * Characteristic is broadcastable
     */
    data object Broadcast : CharacteristicProperties(0x01)

    /**
     * Characteristic is readable
     */
    data object Read : CharacteristicProperties(0x02)

    /**
     * Characteristic can be written without response
     */
    data object WriteWithoutResponse : CharacteristicProperties(0x04)

    /**
     * Characteristic can be written
     */
    data object Write : CharacteristicProperties(0x08)

    /**
     * Characteristic supports notification
     */
    data object Notify : CharacteristicProperties(0x10)

    /**
     * Characteristic supports indication
     */
    data object Indicate : CharacteristicProperties(0x20)

    /**
     * Characteristic supports write with signature
     */
    data object SignedWrite : CharacteristicProperties(0x40)

    /**
     * Characteristic has extended properties
     */
    data object ExtendedProperties : CharacteristicProperties(0x80)

    /**
     * Gets a [CharacteristicProperties] by combining two [CharacteristicProperties]
     * @param other the [CharacteristicProperties] to combine with
     * @return the combined [CharacteristicProperties]
     */
    infix fun or(other: CharacteristicProperties) = rawValue or other.rawValue
}
