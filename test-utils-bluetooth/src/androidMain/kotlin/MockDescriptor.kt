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

import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import java.util.UUID

class AndroidMockDescriptorWrapper(override val uuid: UUID = UUID.randomUUID(), override val characteristic: CharacteristicWrapper) : MockDescriptorWrapper {
    override fun updateMockValue(value: ByteArray?) = updateValue(value)

    override var value: ByteArray? = null
    override val permissions: Int = 0

    override fun updateValue(value: ByteArray?) {
        this.value = value
    }
}
