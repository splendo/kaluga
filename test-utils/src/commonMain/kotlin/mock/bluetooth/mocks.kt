/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.bluetooth

import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.randomUUID
import com.splendo.kaluga.test.mock.bluetooth.device.randomIdentifier

expect fun createDeviceWrapper(deviceName: String? = null, identifier: Identifier = randomIdentifier()): DeviceWrapper

expect fun createServiceWrapper(
    stateRepo: DeviceStateFlowRepo,
    uuid: UUID = randomUUID(),
    characteristics: List<Pair<UUID, List<UUID>>> = listOf(randomUUID() to listOf(randomUUID()))
): ServiceWrapper

interface CanUpdateMockValue {
    fun updateMockValue(value: ByteArray?)
}
