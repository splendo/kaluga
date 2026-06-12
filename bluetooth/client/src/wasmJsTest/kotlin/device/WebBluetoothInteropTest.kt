/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.test.testRunBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

// Node has no Web Bluetooth, but the interop only reaches the browser API through the handles in
// the `globalThis.__kbt` registry — so planting fake characteristics/descriptors there lets the
// real suspend bridging, callbacks and byte conversions run end-to-end without a browser.

private fun seedReadCharacteristic(key: String): Unit = js(
    """{
    globalThis.__kbt.chars[key] = {
        readValue: function () { return Promise.resolve(new DataView(new Uint8Array([1, 2, 250, 255]).buffer)); }
    };
    }""",
)

private fun seedFailingCharacteristic(key: String): Unit = js(
    """{
    globalThis.__kbt.chars[key] = {
        readValue: function () { return Promise.reject({ name: 'NotSupportedError' }); }
    };
    }""",
)

private fun seedWritableCharacteristic(key: String): Unit = js(
    """{
    globalThis.__testWrites = {};
    globalThis.__kbt.chars[key] = {
        writeValueWithResponse: function (b) { globalThis.__testWrites.withResponse = b; return Promise.resolve(); },
        writeValueWithoutResponse: function (b) { globalThis.__testWrites.withoutResponse = b; return Promise.resolve(); }
    };
    }""",
)

private fun writtenLength(mode: String): Int = js("globalThis.__testWrites[mode].length")
private fun writtenByte(mode: String, index: Int): Int = js("globalThis.__testWrites[mode][index]")

private fun seedDescriptor(key: String): Unit = js(
    """{
    globalThis.__testDescriptorWrite = null;
    globalThis.__kbt.descs[key] = {
        readValue: function () { return Promise.resolve(new DataView(new Uint8Array([42, 128]).buffer)); },
        writeValue: function (b) { globalThis.__testDescriptorWrite = b; return Promise.resolve(); }
    };
    }""",
)

private fun descriptorWrittenLength(): Int = js("globalThis.__testDescriptorWrite.length")
private fun descriptorWrittenByte(index: Int): Int = js("globalThis.__testDescriptorWrite[index]")

private fun seedNotifyingCharacteristic(key: String): Unit = js(
    """{
    globalThis.__kbt.chars[key] = {
        addEventListener: function (name, listener) { globalThis.__testNotifyListener = listener; },
        startNotifications: function () { return Promise.resolve(); },
        stopNotifications: function () { return Promise.resolve(); }
    };
    }""",
)

private fun fireNotification(): Unit = js("globalThis.__testNotifyListener({ target: { value: new DataView(new Uint8Array([9, 8, 7]).buffer) } })")

class WebBluetoothInteropTest {

    private fun registryKey(identifier: String, service: String, characteristic: String): String {
        // Triggers the production ensureRegistry() before fakes are planted.
        webIsConnected(identifier)
        return "$identifier|$service|$characteristic"
    }

    @Test
    fun readCharacteristicConvertsDataViewAndCaches() = testRunBlocking {
        val key = registryKey("dev1", "svc", "char")
        seedReadCharacteristic(key)

        val result = webReadCharacteristic("dev1", "svc", "char")

        val success = assertIs<WebGattResult.Success>(result)
        assertContentEquals(byteArrayOf(1, 2, 250.toByte(), 255.toByte()), success.value)
        assertContentEquals(byteArrayOf(1, 2, 250.toByte(), 255.toByte()), webCachedCharacteristicValue("dev1", "svc", "char"))
    }

    @Test
    fun readCharacteristicFailureReportsErrorName() = testRunBlocking {
        val key = registryKey("dev2", "svc", "char")
        seedFailingCharacteristic(key)

        val result = webReadCharacteristic("dev2", "svc", "char")

        assertEquals("NotSupportedError", assertIs<WebGattResult.Failure>(result).errorName)
    }

    @Test
    fun readCharacteristicWithoutHandleFails() = testRunBlocking {
        registryKey("dev3", "svc", "char")

        assertIs<WebGattResult.Failure>(webReadCharacteristic("dev3", "svc", "unknown"))
    }

    @Test
    fun writeCharacteristicMarshalsBytesBothModes() = testRunBlocking {
        val key = registryKey("dev4", "svc", "char")
        seedWritableCharacteristic(key)
        val payload = byteArrayOf(7, 0, 200.toByte(), 127, 128.toByte())

        assertIs<WebGattResult.Success>(webWriteCharacteristic("dev4", "svc", "char", payload, withResponse = true))
        assertIs<WebGattResult.Success>(webWriteCharacteristic("dev4", "svc", "char", payload, withResponse = false))

        for (mode in listOf("withResponse", "withoutResponse")) {
            assertEquals(payload.size, writtenLength(mode))
            assertContentEquals(payload, ByteArray(payload.size) { writtenByte(mode, it).toByte() })
        }
    }

    @Test
    fun descriptorRoundTrip() = testRunBlocking {
        val key = registryKey("dev5", "svc", "char") + "|desc"
        seedDescriptor(key)
        val payload = byteArrayOf(1, 255.toByte())

        val read = webReadDescriptor("dev5", "svc", "char", "desc")
        assertContentEquals(byteArrayOf(42, 128.toByte()), assertIs<WebGattResult.Success>(read).value)

        assertIs<WebGattResult.Success>(webWriteDescriptor("dev5", "svc", "char", "desc", payload))
        assertEquals(payload.size, descriptorWrittenLength())
        assertContentEquals(payload, ByteArray(payload.size) { descriptorWrittenByte(it).toByte() })
    }

    @Test
    fun notificationsDeliverConvertedBytesToHandler() = testRunBlocking {
        val key = registryKey("dev6", "svc", "char")
        seedNotifyingCharacteristic(key)
        var notified: Pair<String, String>? = null
        webSetNotificationHandler("dev6") { service, characteristic -> notified = service to characteristic }

        assertIs<WebGattResult.Success>(webSetNotifying("dev6", "svc", "char", enable = true))
        assertNull(webCachedCharacteristicValue("dev6", "svc", "char"))

        fireNotification()

        assertEquals("svc" to "char", notified)
        assertContentEquals(byteArrayOf(9, 8, 7), webCachedCharacteristicValue("dev6", "svc", "char"))
        assertIs<WebGattResult.Success>(webSetNotifying("dev6", "svc", "char", enable = false))
    }
}
