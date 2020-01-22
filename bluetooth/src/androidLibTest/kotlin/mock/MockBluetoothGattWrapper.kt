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

package com.splendo.kaluga.bluetooth.mock

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.splendo.kaluga.bluetooth.device.BluetoothGattWrapper
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred

class MockBluetoothGattWrapper : BluetoothGattWrapper {

    val connectCompleted = EmptyCompletableDeferred()
    val discoverServicesCompleted = EmptyCompletableDeferred()
    val disconnectCompleted = EmptyCompletableDeferred()
    val readRemoteRssiCompleted = EmptyCompletableDeferred()
    val readCharacteristicCompleted = CompletableDeferred<BluetoothGattCharacteristic>()
    val readDescriptorCompleted = CompletableDeferred<BluetoothGattDescriptor>()
    val writeCharacteristicCompleted = CompletableDeferred<BluetoothGattCharacteristic>()
    val writeDescriptorCompleted = CompletableDeferred<BluetoothGattDescriptor>()
    val setCharacteristicNotificationCompleted = CompletableDeferred<Pair<BluetoothGattCharacteristic, Boolean>>()

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

    override fun readRemoteRssi(): Boolean {
        readRemoteRssiCompleted.complete()
        return true
    }

    override fun readCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        readCharacteristicCompleted.complete(characteristic)
        return true
    }

    override fun readDescriptor(descriptor: BluetoothGattDescriptor): Boolean {
        readDescriptorCompleted.complete(descriptor)
        return true
    }

    override fun writeCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        writeCharacteristicCompleted.complete(characteristic)
        return true
    }

    override fun writeDescriptor(descriptor: BluetoothGattDescriptor): Boolean {
        writeDescriptorCompleted.complete(descriptor)
        return true
    }

    override fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, enable: Boolean): Boolean {
        setCharacteristicNotificationCompleted.complete(Pair(characteristic, enable))
        return true
    }
}

