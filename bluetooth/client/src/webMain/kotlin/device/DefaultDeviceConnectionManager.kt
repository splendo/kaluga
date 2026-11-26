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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

internal actual class DefaultDeviceConnectionManager(
    deviceWrapper: DeviceWrapper,
    settings: ConnectionSettings,
    coroutineScope: CoroutineScope,
    private val notSupportedDisconnectGrace: Duration,
) : BaseDeviceConnectionManager(deviceWrapper, settings, coroutineScope) {

    class Builder(private val notSupportedDisconnectGrace: Duration) : DeviceConnectionManager.Builder {
        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): DefaultDeviceConnectionManager =
            DefaultDeviceConnectionManager(deviceWrapper, settings, coroutineScope, notSupportedDisconnectGrace)
    }

    /** An answer held back while waiting for the disconnect that may follow it. */
    private class HeldAnswer(val linkDown: () -> Unit, val asIs: () -> Unit) {
        val released = CompletableDeferred<Unit>()
    }

    private var heldAnswer: HeldAnswer? = null

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
            if (webGattConnect(identifier) { handleLinkDown() }) {
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

    actual override fun intentionalDisconnect() {
        // The answer arrived before the link was asked to close, so it is the device's answer.
        releaseHeldAnswer { asIs() }
        webGattDisconnect(identifier)
        handleDisconnect()
    }

    private fun handleLinkDown() {
        releaseHeldAnswer { linkDown() }
        handleDisconnect()
    }

    private fun releaseHeldAnswer(answer: HeldAnswer.() -> Unit) {
        val held = heldAnswer ?: return
        heldAnswer = null
        held.answer()
        held.released.complete(Unit)
    }

    /**
     * Answers this action with [result] through [handle]. Chrome rejects a request in flight as the link drops with
     * `NotSupportedError` a few milliseconds before it reports the disconnect, and gives a peripheral's own error answer
     * the same name. So that rejection is held for [notSupportedDisconnectGrace]: if the link goes down meanwhile, the
     * action answers [GattResponse.DeviceUnavailable] with the error as its cause.
     */
    private suspend fun <R : GattResponse> DeviceAction<R>.answer(
        result: WebGattResult,
        response: WebGattResult.() -> R,
        linkDown: (GattResponse.Error) -> R,
        handle: (R) -> Unit,
    ) {
        val answer = result.response()
        if (answer !is GattResponse.Error || !result.isNotSupported || notSupportedDisconnectGrace <= Duration.ZERO || this.response.isCompleted) {
            handle(answer)
            return
        }
        val held = HeldAnswer(linkDown = { completeBeforeDisconnect(linkDown(answer)) }, asIs = { completeBeforeDisconnect(answer) })
        heldAnswer = held
        val released = try {
            withTimeoutOrNull(notSupportedDisconnectGrace) { held.released.await() } != null
        } finally {
            if (heldAnswer === held) heldAnswer = null
        }
        if (!released) handle(answer)
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
                action.answer(result, WebGattResult::readResponse, GattResponse::DeviceUnavailable) { handleCharacteristicRead(action.characteristic.uuid, it) }
            }

            is DeviceAction.Read.Descriptor -> {
                val result = webReadDescriptor(
                    identifier,
                    action.descriptor.characteristic.service.uuid.uuidString,
                    action.descriptor.characteristic.uuid.uuidString,
                    action.descriptor.uuid.uuidString,
                )
                action.answer(result, WebGattResult::readResponse, GattResponse::DeviceUnavailable) { handleDescriptorRead(action.descriptor.uuid, it) }
            }

            is DeviceAction.Write.Characteristic -> {
                val withResponse = when (action.writeType) {
                    WriteType.WithResponse -> true
                    WriteType.WithoutResponse -> false
                    null -> action.characteristic.hasProperty(CharacteristicProperty.Write)
                }
                val result =
                    webWriteCharacteristic(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, action.newValue, withResponse)
                action.answer(result, WebGattResult::writeResponse, GattResponse::DeviceUnavailable) { handleCharacteristicWritten(action.characteristic.uuid, it) }
            }

            is DeviceAction.Write.Descriptor -> {
                val result = webWriteDescriptor(
                    identifier,
                    action.descriptor.characteristic.service.uuid.uuidString,
                    action.descriptor.characteristic.uuid.uuidString,
                    action.descriptor.uuid.uuidString,
                    action.newValue,
                )
                action.answer(result, WebGattResult::writeResponse, GattResponse::DeviceUnavailable) { handleDescriptorWritten(action.descriptor.uuid, it) }
            }

            is DeviceAction.Notification.Enable -> {
                val result = webSetNotifying(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, true)
                action.answer(result, WebGattResult::writeResponse, GattResponse::DeviceUnavailable) { action.handleNotificationStateChanged(it) }
            }

            is DeviceAction.Notification.Disable -> {
                val result = webSetNotifying(identifier, action.characteristic.service.uuid.uuidString, action.characteristic.uuid.uuidString, false)
                action.answer(result, WebGattResult::writeResponse, GattResponse::DeviceUnavailable) { action.handleNotificationStateChanged(it) }
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
