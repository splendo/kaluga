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

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

// Wasm cannot share Promise<T> across the JS family and cannot hold the live Web Bluetooth objects in
// typed Kotlin, so the handles live in a JS-side registry (`globalThis.__kbt`) keyed by the string
// ids/uuids of the webMain interop surface. Each async op is bridged to a coroutine via plain
// success/error callbacks passed into `js(...)`; byte payloads cross the boundary as DataView /
// Uint8Array through the minimal externals below.

// The Kotlin/Wasm stdlib ships no typed-array bindings (org.khronos.webgl is JS-only), and the
// Web Bluetooth API itself has no Kotlin declarations anywhere, so a hand-rolled surface this
// small is cheaper than a dependency. Byte is not a JS interop type; the seam speaks Int.
private external interface JsDataView : JsAny {
    val byteLength: Int
    fun getInt8(byteOffset: Int): Int
}

private fun newUint8Array(size: Int): JsAny = js("new Uint8Array(size)")
private fun uint8ArraySet(array: JsAny, index: Int, value: Int): Unit = js("{ array[index] = value; }")

private fun JsDataView.toByteArray(): ByteArray = ByteArray(byteLength) { getInt8(it).toByte() }

private fun ByteArray.toUint8Array(): JsAny {
    val array = newUint8Array(size)
    forEachIndexed { index, byte -> uint8ArraySet(array, index, byte.toInt() and 0xFF) }
    return array
}

private val cachedValues = mutableMapOf<String, ByteArray>()
private val notificationHandlers = mutableMapOf<String, (String, String) -> Unit>()

// The `gattserverdisconnected` listener is registered once per device; it dispatches to the latest handler.
private val disconnectListenerAdded = mutableSetOf<String>()
private val onDisconnectedHandlers = mutableMapOf<String, () -> Unit>()

private fun characteristicKey(identifier: String, service: String, characteristic: String) = "$identifier|$service|$characteristic"

// Drops the connection-scoped state for a device (its discovered handles, cached values and notification
// handler) on disconnect; the device handle itself is kept so it can be reconnected without re-picking.
private fun clearConnectionState(identifier: String) {
    cachedValues.keys.removeAll { it.startsWith("$identifier|") }
    notificationHandlers.remove(identifier)
    jsClearConnectionState(identifier)
}

private fun ensureRegistry() {
    js(
        """
        if (!globalThis.__kbt) {
            globalThis.__kbt = {
                devices: {}, chars: {}, descs: {},
                props: function (p) {
                    var v = 0;
                    if (p.broadcast) v |= 1;
                    if (p.read) v |= 2;
                    if (p.writeWithoutResponse) v |= 4;
                    if (p.write) v |= 8;
                    if (p.notify) v |= 16;
                    if (p.indicate) v |= 32;
                    if (p.authenticatedSignedWrites) v |= 64;
                    return v;
                }
            };
        }
        """,
    )
}

internal actual fun webBluetoothSupported(): Boolean = js("(typeof navigator !== 'undefined' && !!navigator.bluetooth)")

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
    ensureRegistry()
    webHideDevicePicker()
    jsShowDevicePicker(
        filterServices.joinToString(","),
        optionalServices.joinToString(","),
        title,
        addButtonLabel,
        emptyLabel,
        cssClassPrefix,
        containerId ?: "",
        onDevicePicked,
    )
}

internal actual fun webHideDevicePicker() {
    ensureRegistry()
    jsHideDevicePicker()
}

internal actual suspend fun webGattConnect(identifier: String, onDisconnected: () -> Unit): Boolean {
    ensureRegistry()
    onDisconnectedHandlers[identifier] = onDisconnected
    if (disconnectListenerAdded.add(identifier)) {
        jsAddDisconnectListener(identifier) {
            clearConnectionState(identifier)
            onDisconnectedHandlers[identifier]?.invoke()
        }
    }
    return suspendCancellableCoroutine { continuation ->
        jsGattConnect(identifier, { continuation.resume(true) }, { continuation.resume(false) })
    }
}

internal actual fun webGattDisconnect(identifier: String) {
    ensureRegistry()
    jsGattDisconnect(identifier)
}

internal actual fun webForgetDevice(identifier: String) {
    clearConnectionState(identifier)
    disconnectListenerAdded.remove(identifier)
    onDisconnectedHandlers.remove(identifier)
    jsForgetDevice(identifier)
}

internal actual fun webIsConnected(identifier: String): Boolean {
    ensureRegistry()
    return jsIsConnected(identifier)
}

