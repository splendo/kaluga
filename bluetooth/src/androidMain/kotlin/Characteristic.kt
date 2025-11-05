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
actual interface RemoteCharacteristicWrapper {

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
     * The [RemoteServiceWrapper] of the Service of the [BluetoothGattCharacteristic]
     */
    actual val service: RemoteServiceWrapper

    /**
     * The list of [RemoteDescriptorWrapper] of associated with the characteristic
     */
    actual val descriptors: List<RemoteDescriptorWrapper>

    /**
     * The integer representing all permissions for the characteristic
     */
    val permissions: Int

    /**
     * The set of all [CharacteristicProperty] of the characteristic
     */
    actual val properties: Set<CharacteristicProperty>

    /**
     * The [WriteType] of the characteristic
     */
    var writeType: WriteType

    /**
     * Gets the [RemoteDescriptorWrapper] for the descriptor with a given [java.util.UUID] if it belongs to the characteristic
     * @param uuid the [java.util.UUID] of the descriptor to get
     * @return the [RemoteDescriptorWrapper] belonging to [uuid] if it exists, or `null` otherwise
     */
    fun getDescriptor(uuid: java.util.UUID): RemoteDescriptorWrapper?
}

/**
 * Default implementation of [RemoteCharacteristicWrapper]
 * @param gattCharacteristic the [BluetoothGattCharacteristic] to wrap
 */
class DefaultRemoteCharacteristicWrapper(private val gattCharacteristic: BluetoothGattCharacteristic) : RemoteCharacteristicWrapper {

    override val uuid: java.util.UUID
        get() {
            return gattCharacteristic.uuid
        }

    override val service: RemoteServiceWrapper
        get() = DefaultGattServiceWrapper(gattCharacteristic.service)
    override val descriptors: List<RemoteDescriptorWrapper>
        get() {
            return gattCharacteristic.descriptors.map { DefaultRemoteDescriptorWrapper(it) }
        }
    override val permissions: Int
        get() {
            return gattCharacteristic.permissions
        }
    override val properties: Set<CharacteristicProperty>
        get() {
            return CharacteristicProperty.fromInt(gattCharacteristic.properties)
        }
    override var writeType: RemoteCharacteristicWrapper.WriteType
        get() = when (gattCharacteristic.writeType) {
            BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE -> RemoteCharacteristicWrapper.WriteType.NO_RESPONSE
            BluetoothGattCharacteristic.WRITE_TYPE_SIGNED -> RemoteCharacteristicWrapper.WriteType.SIGNED
            else -> RemoteCharacteristicWrapper.WriteType.DEFAULT
        }
        set(value) {
            gattCharacteristic.writeType = value.rawValue
        }

    override fun getDescriptor(uuid: java.util.UUID): RemoteDescriptorWrapper? = gattCharacteristic.getDescriptor(uuid)?.let { DefaultRemoteDescriptorWrapper(it) }
}
