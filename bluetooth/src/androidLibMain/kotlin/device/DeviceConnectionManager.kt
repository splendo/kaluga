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
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_INDICATE
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.containsAnyOf
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal actual class DeviceConnectionManager(
    private val context: Context,
    deviceWrapper: DeviceWrapper,
    bufferCapacity: Int = BUFFER_CAPACITY,
    val mainDispatcher: CoroutineContext = Dispatchers.Main.immediate, // can be replaced for testing,
    coroutineScope: CoroutineScope
) : BaseDeviceConnectionManager(deviceWrapper, bufferCapacity, coroutineScope) {

    private companion object {
        const val TAG = "Android Bluetooth DeviceConnectionManager"
        val CLIENT_CONFIGURATION: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    class Builder(private val context: Context = ApplicationHolder.applicationContext) : BaseDeviceConnectionManager.Builder {
        override fun create(
            deviceWrapper: DeviceWrapper,
            bufferCapacity: Int,
            coroutineScope: CoroutineScope
        ): BaseDeviceConnectionManager {
            return DeviceConnectionManager(context, deviceWrapper, bufferCapacity, coroutineScope = coroutineScope)
        }
    }

    override val coroutineContext: CoroutineContext = coroutineScope.coroutineContext

    private val device: BluetoothDevice = deviceWrapper.device
    private var gatt: CompletableDeferred<BluetoothGattWrapper> = CompletableDeferred()
    private val callback = object : BluetoothGattCallback() {

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            launch(mainDispatcher) {
                handleNewRssi(rssi)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            debug(TAG) { "onMtuChanged($gatt, $mtu, $status)" }
            if (status == GATT_SUCCESS) {
                handleNewMtu(mtu)
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            updateCharacteristic(characteristic, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            updateCharacteristic(characteristic, status)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            launch(mainDispatcher) {
                val services = gatt?.services?.map { Service(DefaultGattServiceWrapper(it), newAction) } ?: emptyList()
                handleDiscoverCompleted(services)
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            updateDescriptor(descriptor, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            characteristic ?: return
            updateCharacteristic(characteristic, status = GATT_SUCCESS)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            updateDescriptor(descriptor, status)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            lastKnownState = newState
            launch(mainDispatcher) {
                when (newState) {
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        handleDisconnect {
                            closeGatt()
                        }
                    }
                    BluetoothProfile.STATE_CONNECTED -> {
                        handleConnect()
                    }
                }
            }
        }
    }
    private var lastKnownState = BluetoothProfile.STATE_DISCONNECTED

    @SuppressLint("MissingPermission")
    override suspend fun connect() {
        if (lastKnownState != BluetoothProfile.STATE_CONNECTED || !gatt.isCompleted) {
            unpair()
            if (gatt.isCompleted) {
                if (!gatt.getCompleted().connect()) {
                    launch(mainDispatcher) {
                        handleDisconnect { closeGatt() }
                    }
                } else if (lastKnownState != BluetoothProfile.STATE_CONNECTED) {
                    launch(mainDispatcher) {
                        handleConnect()
                    }
                }
            } else {
                val gattService = device.connectGatt(context, false, callback)
                gatt.complete(DefaultBluetoothGattWrapper(gattService))
            }
        } else {
            launch(mainDispatcher) {
                handleConnect()
            }
        }
    }

    override suspend fun discoverServices() {
        gatt.await().discoverServices()
    }

    override suspend fun disconnect() {
        if (lastKnownState != BluetoothProfile.STATE_DISCONNECTED)
            gatt.await().disconnect()
        else
            launch(mainDispatcher) {
                handleDisconnect {
                    closeGatt()
                }
            }
    }

    private fun closeGatt() {
        if (gatt.isCompleted) {
            gatt.getCompleted().close()
        }
        gatt = CompletableDeferred()
    }

    override suspend fun readRssi() {
        gatt.await().readRemoteRssi()
    }

    override suspend fun requestMtu(mtu: Int): Boolean {
        return gatt.await().requestMtu(mtu)
    }

    override suspend fun performAction(action: DeviceAction) {
        currentAction = action
        val succeeded = when (action) {
            is DeviceAction.Read.Characteristic -> gatt.await().readCharacteristic(action.characteristic.wrapper)
            is DeviceAction.Read.Descriptor -> gatt.await().readDescriptor(action.descriptor.wrapper)
            is DeviceAction.Write.Characteristic -> writeCharacteristic(action.characteristic, action.newValue)
            is DeviceAction.Write.Descriptor -> writeDescriptor(action.descriptor, action.newValue)
            is DeviceAction.Notification.Enable -> setNotification(action.characteristic, true)
            is DeviceAction.Notification.Disable -> setNotification(action.characteristic, false)
        }

        // Action Failed
        if (!succeeded) {
            launch(mainDispatcher) {
                handleCurrentActionCompleted(succeeded = false)
            }
        }
    }

    private suspend fun writeCharacteristic(characteristic: Characteristic, value: ByteArray?): Boolean {
        characteristic.wrapper.updateValue(value)
        return gatt.await().writeCharacteristic(characteristic.wrapper)
    }

    private suspend fun writeDescriptor(descriptor: Descriptor, value: ByteArray?): Boolean {
        descriptor.wrapper.updateValue(value)
        return gatt.await().writeDescriptor(descriptor.wrapper)
    }

    private suspend fun setNotification(characteristic: Characteristic, enable: Boolean): Boolean {
        val uuid = characteristic.uuid.uuidString
        if (enable) notifyingCharacteristics[uuid] = characteristic
        else notifyingCharacteristics.remove(uuid)
        if (!gatt.await().setCharacteristicNotification(characteristic.wrapper, enable)) {
            return false
        }

        val writeValue = when {
            enable && characteristic.wrapper.containsAnyOf(PROPERTY_NOTIFY) ->
                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            enable && characteristic.wrapper.containsAnyOf(PROPERTY_INDICATE) ->
                BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            !enable && characteristic.wrapper.containsAnyOf(PROPERTY_INDICATE, PROPERTY_NOTIFY) ->
                BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
            else -> null
        }

        return if (writeValue != null) {
            characteristic.descriptors.firstOrNull { it.uuid == CLIENT_CONFIGURATION }?.let { descriptor ->
                descriptor.wrapper.updateValue(writeValue)
                gatt.await().writeDescriptor(descriptor.wrapper)
            } ?: false
        } else {
            warn(TAG) {
                "(${characteristic.uuid.uuidString}) Failed attempt to perform set notification action. " +
                    "neither NOTIFICATION nor INDICATION is supported. " +
                    "Supported properties: ${characteristic.wrapper.properties}"
            }
            false
        }
    }

    private fun updateCharacteristic(characteristic: BluetoothGattCharacteristic, status: Int) {
        launch(mainDispatcher) {
            handleUpdatedCharacteristic(characteristic.uuid, succeeded = status == GATT_SUCCESS) {
                it.wrapper.updateValue(characteristic.value)
            }
        }
    }

    private fun updateDescriptor(descriptor: BluetoothGattDescriptor, status: Int) {
        launch(mainDispatcher) {
            val succeeded = status == GATT_SUCCESS
            // Notification enable/disable done by client configuration descriptor write
            if (descriptor.uuid == CLIENT_CONFIGURATION && currentAction is DeviceAction.Notification) {
                handleCurrentActionCompleted(succeeded)
            }
            handleUpdatedDescriptor(descriptor.uuid, succeeded) {
                it.wrapper.updateValue(descriptor.value)
            }
        }
    }

    private fun unpair() {
        // unpair to prevent connection problems
        if (device.bondState != BluetoothDevice.BOND_NONE) {
            deviceWrapper.removeBond()
        }
    }
}
