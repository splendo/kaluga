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

package com.splendo.kaluga.bluetooth.mock

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.DescriptorWrapper
import com.splendo.kaluga.bluetooth.GattServiceWrapper
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import java.util.*

class MockCharacteristic(characteristicWrapper: MockCharacteristicWrapper, stateRepo: StateRepo<DeviceState>) : Characteristic(characteristicWrapper, stateRepo) {

    val didUpdate = EmptyCompletableDeferred()

    internal override suspend fun updateValue() {
        didUpdate.complete()
    }

}

class MockCharacteristicWrapper(override val uuid: UUID = UUID.randomUUID(), private val descriptorUuids: List<UUID> = emptyList(), override val service: GattServiceWrapper) : CharacteristicWrapper {

    override var value: ByteArray? = null

    override val descriptors: List<DescriptorWrapper> = descriptorUuids.map { MockDescriptorWrapper(it, this) }
    override val permissions: Int
        get() = 0
    override val properties: Int
        get() = 0
    override var writeType = 0

    @ExperimentalStdlibApi
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

