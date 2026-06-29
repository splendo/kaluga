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

package com.splendo.kaluga.bluetooth.test.server

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.AttributeIdentity
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic
import com.splendo.kaluga.bluetooth.server.LocalCharacteristicWrapper
import com.splendo.kaluga.bluetooth.server.LocalDescriptor
import com.splendo.kaluga.bluetooth.server.LocalDescriptorWrapper
import com.splendo.kaluga.bluetooth.server.LocalServiceWrapper
import com.splendo.kaluga.bluetooth.server.LocalServiceWrapperBuilder

/**
 * An [AttributeIdentity] for mock wrappers; each instance is distinct, mirroring the per-attribute
 * identity of the platform implementations.
 */
class MockAttributeIdentity : AttributeIdentity

/**
 * A [LocalServiceWrapper] for use in tests. Holds only the modeled attributes; the platform-level
 * actions (adding to a server, notifying) are not supported and throw if invoked.
 * @property uuid the [UUID] of the service
 */
expect class MockLocalServiceWrapper(uuid: UUID) : LocalServiceWrapper {
    override val uuid: UUID
    override fun addIncludedService(service: LocalServiceWrapper)
    override fun addCharacteristic(characteristic: LocalCharacteristicWrapper)
}

/**
 * A [LocalCharacteristicWrapper] for use in tests.
 * @property uuid the [UUID] of the characteristic
 * @property properties the [CharacteristicProperty] of the characteristic
 * @property permissions the [LocalCharacteristic.Permission] of the characteristic
 */
expect class MockLocalCharacteristicWrapper(uuid: UUID, properties: Set<CharacteristicProperty>, permissions: Set<LocalCharacteristic.Permission>) : LocalCharacteristicWrapper {
    override val uuid: UUID
    override val properties: Set<CharacteristicProperty>
    override val permissions: Set<LocalCharacteristic.Permission>
    override fun addDescriptor(descriptor: LocalDescriptorWrapper)
}

/**
 * A [LocalDescriptorWrapper] for use in tests.
 * @property uuid the [UUID] of the descriptor
 */
expect class MockLocalDescriptorWrapper(uuid: UUID) : LocalDescriptorWrapper {
    override val uuid: UUID
}

/**
 * A [LocalServiceWrapperBuilder] that produces mock wrappers, allowing attribute graphs to be built
 * in tests on any platform without a live Bluetooth stack.
 */
class MockLocalServiceWrapperBuilder : LocalServiceWrapperBuilder {
    override fun createService(uuid: UUID, type: Service.Type): LocalServiceWrapper = MockLocalServiceWrapper(uuid)
    override fun createCharacteristic(
        uuid: UUID,
        properties: Set<CharacteristicProperty>,
        encryptedNotification: Boolean,
        permissions: Set<LocalCharacteristic.Permission>,
    ): LocalCharacteristicWrapper = MockLocalCharacteristicWrapper(uuid, properties, permissions)
    override fun createDescriptor(uuid: UUID, permissions: Set<LocalDescriptor.Permissions>): LocalDescriptorWrapper = MockLocalDescriptorWrapper(uuid)
}