internal actual suspend fun webDiscoverServices(identifier: String): List<WebService> {
    ensureRegistry()
    return suspendCancellableCoroutine { continuation ->
        val services = LinkedHashMap<String, ServiceAccumulator>()
        jsDiscoverServices(
            identifier,
            { serviceUuid, isPrimary, characteristicUuid, properties, descriptorCsv ->
                val accumulator = services.getOrPut(serviceUuid) { ServiceAccumulator(isPrimary) }
                val descriptors = if (descriptorCsv.isEmpty()) emptyList() else descriptorCsv.split(",")
                accumulator.characteristics.add(WebCharacteristic(characteristicUuid, properties, descriptors))
            },
            { continuation.resume(services.map { (uuid, accumulator) -> WebService(uuid, accumulator.isPrimary, accumulator.characteristics) }) },
            { continuation.resume(emptyList()) },
        )
    }
}

internal actual suspend fun webReadCharacteristic(identifier: String, service: String, characteristic: String): WebGattResult {
    ensureRegistry()
    return suspendCancellableCoroutine { continuation ->
        jsReadCharacteristic(
            identifier,
            service,
            characteristic,
            { view ->
                val bytes = view.toByteArray()
                cachedValues[characteristicKey(identifier, service, characteristic)] = bytes
                continuation.resume(WebGattResult.Success(bytes))
            },
            { errorName -> continuation.resume(WebGattResult.Failure(errorName)) },
        )
    }
}

internal actual suspend fun webWriteCharacteristic(identifier: String, service: String, characteristic: String, value: ByteArray, withResponse: Boolean): WebGattResult {
    ensureRegistry()
    return suspendCancellableCoroutine { continuation ->
        jsWriteCharacteristic(
            identifier,
            service,
            characteristic,
            value.toUint8Array(),
            withResponse,
            { continuation.resume(WebGattResult.Success(null)) },
            { errorName -> continuation.resume(WebGattResult.Failure(errorName)) },
        )
    }
}

internal actual suspend fun webReadDescriptor(identifier: String, service: String, characteristic: String, descriptor: String): WebGattResult {
    ensureRegistry()
    return suspendCancellableCoroutine { continuation ->
        jsReadDescriptor(
            identifier,
            service,
            characteristic,
            descriptor,
            { view -> continuation.resume(WebGattResult.Success(view.toByteArray())) },
            { errorName -> continuation.resume(WebGattResult.Failure(errorName)) },
        )
    }
}

internal actual suspend fun webWriteDescriptor(identifier: String, service: String, characteristic: String, descriptor: String, value: ByteArray): WebGattResult {
    ensureRegistry()
    return suspendCancellableCoroutine { continuation ->
        jsWriteDescriptor(
            identifier,
            service,
            characteristic,
            descriptor,
            value.toUint8Array(),
            { continuation.resume(WebGattResult.Success(null)) },
            { errorName -> continuation.resume(WebGattResult.Failure(errorName)) },
        )
    }
}

internal actual suspend fun webSetNotifying(identifier: String, service: String, characteristic: String, enable: Boolean): WebGattResult {
    ensureRegistry()
    return suspendCancellableCoroutine { continuation ->
        if (enable) {
            jsStartNotifications(
                identifier,
                service,
                characteristic,
                { view ->
                    cachedValues[characteristicKey(identifier, service, characteristic)] = view.toByteArray()
                    notificationHandlers[identifier]?.invoke(service, characteristic)
                },
                { continuation.resume(WebGattResult.Success(null)) },
                { errorName -> continuation.resume(WebGattResult.Failure(errorName)) },
            )
        } else {
            jsStopNotifications(
                identifier,
                service,
                characteristic,
                { continuation.resume(WebGattResult.Success(null)) },
                { errorName -> continuation.resume(WebGattResult.Failure(errorName)) },
            )
        }
    }
}

internal actual fun webSetNotificationHandler(identifier: String, handler: (service: String, characteristic: String) -> Unit) {
    notificationHandlers[identifier] = handler
}

internal actual fun webCachedCharacteristicValue(identifier: String, service: String, characteristic: String): ByteArray? =
    cachedValues[characteristicKey(identifier, service, characteristic)]

private class ServiceAccumulator(val isPrimary: Boolean) {
    val characteristics = mutableListOf<WebCharacteristic>()
}

