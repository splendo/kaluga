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

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.DefaultCharacteristicWrapper
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.GattServiceWrapper
import java.util.*

class MockServiceWrapper(override val uuid: UUID = UUID.randomUUID(), characteristicUuids: List<Pair<UUID, List<UUID>>> = emptyList()) : GattServiceWrapper {

    override val type: Int = 0
    override val instanceId: Int = 0
    private val mutableCharacteristics = mutableListOf<CharacteristicWrapper>(*characteristicUuids.map { MockCharacteristicWrapper(it.first, it.second, this) }.toTypedArray())
    override val characteristics: List<CharacteristicWrapper>
        get() = mutableCharacteristics
    private val mutableIncludedServices = mutableListOf<GattServiceWrapper>()
    override val includedServices: List<GattServiceWrapper>
        get() = mutableIncludedServices

    override fun getCharacteristic(uuid: UUID): CharacteristicWrapper? {
        return characteristics.firstOrNull { it.uuid == uuid }
    }

    override fun addCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        mutableCharacteristics.add(DefaultCharacteristicWrapper(characteristic))
        return true
    }

    override fun addService(service: BluetoothGattService): Boolean {
        mutableIncludedServices.add(DefaultGattServiceWrapper(service))
        return true
    }
}

