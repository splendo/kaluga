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
/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.server

import android.bluetooth.BluetoothGattCharacteristic
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.rawValue
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Permission

actual class LocalCharacteristicWrapper(val characteristic: BluetoothGattCharacteristic) {
    actual constructor(
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
    actual val uuid: UUID = characteristic.uuid
    actual val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties)
    actual val permissions: Set<Permission> = Permission.entries.filter {
        it.androidPermission and characteristic.permissions != 0
    }.toSet()

    actual fun addDescriptor(descriptor: LocalDescriptorWrapper) {
        characteristic.addDescriptor(descriptor.descriptor)
    }
}

private val Permission.androidPermission: Int get() = when (this) {
    Permission.READABLE -> BluetoothGattCharacteristic.PERMISSION_READ
    Permission.WRITABLE -> BluetoothGattCharacteristic.PERMISSION_WRITE
    Permission.READ_ENCRYPTION_REQUIRED -> BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED
    Permission.WRITE_ENCRYPTION_REQUIRED -> BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED
}
