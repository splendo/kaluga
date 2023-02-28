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

import com.splendo.kaluga.base.utils.typedList
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBService
import platform.CoreBluetooth.CBUUID

/**
 * Accessor to a [CBService]
 */
actual interface ServiceWrapper {

    /**
     * The [UUID] of the service
     */
    actual val uuid: CBUUID

    /**
     * The list of [CharacteristicWrapper] associated with the service
     */
    actual val characteristics: List<CharacteristicWrapper>
}

/**
 * Default implementation of [ServiceWrapper]
 * @param service the [CBService] to wrap
 */
class DefaultServiceWrapper(service: CBService) : ServiceWrapper {

    override val uuid: CBUUID = service.UUID
    override val characteristics: List<CharacteristicWrapper> = service.characteristics?.typedList<CBCharacteristic>()?.map { DefaultCharacteristicWrapper(it) } ?: emptyList()
}
