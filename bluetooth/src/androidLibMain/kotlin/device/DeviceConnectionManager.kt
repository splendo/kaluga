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
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_INDICATE
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.DefaultGattServiceWrapper
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.hasProperty
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal actual class DeviceConnectionManager(
    private val context: Context,
    connectionSettings: ConnectionSettings,
    deviceWrapper: DeviceWrapper,
    stateRepo: DeviceStateFlowRepo,
    val mainDispatcher:CoroutineContext = Dispatchers.Main.immediate // Implementation wanted some things to run on the main thread explicitly, however for testing it might make sense to replace this
) : BaseDeviceConnectionManager(connectionSettings, deviceWrapper, stateRepo), CoroutineScope by stateRepo {

    private companion object {
         const val TAG = "Android Bluetooth DeviceConnectionManager"
    }

    override val coroutineContext: CoroutineContext
        get() = stateRepo.coroutineContext

    class Builder(private val context: Context = ApplicationHolder.applicationContext) : BaseDeviceConnectionManager.Builder {
        override fun create(
            connectionSettings: ConnectionSettings,
            deviceWrapper: DeviceWrapper,
            stateRepo: DeviceStateFlowRepo,
        ): BaseDeviceConnectionManager {
            return DeviceConnectionManager(context, connectionSettings, deviceWrapper, stateRepo)
        }
    }

    private val device: android.bluetooth.BluetoothDevice = deviceWrapper.device
    private var gatt: CompletableDeferred<BluetoothGattWrapper> = CompletableDeferred()
    private val callback = object : BluetoothGattCallback() {

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            launch(mainDispatcher) {
                handleNewRssi(rssi)
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            info(TAG, "onCharacteristicRead $gatt $characteristic $status")
            characteristic ?: return
            updateCharacteristic(characteristic)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            info(TAG, "onCharacteristicWrite $gatt $characteristic $status")
            characteristic ?: return

            updateCharacteristic(characteristic)

            //TODO: Remove it before merge to master
            // if (characteristic.properties and PROPERTY_INDICATE != 0) {
            //     gatt?.readCharacteristic(characteristic)
            // }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            launch(mainDispatcher) {
                val services = gatt?.services?.map { Service(DefaultGattServiceWrapper(it), stateRepo) } ?: emptyList()
                handleScanCompleted(services)
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            updateDescriptor(descriptor)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            info(TAG, "onCharacteristicChanged $gatt $characteristic")
            characteristic ?: return
            updateCharacteristic(characteristic)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptor ?: return
            updateDescriptor(descriptor)
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

    override suspend fun performAction(action: DeviceAction) {
        currentAction = action
        val shouldWait = when (action) {
            is DeviceAction.Read.Characteristic -> gatt.await().readCharacteristic(action.characteristic.wrapper)
            is DeviceAction.Read.Descriptor -> gatt.await().readDescriptor(action.descriptor.wrapper)
            is DeviceAction.Write.Characteristic -> {
                action.characteristic.wrapper.updateValue(action.newValue)
                gatt.await().writeCharacteristic(action.characteristic.wrapper)
            }
            is DeviceAction.Write.Descriptor -> {
                action.descriptor.wrapper.updateValue(action.newValue)
                gatt.await().writeDescriptor(action.descriptor.wrapper)
            }
            is DeviceAction.Notification -> {
                val uuid = action.characteristic.uuid.uuidString
                if (action.enable) {
                    notifyingCharacteristics[uuid] = action.characteristic
                } else notifyingCharacteristics.remove(uuid)
                gatt.await().setCharacteristicNotification(action.characteristic.wrapper, action.enable).also {
                    info(TAG, "setCharacteristicNotification result: $it")
                }

                info { " * wrapper property: ${action.characteristic.wrapper.properties}" }
                val value = when {
                    action.enable and action.characteristic.wrapper.hasProperty(PROPERTY_INDICATE) -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                    action.enable and action.characteristic.wrapper.hasProperty(PROPERTY_NOTIFY) -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    else -> BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                }

                action.characteristic.descriptors.forEach { descriptor ->
                    info(TAG, "(${action.characteristic.uuid.uuidString}) writeValue 0x${value.toHexString()} to $descriptor")
                    descriptor.wrapper.updateValue(value)
                    gatt.await().writeDescriptor(descriptor.wrapper)
                }
                false
            }
        }
        // Action Failed or Already Completed
        if (!shouldWait) {
            launch(mainDispatcher) {
                handleCurrentActionCompleted()
            }
        }
    }

    private fun updateCharacteristic(characteristic: BluetoothGattCharacteristic) {
        launch(mainDispatcher) {
            handleUpdatedCharacteristic(characteristic.uuid) {
                it.wrapper.updateValue(characteristic.value)
            }
        }
    }

    private fun updateDescriptor(descriptor: BluetoothGattDescriptor) {
        launch(mainDispatcher) {
            handleUpdatedDescriptor(descriptor.uuid) {
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
