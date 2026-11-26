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
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

// A device in the interop registry whose requests stay pending until the test rejects them, and whose
// `gattserverdisconnected` listener the test fires, in the order Chrome was seen to use.
private fun seedDevice(identifier: String): Unit = js(
    """{
    var test = { reject: null, listener: null };
    globalThis.__testGrace = globalThis.__testGrace || {};
    globalThis.__testGrace[identifier] = test;
    var pending = function () { return new Promise(function (resolve, reject) { test.reject = reject; }); };
    var characteristic = {
        uuid: '6b7a0001-5d1c-4f0e-9a51-2f6f3c1d8e01',
        properties: { read: true, write: true, notify: true },
        getDescriptors: function () { return Promise.resolve([]); },
        readValue: pending,
        startNotifications: pending,
        addEventListener: function () {},
        removeEventListener: function () {}
    };
    var service = {
        uuid: '6b7a0000-5d1c-4f0e-9a51-2f6f3c1d8e01',
        isPrimary: true,
        getCharacteristics: function () { return Promise.resolve([characteristic]); }
    };
    globalThis.__kbt.devices[identifier] = {
        id: identifier,
        gatt: {
            connected: false,
            connect: function () { this.connected = true; return Promise.resolve(this); },
            disconnect: function () { this.connected = false; test.listener(); },
            getPrimaryServices: function () { return Promise.resolve([service]); }
        },
        addEventListener: function (name, listener) { test.listener = listener; }
    };
    }""",
)

private fun isRequestPending(identifier: String): Boolean = js("globalThis.__testGrace[identifier].reject !== null")

private fun rejectRequestAsNotSupported(identifier: String): Unit = js(
    "{ var test = globalThis.__testGrace[identifier]; var reject = test.reject; test.reject = null; reject({ name: 'NotSupportedError' }); }",
)

private fun dropLink(identifier: String): Unit = js(
    "{ globalThis.__kbt.devices[identifier].gatt.connected = false; globalThis.__testGrace[identifier].listener(); }",
)

class NotSupportedDisconnectGraceTest {

    private class Connection(val identifier: String, val manager: DefaultDeviceConnectionManager, val scope: CoroutineScope) {
        val events = Channel<DeviceConnectionManager.Event>(Channel.UNLIMITED)
        lateinit var characteristic: RemoteCharacteristic

        suspend fun <R : GattResponse> start(action: DeviceAction<R>): DeviceAction<R> {
            scope.launch { manager.performAction(action) }
            while (!isRequestPending(identifier)) delay(1)
            return action
        }

        suspend fun answer(action: DeviceAction<*>): GattResponse? = withTimeoutOrNull(1.seconds) { action.response.await() }

        // Long enough for the rejection to reach the connection manager, well inside the grace of the tests that drop the link.
        suspend fun letRejectionArrive() = delay(50.milliseconds)
    }

    private suspend fun connect(identifier: String, grace: Duration): Connection {
        webIsConnected(identifier)
        seedDevice(identifier)
        val scope = CoroutineScope(Job())
        val connection = Connection(identifier, DefaultDeviceConnectionManager(WebDeviceWrapper(identifier, null), ConnectionSettings(), scope, grace), scope)
        scope.launch { connection.manager.events.collect { connection.events.send(it) } }
        connection.manager.connect()
        assertIs<DeviceConnectionManager.Event.Connected>(connection.events.receive())
        connection.manager.discoverServices()
        connection.characteristic = assertIs<DeviceConnectionManager.Event.DiscoveredServices>(connection.events.receive()).services.single().characteristics.single()
        return connection
    }

    private fun testConnection(identifier: String, grace: Duration, block: suspend Connection.() -> Unit) = testRunBlocking {
        val connection = connect(identifier, grace)
        try {
            connection.block()
        } finally {
            connection.scope.cancel()
        }
    }

    @Test
    fun readAnswersDeviceUnavailableWhenTheLinkDropsWithinTheGrace() = testConnection("grace-read-drop", 5.seconds) {
        val read = start(DeviceAction.Read.Characteristic(characteristic))
        rejectRequestAsNotSupported(identifier)
        letRejectionArrive()
        dropLink(identifier)

        // Answered without a completion event, so the disconnect answers the actions queued behind it.
        assertIs<DeviceConnectionManager.Event.Disconnected>(events.receive())
        assertEquals(GattResponse.DeviceUnavailable(GattResponse.RequestNotSupported), answer(read))
    }

    @Test
    fun notificationAnswersDeviceUnavailableWhenTheLinkDropsWithinTheGrace() = testConnection("grace-notify-drop", 5.seconds) {
        val enable = start(DeviceAction.Notification.Enable(characteristic))
        rejectRequestAsNotSupported(identifier)
        letRejectionArrive()
        dropLink(identifier)

        assertIs<DeviceConnectionManager.Event.Disconnected>(events.receive())
        assertEquals(GattResponse.DeviceUnavailable(GattResponse.RequestNotSupported), answer(enable))
    }

    @Test
    fun readAnswersRequestNotSupportedAfterTheGraceWithoutDisconnect() = testConnection("grace-read-stays", 300.milliseconds) {
        val read = start(DeviceAction.Read.Characteristic(characteristic))
        val rejected = TimeSource.Monotonic.markNow()
        rejectRequestAsNotSupported(identifier)

        val completed = assertIs<DeviceConnectionManager.Event.CompletedAction<*>>(events.receive())
        val elapsed = rejected.elapsedNow()
        assertTrue(elapsed >= 300.milliseconds, "answered after $elapsed")
        assertEquals(read, completed.action)
        assertEquals(GattResponse.RequestNotSupported, completed.response)
    }

    @Test
    fun readAnswersRequestNotSupportedAtOnceWithoutGrace() = testConnection("grace-read-off", Duration.ZERO) {
        val read = start(DeviceAction.Read.Characteristic(characteristic))
        rejectRequestAsNotSupported(identifier)
        letRejectionArrive()
        dropLink(identifier)

        val completed = assertIs<DeviceConnectionManager.Event.CompletedAction<*>>(events.receive())
        assertEquals(read, completed.action)
        assertEquals(GattResponse.RequestNotSupported, completed.response)
        assertIs<DeviceConnectionManager.Event.Disconnected>(events.receive())
    }

    @Test
    fun requestedDisconnectWithinTheGraceAnswersRequestNotSupported() = testConnection("grace-read-requested", 5.seconds) {
        val read = start(DeviceAction.Read.Characteristic(characteristic))
        rejectRequestAsNotSupported(identifier)
        letRejectionArrive()
        manager.disconnect()

        assertEquals(GattResponse.RequestNotSupported, answer(read))
    }
}
