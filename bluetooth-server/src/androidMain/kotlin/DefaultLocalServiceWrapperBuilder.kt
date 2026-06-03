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
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic.Permission

/**
 * The [LocalServiceWrapperBuilder] backed by the Android Bluetooth framework.
 */
class DefaultLocalServiceWrapperBuilder : LocalServiceWrapperBuilder {
    override fun createService(uuid: UUID, type: Service.Type): LocalServiceWrapper = DefaultLocalServiceWrapper(uuid, type)
    override fun createCharacteristic(
        uuid: UUID,
        properties: Set<CharacteristicProperty>,
        encryptedNotification: Boolean,
        permissions: Set<Permission>,
    ): LocalCharacteristicWrapper = DefaultLocalCharacteristicWrapper(uuid, properties, encryptedNotification, permissions)
    override fun createDescriptor(uuid: UUID, permissions: Set<LocalDescriptor.Permissions>): LocalDescriptorWrapper = DefaultLocalDescriptorWrapper(uuid, permissions)
}
