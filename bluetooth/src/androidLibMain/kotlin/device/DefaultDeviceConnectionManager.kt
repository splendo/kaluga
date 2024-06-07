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
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_INDICATE
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.containsAnyOf
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.e
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal actual class DefaultDeviceConnectionManager(
    private val context: Context,
    deviceWrapper: DeviceWrapper,
    connectionSettings: ConnectionSettings = ConnectionSettings(),
    coroutineScope: CoroutineScope,
) : BaseDeviceConnectionManager(deviceWrapper, connectionSettings, coroutineScope) {

    private companion object {
        val CLIENT_CONFIGURATION: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    class Builder(private val context: Context = ApplicationHolder.applicationContext) : DeviceConnectionManager.Builder {
        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): BaseDeviceConnectionManager {
            return DefaultDeviceConnectionManager(context, deviceWrapper, settings, coroutineScope = coroutineScope)
        }
    }

    override val coroutineContext: CoroutineContext = coroutineScope.coroutineContext

    private var gatt: CompletableDeferred<BluetoothGattWrapper> = CompletableDeferred()
    private val callback = object : BluetoothGattCallback() {

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            handleNewRssi(rssi)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            if (status == GATT_SUCCESS) {
                handleNewMtu(mtu)
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            @Suppress("DEPRECATION")
            updateCharacteristic(characteristic, characteristic.value, status)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) =
            updateCharacteristic(characteristic, value, status)

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            handleUpdatedCharacteristic(characteristic.uuid, succeeded = status == GATT_SUCCESS)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            launch {
                val services = gatt?.services?.map { DefaultGattServiceWrapper(it) } ?: emptyList()
                handleDiscoverCompleted(services)
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            characteristic ?: return
            @Suppress("DEPRECATION")
            updateCharacteristic(characteristic, characteristic.value, status = GATT_SUCCESS)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) =
            updateCharacteristic(characteristic, value, status = GATT_SUCCESS)

        @Deprecated("Deprecated in Java")
        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            @Suppress("DEPRECATION")
            updateDescriptor(descriptor, descriptor.value, status)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int, value: ByteArray) = updateDescriptor(descriptor, value, status)

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            val succeeded = status == GATT_SUCCESS
            // Notification enable/disable done by client configuration descriptor write
            if (descriptor.uuid == CLIENT_CONFIGURATION && currentAction is DeviceAction.Notification) {
                handleCurrentActionCompleted(succeeded)
            }
            handleUpdatedDescriptor(descriptor.uuid, succeeded)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            lastKnownState = newState
            launch {
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

    actual override fun getCurrentState(): DeviceConnectionManager.State = when (lastKnownState) {
        BluetoothProfile.STATE_CONNECTED -> DeviceConnectionManager.State.CONNECTED
        BluetoothProfile.STATE_CONNECTING -> DeviceConnectionManager.State.CONNECTING
        BluetoothProfile.STATE_DISCONNECTED -> DeviceConnectionManager.State.DISCONNECTED
        BluetoothProfile.STATE_DISCONNECTING -> DeviceConnectionManager.State.DISCONNECTING
        else -> DeviceConnectionManager.State.DISCONNECTED
    }

    @SuppressLint("MissingPermission")
    actual override fun connect() {
        when {
            !gatt.isCompleted -> gatt.complete(deviceWrapper.connectGatt(context, false, callback))
            lastKnownState == BluetoothProfile.STATE_CONNECTED -> handleConnect()
            !gatt.getCompleted().connect() -> handleDisconnect { closeGatt() }
            else -> {}
        }
    }

    actual override suspend fun discoverServices() {
        gatt.await().discoverServices()
    }

    actual override fun disconnect() {
        val gatt = gatt.getCompletedOrNull()
        if (gatt != null && lastKnownState != BluetoothProfile.STATE_DISCONNECTED) {
            gatt.disconnect()
        } else {
            handleDisconnect {
                closeGatt()
            }
        }
    }

    private fun closeGatt() {
        gatt.getCompletedOrNull()?.close()
        gatt = CompletableDeferred()
    }

    override suspend fun readRssi() {
        gatt.await().readRemoteRssi()
    }

    actual override suspend fun requestMtu(mtu: MTU): Boolean {
        return gatt.await().requestMtu(mtu)
    }

    actual override suspend fun didStartPerformingAction(action: DeviceAction) {
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
            handleCurrentActionCompleted(succeeded = false)
        }
    }

    @SuppressLint("MissingPermission")
    actual override suspend fun requestStartPairing() {
        if (deviceWrapper.bondState == DeviceWrapper.BondState.NONE) {
            deviceWrapper.createBond()
        }
    }

    @SuppressLint("MissingPermission")
    actual override suspend fun requestStartUnpairing() {
        if (deviceWrapper.bondState != DeviceWrapper.BondState.NONE) {
            deviceWrapper.removeBond()
        }
    }

    private suspend fun writeCharacteristic(characteristic: Characteristic, value: ByteArray): Boolean {
        return gatt.await().writeCharacteristic(characteristic.wrapper, value)
    }

    private suspend fun writeDescriptor(descriptor: Descriptor, value: ByteArray): Boolean {
        descriptor.wrapper.updateValue(value)
        return gatt.await().writeDescriptor(descriptor.wrapper, value)
    }

    private suspend fun setNotification(characteristic: Characteristic, enable: Boolean): Boolean {
        val uuid = characteristic.uuid.uuidString
        if (enable) {
            notifyingCharacteristics[uuid] = characteristic
        } else {
            notifyingCharacteristics.remove(uuid)
        }
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
                gatt.await().writeDescriptor(descriptor.wrapper, writeValue)
            } ?: false
        } else {
            e {
                "(${characteristic.uuid.uuidString}) Failed attempt to perform set notification action. " +
                    "neither NOTIFICATION nor INDICATION is supported. " +
                    "Supported properties: ${characteristic.wrapper.properties}"
            }
            false
        }
    }

    private fun updateCharacteristic(characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) {
        handleUpdatedCharacteristic(characteristic.uuid, succeeded = status == GATT_SUCCESS) {
            it.wrapper.updateValue(value)
        }
    }

    private fun updateDescriptor(descriptor: BluetoothGattDescriptor, value: ByteArray, status: Int) {
        val succeeded = status == GATT_SUCCESS
        handleUpdatedDescriptor(descriptor.uuid, succeeded) {
            it.wrapper.updateValue(value)
        }
    }
}
