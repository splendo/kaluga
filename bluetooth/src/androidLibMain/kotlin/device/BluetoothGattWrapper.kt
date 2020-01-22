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

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

interface BluetoothGattWrapper {
    fun connect(): Boolean
    fun discoverServices(): Boolean
    fun disconnect()
    fun readRemoteRssi(): Boolean

    fun readCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean
    fun readDescriptor(descriptor: BluetoothGattDescriptor): Boolean
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean
    fun writeDescriptor(descriptor: BluetoothGattDescriptor): Boolean
    fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, enable: Boolean): Boolean
}

class DefaultBluetoothGattWrapper(private val gatt: BluetoothGatt) : BluetoothGattWrapper {

    override fun connect(): Boolean {
        return gatt.connect()
    }

    override fun discoverServices(): Boolean {
        return gatt.discoverServices()
    }

    override fun disconnect() {
        gatt.disconnect()
    }

    override fun readRemoteRssi(): Boolean {
        return gatt.readRemoteRssi()
    }

    override fun readCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        return gatt.readCharacteristic(characteristic)
    }

    override fun readDescriptor(descriptor: BluetoothGattDescriptor): Boolean {
        return gatt.readDescriptor(descriptor)
    }

    override fun writeCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        return gatt.writeCharacteristic(characteristic)
    }

    override fun writeDescriptor(descriptor: BluetoothGattDescriptor): Boolean {
        return gatt.writeDescriptor(descriptor)
    }

    override fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, enable: Boolean): Boolean {
        return gatt.setCharacteristicNotification(characteristic, enable)
    }
}