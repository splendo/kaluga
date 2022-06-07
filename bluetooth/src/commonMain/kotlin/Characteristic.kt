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

import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.base.flow.SequentialMutableSharedFlow
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

open class Characteristic(val wrapper: CharacteristicWrapper, initialValue: ByteArray? = null, newActionFlow: SequentialMutableSharedFlow<in BaseDeviceConnectionManager.Event.AddAction>) : Attribute<DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(initialValue, newActionFlow) {

    private val isBusy = MutableStateFlow(false)
    private val _isNotifying = AtomicBoolean(false)
    var isNotifying: Boolean
        get() = _isNotifying.value
        set(value) { _isNotifying.value = value }

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

    val descriptors: List<Descriptor> = wrapper.descriptors.map { Descriptor(it, newActionFlow = newActionFlow) }

    override fun createReadAction(): DeviceAction.Read.Characteristic {
        return DeviceAction.Read.Characteristic(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Characteristic {
        return DeviceAction.Write.Characteristic(newValue, this)
    }

    private fun createNotificationAction(enabled: Boolean): DeviceAction.Notification {
        return if (enabled) DeviceAction.Notification.Enable(this)
        else DeviceAction.Notification.Disable(this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return wrapper.value?.asBytes
    }

    fun hasProperty(property: CharacteristicProperties) = hasProperties(listOf(property))

    private fun hasProperties(properties: List<CharacteristicProperties>) = wrapper
        .containsAnyOf(*properties.map(CharacteristicProperties::rawValue).toIntArray())
}

expect interface CharacteristicWrapper {
    val uuid: UUID
    val descriptors: List<DescriptorWrapper>
    val value: Value?
    val properties: Int
}

fun CharacteristicWrapper.containsAnyOf(vararg property: Int) = if (property.isNotEmpty()) {
    properties and property.reduce { acc, i -> acc.or(i) } != 0
} else { false }

sealed class CharacteristicProperties(val rawValue: Int) {
    object Broadcast : CharacteristicProperties(0x01)
    object Read : CharacteristicProperties(0x02)
    object WriteWithoutResponse : CharacteristicProperties(0x04)
    object Write : CharacteristicProperties(0x08)
    object Notify : CharacteristicProperties(0x10)
    object Indicate : CharacteristicProperties(0x20)
    object SignedWrite : CharacteristicProperties(0x40)
    object ExtendedProperties : CharacteristicProperties(0x80)

    infix fun or(other: CharacteristicProperties) = rawValue or other.rawValue
}
