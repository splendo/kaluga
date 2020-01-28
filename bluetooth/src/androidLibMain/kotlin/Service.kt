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

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.os.ParcelUuid
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

actual open class Service(private val service: GattServiceWrapper, private val stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseService {

    override val uuid = service.uuid

    override val characteristics = service.characteristics.map { Characteristic(it, stateRepoAccesor) }
}

interface GattServiceWrapper {

    val uuid: java.util.UUID
    val type: Int
    val instanceId: Int
    val characteristics: List<CharacteristicWrapper>
    val includedServices: List<GattServiceWrapper>

    fun getCharacteristic(uuid: java.util.UUID): CharacteristicWrapper?
    fun addCharacteristic(characteristic: BluetoothGattCharacteristic) : Boolean
    fun addService(service: BluetoothGattService) : Boolean

}

class DefaultGattServiceWrapper(private val gattService: BluetoothGattService) : GattServiceWrapper {

    override val uuid: java.util.UUID
        get() = gattService.uuid
    override val type: Int
        get() = gattService.type
    override val instanceId: Int
        get() = gattService.instanceId
    override val characteristics: List<CharacteristicWrapper>
        get() = gattService.characteristics.map { DefaultCharacteristicWrapper(it) }
    override val includedServices: List<GattServiceWrapper>
        get() = gattService.includedServices.map { DefaultGattServiceWrapper(it) }

    override fun getCharacteristic(uuid: java.util.UUID): CharacteristicWrapper? {
        return gattService.getCharacteristic(uuid)?.let { DefaultCharacteristicWrapper(it) }
    }

    override fun addCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        return gattService.addCharacteristic(characteristic)
    }

    override fun addService(service: BluetoothGattService): Boolean {
        return gattService.addService(service)
    }
}