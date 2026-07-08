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

import com.splendo.kaluga.base.utils.typedList
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData

/**
 * Accessor to a [CBCharacteristic]
 */
actual interface RemoteCharacteristicWrapper {

    /**
     * The [UUID] of the characteristic
     */
    actual val uuid: CBUUID

    actual val service: RemoteServiceWrapper

    /**
     * The list of [RemoteDescriptorWrapper] of associated with the characteristic
     */
    actual val descriptors: List<RemoteDescriptorWrapper>

    /**
     * The set of all [CharacteristicProperty] of the characteristic
     */
    actual val properties: Set<CharacteristicProperty>

    /**
     * Request a [CBPeripheral] to read the characteristic
     * @param peripheral the [CBPeripheral] to perform the read operation
     */
    fun readValue(peripheral: CBPeripheral)

    /**
     * Request a [CBPeripheral] to write [value] to the characteristic
     * @param value the [NSData] to write to the characteristic
     * @param peripheral the [CBPeripheral] to perform the write operation
     * @param withResponse whether the write response is expected
     */
    fun writeValue(value: NSData, peripheral: CBPeripheral, withResponse: Boolean)

    /**
     * Request a [CBPeripheral] to update the notifying status of the characteristic
     * @param enabled if `true` notification should be enabled
     * @param peripheral the [CBPeripheral] to perform the notifying operation
     */
    fun setNotificationValue(enabled: Boolean, peripheral: CBPeripheral)
}

/**
 * Default implementation of [RemoteCharacteristicWrapper]
 * @param characteristic the [CBCharacteristic] to wrap
 */
class DefaultCharacteristicWrapper(private val characteristic: CBCharacteristic, override val service: RemoteServiceWrapper) : RemoteCharacteristicWrapper {

    override val uuid: CBUUID get() {
        return characteristic.UUID
    }
    override val descriptors: List<RemoteDescriptorWrapper> by lazy {
        characteristic.descriptors?.typedList<CBDescriptor>()?.map { DefaultDescriptorWrapper(it, this) } ?: emptyList()
    }
    override val properties get() = CharacteristicProperty.fromInt(characteristic.propertyBits())

    override fun readValue(peripheral: CBPeripheral) {
        peripheral.readValueForCharacteristic(characteristic)
    }

    override fun writeValue(value: NSData, peripheral: CBPeripheral, withResponse: Boolean) {
        peripheral.writeCharacteristicValue(value, characteristic, withResponse)
    }

    override fun setNotificationValue(enabled: Boolean, peripheral: CBPeripheral) {
        peripheral.setNotifyValue(enabled, characteristic)
    }
}
