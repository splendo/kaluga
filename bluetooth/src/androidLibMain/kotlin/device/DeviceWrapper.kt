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

package com.splendo.kaluga.bluetooth.device

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCallback
import android.content.Context
import android.os.Build

actual interface DeviceWrapper {
    actual val name: String?
    actual val identifier: Identifier
    val bondState: Int
    fun connectGatt(context: Context, autoConnect: Boolean, callback: BluetoothGattCallback): BluetoothGattWrapper
    fun removeBond()

    val device:BluetoothDevice
}

class DefaultDeviceWrapper(override val device: BluetoothDevice) : DeviceWrapper {

    override val name: String?
        get() = device.name
    override val identifier: Identifier
        get() = device.address
    override val bondState: Int
        get() = device.bondState

    override fun connectGatt(context: Context, autoConnect: Boolean, callback: BluetoothGattCallback): BluetoothGattWrapper {
        val gatt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            device.connectGatt(context, autoConnect, callback, BluetoothDevice.TRANSPORT_LE)
        } else {
            device.connectGatt(context, autoConnect, callback)
        }
        return DefaultBluetoothGattWrapper(gatt)
    }

    override fun removeBond() {
        try {
            device.javaClass.getMethod("removeBond").invoke(device)
        } catch (localException: Exception) {
        }
    }
}
