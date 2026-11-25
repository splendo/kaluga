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

package com.splendo.kaluga.bluetooth.test

import com.splendo.kaluga.bluetooth.PortableRemoteCharacteristicWrapper
import com.splendo.kaluga.bluetooth.PortableRemoteDescriptorWrapper
import com.splendo.kaluga.bluetooth.PortableRemoteServiceWrapper
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.RemoteCharacteristicWrapper
import com.splendo.kaluga.bluetooth.RemoteDescriptorWrapper
import com.splendo.kaluga.bluetooth.RemoteServiceWrapper
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID

/**
 * A [RemoteServiceWrapper] holding [MockCharacteristicWrapperImpl]s, for devices driven by a mock connection manager.
 */
class MockServiceWrapper(
    override val uuid: UUID = randomUUID(),
    initialCharacteristics: List<ServiceWrapperBuilder.Characteristic> = emptyList(),
    override val type: Service.Type = Service.Type.PRIMARY,
    override val includedServices: List<RemoteServiceWrapper> = emptyList(),
) : PortableRemoteServiceWrapper() {

    constructor(builder: ServiceWrapperBuilder) : this(builder.uuid, builder.characteristics)

    override val characteristics: List<RemoteCharacteristicWrapper> = initialCharacteristics.map {
        MockCharacteristicWrapperImpl(it.uuid, it.properties, this, it.descriptorUUIDs)
    }
}

/**
 * A [MockCharacteristicWrapper] whose [value] a mock connection manager reads and writes.
 */
class MockCharacteristicWrapperImpl(
    override val uuid: UUID = randomUUID(),
    override val properties: Set<CharacteristicProperty> = emptySet(),
    override val service: RemoteServiceWrapper = MockServiceWrapper(),
    descriptorUUIDs: List<UUID> = emptyList(),
) : PortableRemoteCharacteristicWrapper(),
    MockCharacteristicWrapper {

    override var value: ByteArray? = null

    override val descriptors: List<RemoteDescriptorWrapper> = descriptorUUIDs.map { MockDescriptorWrapperImpl(it, this) }
}

/**
 * A [MockDescriptorWrapper] whose [value] a mock connection manager reads and writes.
 */
class MockDescriptorWrapperImpl(override val uuid: UUID = randomUUID(), override val characteristic: RemoteCharacteristicWrapper = MockCharacteristicWrapperImpl()) :
    PortableRemoteDescriptorWrapper(),
    MockDescriptorWrapper {

    override var value: ByteArray? = null
}
