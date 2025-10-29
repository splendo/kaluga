/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID

sealed interface LocalServiceDSL {

    interface Primary : LocalServiceDSL {
        fun includedService(uuid: UUID, service: Secondary.() -> Unit)
    }

    interface Secondary : LocalServiceDSL
    fun characteristic(uuid: UUID, characteristic: LocalCharacteristicDSL.() -> Unit)
}

expect class LocalService : Service {

    override val uuid: UUID
    override val type: Service.Type

    override val characteristics: List<LocalCharacteristic>
    override val includedServices: List<LocalService>
}
