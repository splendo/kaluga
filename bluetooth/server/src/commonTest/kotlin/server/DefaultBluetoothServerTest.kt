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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.test.randomUUID
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.test.MockPermissionsBuilder
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultBluetoothServerTest : BaseTest() {

    private fun settings() = ServerSettings(Permissions(MockPermissionsBuilder()))

    /**
     * Runs [block] against a [DefaultBluetoothServer] that starts at [MockAwaitingPermissions] and immediately becomes [MockAvailable].
     * The server runs under a dedicated [Job] that is cancelled afterwards so the test scope does not leak coroutines.
     */
    private fun testServer(block: suspend (server: BluetoothServer, available: MockAvailable) -> Unit) = testRunBlocking {
        val available = MockAvailable()
        val job = Job()
        val server = DefaultBluetoothServer(settings(), MockAwaitingPermissions { available }, coroutineContext + job)
        try {
            withTimeout(TIMEOUT) {
                server.status.first { it == ServerStatus.AVAILABLE }
                block(server, available)
            }
        } finally {
            server.close()
            job.cancelAndJoin()
        }
    }

    @Test
    fun testReachesAvailable() = testServer { server, _ ->
        assertEquals(ServerStatus.AVAILABLE, server.status.value)
    }

    @Test
    fun testAdvertiseLifecycle() = testServer { server, available ->
        assertFalse(server.isAdvertising.value)
        assertTrue(server.advertise { localName = "Test" })
        server.isAdvertising.first { it }
        available.startAdvertisingMock.verify()

        server.stopAdvertising()
        server.isAdvertising.first { !it }
    }

    @Test
    fun testBluetoothDisabledTransition() = testRunBlocking {
        val available = MockAvailable()
        val reEnabled = MockAvailable()
        val enabledTrigger = CompletableDeferred<ServerState.Available>()
        val job = Job()
        val server = DefaultBluetoothServer(settings(), MockAwaitingPermissions { available }, coroutineContext + job)
        try {
            withTimeout(TIMEOUT) {
                server.status.first { it == ServerStatus.AVAILABLE }
                // Trigger Bluetooth being disabled; awaitEnabled is gated on enabledTrigger
                available.onAwaitDisabled.complete(MockAwaitingBluetoothEnabled { enabledTrigger.await() })
                server.status.first { it == ServerStatus.AWAITING_BLUETOOTH_ENABLED }
                // Re-enable Bluetooth
                enabledTrigger.complete(reEnabled)
                server.status.first { it == ServerStatus.AVAILABLE }
            }
        } finally {
            server.close()
            job.cancelAndJoin()
        }
    }

    @Test
    fun testPermissionsRevokedTransition() = testRunBlocking {
        val available = MockAvailable()
        val rePermitted = MockAvailable()
        val permittedTrigger = CompletableDeferred<ServerState.HasPermissions>()
        val job = Job()
        val server = DefaultBluetoothServer(settings(), MockAwaitingPermissions { available }, coroutineContext + job)
        try {
            withTimeout(TIMEOUT) {
                server.status.first { it == ServerStatus.AVAILABLE }
                // Trigger permissions being revoked; the resulting AwaitingPermissions is gated on permittedTrigger
                available.onAwaitRevoked.complete(MockAwaitingPermissions { permittedTrigger.await() })
                server.status.first { it == ServerStatus.AWAITING_PERMISSIONS }
                permittedTrigger.complete(rePermitted)
                server.status.first { it == ServerStatus.AVAILABLE }
            }
        } finally {
            server.close()
            job.cancelAndJoin()
        }
    }

    @Test
    fun testCloseReportsClosedAndFailsActions() = testRunBlocking {
        val available = MockAvailable()
        val job = Job()
        val server = DefaultBluetoothServer(settings(), MockAwaitingPermissions { available }, coroutineContext + job)
        try {
            withTimeout(TIMEOUT) {
                server.status.first { it == ServerStatus.AVAILABLE }
                server.close()
                server.status.first { it == ServerStatus.CLOSED }
                assertFalse(server.advertise { })
                assertEquals(null, server.add(randomUUID()) { })
            }
        } finally {
            job.cancelAndJoin()
        }
    }

    companion object {
        private const val TIMEOUT = 10_000L
    }
}
