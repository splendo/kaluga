/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.bluetooth

import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.DescriptorWrapper
import com.splendo.kaluga.bluetooth.ServiceWrapper
import java.util.UUID

class MockCharacteristicWrapper(override val uuid: UUID = UUID.randomUUID(), descriptorUuids: List<UUID> = emptyList(), override val service: ServiceWrapper) : CharacteristicWrapper {

    override var value: ByteArray? = null
    override fun updateValue(value: ByteArray?) {
        this.value = value
    }

    override val descriptors: List<DescriptorWrapper> = descriptorUuids.map { AndroidMockDescriptorWrapper(it, this) }
    override val permissions: Int
        get() = 0
    override val properties: Int
        get() = 0
    override var writeType = 0

    override fun setValue(newValue: String): Boolean {
        value = newValue.encodeToByteArray()
        return true
    }

    override fun setValue(newValue: ByteArray?): Boolean {
        value = newValue
        return true
    }

    override fun setValue(mantissa: Int, exponent: Int, formatType: Int, offset: Int): Boolean {
        return true
    }

    override fun getDescriptor(uuid: UUID): DescriptorWrapper? {
        return descriptors.firstOrNull { it.uuid == uuid }
    }

    override fun floatValue(formatType: Int, offset: Int): Float {
        return 0.0f
    }

    override fun intValue(formatType: Int, offset: Int): Int {
        return 0
    }
}
