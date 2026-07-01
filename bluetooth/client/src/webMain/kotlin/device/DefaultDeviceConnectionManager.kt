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

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.WriteType
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.bluetooth.uuidString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

internal actual class DefaultDeviceConnectionManager(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope) :
    BaseDeviceConnectionManager(deviceWrapper, settings, coroutineScope) {

    class Builder : DeviceConnectionManager.Builder {
        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): DefaultDeviceConnectionManager =
            DefaultDeviceConnectionManager(deviceWrapper, settings, coroutineScope)
    }

    private val identifier = deviceWrapper.identifier

    init {
        // Release the device from the interop registry once this manager's scope is torn down — e.g. when
        // the scanner cleans the device away (REMOVE_ALL) or the bluetooth client is deinitialized.
        coroutineContext.job.invokeOnCompletion { webForgetDevice(identifier) }
    }

    actual override fun getCurrentState(): DeviceConnectionManager.State =
        if (webIsConnected(identifier)) DeviceConnectionManager.State.CONNECTED else DeviceConnectionManager.State.DISCONNECTED

    actual override fun connect() {
        launch {
            if (webGattConnect(identifier) { handleDisconnect() }) {
                webSetNotificationHandler(identifier) { service, characteristic ->
                    val value = webCachedCharacteristicValue(identifier, service, characteristic) ?: return@webSetNotificationHandler
                    handleCharacteristicReadOrNotified(uuidFrom(characteristic), GattResponse.ReadSuccess(value))
                }
                handleConnect()
            } else {
                handleDisconnect()
            }
        }
    }

    actual override fun disconnect() {
        webGattDisconnect(identifier)
        handleDisconnect()
    }

    actual override suspend fun discoverServices() {
        handleDiscoverCompleted(webDiscoverServices(identifier).map { com.splendo.kaluga.bluetooth.WebServiceWrapper(it) })
    }

    actual override suspend fun didStartPerformingAction(action: DeviceAction<*>) {
        currentAction = action
        when (action) {
            is DeviceAction.Read.Characteristic -> {
                // The awaited readValue() result is unambiguously the read response. readValue() also echoes
                // through the characteristicvaluechanged handler, where the heuristic drops it (see connect()).
                val result = webReadCharacteristic(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString)
                handleCharacteristicRead(action.characteristic.uuid, result.readResponse())
            }

            is DeviceAction.Read.Descriptor -> {
                val result = webReadDescriptor(
                    identifier,
                    action.descriptor.characteristic.service.uuid.uuidString,
                    action.descriptor.characteristic.uuid.uuidString,
                    action.descriptor.uuid.uuidString,
                )
                handleDescriptorRead(action.descriptor.uuid, result.readResponse())
            }

            is DeviceAction.Write.Characteristic -> {
                val withResponse = when (action.writeType) {
                    WriteType.WithResponse -> true
                    WriteType.WithoutResponse -> false
                    null -> action.characteristic.hasProperty(CharacteristicProperty.Write)
                }
                val result =
                    webWriteCharacteristic(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, action.newValue, withResponse)
                handleCharacteristicWritten(action.characteristic.uuid, result.writeResponse())
            }

            is DeviceAction.Write.Descriptor -> {
                val result = webWriteDescriptor(
                    identifier,
                    action.descriptor.characteristic.service.uuid.uuidString,
                    action.descriptor.characteristic.uuid.uuidString,
                    action.descriptor.uuid.uuidString,
                    action.newValue,
                )
                handleDescriptorWritten(action.descriptor.uuid, result.writeResponse())
            }

            is DeviceAction.Notification.Enable -> {
                val result = webSetNotifying(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, true)
                action.handleNotificationStateChanged(result.writeResponse())
            }

            is DeviceAction.Notification.Disable -> {
                val result = webSetNotifying(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, false)
                action.handleNotificationStateChanged(result.writeResponse())
            }

            is DeviceAction.RequestMtu -> {
                // Web Bluetooth does not expose MTU negotiation.
                handleNewMtu(GattResponse.MTUNotPermitted(action.mtu))
            }
        }
    }

    actual override suspend fun requestReadRssi() {
        // Web Bluetooth has no RSSI support
    }

    actual override suspend fun requestStartPairing(): PairingResult {
        // Web Bluetooth has no pairing API; bonding is handled by the user agent.
        return PairingResult.NOT_SUPPORTED
    }

    actual override suspend fun requestStartUnpairing(): PairingResult {
        // Web Bluetooth has no unpairing API; the user manages this through the browser.
        return PairingResult.NOT_SUPPORTED
    }
}
