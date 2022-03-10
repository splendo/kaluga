/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.DefaultCharacteristicWrapper
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.ServiceWrapper
import java.util.UUID

class MockServiceWrapper(
    override val uuid: UUID = UUID.randomUUID(),
    initialCharacteristics: List<ServiceWrapperBuilder.Characteristic> = emptyList()
) : ServiceWrapper {

    constructor(builder: ServiceWrapperBuilder) : this(
        builder.uuid,
        builder.characteristics
    )

    override val type: Int = 0
    override val instanceId: Int = 0
    private val mutableCharacteristics = mutableListOf<CharacteristicWrapper>(
        *initialCharacteristics.map {
            AndroidMockCharacteristicWrapper(
                uuid = it.uuid,
                descriptorUUIDs = it.descriptorUUIDs,
                properties = it.properties,
                service = this
            )
        }.toTypedArray()
    )
    override val characteristics: List<CharacteristicWrapper>
        get() = mutableCharacteristics
    private val mutableIncludedServices = mutableListOf<ServiceWrapper>()
    override val includedServices: List<ServiceWrapper>
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
