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
@file:JvmName("AndroidLocalDescriptor")

package com.splendo.kaluga.bluetooth.server

import android.bluetooth.BluetoothGattDescriptor
import com.splendo.kaluga.bluetooth.UUID

actual class LocalDescriptorWrapper(val descriptor: BluetoothGattDescriptor) {

    internal actual constructor(uuid: UUID, permissions: Set<LocalDescriptor.Permissions>) : this(
        BluetoothGattDescriptor(
            uuid,
            permissions.fold(0) { acc, permission ->
                acc or
                    permission.rawValue
            },
        ),
    )

    actual val uuid: UUID = descriptor.uuid
}

private val LocalDescriptor.Permissions.rawValue: Int get() = when (this) {
    LocalDescriptor.Permissions.READ -> BluetoothGattDescriptor.PERMISSION_READ
    LocalDescriptor.Permissions.READ_ENCRYPTED -> BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED
    LocalDescriptor.Permissions.WRITE -> BluetoothGattDescriptor.PERMISSION_WRITE
    LocalDescriptor.Permissions.WRITE_ENCRYPTED -> BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED
}
