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

import android.bluetooth.BluetoothGattDescriptor

/**
 * Accessor to a [BluetoothGattDescriptor]
 */
actual interface DescriptorWrapper {

    /**
     * The [UUID] of the descriptor
     */
    actual val uuid: java.util.UUID

    /**
     * The current [Value] of the descriptor
     */
    actual val value: ByteArray?

    /**
     * Accessor for updating [DescriptorWrapper.value]
     * @param value the [ByteArray] to update [DescriptorWrapper.value] with
     */
    fun updateValue(value: ByteArray?)

    /**
     * The integer representing all permissions for the descriptor
     */
    val permissions: Int

    /**
     * The [CharacteristicWrapper] of the Descriptor of the [BluetoothGattDescriptor]
     */
    val characteristic: CharacteristicWrapper
}

/**
 * Default implementation of [DescriptorWrapper]
 * @param gattDescriptor the [BluetoothGattDescriptor] to wrap
 */
class DefaultDescriptorWrapper(private val gattDescriptor: BluetoothGattDescriptor) : DescriptorWrapper {

    override val uuid: java.util.UUID
        get() = gattDescriptor.uuid
    override var value: ByteArray? = null
        private set
    override fun updateValue(value: ByteArray?) {
        this.value = value
    }
    override val permissions: Int
        get() = gattDescriptor.permissions
    override val characteristic: CharacteristicWrapper
        get() = DefaultCharacteristicWrapper(gattDescriptor.characteristic)
}
