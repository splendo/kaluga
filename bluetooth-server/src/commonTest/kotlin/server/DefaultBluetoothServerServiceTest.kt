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

import com.splendo.kaluga.test.base.testRunBlocking
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.bluetooth.randomUUID
import com.splendo.kaluga.test.bluetooth.server.MockConnectedDevice
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Orchestration tests for [DefaultBluetoothServer] that drive [LocalService] graphs through the
 * service/notification channels and the [MockAvailable] state. Builds attributes with mock wrappers,
 * so it runs on every platform without a live Bluetooth stack.
 */
class DefaultBluetoothServerServiceTest : BaseTest() {

    private fun settings() = ServerSettings(Permissions(MockPermissionsBuilder()))

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
    fun testAddServiceRoutesToStateAndPublishes() = testServer { server, available ->
        val uuid = randomUUID()
        val service = server.add(uuid) {
            characteristic(randomUUID()) {
                readableAlwaysSuccess { _, _ -> byteArrayOf() }
            }
        }
        assertNotNull(service)
        assertEquals(uuid, service.uuid)
        available.addServiceMock.verify()
        server.services.first { it.contains(service) }
    }

    @Test
    fun testRemoveService() = testServer { server, available ->
        val service = assertNotNull(
            server.add(randomUUID()) {
                characteristic(randomUUID()) { readableAlwaysSuccess { _, _ -> byteArrayOf() } }
            },
        )
        server.services.first { it.contains(service) }

        assertTrue(server.remove(service))
        available.removeServiceMock.verify()
        server.services.first { it.isEmpty() }
    }

    @Test
    fun testNotifyRoutesToExecute() = testServer { server, available ->
        val service = assertNotNull(
            server.add(randomUUID()) {
                characteristic(randomUUID()) {
                    notifiable(onSubscribe = { }, onUnsubscribe = { })
                }
            },
        )
        val characteristic = assertIs<LocalCharacteristic.Notifiable>(service.characteristics.single())
        val device = MockConnectedDevice()
        characteristic.subscribe(device)

        assertTrue(characteristic.notify(device, byteArrayOf(1, 2)))
        available.executeMock.verify()
    }

    companion object {
        private val TIMEOUT = 10.seconds
    }
}
