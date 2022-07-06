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

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager

class Service(
    service: ServiceWrapper,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    parentLogTag: String,
    logLevel: ConnectionSettings.LogLevel
) {
    val uuid = service.uuid
    val characteristics = service.characteristics.map {
        Characteristic(
            it,
            emitNewAction = emitNewAction,
            parentLogTag = "$parentLogTag Service ${uuid.uuidString}",
            logLevel = logLevel
        )
    }
}

expect interface ServiceWrapper {
    val characteristics: List<CharacteristicWrapper>
    val uuid: UUID
}
