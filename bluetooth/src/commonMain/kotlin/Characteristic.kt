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
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo

open class Characteristic(val wrapper: CharacteristicWrapper, initialValue: ByteArray? = null, stateRepo: DeviceStateFlowRepo) : Attribute<DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(initialValue, stateRepo) {

    private val isBusy = AtomicBoolean(false)
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
     * `null` if notification is already enabled or previous action is not completed yet.
     * @see [disableNotification]
     * @see [isNotifying]
     */
    suspend fun enableNotification(): DeviceAction? {
        return if (isBusy.compareAndSet(expected = false, new = true) && !isNotifying) {
            val action = createNotificationAction(enabled = true)
            addAction(action)
            action.completedSuccessfully.invokeOnCompletion {
                if (it == null && action.completedSuccessfully.getCompleted()) {
                    isNotifying = true
                    isBusy.value = false
                }
            }
            action
        } else {
            null
        }
    }

    /**
     * Disables notification or indication for this [Characteristic]
     *
     * Creates and puts [DeviceAction.Notification.Disable] into queue to be executed.
     * Sets [isNotifying] to `false` after action completed successfully.
     *
     * @return [DeviceAction] if action was added to the queue, or
     * `null` if notification is already disabled or previous action is not completed yet.
     * @see [enableNotification]
     * @see [isNotifying]
     */
    suspend fun disableNotification(): DeviceAction? {
        return if (isBusy.compareAndSet(expected = false, new = true) && isNotifying) {
            val action = createNotificationAction(enabled = false)
            addAction(action)
            action.completedSuccessfully.invokeOnCompletion {
                if (it == null && action.completedSuccessfully.getCompleted()) {
                    isNotifying = false
                    isBusy.value = false
                }
            }
            action
        } else {
            null
        }
    }

    override val uuid = wrapper.uuid

    val descriptors: List<Descriptor> = wrapper.descriptors.map { Descriptor(it, stateRepo = stateRepo) }

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
}

expect interface CharacteristicWrapper {
    val uuid: UUID
    val descriptors: List<DescriptorWrapper>
    val value: Value?
}
