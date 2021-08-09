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

actual interface DescriptorWrapper {
    actual val uuid: java.util.UUID
    actual val value: ByteArray?
    fun updateValue(value: ByteArray?)

    val permissions: Int
    val characteristic: CharacteristicWrapper

    fun setValue(newValue: ByteArray?): Boolean
}

class DefaultDescriptorWrapper(private val gattDescriptor: BluetoothGattDescriptor) : DescriptorWrapper {

    override val uuid: java.util.UUID
        get() = gattDescriptor.uuid
    override val value: ByteArray?
        get() = gattDescriptor.value
    override fun updateValue(value: ByteArray?) {
        gattDescriptor.value = value
    }
    override val permissions: Int
        get() = gattDescriptor.permissions
    override val characteristic: CharacteristicWrapper
        get() = DefaultCharacteristicWrapper(gattDescriptor.characteristic)

    override fun setValue(newValue: ByteArray?): Boolean {
        return gattDescriptor.setValue(newValue)
    }
}
