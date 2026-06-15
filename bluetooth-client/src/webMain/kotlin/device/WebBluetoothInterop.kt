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

import com.splendo.kaluga.bluetooth.GattResponse

// The Web Bluetooth boundary, expressed in plain kaluga/Kotlin types so the orchestration (scanner,
// connection manager, wrappers) can be shared in webMain. The `actual`s in jsMain/wasmJsMain hold the
// live JS GATT objects in a per-target registry keyed by the string ids/uuids passed here, because
// those objects + their `Promise`s + value-changed callbacks can't be shared across the JS family.

/** `true` if the Web Bluetooth API is present in this environment. */
internal expect fun webBluetoothSupported(): Boolean

/** A device returned by the `navigator.bluetooth.requestDevice` picker. */
internal data class WebDeviceInfo(val identifier: String, val name: String?)

/** A characteristic discovered on a connected device. [properties] is the GATT property bitmask. */
internal data class WebCharacteristic(val uuid: String, val properties: Int, val descriptors: List<String>)

/** A service discovered on a connected device. */
internal data class WebService(val uuid: String, val isPrimary: Boolean, val characteristics: List<WebCharacteristic>)

/**
 * The outcome of a GATT read/write/notify operation. Web Bluetooth does not surface the numeric ATT
 * status code — a failed operation rejects with a `DOMException` — so [Failure.errorName] carries that
 * exception's `name` (e.g. `"NetworkError"`, `"NotSupportedError"`) for a best-effort mapping.
 */
internal sealed class WebGattResult {
    /** A successful operation. [value] holds the bytes read, or `null` for writes/notifications. */
    class Success(val value: ByteArray?) : WebGattResult()

    /** A failed operation. [errorName] is the rejected `DOMException.name`, or `null`/`""` when unknown. */
    class Failure(val errorName: String?) : WebGattResult()
}

internal fun WebGattResult.readResponse(): GattResponse.ReadResponse = when (this) {
    is WebGattResult.Success -> GattResponse.ReadSuccess(value ?: ByteArray(0))
    is WebGattResult.Failure -> gattError(errorName) ?: GattResponse.DeviceUnavailable
}

internal fun WebGattResult.writeResponse(): GattResponse.WriteResponse = when (this) {
    is WebGattResult.Success -> GattResponse.WriteSuccess
    is WebGattResult.Failure -> gattError(errorName) ?: GattResponse.DeviceUnavailable
}

// Web Bluetooth collapses several ATT error codes onto each DOMException name, so this is necessarily a
// best-effort translation; connection-loss / unknown names fall through to [GattResponse.DeviceUnavailable].
private fun gattError(errorName: String?): GattResponse.Error? = when (errorName) {
    "NotSupportedError" -> GattResponse.RequestNotSupported
    "SecurityError" -> GattResponse.InsufficientAuthentication
    "InvalidModificationError" -> GattResponse.InvalidAttributeValueLength
    "NotFoundError" -> GattResponse.AttributeNotFound
    "NetworkError", "InvalidStateError", "AbortError", "", null -> null
    else -> GattResponse.UnlikelyError
}

/**
 * Renders the "Add Device" overlay into the DOM and keeps it visible until [webHideDevicePicker].
 *
 * `navigator.bluetooth.requestDevice` requires transient user activation, so the overlay's button
 * invokes it directly from its own click handler — this both satisfies the gesture requirement and
 * lets the user add multiple devices in one scan. [filterServices] (if non-empty) narrows the picker
 * by *advertised* services; [optionalServices] is the advertisement-independent access allowlist.
 * Each successfully picked device is registered in the per-target registry, appended to the overlay's
 * list, and reported through [onDevicePicked] (its [name] is empty when the device exposes none).
 * @param containerId the id of the element to mount the overlay in, or `null` for `document.body`
 */
internal expect fun webShowDevicePicker(
    filterServices: List<String>,
    optionalServices: List<String>,
    title: String,
    addButtonLabel: String,
    emptyLabel: String,
    cssClassPrefix: String,
    containerId: String?,
    onDevicePicked: (identifier: String, name: String) -> Unit,
)

/** Removes the overlay rendered by [webShowDevicePicker], if any. */
internal expect fun webHideDevicePicker()

/** Connects this device's GATT server. [onDisconnected] is invoked on `gattserverdisconnected`. */
internal expect suspend fun webGattConnect(identifier: String, onDisconnected: () -> Unit): Boolean

internal expect fun webGattDisconnect(identifier: String)

/**
 * Forgets a device entirely — drops its connection-scoped state, its `gattserverdisconnected` listener
 * and the device handle from the registry. Called when the device's connection manager is torn down (its
 * scope completes), e.g. once the scanner has cleaned it away.
 */
internal expect fun webForgetDevice(identifier: String)

internal expect fun webIsConnected(identifier: String): Boolean

/** Discovers (and registers) all allowlisted services/characteristics/descriptors of the connected device. */
internal expect suspend fun webDiscoverServices(identifier: String): List<WebService>

internal expect suspend fun webReadCharacteristic(identifier: String, service: String, characteristic: String): WebGattResult

internal expect suspend fun webWriteCharacteristic(identifier: String, service: String, characteristic: String, value: ByteArray, withResponse: Boolean): WebGattResult

internal expect suspend fun webReadDescriptor(identifier: String, service: String, characteristic: String, descriptor: String): WebGattResult

internal expect suspend fun webWriteDescriptor(identifier: String, service: String, characteristic: String, descriptor: String, value: ByteArray): WebGattResult

internal expect suspend fun webSetNotifying(identifier: String, service: String, characteristic: String, enable: Boolean): WebGattResult

/** Registers [handler] (called with service + characteristic uuid) for `characteristicvaluechanged` events. */
internal expect fun webSetNotificationHandler(identifier: String, handler: (service: String, characteristic: String) -> Unit)

/** The last value cached on a characteristic (set after a read or a notification), or `null`. */
internal expect fun webCachedCharacteristicValue(identifier: String, service: String, characteristic: String): ByteArray?
