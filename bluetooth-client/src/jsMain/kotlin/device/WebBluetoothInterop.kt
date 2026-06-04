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

import kotlinx.coroutines.await
import org.khronos.webgl.DataView
import org.khronos.webgl.Int8Array
import kotlin.js.Promise

// Per-target registries holding the live Web Bluetooth handles. They cannot live in webMain (shared
// metadata) because the JS objects + their Promises + value-changed callbacks are not shareable
// across the JS family. Everything is keyed by the string ids/uuids of the webMain interop surface.

private val devices = mutableMapOf<String, dynamic>()
private val characteristics = mutableMapOf<String, dynamic>()
private val descriptors = mutableMapOf<String, dynamic>()
private val cachedValues = mutableMapOf<String, ByteArray>()
private val notificationHandlers = mutableMapOf<String, (String, String) -> Unit>()

private fun characteristicKey(identifier: String, service: String, characteristic: String) = "$identifier|$service|$characteristic"
private fun descriptorKey(identifier: String, service: String, characteristic: String, descriptor: String) = "$identifier|$service|$characteristic|$descriptor"

private fun bluetoothApi(): dynamic = js("navigator.bluetooth")

private suspend fun awaitJs(promise: dynamic): dynamic = (promise as Promise<*>).await().asDynamic()

private fun dataViewToByteArray(dataView: dynamic): ByteArray {
    val view = dataView.unsafeCast<DataView>()
    return ByteArray(view.byteLength) { view.getInt8(it) }
}

private fun propertiesToInt(properties: dynamic): Int {
    var value = 0
    if (properties.broadcast == true) value = value or 0x01
    if (properties.read == true) value = value or 0x02
    if (properties.writeWithoutResponse == true) value = value or 0x04
    if (properties.write == true) value = value or 0x08
    if (properties.notify == true) value = value or 0x10
    if (properties.indicate == true) value = value or 0x20
    if (properties.authenticatedSignedWrites == true) value = value or 0x40
    return value
}