private fun jsShowDevicePicker(
    filterCsv: String,
    optionalCsv: String,
    title: String,
    addLabel: String,
    emptyLabel: String,
    prefix: String,
    containerId: String,
    onPicked: (identifier: String, name: String) -> Unit,
) {
    js(
        """
        if (typeof document === 'undefined') return;
        var reg = globalThis.__kbt;
        var overlay = document.createElement('div');
        overlay.className = prefix + '-overlay';
        // A self-contained dismiss control so the overlay can always be closed even if it covers app UI.
        var closeButton = document.createElement('button');
        closeButton.className = prefix + '-close';
        closeButton.textContent = '✕';
        closeButton.addEventListener('click', function () {
            if (reg.overlay && reg.overlay.parentNode) reg.overlay.parentNode.removeChild(reg.overlay);
            reg.overlay = null;
        });
        overlay.appendChild(closeButton);
        var heading = document.createElement('h2');
        heading.className = prefix + '-title';
        heading.textContent = title;
        overlay.appendChild(heading);
        var list = document.createElement('ul');
        list.className = prefix + '-list';
        var emptyItem = document.createElement('li');
        emptyItem.className = prefix + '-list-empty';
        emptyItem.textContent = emptyLabel;
        list.appendChild(emptyItem);
        overlay.appendChild(list);
        var added = {};
        var button = document.createElement('button');
        button.className = prefix + '-button';
        button.textContent = addLabel;
        button.addEventListener('click', function () {
            // Called synchronously from the click handler so the requestDevice user-gesture requirement holds.
            var options = {};
            if (filterCsv.length === 0) { options.acceptAllDevices = true; } else { options.filters = [{ services: filterCsv.split(',') }]; }
            if (optionalCsv.length > 0) options.optionalServices = optionalCsv.split(',');
            navigator.bluetooth.requestDevice(options).then(function (device) {
                reg.devices[device.id] = device;
                var name = device.name || '';
                if (!added[device.id]) {
                    added[device.id] = true;
                    if (emptyItem.parentNode) list.removeChild(emptyItem);
                    var item = document.createElement('li');
                    item.className = prefix + '-list-item';
                    item.textContent = name.length ? name : device.id;
                    list.appendChild(item);
                }
                onPicked(device.id, name);
            }, function () {});
        });
        overlay.appendChild(button);
        var container = (containerId && document.getElementById(containerId)) || document.body;
        container.appendChild(overlay);
        reg.overlay = overlay;
        """,
    )
}

private fun jsHideDevicePicker() {
    js(
        """
        var reg = globalThis.__kbt;
        if (reg && reg.overlay) {
            if (reg.overlay.parentNode) reg.overlay.parentNode.removeChild(reg.overlay);
            reg.overlay = null;
        }
        """,
    )
}

private fun jsGattConnect(identifier: String, onConnected: () -> Unit, onError: () -> Unit) {
    js(
        """
        var device = globalThis.__kbt.devices[identifier];
        if (!device) { onError(); return; }
        device.gatt.connect().then(function () { onConnected(); }, function () { onError(); });
        """,
    )
}

private fun jsAddDisconnectListener(identifier: String, onDisconnected: () -> Unit) {
    js(
        """
        var device = globalThis.__kbt.devices[identifier];
        if (!device) return;
        device.addEventListener('gattserverdisconnected', function () { onDisconnected(); });
        """,
    )
}

private fun jsGattDisconnect(identifier: String) {
    js("var d = globalThis.__kbt.devices[identifier]; if (d && d.gatt && d.gatt.connected) d.gatt.disconnect();")
}

private fun jsForgetDevice(identifier: String) {
    js("var reg = globalThis.__kbt; if (reg) delete reg.devices[identifier];")
}

private fun jsClearConnectionState(identifier: String) {
    js(
        """
        var reg = globalThis.__kbt;
        if (!reg) return;
        var prefix = identifier + '|';
        ['chars', 'descs'].forEach(function (map) {
            Object.keys(reg[map]).forEach(function (key) { if (key.indexOf(prefix) === 0) delete reg[map][key]; });
        });
        """,
    )
}

private fun jsIsConnected(identifier: String): Boolean = js("(function () { var d = globalThis.__kbt.devices[identifier]; return !!(d && d.gatt && d.gatt.connected); })()")

