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

import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.randomUUID
import com.splendo.kaluga.bluetooth.uuidString

private val bondState = DeviceWrapper.BondState.NONE

actual fun randomIdentifier(): Identifier = randomUUID().uuidString

actual fun createDeviceWrapper(
    deviceName: String?,
    identifier: Identifier
): DeviceWrapper = MockDeviceWrapper(deviceName, identifier, bondState)

actual fun ServiceWrapperBuilder.build(): ServiceWrapper = MockServiceWrapper(builder = this)
