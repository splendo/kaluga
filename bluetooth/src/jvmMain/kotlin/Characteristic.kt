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
import com.splendo.kaluga.state.StateRepoAccesor
import java.util.concurrent.atomic.AtomicReference

actual open class Characteristic(override val uuid: UUID, override val descriptors: List<Descriptor>, initialValue: ByteArray?, stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseCharacteristic(initialValue, stateRepoAccesor) {

    var value = AtomicReference(initialValue)

    override fun createNotificationAction(enabled: Boolean): DeviceAction.Notification {
        return DeviceAction.Notification(this, enabled)
    }

    override fun createReadAction(): DeviceAction.Read.Characteristic {
        return DeviceAction.Read.Characteristic(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Characteristic {
        value.set(newValue)
        return DeviceAction.Write.Characteristic(newValue, this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return value.get()
    }
}