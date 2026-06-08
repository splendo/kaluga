/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.test

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.DefaultRemoteCharacteristicWrapper
import com.splendo.kaluga.bluetooth.RemoteCharacteristicWrapper
import com.splendo.kaluga.bluetooth.RemoteServiceWrapper
import com.splendo.kaluga.bluetooth.Service
import java.util.UUID

class MockRemoteServiceWrapper(override val uuid: UUID = UUID.randomUUID(), initialCharacteristics: List<ServiceWrapperBuilder.Characteristic> = emptyList()) :
    RemoteServiceWrapper {

    constructor(builder: ServiceWrapperBuilder) : this(
        builder.uuid,
        builder.characteristics,
    )

    override val type: Service.Type = Service.Type.PRIMARY
    override val instanceId: Int = 0
    private val mutableCharacteristics = concurrentMutableListOf<RemoteCharacteristicWrapper>(
        *initialCharacteristics.map {
            AndroidMockCharacteristicWrapper(
                uuid = it.uuid,
                descriptorUUIDs = it.descriptorUUIDs,
                properties = it.properties,
                service = this,
            )
        }.toTypedArray(),
    )
    override val characteristics: List<RemoteCharacteristicWrapper>
        get() = mutableCharacteristics
    private val mutableIncludedServices = concurrentMutableListOf<RemoteServiceWrapper>()
    override val includedServices: List<RemoteServiceWrapper>
        get() = mutableIncludedServices

    override fun getCharacteristic(uuid: UUID): RemoteCharacteristicWrapper? = characteristics.firstOrNull { it.uuid == uuid }

    override fun addCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        mutableCharacteristics.add(DefaultRemoteCharacteristicWrapper(characteristic))
        return true
    }

    override fun addService(service: BluetoothGattService): Boolean {
        mutableIncludedServices.add(DefaultGattServiceWrapper(service))
        return true
    }
}

actual fun ServiceWrapperBuilder.build(): RemoteServiceWrapper = MockRemoteServiceWrapper(builder = this)
