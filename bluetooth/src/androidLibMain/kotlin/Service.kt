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

import android.bluetooth.BluetoothGattService
import android.os.ParcelUuid
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

actual class Service(private val service: BluetoothGattService, private val stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseService {

    override val uuid: UUID get() = UUID(ParcelUuid(service.uuid))

    override val characteristics: List<Characteristic>
        get() = service.characteristics.map { Characteristic(it, stateRepoAccesor) }
}