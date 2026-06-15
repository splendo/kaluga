/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.RemoteCharacteristicWrapper
import com.splendo.kaluga.bluetooth.RemoteDescriptorWrapper
import com.splendo.kaluga.bluetooth.RemoteServiceWrapper
import java.util.UUID

class AndroidMockCharacteristicWrapper(
    override val uuid: UUID = UUID.randomUUID(),
    descriptorUUIDs: List<UUID> = emptyList(),
    override val service: RemoteServiceWrapper,
    override val properties: Set<CharacteristicProperty> = emptySet(),
) : MockCharacteristicWrapper {

    override var value: ByteArray? = null

    override val descriptors: List<RemoteDescriptorWrapper> = descriptorUUIDs.map {
        AndroidMockDescriptorWrapper(
            it,
            this,
        )
    }
    override val permissions: Int
        get() = 0
    override var writeType = RemoteCharacteristicWrapper.WriteType.DEFAULT

    override fun getDescriptor(uuid: UUID): RemoteDescriptorWrapper? = descriptors.firstOrNull { it.uuid == uuid }
}
