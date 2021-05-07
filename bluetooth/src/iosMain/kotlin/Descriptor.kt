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
import com.splendo.kaluga.base.toNSData
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.CoreBluetooth.CBUUIDCharacteristicExtendedPropertiesString
import platform.CoreBluetooth.CBUUIDCharacteristicFormatString
import platform.CoreBluetooth.CBUUIDCharacteristicUserDescriptionString
import platform.CoreBluetooth.CBUUIDClientCharacteristicConfigurationString
import platform.CoreBluetooth.CBUUIDServerCharacteristicConfigurationString
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding

actual open class Descriptor(internal val descriptor: DescriptorWrapper, stateRepo: DeviceStateFlowRepo) : BaseDescriptor(descriptor.value?.toByteArray(), stateRepo) {

    override val uuid = descriptor.UUID

    override fun createReadAction(): DeviceAction.Read.Descriptor {
        return DeviceAction.Read.Descriptor(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Descriptor {
        return DeviceAction.Write.Descriptor(newValue, this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return descriptor.value?.toByteArray()
    }
}

interface DescriptorWrapper {
    val UUID: CBUUID
    val value: NSData?

    fun readValue(peripheral: CBPeripheral)
    fun writeValue(value: NSData, peripheral: CBPeripheral)
}

class DefaultDescriptorWrapper(private val descriptor: CBDescriptor) : DescriptorWrapper {

    override val UUID: CBUUID get() { return descriptor.UUID }
    override val value: NSData? get() {
        return when (descriptor.UUID.uuidString) {
            CBUUIDCharacteristicFormatString -> {
                descriptor.value as? NSData
            }
            CBUUIDCharacteristicUserDescriptionString -> {
                (descriptor.value as? NSString)?.dataUsingEncoding(NSUTF8StringEncoding)
            }
            CBUUIDCharacteristicExtendedPropertiesString -> {
                (descriptor.value as? NSNumber)?.let {
                    byteArrayOf(it.shortValue.toByte()).toNSData()
                }
            }
            CBUUIDClientCharacteristicConfigurationString -> {
                (descriptor.value as? NSNumber)?.let {
                    byteArrayOf(it.shortValue.toByte()).toNSData()
                }
            }
            CBUUIDServerCharacteristicConfigurationString -> {
                (descriptor.value as? NSNumber)?.let {
                    byteArrayOf(it.shortValue.toByte()).toNSData()
                }
            }
            else -> descriptor.value as? NSData
        }
    }

    override fun readValue(peripheral: CBPeripheral) {
        peripheral.readValueForDescriptor(descriptor)
    }

    override fun writeValue(value: NSData, peripheral: CBPeripheral) {
        peripheral.writeValue(value, descriptor)
    }
}
