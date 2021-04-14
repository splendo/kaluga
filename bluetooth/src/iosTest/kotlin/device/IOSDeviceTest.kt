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

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.mock.MockCBPeripheralWrapper
import com.splendo.kaluga.bluetooth.mock.MockCharacteristic
import com.splendo.kaluga.bluetooth.mock.MockDescriptor
import com.splendo.kaluga.bluetooth.mock.MockServiceWrapper
import platform.CoreBluetooth.CBUUID

class IOSDeviceTest : DeviceTest() {

    private lateinit var service: MockServiceWrapper
    private lateinit var characteristic: MockCharacteristic
    private lateinit var descriptor: MockDescriptor

    override val deviceHolder: DeviceHolder
        get() = DeviceHolder(MockCBPeripheralWrapper())

    override fun createServices(stateRepo: DeviceStateFlowRepo): List<Service> {
        service = MockServiceWrapper(CBUUID(), listOf(Pair(CBUUID(), listOf(CBUUID()))))
        return listOf(Service(service, stateRepo))
    }

    override fun createCharacteristic(stateRepo: DeviceStateFlowRepo): Characteristic {
        characteristic = MockCharacteristic(service.characteristics.first(), stateRepo)
        return characteristic
    }

    override fun createDescriptor(stateRepo: DeviceStateFlowRepo): Descriptor {
        descriptor = MockDescriptor(characteristic.characteristic.descriptors!!.first(), stateRepo)
        return descriptor
    }

    override fun validateCharacteristicUpdated(): Boolean {
        return characteristic.didUpdate.isCompleted
    }

    override fun validateDescriptorUpdated(): Boolean {
        return descriptor.didUpdate.isCompleted
    }
}
