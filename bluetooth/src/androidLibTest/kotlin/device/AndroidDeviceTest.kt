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
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.mock.MockCharacteristic
import com.splendo.kaluga.bluetooth.mock.MockDescriptor
import com.splendo.kaluga.bluetooth.mock.MockDeviceWrapper
import com.splendo.kaluga.state.StateRepoAccesor
import org.junit.Test
import java.util.*
import kotlin.test.assertTrue

class AndroidDeviceTest : DeviceTest() {

    companion object {
        const val deviceName = "name"
        const val address = ""
        const val deviceState = BluetoothDevice.BOND_NONE
    }

    private lateinit var characteristic: MockCharacteristic
    private lateinit var descriptor: MockDescriptor

    override val deviceInfoHolder: DeviceInfoHolder
        get() = DeviceInfoHolder(MockDeviceWrapper(deviceName, address, deviceState), AdvertisementData(null))

    override fun createServices(repoAccessor: StateRepoAccesor<DeviceState>): List<Service> {
        val gattService = BluetoothGattService(UUID.randomUUID(), BluetoothGattService.SERVICE_TYPE_PRIMARY)
        gattService.addCharacteristic(BluetoothGattCharacteristic(UUID.randomUUID(), BluetoothGattCharacteristic.PROPERTY_READ,  BluetoothGattCharacteristic.PERMISSION_READ))
        return listOf(Service(gattService, repoAccessor))
    }

    override fun createCharacteristic(repoAccessor: StateRepoAccesor<DeviceState>): Characteristic {
        characteristic = MockCharacteristic(repoAccessor)
        return characteristic
    }

    override fun createDescriptor(repoAccessor: StateRepoAccesor<DeviceState>): Descriptor {
        descriptor = MockDescriptor(repoAccessor)
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

