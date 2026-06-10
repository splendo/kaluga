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
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.IntentCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.utils.containsAny
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.RemoteCharacteristicWrapper
import com.splendo.kaluga.bluetooth.RemoteDescriptor
import com.splendo.kaluga.bluetooth.WriteType
import com.splendo.kaluga.bluetooth.extensions.printableString
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal actual class DefaultDeviceConnectionManager(
    private val context: Context,
    deviceWrapper: DeviceWrapper,
    private val connectionSettings: ConnectionSettings = ConnectionSettings(),
    coroutineScope: CoroutineScope,
) : BaseDeviceConnectionManager(deviceWrapper, connectionSettings, coroutineScope) {

    class Builder(private val context: Context = ApplicationHolder.applicationContext) : DeviceConnectionManager.Builder {
        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): BaseDeviceConnectionManager =
            DefaultDeviceConnectionManager(context, deviceWrapper, settings, coroutineScope = coroutineScope)
    }

    override val coroutineContext: CoroutineContext = coroutineScope.coroutineContext

    private var gatt: CompletableDeferred<BluetoothGattWrapper> = CompletableDeferred()

    private inner class Callback(private val logger: ConnectionSettings.ConnectionLogger) : BluetoothGattCallback() {

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            logger.stateLogger.stateChangeLogger.info { "onReadRemoteRssi rssi $rssi status ${status.gattStatusAsString}" }
            handleNewRssi(rssi)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            logger.stateLogger.stateChangeLogger.info { "onMtuChanged mtu $mtu status ${status.gattStatusAsString}" }
            handleNewMtu(if (status == GATT_SUCCESS) GattResponse.MTUSuccess(mtu) else GattResponse.MTUFailure(mtu, GattResponse.Error.from(status)))
        }

        @Suppress("OVERRIDE_DEPRECATION")
        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            @Suppress("DEPRECATION")
            val value = characteristic.value
            logger.dataLogger[characteristic.service.uuid][characteristic.uuid].info {
                "onCharacteristicRead[DEP] value ${value.printableString} status ${status.gattStatusAsString}"
            }
            updateCharacteristic(characteristic, value, status)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) {
            logger.dataLogger[characteristic.service.uuid][characteristic.uuid].info { "onCharacteristicRead value ${value.printableString} status ${status.gattStatusAsString}" }
            updateCharacteristic(characteristic, value, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            characteristic ?: return
            logger.dataLogger[characteristic.service.uuid][characteristic.uuid].info { "onCharacteristicWrite status ${status.gattStatusAsString}" }
            handleCharacteristicWritten(characteristic.uuid, if (status == GATT_SUCCESS) GattResponse.WriteSuccess else GattResponse.Error.from(status))
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            logger.stateLogger.stateChangeLogger.info { "onServicesDiscovered status ${status.gattStatusAsString}" }
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
            logger.dataLogger[characteristic.service.uuid][characteristic.uuid].info { "onCharacteristicChanged[DEP] value ${value.printableString}" }
            updateCharacteristic(characteristic, value, status = GATT_SUCCESS)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            logger.dataLogger[characteristic.service.uuid][characteristic.uuid].info { "onCharacteristicChanged[DEP] value ${value.printableString}" }
            updateCharacteristic(characteristic, value, status = GATT_SUCCESS)
        }

        @Suppress("OVERRIDE_DEPRECATION")
        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            @Suppress("DEPRECATION")
            val value = descriptor.value
            logger.dataLogger[descriptor.characteristic.service.uuid][descriptor.characteristic.uuid][descriptor.uuid].info {
                "onDescriptorRead[DEP] value ${value.printableString} status ${status.gattStatusAsString}"
            }
            updateDescriptor(descriptor, value, status)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int, value: ByteArray) {
            logger.dataLogger[descriptor.characteristic.service.uuid][descriptor.characteristic.uuid][descriptor.uuid].info {
                "onDescriptorRead value ${value.printableString} status ${status.gattStatusAsString}"
            }
            updateDescriptor(descriptor, value, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            logger.dataLogger[descriptor.characteristic.service.uuid][descriptor.characteristic.uuid][descriptor.uuid].info {
                "onDescriptorWrite status ${status.gattStatusAsString}"
            }
            val response = if (status == GATT_SUCCESS) {
                GattResponse.WriteSuccess
            } else {
                GattResponse.Error.from(status)
            }
            // Notification enable/disable done by client configuration descriptor write
            val action = currentAction
            if (descriptor.uuid == Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR && action is DeviceAction.Notification) {
                action.handleNotificationStateChanged(response)
            }
            handleDescriptorWritten(descriptor.uuid, response)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            logger.stateLogger.stateChangeLogger.info { "onConnectionStateChange status ${status.gattStatusAsString} newState ${newState.connectionStateAsString}" }
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
            !gatt.isCompleted -> gatt.complete(deviceWrapper.connectGatt(context, false, Callback(connectionSettings.logger(deviceWrapper.identifier))))
            lastKnownState == BluetoothProfile.STATE_CONNECTED -> handleConnect()
            !gatt.getCompleted().connect() -> handleDisconnect { closeGatt() }
            else -> {}
        }
    }

    actual override suspend fun discoverServices() {
        // A false return means discovery never started, so the awaited onServicesDiscovered would never fire.
        // Disconnect instead of leaving the state machine stuck in Discovering with no timeout.
        if (!gatt.await().discoverServices()) {
            logger.stateLogger.stateChangeLogger.info { "Failed to start service discovery" }
            handleDisconnect { closeGatt() }
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
        if (!gatt.await().readRemoteRssi()) {
            logger.stateLogger.stateChangeLogger.info { "Failed to start RSSI read" }
        }
    }

    actual override suspend fun didStartPerformingAction(action: DeviceAction<*>) {
        currentAction = action
        val readyGatt = gatt.await()
        when (action) {
            is DeviceAction.Read.Characteristic -> if (!readyGatt.readCharacteristic(action.characteristic.wrapper)) {
                action.handleActionCompleted(GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Read.Descriptor -> if (!readyGatt.readDescriptor(action.descriptor.wrapper)) {
                action.handleActionCompleted(GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Write.Characteristic -> if (!readyGatt.writeCharacteristic(action.characteristic, action.newValue, action.writeType)) {
                action.handleActionCompleted(GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Write.Descriptor -> if (!readyGatt.writeDescriptor(action.descriptor, action.newValue)) {
                action.handleActionCompleted(GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Notification.Enable -> if (!readyGatt.setNotification(action.characteristic, true)) {
                action.handleActionCompleted(GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Notification.Disable -> if (!readyGatt.setNotification(action.characteristic, false)) {
                action.handleActionCompleted(GattResponse.DeviceUnavailable)
            }

            is DeviceAction.RequestMtu -> if (!readyGatt.requestMtu(action.mtu)) {
                action.handleActionCompleted(GattResponse.MTUNotPermitted(action.mtu))
            }
        }
    }

    @SuppressLint("MissingPermission")
    actual override suspend fun requestStartPairing(): PairingResult {
        if (deviceWrapper.bondState == DeviceWrapper.BondState.BONDED) return PairingResult.SUCCESS
        return awaitBondStateChange(DeviceWrapper.BondState.BONDED) { deviceWrapper.createBond() }
    }

    @SuppressLint("MissingPermission")
    actual override suspend fun requestStartUnpairing(): PairingResult {
        if (deviceWrapper.bondState == DeviceWrapper.BondState.NONE) return PairingResult.SUCCESS
        return awaitBondStateChange(DeviceWrapper.BondState.NONE) { deviceWrapper.removeBond() }
    }

    /**
     * Triggers a bond change via [startBondChange] and waits for this device's bond state to settle, using the
     * system [BluetoothDevice.ACTION_BOND_STATE_CHANGED] broadcast: [PairingResult.SUCCESS] once it reaches
     * [target], [PairingResult.FAILURE] if it settles into the other terminal state. This suspends until the
     * bond state settles; wrap the call in `withTimeoutOrNull` to bound the wait (cancelling unregisters the
     * receiver).
     */
    private suspend fun awaitBondStateChange(target: DeviceWrapper.BondState, startBondChange: () -> Unit): PairingResult {
        val result = CompletableDeferred<PairingResult>()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(receiverContext: Context?, intent: Intent?) {
                if (intent?.action != BluetoothDevice.ACTION_BOND_STATE_CHANGED) return
                val device = IntentCompat.getParcelableExtra(intent, BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                if (device?.address != deviceWrapper.identifier) return
                when (intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)) {
                    BluetoothDevice.BOND_BONDED ->
                        result.complete(if (target == DeviceWrapper.BondState.BONDED) PairingResult.SUCCESS else PairingResult.FAILURE)

                    BluetoothDevice.BOND_NONE ->
                        result.complete(if (target == DeviceWrapper.BondState.NONE) PairingResult.SUCCESS else PairingResult.FAILURE)
                    // BOND_BONDING is a transient state; keep waiting for a terminal one.
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
        return try {
            startBondChange()
            result.await()
        } finally {
            context.unregisterReceiver(receiver)
        }
    }

    private fun BluetoothGattWrapper.writeCharacteristic(characteristic: RemoteCharacteristic, value: ByteArray, writeType: WriteType?): Boolean {
        when (writeType) {
            WriteType.WithResponse -> characteristic.wrapper.writeType = RemoteCharacteristicWrapper.WriteType.DEFAULT
            WriteType.WithoutResponse -> characteristic.wrapper.writeType = RemoteCharacteristicWrapper.WriteType.NO_RESPONSE
            null -> {}
        }
        return writeCharacteristic(characteristic.wrapper, value)
    }

    private fun BluetoothGattWrapper.writeDescriptor(descriptor: RemoteDescriptor, value: ByteArray): Boolean = writeDescriptor(descriptor.wrapper, value)

    private fun BluetoothGattWrapper.setNotification(characteristic: RemoteCharacteristic, enable: Boolean): Boolean {
        if (!setCharacteristicNotification(characteristic.wrapper, enable)) {
            return false
        }

        val writeValue = when {
            enable && characteristic.wrapper.properties.contains(CharacteristicProperty.Notify) ->
                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE

            enable && characteristic.wrapper.properties.contains(CharacteristicProperty.Indicate) ->
                BluetoothGattDescriptor.ENABLE_INDICATION_VALUE

            !enable && characteristic.wrapper.properties.containsAny(setOf(CharacteristicProperty.Indicate, CharacteristicProperty.Notify)) ->
                BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE

            else -> null
        }

        return if (writeValue != null) {
            characteristic.descriptors.firstOrNull { it.uuid == Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR }?.let { descriptor ->
                writeDescriptor(descriptor.wrapper, writeValue)
            } == true
        } else {
            connectionSettings.logger(deviceWrapper.identifier).dataLogger[characteristic.wrapper.service.uuid][characteristic.wrapper.uuid].error {
                "(${characteristic.uuid.uuidString}) Failed attempt to perform set notification action. " +
                    "neither NOTIFICATION nor INDICATION is supported. " +
                    "Supported properties: ${characteristic.wrapper.properties}"
            }
            false
        }
    }

    private fun updateCharacteristic(characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) {
        handleCharacteristicReadOrNotified(characteristic.uuid, response = if (status == GATT_SUCCESS) GattResponse.ReadSuccess(value) else GattResponse.Error.from(status))
    }

    private fun updateDescriptor(descriptor: BluetoothGattDescriptor, value: ByteArray, status: Int) {
        handleDescriptorRead(descriptor.uuid, response = if (status == GATT_SUCCESS) GattResponse.ReadSuccess(value) else GattResponse.Error.from(status))
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
    19 -> "REMOTE_USER_TERMINATED_CONNECTION"
    133 -> "DEVICE_NOT_FOUND"
    else -> "ERROR_OTHER($this)"
}

private val Int.connectionStateAsString get() = when (this) {
    BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
    BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
    BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
    BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
    else -> "OTHER($this)"
}
