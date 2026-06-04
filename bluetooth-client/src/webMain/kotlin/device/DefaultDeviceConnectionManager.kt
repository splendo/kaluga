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
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.bluetooth.uuidString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal actual class DefaultDeviceConnectionManager(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope) :
    BaseDeviceConnectionManager(deviceWrapper, settings, coroutineScope) {

    class Builder : DeviceConnectionManager.Builder {
        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): DefaultDeviceConnectionManager =
            DefaultDeviceConnectionManager(deviceWrapper, settings, coroutineScope)
    }

    private val identifier = deviceWrapper.identifier

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
                val value = webReadCharacteristic(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString)
                handleCharacteristicReadOrNotified(action.characteristic.uuid, value?.let { GattResponse.ReadSuccess(it) } ?: GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Read.Descriptor -> {
                val value = webReadDescriptor(
                    identifier,
                    action.descriptor.characteristic.service.uuid.uuidString,
                    action.descriptor.characteristic.uuid.uuidString,
                    action.descriptor.uuid.uuidString,
                )
                handleDescriptorRead(action.descriptor.uuid, value?.let { GattResponse.ReadSuccess(it) } ?: GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Write.Characteristic -> {
                val withResponse = action.characteristic.hasProperty(CharacteristicProperty.Write) ||
                    !action.characteristic.hasProperty(CharacteristicProperty.WriteWithoutResponse)
                val success =
                    webWriteCharacteristic(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, action.newValue, withResponse)
                handleCharacteristicWritten(action.characteristic.uuid, if (success) GattResponse.WriteSuccess else GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Write.Descriptor -> {
                val success = webWriteDescriptor(
                    identifier,
                    action.descriptor.characteristic.service.uuid.uuidString,
                    action.descriptor.characteristic.uuid.uuidString,
                    action.descriptor.uuid.uuidString,
                    action.newValue,
                )
                handleDescriptorWritten(action.descriptor.uuid, if (success) GattResponse.WriteSuccess else GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Notification.Enable -> {
                val success = webSetNotifying(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, true)
                action.handleNotificationStateChanged(if (success) GattResponse.WriteSuccess else GattResponse.DeviceUnavailable)
            }

            is DeviceAction.Notification.Disable -> {
                val success = webSetNotifying(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, false)
                action.handleNotificationStateChanged(if (success) GattResponse.WriteSuccess else GattResponse.DeviceUnavailable)
            }

            is DeviceAction.RequestMtu -> {
                // Web Bluetooth does not expose MTU negotiation.
                handleNewMtu(GattResponse.MTUNotPermitted(action.mtu))
            }
        }
    }

    actual override suspend fun requestStartPairing() {
        // Web Bluetooth has no pairing API; bonding is handled by the user agent.
    }

    actual override suspend fun requestStartUnpairing() {
        // Web Bluetooth has no unpairing API; the user manages this through the browser.
    }
}
