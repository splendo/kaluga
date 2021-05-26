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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.DescriptorWrapper
import kotlinx.coroutines.CompletableDeferred
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData

class MockCharacteristicWrapper(override val uuid: CBUUID = CBUUID(), descriptorUuids: List<CBUUID> = emptyList()) :
    CharacteristicWrapper {

    val isReadCompleted = EmptyCompletableDeferred()
    val isWriteCompleted = CompletableDeferred<NSData>()
    val isNotificationCompleted = CompletableDeferred<Boolean>()

    override var value: NSData? = null

    override val descriptors: List<DescriptorWrapper> = descriptorUuids.map {
        IOSMockDescriptorWrapper(
            it
        )
    }

    override fun readValue(peripheral: CBPeripheral) {
        isReadCompleted.complete()
    }

    override fun setNotificationValue(enabled: Boolean, peripheral: CBPeripheral) {
        isNotificationCompleted.complete(enabled)
    }

    override fun writeValue(value: NSData, peripheral: CBPeripheral) {
        this.value = value
        isWriteCompleted.complete(value)
    }
}