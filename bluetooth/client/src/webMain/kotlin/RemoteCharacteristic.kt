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

import com.splendo.kaluga.bluetooth.device.WebCharacteristic

/**
 * Accessor to a Web Bluetooth `BluetoothRemoteGATTCharacteristic`. A structural snapshot built during
 * service discovery; the actual read/write/notify go through the connection manager + per-target interop.
 */
actual interface RemoteCharacteristicWrapper {
    actual val uuid: UUID
    actual val service: RemoteServiceWrapper
    actual val descriptors: List<RemoteDescriptorWrapper>
    actual val properties: Set<CharacteristicProperty>
}

internal class WebCharacteristicWrapper(characteristic: WebCharacteristic, override val service: RemoteServiceWrapper) : RemoteCharacteristicWrapper {
    override val uuid: UUID = uuidFrom(characteristic.uuid)
    override val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties)
    override val descriptors: List<RemoteDescriptorWrapper> = characteristic.descriptors.map { WebDescriptorWrapper(uuidFrom(it), this) }
}
