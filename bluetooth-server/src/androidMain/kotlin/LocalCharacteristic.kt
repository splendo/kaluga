/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

@file:JvmName("AndroidLocalCharacteristic")

package com.splendo.kaluga.bluetooth.server

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.rawValue
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Permission

actual interface LocalCharacteristicWrapper {
    actual val uuid: UUID
    actual val properties: Set<CharacteristicProperty>
    actual val permissions: Set<Permission>

    /**
     * Adds a [LocalDescriptorWrapper] to the characteristic
     */
    actual fun addDescriptor(descriptor: LocalDescriptorWrapper)

    /**
     * Identity used to correlate this characteristic with incoming GATT callbacks.
     */
    val identity: AttributeIdentity

    /**
     * Adds the characteristic to a [BluetoothGattService]
     */
    fun addToService(service: BluetoothGattService)

    /**
     * Notifies a [BluetoothDevice] of a new [value] through a [BluetoothGattServer]
     */
    fun notify(gattServer: BluetoothGattServer, device: BluetoothDevice, value: ByteArray, indicate: Boolean): Boolean
}

class DefaultLocalCharacteristicWrapper(internal val characteristic: BluetoothGattCharacteristic) : LocalCharacteristicWrapper {
    constructor(
        uuid: UUID,
        properties: Set<CharacteristicProperty>,
        encryptedNotification: Boolean,
        permissions: Set<Permission>,
    ) : this(
        BluetoothGattCharacteristic(
            uuid,
            // Android does not have a specific property for encrypted notifications
            properties.rawValue(false),
            permissions.fold(0) { acc, permission -> acc or permission.androidPermission },
        ),
    )
    override val uuid: UUID = characteristic.uuid
    override val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties)
    override val permissions: Set<Permission> = Permission.entries.filter {
        it.androidPermission and characteristic.permissions != 0
    }.toSet()

    override fun addDescriptor(descriptor: LocalDescriptorWrapper) {
        descriptor.addToCharacteristic(characteristic)
    }

    override val identity: AttributeIdentity get() = GattCharacteristicIdentity(characteristic)

    override fun addToService(service: BluetoothGattService) {
        service.addCharacteristic(characteristic)
    }

    override fun notify(gattServer: BluetoothGattServer, device: BluetoothDevice, value: ByteArray, indicate: Boolean): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gattServer.notifyCharacteristicChanged(device, characteristic, indicate, value) == BluetoothStatusCodes.SUCCESS
        } else {
            @Suppress("DEPRECATION")
            characteristic.value = value
            @Suppress("DEPRECATION")
            gattServer.notifyCharacteristicChanged(device, characteristic, indicate)
        }
}

@JvmInline
value class GattCharacteristicIdentity(val characteristic: BluetoothGattCharacteristic) : AttributeIdentity

private val Permission.androidPermission: Int get() = when (this) {
    Permission.READABLE -> BluetoothGattCharacteristic.PERMISSION_READ
    Permission.WRITABLE -> BluetoothGattCharacteristic.PERMISSION_WRITE
    Permission.READ_ENCRYPTION_REQUIRED -> BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED
    Permission.WRITE_ENCRYPTION_REQUIRED -> BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED
}
