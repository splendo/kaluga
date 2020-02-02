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
import com.splendo.kaluga.bluetooth.mock.MockCBCentralManager
import com.splendo.kaluga.bluetooth.mock.MockCBPeripheral
import com.splendo.kaluga.bluetooth.mock.MockCBService
import com.splendo.kaluga.state.StateRepo

class IOSDeviceTest : DeviceTest() {

    override val deviceInfoHolder: DeviceInfoHolder
        get() = DeviceInfoHolder(MockCBPeripheral(), MockCBCentralManager(), AdvertisementData(emptyMap()))

    override fun createServices(stateRepo: StateRepo<DeviceState>): List<Service> {
        val service = Service(MockCBService(), stateRepo)
        return listOf(service)
    }

    override fun createCharacteristic(stateRepo: StateRepo<DeviceState>) : Characteristic {

    }

    override fun createDescriptor(stateRepo: StateRepo<DeviceState>) : Descriptor {

    }

    override fun validateCharacteristicUpdated() : Boolean {

    }

    override fun validateDescriptorUpdated() : Boolean {

    }

}