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

actual interface CharacteristicWrapper {

    actual val uuid: java.util.UUID
    actual val value: ByteArray?
    fun updateValue(value:ByteArray?)

    val service: ServiceWrapper
    actual val descriptors: List<DescriptorWrapper>
    val permissions: Int
    val properties: Int
    var writeType: Int

    fun setValue(newValue: String): Boolean
    fun setValue(newValue: ByteArray?): Boolean
    fun setValue(mantissa: Int, exponent: Int, formatType: Int, offset: Int): Boolean
    fun getDescriptor(uuid: java.util.UUID): DescriptorWrapper?

    fun floatValue(formatType: Int, offset: Int): Float
    fun intValue(formatType: Int, offset: Int): Int
}

fun CharacteristicWrapper.hasProperty(property: Int) = properties.and(property) == property

class DefaultCharacteristicWrapper(private val gattCharacteristic: BluetoothGattCharacteristic) : CharacteristicWrapper {

    override val uuid: java.util.UUID
        get() { return gattCharacteristic.uuid }
    override val value: ByteArray?
        get() {
            return gattCharacteristic.value
        }

    override fun updateValue(value: ByteArray?) {
        gattCharacteristic.value = value
    }

    override val service: ServiceWrapper
        get() = DefaultGattServiceWrapper(gattCharacteristic.service)
    override val descriptors: List<DescriptorWrapper>
        get() { return gattCharacteristic.descriptors.map { DefaultDescriptorWrapper(it) } }
    override val permissions: Int
        get() { return gattCharacteristic.permissions }
    override val properties: Int
        get() { return gattCharacteristic.properties }
    override var writeType: Int
        get() { return gattCharacteristic.writeType }
        set(value) { gattCharacteristic.writeType = value }

    override fun setValue(newValue: String): Boolean {
        return gattCharacteristic.setValue(newValue)
    }

    override fun setValue(newValue: ByteArray?): Boolean {
        return gattCharacteristic.setValue(newValue)
    }

    override fun setValue(mantissa: Int, exponent: Int, formatType: Int, offset: Int): Boolean {
        return gattCharacteristic.setValue(mantissa, exponent, formatType, offset)
    }

    override fun getDescriptor(uuid: java.util.UUID): DescriptorWrapper? {
        return gattCharacteristic.getDescriptor(uuid)?.let { DefaultDescriptorWrapper(it) }
    }

    override fun floatValue(formatType: Int, offset: Int): Float {
        return gattCharacteristic.getFloatValue(formatType, offset)
    }

    override fun intValue(formatType: Int, offset: Int): Int {
        return gattCharacteristic.getIntValue(formatType, offset)
    }
}
