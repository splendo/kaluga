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

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothStatusCodes
import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.DescriptorWrapper
import com.splendo.kaluga.bluetooth.MTU

/**
 * A wrapper to access a [BluetoothGatt]
 */
interface BluetoothGattWrapper {

    /**
     * Connect to the Bluetooth device
     * @return `true` if the connection attempt was initiated successfully
     */
    fun connect(): Boolean

    /**
     * Discovers services offered by a remote device as well as their characteristics and descriptors.
     * @return `true` if the discovery attempt was initiated successfully
     */
    fun discoverServices(): Boolean

    /**
     * Disconnects the current connection or cancels the current attempt if it is in progress
     */
    fun disconnect()

    /**
     * Close connection to the Bluetooth Gatt server
     */
    fun close()

    /**
     * Reads the RSSI value
     * @return `true` if the RSSI value has been requested successfully
     */
    fun readRemoteRssi(): Boolean

    /**
     * Request a [MTU] size
     * @param mtu the [MTU] size
     * @return `true` if the new MTU value has been requested successfully
     */
    fun requestMtu(mtu: MTU): Boolean

    /**
     * Reads the value of the [CharacteristicWrapper] from the device
     * @param wrapper the [CharacteristicWrapper] to read from
     * @return `true` if the read operation was initiated successfully
     */
    fun readCharacteristic(wrapper: CharacteristicWrapper): Boolean

    /**
     * Reads the value of the [DeviceWrapper] from the device
     * @param wrapper the [DeviceWrapper] to read from
     * @return `true` if the read operation was initiated successfully
     */
    fun readDescriptor(wrapper: DescriptorWrapper): Boolean

    /**
     * Writes a value to the [CharacteristicWrapper] from the device
     * @param wrapper the [CharacteristicWrapper] to write to
     * @param value the [ByteArray] to write
     * @return `true` if the write operation was initiated successfully
     */
    fun writeCharacteristic(wrapper: CharacteristicWrapper, value: ByteArray): Boolean

    /**
     * Writes a value to the [DescriptorWrapper] from the device
     * @param wrapper the [DescriptorWrapper] to write to
     * @param value the [ByteArray] to write
     * @return `true` if the write operation was initiated successfully
     */
    fun writeDescriptor(wrapper: DescriptorWrapper, value: ByteArray): Boolean

    /**
     * Enable or disable notifications for a given [CharacteristicWrapper]
     * @param wrapper the [CharacteristicWrapper] to enable/disable notifications for
     * @param enable if `true` notifications should be enabled
     * @return `true` if the requested notification status was set successfully
     */
    fun setCharacteristicNotification(wrapper: CharacteristicWrapper, enable: Boolean): Boolean
}

/**
 * Default implementation of [BluetoothGattWrapper]
 * @param gatt the [BluetoothGatt] being wrapped
 */
@SuppressLint("MissingPermission")
class DefaultBluetoothGattWrapper(private val gatt: BluetoothGatt) : BluetoothGattWrapper {

    override fun connect(): Boolean = gatt.connect()

    override fun discoverServices(): Boolean = gatt.discoverServices()

    override fun disconnect() {
        gatt.disconnect()
    }

    override fun close() {
        gatt.close()
    }

    override fun readRemoteRssi(): Boolean = gatt.readRemoteRssi()

    override fun requestMtu(mtu: MTU): Boolean = gatt.requestMtu(mtu)

    override fun readCharacteristic(wrapper: CharacteristicWrapper): Boolean {
        val characteristic = getCharacteristic(wrapper) ?: return false
        return gatt.readCharacteristic(characteristic)
    }

    override fun readDescriptor(wrapper: DescriptorWrapper): Boolean {
        val descriptor = getDescriptor(wrapper) ?: return false
        return gatt.readDescriptor(descriptor)
    }

    override fun writeCharacteristic(wrapper: CharacteristicWrapper, value: ByteArray): Boolean {
        val characteristic = getCharacteristic(wrapper) ?: return false
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            gatt.writeCharacteristic(characteristic, value, characteristic.writeType) == BluetoothStatusCodes.SUCCESS
        } else {
            @Suppress("DEPRECATION")
            characteristic.value = value
            @Suppress("DEPRECATION")
            gatt.writeCharacteristic(characteristic)
        }
    }

    override fun writeDescriptor(wrapper: DescriptorWrapper, value: ByteArray): Boolean {
        val descriptor = getDescriptor(wrapper) ?: return false
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            gatt.writeDescriptor(descriptor, value) == BluetoothStatusCodes.SUCCESS
        } else {
            @Suppress("DEPRECATION")
            descriptor.value = value
            @Suppress("DEPRECATION")
            gatt.writeDescriptor(descriptor)
        }
    }

    override fun setCharacteristicNotification(wrapper: CharacteristicWrapper, enable: Boolean): Boolean {
        val characteristic = getCharacteristic(wrapper) ?: return false
        return gatt.setCharacteristicNotification(characteristic, enable)
    }

    private fun getCharacteristic(wrapper: CharacteristicWrapper): BluetoothGattCharacteristic? = gatt.getService(wrapper.service.uuid)?.getCharacteristic(wrapper.uuid)

    private fun getDescriptor(wrapper: DescriptorWrapper): BluetoothGattDescriptor? = getCharacteristic(wrapper.characteristic)?.getDescriptor(wrapper.uuid)
}
