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

import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.DescriptorWrapper
import com.splendo.kaluga.bluetooth.device.BluetoothGattWrapper
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import java.util.*

class MockDescriptor(descriptorWrapper: DescriptorWrapper, stateRepoAccessor: StateRepoAccesor<DeviceState>) : Descriptor(descriptorWrapper, stateRepoAccessor) {

    val didUpdate = EmptyCompletableDeferred()

    internal override suspend fun updateValue() {
        didUpdate.complete()
    }

}

class MockDescriptorWrapper(override val uuid: UUID = UUID.randomUUID(), override val characteristic: CharacteristicWrapper) : DescriptorWrapper {

    override var value: ByteArray? = null
    override val permissions: Int = 0

    override fun setValue(newValue: ByteArray?): Boolean {
        value = newValue
        return true
    }

}

