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

import com.splendo.kaluga.base.toByteArray
import com.splendo.kaluga.base.typedList
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData

actual open class Characteristic(
    val characteristic: CharacteristicWrapper,
    stateRepo: DeviceStateFlowRepo
) : BaseCharacteristic(characteristic.value?.toByteArray(), stateRepo) {

    override val uuid = characteristic.UUID

    override val descriptors = characteristic.descriptors?.map { Descriptor(it, stateRepo) } ?: emptyList()

    override fun createReadAction(): DeviceAction.Read.Characteristic {
        return DeviceAction.Read.Characteristic(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Characteristic {
        return DeviceAction.Write.Characteristic(newValue, this)
    }

    override fun createNotificationAction(enabled: Boolean): DeviceAction.Notification {
        return DeviceAction.Notification(this, enabled)
    }

    override fun getUpdatedValue(): ByteArray? {
        return characteristic.value?.toByteArray()
    }
}

interface CharacteristicWrapper {
    val UUID: CBUUID
    val descriptors: List<DescriptorWrapper>?
    val value: NSData?

    fun readValue(peripheral: CBPeripheral)
    fun writeValue(value: NSData, peripheral: CBPeripheral)
    fun setNotificationValue(enabled: Boolean, peripheral: CBPeripheral)
}

class DefaultCharacteristicWrapper(private val characteristic: CBCharacteristic) : CharacteristicWrapper {

    override val UUID: CBUUID get() { return characteristic.UUID }
    override val descriptors: List<DescriptorWrapper>? = characteristic.descriptors?.typedList<CBDescriptor>()?.map { DefaultDescriptorWrapper(it) }
    override val value: NSData? get() { return characteristic.value }

    override fun readValue(peripheral: CBPeripheral) {
        peripheral.readValueForCharacteristic(characteristic)
    }

    override fun writeValue(value: NSData, peripheral: CBPeripheral) {
        peripheral.writeValue(value, characteristic, CBCharacteristicWriteWithResponse)
    }

    override fun setNotificationValue(enabled: Boolean, peripheral: CBPeripheral) {
        peripheral.setNotifyValue(enabled, characteristic)
    }
}
