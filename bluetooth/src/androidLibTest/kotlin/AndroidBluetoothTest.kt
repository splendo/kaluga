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

import com.splendo.kaluga.bluetooth.device.AndroidDeviceTest
import com.splendo.kaluga.bluetooth.device.DeviceHolder
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.mock.MockDeviceWrapper
import com.splendo.kaluga.bluetooth.mock.MockServiceWrapper

class AndroidBluetoothTest : BluetoothTest() {

    override fun createFilter(): Set<UUID> {
        return setOf(UUID.randomUUID())
    }

    override fun createDeviceHolder(): DeviceHolder {
        return DeviceHolder(MockDeviceWrapper(AndroidDeviceTest.deviceName, AndroidDeviceTest.address, AndroidDeviceTest.deviceState))
    }

    override fun createService(stateRepo: DeviceStateFlowRepo): Service {
        val uuid = UUID.randomUUID()
        val serviceWrapper = MockServiceWrapper(uuid, listOf(Pair(UUID.randomUUID(), listOf(UUID.randomUUID()))))
        return Service(serviceWrapper, stateRepo)
    }
}