private fun jsDiscoverServices(
    identifier: String,
    onCharacteristic: (service: String, isPrimary: Boolean, characteristic: String, properties: Int, descriptorCsv: String) -> Unit,
    onDone: () -> Unit,
    onError: () -> Unit,
) {
    js(
        """
        var reg = globalThis.__kbt;
        var device = reg.devices[identifier];
        if (!device) { onError(); return; }
        device.gatt.getPrimaryServices().then(function (services) {
            return services.reduce(function (servicePromise, service) {
                return servicePromise.then(function () {
                    return service.getCharacteristics().then(function (characteristics) {
                        return characteristics.reduce(function (characteristicPromise, characteristic) {
                            return characteristicPromise.then(function () {
                                reg.chars[identifier + '|' + service.uuid + '|' + characteristic.uuid] = characteristic;
                                return characteristic.getDescriptors().then(function (d) { return d; }, function () { return []; }).then(function (descriptors) {
                                    var uuids = descriptors.map(function (descriptor) {
                                        reg.descs[identifier + '|' + service.uuid + '|' + characteristic.uuid + '|' + descriptor.uuid] = descriptor;
                                        return descriptor.uuid;
                                    });
                                    onCharacteristic(service.uuid, service.isPrimary !== false, characteristic.uuid, reg.props(characteristic.properties), uuids.join(','));
                                });
                            });
                        }, Promise.resolve());
                    });
                });
            }, Promise.resolve());
        }).then(function () { onDone(); }, function () { onError(); });
        """,
    )
}

private fun jsReadCharacteristic(identifier: String, service: String, characteristic: String, onResult: (JsDataView) -> Unit, onError: (errorName: String) -> Unit) {
    js(
        """
        var c = globalThis.__kbt.chars[identifier + '|' + service + '|' + characteristic];
        if (!c) { onError(''); return; }
        c.readValue().then(function (view) { onResult(view); }, function (e) { onError(e && e.name ? e.name : ''); });
        """,
    )
}

private fun jsWriteCharacteristic(
    identifier: String,
    service: String,
    characteristic: String,
    buffer: JsAny,
    withResponse: Boolean,
    onResult: () -> Unit,
    onError: (errorName: String) -> Unit,
) {
    js(
        """
        var c = globalThis.__kbt.chars[identifier + '|' + service + '|' + characteristic];
        if (!c) { onError(''); return; }
        var promise = withResponse ? c.writeValueWithResponse(buffer) : c.writeValueWithoutResponse(buffer);
        promise.then(function () { onResult(); }, function (e) { onError(e && e.name ? e.name : ''); });
        """,
    )
}

private fun jsReadDescriptor(
    identifier: String,
    service: String,
    characteristic: String,
    descriptor: String,
    onResult: (JsDataView) -> Unit,
    onError: (errorName: String) -> Unit,
) {
    js(
        """
        var d = globalThis.__kbt.descs[identifier + '|' + service + '|' + characteristic + '|' + descriptor];
        if (!d) { onError(''); return; }
        d.readValue().then(function (view) { onResult(view); }, function (e) { onError(e && e.name ? e.name : ''); });
        """,
    )
}

private fun jsWriteDescriptor(
    identifier: String,
    service: String,
    characteristic: String,
    descriptor: String,
    buffer: JsAny,
    onResult: () -> Unit,
    onError: (errorName: String) -> Unit,
) {
    js(
        """
        var d = globalThis.__kbt.descs[identifier + '|' + service + '|' + characteristic + '|' + descriptor];
        if (!d) { onError(''); return; }
        d.writeValue(buffer).then(function () { onResult(); }, function (e) { onError(e && e.name ? e.name : ''); });
        """,
    )
}

private fun jsStartNotifications(
    identifier: String,
    service: String,
    characteristic: String,
    onValue: (JsDataView) -> Unit,
    onResult: () -> Unit,
    onError: (errorName: String) -> Unit,
) {
    js(
        """
        var c = globalThis.__kbt.chars[identifier + '|' + service + '|' + characteristic];
        if (!c) { onError(''); return; }
        c.addEventListener('characteristicvaluechanged', function (event) { onValue(event.target.value); });
        c.startNotifications().then(function () { onResult(); }, function (e) { onError(e && e.name ? e.name : ''); });
        """,
    )
}

private fun jsStopNotifications(identifier: String, service: String, characteristic: String, onResult: () -> Unit, onError: (errorName: String) -> Unit) {
    js(
        """
        var c = globalThis.__kbt.chars[identifier + '|' + service + '|' + characteristic];
        if (!c) { onError(''); return; }
        c.stopNotifications().then(function () { onResult(); }, function (e) { onError(e && e.name ? e.name : ''); });
        """,
    )
}
