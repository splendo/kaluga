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

import com.splendo.kaluga.base.typedList
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData

actual interface CharacteristicWrapper {
    actual val uuid: CBUUID
    actual val descriptors: List<DescriptorWrapper>
    actual val value: NSData?

    fun readValue(peripheral: CBPeripheral)
    fun writeValue(value: NSData, peripheral: CBPeripheral)
    fun setNotificationValue(enabled: Boolean, peripheral: CBPeripheral)
}

class DefaultCharacteristicWrapper(private val characteristic: CBCharacteristic) : CharacteristicWrapper {

    override val uuid: CBUUID get() { return characteristic.UUID }
    override val descriptors: List<DescriptorWrapper> = characteristic.descriptors?.typedList<CBDescriptor>()?.map { DefaultDescriptorWrapper(it) } ?: emptyList()
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
