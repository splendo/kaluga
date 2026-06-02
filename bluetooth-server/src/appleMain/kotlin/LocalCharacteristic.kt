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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.rawValue
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Permission
import platform.CoreBluetooth.CBAttributePermissionsReadEncryptionRequired
import platform.CoreBluetooth.CBAttributePermissionsReadable
import platform.CoreBluetooth.CBAttributePermissionsWriteEncryptionRequired
import platform.CoreBluetooth.CBAttributePermissionsWriteable
import platform.CoreBluetooth.CBMutableCharacteristic

actual class LocalCharacteristicWrapper(val characteristic: CBMutableCharacteristic) {
    actual constructor(
        uuid: UUID,
        properties: Set<CharacteristicProperty>,
        encryptedNotification: Boolean,
        permissions: Set<Permission>,
    ) : this(
        CBMutableCharacteristic(
            uuid,
            properties.rawValue(encryptedNotification).toULong(),
            null,
            permissions.fold(0UL) { acc, permission -> acc or permission.cbAttributePermission },
        ),
    )
    actual val uuid = characteristic.UUID
    actual val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties.toInt())
    actual val permissions: Set<Permission> = Permission.entries.filter {
        it.cbAttributePermission and characteristic.permissions != 0UL
    }.toSet()

    actual fun addDescriptor(descriptor: LocalDescriptorWrapper) {
        characteristic.setDescriptors(characteristic.descriptors.orEmpty() + descriptor.descriptor)
    }
}

private val Permission.cbAttributePermission: ULong get() = when (this) {
    Permission.READABLE -> CBAttributePermissionsReadable
    Permission.WRITABLE -> CBAttributePermissionsWriteable
    Permission.READ_ENCRYPTION_REQUIRED -> CBAttributePermissionsReadEncryptionRequired
    Permission.WRITE_ENCRYPTION_REQUIRED -> CBAttributePermissionsWriteEncryptionRequired
}
