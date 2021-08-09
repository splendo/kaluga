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
import com.splendo.kaluga.bluetooth.device.BluetoothGattWrapper
import kotlinx.coroutines.CompletableDeferred

class MockBluetoothGattWrapper : BluetoothGattWrapper {

    val connectCompleted = EmptyCompletableDeferred()
    val discoverServicesCompleted = EmptyCompletableDeferred()
    val disconnectCompleted = EmptyCompletableDeferred()
    val closeCompleted = EmptyCompletableDeferred()
    val readRemoteRssiCompleted = EmptyCompletableDeferred()
    val readCharacteristicCompleted = CompletableDeferred<CharacteristicWrapper>()
    val readDescriptorCompleted = CompletableDeferred<DescriptorWrapper>()
    val writeCharacteristicCompleted = CompletableDeferred<CharacteristicWrapper>()
    val writeDescriptorCompleted = CompletableDeferred<DescriptorWrapper>()
    val setCharacteristicNotificationCompleted = CompletableDeferred<Pair<CharacteristicWrapper, Boolean>>()

    override fun connect(): Boolean {
        connectCompleted.complete()
        return true
    }

    override fun discoverServices(): Boolean {
        discoverServicesCompleted.complete()
        return true
    }

    override fun disconnect() {
        disconnectCompleted.complete()
    }

    override fun close() {
        closeCompleted.complete()
    }

    override fun readRemoteRssi(): Boolean {
        readRemoteRssiCompleted.complete()
        return true
    }

    override fun readCharacteristic(wrapper: CharacteristicWrapper): Boolean {
        readCharacteristicCompleted.complete(wrapper)
        return true
    }

    override fun readDescriptor(wrapper: DescriptorWrapper): Boolean {
        readDescriptorCompleted.complete(wrapper)
        return true
    }

    override fun writeCharacteristic(wrapper: CharacteristicWrapper): Boolean {
        writeCharacteristicCompleted.complete(wrapper)
        return true
    }

    override fun writeDescriptor(wrapper: DescriptorWrapper): Boolean {
        writeDescriptorCompleted.complete(wrapper)
        return true
    }

    override fun setCharacteristicNotification(wrapper: CharacteristicWrapper, enable: Boolean): Boolean {
        setCharacteristicNotificationCompleted.complete(Pair(wrapper, enable))
        return true
    }
}
