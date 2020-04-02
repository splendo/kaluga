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

package com.splendo.kaluga.bluetooth.device

import android.bluetooth.BluetoothDevice
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.mock.MockCharacteristic
import com.splendo.kaluga.bluetooth.mock.MockCharacteristicWrapper
import com.splendo.kaluga.bluetooth.mock.MockDescriptor
import com.splendo.kaluga.bluetooth.mock.MockDeviceWrapper
import com.splendo.kaluga.bluetooth.mock.MockServiceWrapper
import com.splendo.kaluga.state.StateRepo
import java.util.UUID

class AndroidDeviceTest : DeviceTest() {

    companion object {
        const val deviceName = "name"
        const val address = ""
        const val deviceState = BluetoothDevice.BOND_NONE
    }

    private lateinit var gattServiceWrapper: MockServiceWrapper
    private lateinit var characteristic: MockCharacteristic
    private lateinit var descriptor: MockDescriptor

    override val deviceHolder: DeviceHolder get() {
        return DeviceHolder(MockDeviceWrapper(deviceName, address, deviceState))
    }

    override fun createServices(stateRepo: StateRepo<DeviceState>): List<Service> {
        gattServiceWrapper = MockServiceWrapper(UUID.randomUUID(), listOf(Pair(UUID.randomUUID(), listOf(UUID.randomUUID()))))
        return listOf(Service(gattServiceWrapper, stateRepo))
    }

    override fun createCharacteristic(stateRepo: StateRepo<DeviceState>): Characteristic {
        val characteristicWrapper = gattServiceWrapper.characteristics.first() as MockCharacteristicWrapper
        characteristic = MockCharacteristic(characteristicWrapper, stateRepo)
        return characteristic
    }

    override fun createDescriptor(stateRepo: StateRepo<DeviceState>): Descriptor {
        val descriptorWrapper = characteristic.characteristic.descriptors.first()
        descriptor = MockDescriptor(descriptorWrapper, stateRepo)
        return descriptor
    }

    override fun validateCharacteristicUpdated(): Boolean {
        return characteristic.didUpdate.isCompleted
    }

    override fun validateDescriptorUpdated(): Boolean {
        return descriptor.didUpdate.isCompleted
    }

}

