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

/**
 * Accessor to a [BluetoothGattCharacteristic]
 */
actual interface CharacteristicWrapper {

    /**
     * The write type of a characteristic
     */
    enum class WriteType(val rawValue: Int) {
        /**
         * Write characteristic, requesting acknowledgement by the remote device
         */
        DEFAULT(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT),

        /**
         * Write characteristic without requiring a response by the remote device
         */
        NO_RESPONSE(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE),

        /**
         * Write characteristic including authentication signature
         */
        SIGNED(BluetoothGattCharacteristic.WRITE_TYPE_SIGNED),
    }

    /**
     * The [UUID] of the characteristic
     */
    actual val uuid: java.util.UUID

    /**
     * The current [Value] of the characteristic
     */
    actual val value: ByteArray?

    /**
     * Accessor for updating [CharacteristicWrapper.value]
     * @param value the [ByteArray] to update [CharacteristicWrapper.value] with
     */
    fun updateValue(value: ByteArray?)

    /**
     * The [ServiceWrapper] of the Service of the [BluetoothGattCharacteristic]
     */
    val service: ServiceWrapper

    /**
     * The list of [DescriptorWrapper] of associated with the characteristic
     */
    actual val descriptors: List<DescriptorWrapper>

    /**
     * The integer representing all permissions for the characteristic
     */
    val permissions: Int

    /**
     * The integer representing all [CharacteristicProperties] of the characteristic
     */
    actual val properties: Int

    /**
     * The [WriteType] of the characteristic
     */
    var writeType: WriteType

    /**
     * Gets the [DescriptorWrapper] for the descriptor with a given [java.util.UUID] if it belongs to the characteristic
     * @param uuid the [java.util.UUID] of the descriptor to get
     * @return the [DescriptorWrapper] belonging to [uuid] if it exists, or `null` otherwise
     */
    fun getDescriptor(uuid: java.util.UUID): DescriptorWrapper?
}

/**
 * Default implementation of [CharacteristicWrapper]
 * @param gattCharacteristic the [BluetoothGattCharacteristic] to wrap
 */
class DefaultCharacteristicWrapper(private val gattCharacteristic: BluetoothGattCharacteristic) : CharacteristicWrapper {

    override val uuid: java.util.UUID
        get() {
            return gattCharacteristic.uuid
        }
    override var value: ByteArray? = null
        private set

    override fun updateValue(value: ByteArray?) {
        this.value = value
    }

    override val service: ServiceWrapper
        get() = DefaultGattServiceWrapper(gattCharacteristic.service)
    override val descriptors: List<DescriptorWrapper>
        get() {
            return gattCharacteristic.descriptors.map { DefaultDescriptorWrapper(it) }
        }
    override val permissions: Int
        get() {
            return gattCharacteristic.permissions
        }
    override val properties: Int
        get() {
            return gattCharacteristic.properties
        }
    override var writeType: CharacteristicWrapper.WriteType
        get() = when (gattCharacteristic.writeType) {
            BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE -> CharacteristicWrapper.WriteType.NO_RESPONSE
            BluetoothGattCharacteristic.WRITE_TYPE_SIGNED -> CharacteristicWrapper.WriteType.SIGNED
            else -> CharacteristicWrapper.WriteType.DEFAULT
        }
        set(value) {
            gattCharacteristic.writeType = value.rawValue
        }

    override fun getDescriptor(uuid: java.util.UUID): DescriptorWrapper? {
        return gattCharacteristic.getDescriptor(uuid)?.let { DefaultDescriptorWrapper(it) }
    }
}
