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
import android.bluetooth.BluetoothGatt.GATT_CONNECTION_CONGESTED
import android.bluetooth.BluetoothGatt.GATT_CONNECTION_TIMEOUT
import android.bluetooth.BluetoothGatt.GATT_FAILURE
import android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION
import android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_AUTHORIZATION
import android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION
import android.bluetooth.BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH
import android.bluetooth.BluetoothGatt.GATT_INVALID_OFFSET
import android.bluetooth.BluetoothGatt.GATT_READ_NOT_PERMITTED
import android.bluetooth.BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGatt.GATT_WRITE_NOT_PERMITTED
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_INDICATE
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.containsAnyOf
import com.splendo.kaluga.bluetooth.extensions.printableString
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.SensitiveAwareLogger
import com.splendo.kaluga.logging.e
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): BaseDeviceConnectionManager =
            DefaultDeviceConnectionManager(context, deviceWrapper, settings, coroutineScope = coroutineScope)
    }

    override val coroutineContext: CoroutineContext = coroutineScope.coroutineContext

    private var gatt: CompletableDeferred<BluetoothGattWrapper> = CompletableDeferred()

    private inner class Callback(logger: SensitiveAwareLogger) : BluetoothGattCallback() {
        val logger = logger.withTag("BluetoothGattCallback ${deviceWrapper.identifier.stringValue}")

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            logger.debug { "onReadRemoteRssi rssi $rssi status ${status.gattStatusAsString}" }
            handleNewRssi(rssi)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            logger.debug { "onMtuChanged mtu $mtu status ${status.gattStatusAsString}" }
            if (status == GATT_SUCCESS) {
                handleNewMtu(mtu)
            }
        }

        @Suppress("OVERRIDE_DEPRECATION")
        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            @Suppress("DEPRECATION")
            val value = characteristic.value
            logger.debug { "onCharacteristicRead[DEP] characteristic ${characteristic.uuid} value ${value.printableString.sensitive} status ${status.gattStatusAsString}" }
            updateCharacteristic(characteristic, value, status)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) {
            logger.debug { "onCharacteristicRead characteristic ${characteristic.uuid} value ${value.printableString.sensitive} status ${status.gattStatusAsString}" }
            updateCharacteristic(characteristic, value, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            logger.debug { "onCharacteristicWrite characteristic ${characteristic.uuid} status ${status.gattStatusAsString}" }
            handleUpdatedCharacteristic(characteristic.uuid, succeeded = status == GATT_SUCCESS)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            logger.debug { "onServicesDiscovered status ${status.gattStatusAsString}" }
            launch {
                val services = gatt?.services?.map { DefaultGattServiceWrapper(it) } ?: emptyList()
                handleDiscoverCompleted(services)
            }
        }

        @Suppress("OVERRIDE_DEPRECATION")
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            characteristic ?: return
            @Suppress("DEPRECATION")
            val value = characteristic.value
            logger.debug { "onCharacteristicChanged[DEP] characteristic ${characteristic.uuid} value ${value.printableString.sensitive}" }
            updateCharacteristic(characteristic, value, status = GATT_SUCCESS)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            logger.debug { "onCharacteristicChanged[DEP] characteristic ${characteristic.uuid} value ${value.printableString.sensitive}" }
            updateCharacteristic(characteristic, value, status = GATT_SUCCESS)
        }

        @Suppress("OVERRIDE_DEPRECATION")
        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            @Suppress("DEPRECATION")
            val value = descriptor.value
            logger.debug { "onDescriptorRead[DEP] descriptor ${descriptor.uuid} value ${value.printableString.sensitive} status ${status.gattStatusAsString}" }
            updateDescriptor(descriptor, value, status)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int, value: ByteArray) {
            logger.debug { "onDescriptorRead descriptor ${descriptor.uuid} value ${value.printableString.sensitive} status ${status.gattStatusAsString}" }
            updateDescriptor(descriptor, value, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            logger.debug { "onDescriptorWrite descriptor ${descriptor.uuid} status ${status.gattStatusAsString}" }
            val succeeded = status == GATT_SUCCESS
            // Notification enable/disable done by client configuration descriptor write
            if (descriptor.uuid == CLIENT_CONFIGURATION && currentAction is DeviceAction.Notification) {
                handleCurrentActionCompleted(succeeded)
            }
            handleUpdatedDescriptor(descriptor.uuid, succeeded)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            logger.debug { "onConnectionStateChange status ${status.gattStatusAsString} newState ${newState.connectionStateAsString}" }
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
            !gatt.isCompleted -> gatt.complete(deviceWrapper.connectGatt(context, false, Callback(logger)))
            lastKnownState == BluetoothProfile.STATE_CONNECTED -> handleConnect()
            !gatt.getCompleted().connect() -> handleDisconnect { closeGatt() }
            else -> {}
        }
    }

    actual override suspend fun discoverServices() {
        fun useSamsung12Workaround(): Boolean {
            // Note: an issue is discovered on a samsung device running os 12. when `discoverServices` called on non main thread, the command returns `true`
            // but nothing is sent to the bluetooth device. not reproducible on samsung running os 14.
            // Note2: including os 13 as it's not clear whether issue exists there as well
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        }

        if (useSamsung12Workaround()) {
            withContext(Dispatchers.Main) {
                gatt.await().discoverServices()
            }
        } else {
            gatt.await().discoverServices()
        }
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

    actual override suspend fun requestMtu(mtu: MTU): Boolean = gatt.await().requestMtu(mtu)

    actual override suspend fun didStartPerformingAction(action: DeviceAction) {
        currentAction = action
        val readyGatt = gatt.await()
        val succeeded = when (action) {
            is DeviceAction.Read.Characteristic -> readyGatt.readCharacteristic(action.characteristic.wrapper)
            is DeviceAction.Read.Descriptor -> readyGatt.readDescriptor(action.descriptor.wrapper)
            is DeviceAction.Write.Characteristic -> readyGatt.writeCharacteristic(action.characteristic, action.newValue)
            is DeviceAction.Write.Descriptor -> readyGatt.writeDescriptor(action.descriptor, action.newValue)
            is DeviceAction.Notification.Enable -> readyGatt.setNotification(action.characteristic, true)
            is DeviceAction.Notification.Disable -> readyGatt.setNotification(action.characteristic, false)
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

    private fun BluetoothGattWrapper.writeCharacteristic(characteristic: Characteristic, value: ByteArray): Boolean = writeCharacteristic(characteristic.wrapper, value)

    private fun BluetoothGattWrapper.writeDescriptor(descriptor: Descriptor, value: ByteArray): Boolean {
        descriptor.wrapper.updateValue(value)
        return writeDescriptor(descriptor.wrapper, value)
    }

    private fun BluetoothGattWrapper.setNotification(characteristic: Characteristic, enable: Boolean): Boolean {
        val uuid = characteristic.uuid.uuidString
        if (enable) {
            notifyingCharacteristics[uuid] = characteristic
        } else {
            notifyingCharacteristics.remove(uuid)
        }
        if (!setCharacteristicNotification(characteristic.wrapper, enable)) {
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
                writeDescriptor(descriptor.wrapper, writeValue)
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

private val Int.gattStatusAsString get() = when (this) {
    GATT_SUCCESS -> "SUCCESS"
    GATT_READ_NOT_PERMITTED -> "ERROR_READ_NOT_PERMITTED"
    GATT_WRITE_NOT_PERMITTED -> "ERROR_WRITE_NOT_PERMITTED"
    GATT_INSUFFICIENT_AUTHENTICATION -> "ERROR_INSUFFICIENT_AUTHENTICATION"
    GATT_REQUEST_NOT_SUPPORTED -> "ERROR_REQUEST_NOT_SUPPORTED"
    GATT_INSUFFICIENT_ENCRYPTION -> "ERROR_INSUFFICIENT_ENCRYPTION"
    GATT_INVALID_OFFSET -> "ERROR_INVALID_OFFSET"
    GATT_INSUFFICIENT_AUTHORIZATION -> "ERROR_INSUFFICIENT_AUTHORIZATION"
    GATT_INVALID_ATTRIBUTE_LENGTH -> "ERROR_INVALID_ATTRIBUTE_LENGTH"
    GATT_CONNECTION_CONGESTED -> "ERROR_CONNECTION_CONGESTED"
    GATT_CONNECTION_TIMEOUT -> "ERROR_CONNECTION_TIMEOUT"
    GATT_FAILURE -> "ERROR_FAILURE"
    else -> "ERROR_OTHER($this)"
}

private val Int.connectionStateAsString get() = when (this) {
    BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
    BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
    BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
    BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
    else -> "OTHER($this)"
}
