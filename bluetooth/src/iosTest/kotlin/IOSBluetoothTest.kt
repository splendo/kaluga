/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.UUID
import com.splendo.kaluga.bluetooth.device.DeviceHolder
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.mock.MockCBPeripheralWrapper
import com.splendo.kaluga.bluetooth.mock.MockServiceWrapper
import com.splendo.kaluga.state.StateRepo
import platform.CoreBluetooth.CBUUID

class IOSBluetoothTest : BluetoothTest() {

    override fun createFilter(): Set<UUID> {
        return setOf(CBUUID())
    }

    override fun createDeviceHolder(): DeviceHolder {
        return DeviceHolder(MockCBPeripheralWrapper())
    }

    override fun createService(stateRepo: StateRepo<DeviceState>): Service {
        return Service(MockServiceWrapper(CBUUID(), listOf(Pair(CBUUID(), listOf(CBUUID())))), stateRepo)
    }
}
