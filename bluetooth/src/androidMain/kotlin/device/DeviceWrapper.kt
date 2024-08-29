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
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCallback
import android.content.Context

/**
 * Accessor to the [BluetoothDevice]
 */
actual interface DeviceWrapper {
    actual val name: String?
    actual val identifier: Identifier

    /**
     * Bond state of the Device
     */
    enum class BondState {
        /**
         * Indicates the device is not bonded
         */
        NONE,

        /**
         * Indicates the device is in the process of bonding
         */
        BONDING,

        /**
         * Indicates the device s bonded
         */
        BONDED,
    }

    /**
     * The [BondState] of the device
     */
    val bondState: BondState

    /**
     * Connects to the GATT Server hosted by the [BluetoothDevice]
     * @param context the [Context] used for connecting to the GATT server
     * @param autoConnect if `true` the device will connect as soon as it becomes available, otherwise connects directly
     * @param callback the [BluetoothGattCallback] used to receive for asynchronous result
     */
    fun connectGatt(context: Context, autoConnect: Boolean, callback: BluetoothGattCallback): BluetoothGattWrapper

    /**
     * Removes the bond from the device (unpair)
     */
    fun removeBond()

    /**
     * Creates a bond with the device (pair)
     */
    fun createBond()
}

/**
 * Default implementation of [DeviceWrapper]
 * @param device the [BluetoothDevice] being wrapped
 */
@SuppressLint("MissingPermission")
class DefaultDeviceWrapper(private val device: BluetoothDevice) : DeviceWrapper {

    override val name: String?
        get() = device.name
    override val identifier: Identifier
        get() = device.address
    override val bondState: DeviceWrapper.BondState
        get() = when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> DeviceWrapper.BondState.BONDED
            BluetoothDevice.BOND_BONDING -> DeviceWrapper.BondState.BONDING
            else -> DeviceWrapper.BondState.NONE
        }

    override fun connectGatt(context: Context, autoConnect: Boolean, callback: BluetoothGattCallback): BluetoothGattWrapper {
        val gatt = device.connectGatt(context, autoConnect, callback, BluetoothDevice.TRANSPORT_LE)
        return DefaultBluetoothGattWrapper(gatt)
    }

    override fun removeBond() {
        try {
            device.javaClass.getMethod("removeBond").invoke(device)
        } catch (localException: Exception) {
        }
    }

    override fun createBond() {
        try {
            device.createBond()
        } catch (localException: Exception) {
        }
    }
}