internal actual fun webBluetoothSupported(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.bluetooth").unsafeCast<Boolean>()

private var pickerOverlay: dynamic = null
private val addedDeviceIds = mutableSetOf<String>()

private fun requestDeviceOptions(filterServices: List<String>, optionalServices: List<String>): dynamic {
    val options: dynamic = js("({})")
    if (filterServices.isEmpty()) {
        options.acceptAllDevices = true
    } else {
        val filter: dynamic = js("({})")
        filter.services = filterServices.toTypedArray()
        options.filters = arrayOf(filter)
    }
    if (optionalServices.isNotEmpty()) {
        options.optionalServices = optionalServices.toTypedArray()
    }
    return options
}

internal actual fun webShowDevicePicker(
    filterServices: List<String>,
    optionalServices: List<String>,
    title: String,
    addButtonLabel: String,
    emptyLabel: String,
    cssClassPrefix: String,
    containerId: String?,
    onDevicePicked: (identifier: String, name: String) -> Unit,
) {
    val document: dynamic = js("(typeof document !== 'undefined' ? document : null)")
    if (document == null) return
    webHideDevicePicker()

    val overlay = document.createElement("div")
    overlay.className = "$cssClassPrefix-overlay"

    // A self-contained dismiss control so the overlay can always be closed even if it covers app UI.
    val closeButton = document.createElement("button")
    closeButton.className = "$cssClassPrefix-close"
    closeButton.textContent = "✕"
    closeButton.addEventListener("click") { webHideDevicePicker() }
    overlay.appendChild(closeButton)

    val heading = document.createElement("h2")
    heading.className = "$cssClassPrefix-title"
    heading.textContent = title
    overlay.appendChild(heading)

    val list = document.createElement("ul")
    list.className = "$cssClassPrefix-list"
    val emptyItem = document.createElement("li")
    emptyItem.className = "$cssClassPrefix-list-empty"
    emptyItem.textContent = emptyLabel
    list.appendChild(emptyItem)
    overlay.appendChild(list)

    val button = document.createElement("button")
    button.className = "$cssClassPrefix-button"
    button.textContent = addButtonLabel
    button.addEventListener("click") {
        // Called synchronously from the click handler so the requestDevice user-gesture requirement holds.
        bluetoothApi().requestDevice(requestDeviceOptions(filterServices, optionalServices)).then(
            { device ->
                val id = device.id.unsafeCast<String>()
                devices[id] = device
                val name = (device.name as? String) ?: ""
                if (addedDeviceIds.add(id)) {
                    if (emptyItem.parentNode != null) list.removeChild(emptyItem)
                    val item = document.createElement("li")
                    item.className = "$cssClassPrefix-list-item"
                    item.textContent = if (name.isEmpty()) id else name
                    list.appendChild(item)
                }
                onDevicePicked(id, name)
            },
            { _ -> Unit }, // user cancelled the picker or it failed
        )
    }
    overlay.appendChild(button)

    val container = containerId?.let { document.getElementById(it) } ?: document.body
    container.appendChild(overlay)
    pickerOverlay = overlay
}

internal actual fun webHideDevicePicker() {
    val overlay = pickerOverlay ?: return
    if (overlay.parentNode != null) {
        overlay.parentNode.removeChild(overlay)
    }
    pickerOverlay = null
    addedDeviceIds.clear()
}

internal actual suspend fun webGattConnect(identifier: String, onDisconnected: () -> Unit): Boolean {
    val device = devices[identifier] ?: return false
    return try {
        device.addEventListener("gattserverdisconnected", { onDisconnected() })
        awaitJs(device.gatt.connect())
        true
    } catch (e: Throwable) {
        false
    }
}

internal actual fun webGattDisconnect(identifier: String) {
    val device = devices[identifier] ?: return
    if (device.gatt.connected == true) {
        device.gatt.disconnect()
    }
}

internal actual fun webIsConnected(identifier: String): Boolean = devices[identifier]?.gatt?.connected == true

internal actual suspend fun webDiscoverServices(identifier: String): List<WebService> {
    val device = devices[identifier] ?: return emptyList()
    val services = awaitJs(device.gatt.getPrimaryServices()).unsafeCast<Array<dynamic>>()
    return services.map { service ->
        val serviceUuid = service.uuid.unsafeCast<String>()
        val characteristicHandles = awaitJs(service.getCharacteristics()).unsafeCast<Array<dynamic>>()
        val webCharacteristics = characteristicHandles.map { characteristic ->
            val characteristicUuid = characteristic.uuid.unsafeCast<String>()
            characteristics[characteristicKey(identifier, serviceUuid, characteristicUuid)] = characteristic
            val descriptorHandles = try {
                awaitJs(characteristic.getDescriptors()).unsafeCast<Array<dynamic>>()
            } catch (e: Throwable) {
                emptyArray<dynamic>()
            }
            val descriptorUuids = descriptorHandles.map { descriptor ->
                val descriptorUuid = descriptor.uuid.unsafeCast<String>()
                descriptors[descriptorKey(identifier, serviceUuid, characteristicUuid, descriptorUuid)] = descriptor
                descriptorUuid
            }
            WebCharacteristic(characteristicUuid, propertiesToInt(characteristic.properties), descriptorUuids)
        }
        WebService(serviceUuid, service.isPrimary != false, webCharacteristics)
    }
}

// The `name` of the rejected DOMException (e.g. "NetworkError", "NotSupportedError"); Web Bluetooth
// does not expose the underlying numeric ATT status code.
private fun errorName(e: Throwable): String? = (e.asDynamic().name as? String) ?: e.message

internal actual suspend fun webReadCharacteristic(identifier: String, service: String, characteristic: String): WebGattResult {
    val handle = characteristics[characteristicKey(identifier, service, characteristic)] ?: return WebGattResult.Failure(null)
    return try {
        val bytes = dataViewToByteArray(awaitJs(handle.readValue()))
        cachedValues[characteristicKey(identifier, service, characteristic)] = bytes
        WebGattResult.Success(bytes)
    } catch (e: Throwable) {
        WebGattResult.Failure(errorName(e))
    }
}

internal actual suspend fun webWriteCharacteristic(identifier: String, service: String, characteristic: String, value: ByteArray, withResponse: Boolean): WebGattResult {
    val handle = characteristics[characteristicKey(identifier, service, characteristic)] ?: return WebGattResult.Failure(null)
    val buffer = value.unsafeCast<Int8Array>()
    return try {
        if (withResponse) {
            awaitJs(handle.writeValueWithResponse(buffer))
        } else {
            awaitJs(handle.writeValueWithoutResponse(buffer))
        }
        WebGattResult.Success(null)
    } catch (e: Throwable) {
        WebGattResult.Failure(errorName(e))
    }
}

internal actual suspend fun webReadDescriptor(identifier: String, service: String, characteristic: String, descriptor: String): WebGattResult {
    val handle = descriptors[descriptorKey(identifier, service, characteristic, descriptor)] ?: return WebGattResult.Failure(null)
    return try {
        WebGattResult.Success(dataViewToByteArray(awaitJs(handle.readValue())))
    } catch (e: Throwable) {
        WebGattResult.Failure(errorName(e))
    }
}

internal actual suspend fun webWriteDescriptor(identifier: String, service: String, characteristic: String, descriptor: String, value: ByteArray): WebGattResult {
    val handle = descriptors[descriptorKey(identifier, service, characteristic, descriptor)] ?: return WebGattResult.Failure(null)
    return try {
        awaitJs(handle.writeValue(value.unsafeCast<Int8Array>()))
        WebGattResult.Success(null)
    } catch (e: Throwable) {
        WebGattResult.Failure(errorName(e))
    }
}

internal actual suspend fun webSetNotifying(identifier: String, service: String, characteristic: String, enable: Boolean): WebGattResult {
    val handle = characteristics[characteristicKey(identifier, service, characteristic)] ?: return WebGattResult.Failure(null)
    return try {
        if (enable) {
            handle.addEventListener("characteristicvaluechanged") { event: dynamic ->
                val bytes = dataViewToByteArray(event.target.value)
                cachedValues[characteristicKey(identifier, service, characteristic)] = bytes
                notificationHandlers[identifier]?.invoke(service, characteristic)
            }
            awaitJs(handle.startNotifications())
        } else {
            awaitJs(handle.stopNotifications())
        }
        WebGattResult.Success(null)
    } catch (e: Throwable) {
        WebGattResult.Failure(errorName(e))
    }
}

internal actual fun webSetNotificationHandler(identifier: String, handler: (service: String, characteristic: String) -> Unit) {
    notificationHandlers[identifier] = handler
}

internal actual fun webCachedCharacteristicValue(identifier: String, service: String, characteristic: String): ByteArray? =
    cachedValues[characteristicKey(identifier, service, characteristic)]
