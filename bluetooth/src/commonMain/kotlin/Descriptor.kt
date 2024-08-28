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

/**
 * An [Attribute] of a Bluetooth Descriptor
 * @property wrapper the [DescriptorWrapper] to access the platform descriptor
 * @param initialValue the initial [ByteArray] value of the descriptor
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param parentLogTag the log tag used to modify the log tag of this descriptor
 * @param logger the [Logger] to use for logging.
 */
open class Descriptor(
    val wrapper: DescriptorWrapper,
    initialValue: ByteArray? = null,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    parentLogTag: String,
    logger: Logger,
) : Attribute<DeviceAction.Read.Descriptor, DeviceAction.Write.Descriptor>(
    initialValue,
    emitNewAction,
    "$parentLogTag Descriptor",
    logger,
) {

    override val uuid = wrapper.uuid

    override fun createReadAction(): DeviceAction.Read.Descriptor = DeviceAction.Read.Descriptor(this)

    override fun createWriteAction(newValue: ByteArray): DeviceAction.Write.Descriptor = DeviceAction.Write.Descriptor(newValue, this)

    override fun getUpdatedValue(): ByteArray? = wrapper.value?.asBytes
}

/**
 * Accessor to the platform level Bluetooth Descriptor
 */
expect interface DescriptorWrapper {

    /**
     * The [UUID] of the descriptor
     */
    val uuid: UUID

    /**
     * The current [Value] of the descriptor
     */
    val value: Value?
}
