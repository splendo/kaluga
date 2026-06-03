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

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.AttributeIdentity
import com.splendo.kaluga.bluetooth.server.KalugaBluetoothServerWrapper
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Permission
import com.splendo.kaluga.bluetooth.server.LocalCharacteristicWrapper
import com.splendo.kaluga.bluetooth.server.LocalDescriptorWrapper
import com.splendo.kaluga.bluetooth.server.LocalServiceWrapper
import platform.CoreBluetooth.CBCentral
import platform.CoreBluetooth.CBMutableCharacteristic
import platform.CoreBluetooth.CBMutableService
import platform.Foundation.NSData

private const val NO_PLATFORM = "Mock wrappers have no platform attribute and do not support server actions"

actual class MockLocalServiceWrapper actual constructor(actual override val uuid: UUID) : LocalServiceWrapper {
    override val identity: AttributeIdentity = MockAttributeIdentity()
    override fun addIncludedService(service: LocalServiceWrapper) {}
    override fun addCharacteristic(characteristic: LocalCharacteristicWrapper) {}
    override fun addToParent(parent: CBMutableService): Unit = error(NO_PLATFORM)
    override fun addTo(serverWrapper: KalugaBluetoothServerWrapper): Unit = error(NO_PLATFORM)
    override fun removeFrom(serverWrapper: KalugaBluetoothServerWrapper): Unit = error(NO_PLATFORM)
}

actual class MockLocalCharacteristicWrapper actual constructor(
    actual override val uuid: UUID,
    actual override val properties: Set<CharacteristicProperty>,
    actual override val permissions: Set<Permission>,
) : LocalCharacteristicWrapper {
    override fun addDescriptor(descriptor: LocalDescriptorWrapper) {}
    override val identity: AttributeIdentity = MockAttributeIdentity()
    override fun addToService(service: CBMutableService): Unit = error(NO_PLATFORM)
    override fun updateValue(serverWrapper: KalugaBluetoothServerWrapper, value: NSData, centrals: List<CBCentral>): Boolean = error(NO_PLATFORM)
}

actual class MockLocalDescriptorWrapper actual constructor(actual override val uuid: UUID) : LocalDescriptorWrapper {
    override fun addToCharacteristic(characteristic: CBMutableCharacteristic): Unit = error(NO_PLATFORM)
}
