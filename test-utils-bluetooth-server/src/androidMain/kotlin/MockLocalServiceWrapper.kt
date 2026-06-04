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

package com.splendo.kaluga.test.bluetooth.server

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattService
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.AttributeIdentity
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Permission
import com.splendo.kaluga.bluetooth.server.LocalCharacteristicWrapper
import com.splendo.kaluga.bluetooth.server.LocalDescriptorWrapper
import com.splendo.kaluga.bluetooth.server.LocalServiceWrapper

private const val NO_PLATFORM = "Mock wrappers have no platform attribute and do not support server actions"

actual class MockLocalServiceWrapper actual constructor(actual override val uuid: UUID) : LocalServiceWrapper {
    override val identity: AttributeIdentity = MockAttributeIdentity()
    actual override fun addIncludedService(service: LocalServiceWrapper) {}
    actual override fun addCharacteristic(characteristic: LocalCharacteristicWrapper) {}
    override fun addToParent(parent: BluetoothGattService): Unit = error(NO_PLATFORM)
    override fun addTo(gattServer: BluetoothGattServer): Boolean = error(NO_PLATFORM)
    override fun removeFrom(gattServer: BluetoothGattServer): Unit = error(NO_PLATFORM)
}

actual class MockLocalCharacteristicWrapper actual constructor(
    actual override val uuid: UUID,
    actual override val properties: Set<CharacteristicProperty>,
    actual override val permissions: Set<Permission>,
) : LocalCharacteristicWrapper {
    actual override fun addDescriptor(descriptor: LocalDescriptorWrapper) {}
    override val identity: AttributeIdentity = MockAttributeIdentity()
    override fun addToService(service: BluetoothGattService): Unit = error(NO_PLATFORM)
    override fun notify(gattServer: BluetoothGattServer, device: BluetoothDevice, value: ByteArray, indicate: Boolean): Boolean = error(NO_PLATFORM)
}

actual class MockLocalDescriptorWrapper actual constructor(actual override val uuid: UUID) : LocalDescriptorWrapper {
    override val identity: AttributeIdentity = MockAttributeIdentity()
    override fun addToCharacteristic(characteristic: BluetoothGattCharacteristic): Unit = error(NO_PLATFORM)
}
