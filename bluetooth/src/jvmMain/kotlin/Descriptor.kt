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

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepo
import java.util.concurrent.atomic.AtomicReference

actual class Descriptor(override val uuid: UUID, initialValue: ByteArray?, stateRepo: DeviceStateRepo) : BaseDescriptor(initialValue, stateRepo) {

    private val value = AtomicReference(initialValue)

    override fun createReadAction(): DeviceAction.Read.Descriptor {
        return DeviceAction.Read.Descriptor(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Descriptor {
        value.set(newValue)
        return DeviceAction.Write.Descriptor(newValue, this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return value.get()
    }
}
