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
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.mock.*
import com.splendo.kaluga.state.StateRepoAccesor
import org.junit.Test
import java.util.*
import java.util.UUID
import kotlin.test.assertTrue

class AndroidDeviceTest : DeviceTest() {

    companion object {
        const val deviceName = "name"
        const val address = ""
        const val deviceState = BluetoothDevice.BOND_NONE
    }

    private lateinit var gattServiceWrapper: MockServiceWrapper
    private lateinit var characteristic: MockCharacteristic
    private lateinit var descriptor: MockDescriptor

    override val deviceInfoHolder: DeviceInfoHolder
        get() = DeviceInfoHolder(MockDeviceWrapper(deviceName, address, deviceState), AdvertisementData(null))

    override fun createServices(repoAccessor: StateRepoAccesor<DeviceState>): List<Service> {
        gattServiceWrapper = MockServiceWrapper(UUID.randomUUID(), listOf(Pair(UUID.randomUUID(), listOf(UUID.randomUUID()))))
        return listOf(Service(gattServiceWrapper, repoAccessor))
    }

    override fun createCharacteristic(repoAccessor: StateRepoAccesor<DeviceState>): Characteristic {
        val characteristicWrapper = gattServiceWrapper.characteristics.first() as MockCharacteristicWrapper
        characteristic = MockCharacteristic(characteristicWrapper, repoAccessor)
        return characteristic
    }

    override fun createDescriptor(repoAccessor: StateRepoAccesor<DeviceState>): Descriptor {
        val descriptorWrapper = characteristic.characteristic.descriptors.first()
        descriptor = MockDescriptor(descriptorWrapper, repoAccessor)
        return descriptor
    }

    override fun validateCharacteristicUpdated(): Boolean {
        return characteristic.didUpdate.isCompleted
    }

    override fun validateDescriptorUpdated(): Boolean {
        return descriptor.didUpdate.isCompleted
    }

    @Test
    fun testSomeBullshit() {
        assertTrue(true)
    }

}

