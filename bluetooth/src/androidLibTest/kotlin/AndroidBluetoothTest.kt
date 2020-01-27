import android.os.ParcelUuid
import com.splendo.kaluga.bluetooth.BluetoothTest
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.AndroidDeviceTest
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.mock.MockDeviceWrapper
import com.splendo.kaluga.bluetooth.mock.MockServiceWrapper
import com.splendo.kaluga.state.StateRepoAccesor

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

class AndroidBluetoothTest : BluetoothTest() {

    override fun createFilter(): Set<UUID> {
        return setOf(UUID.randomUUID())
    }

    override fun createDeviceInfoHolder(): DeviceInfoHolder {
        return DeviceInfoHolder(MockDeviceWrapper(AndroidDeviceTest.deviceName, AndroidDeviceTest.address, AndroidDeviceTest.deviceState), AdvertisementData(null))
    }

    override fun createService(stateRepoAccesor: StateRepoAccesor<DeviceState>): Service {
        val uuid = UUID.randomUUID()
        val serviceWrapper = MockServiceWrapper(uuid, listOf(Pair(UUID.randomUUID(), listOf(UUID.randomUUID()))))
        return Service(serviceWrapper, stateRepoAccesor)
    }
}