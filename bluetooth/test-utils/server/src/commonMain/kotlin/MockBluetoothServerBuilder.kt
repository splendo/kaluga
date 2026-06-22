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

package com.splendo.kaluga.bluetooth.test.server

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.parameters.mock
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.AdvertiseData
import com.splendo.kaluga.bluetooth.server.BaseBluetoothServerBuilder
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.bluetooth.server.LocalService
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.permissions.base.Permissions
import kotlin.coroutines.CoroutineContext

/**
 * A mock implementation of [BaseBluetoothServerBuilder] that creates behavioral [MockBluetoothServer]s.
 *
 * [createServer] applies the requested [BluetoothServerDSL] to a fresh [MockBluetoothServer] (its `service { }` calls
 * route to [BluetoothServer.add] and `advertise { }` to [BluetoothServer.advertise]), so the server's [BluetoothServer.services]
 * gets populated and the generated `SharedDeviceServer.sharedService` lazily resolves. A test can then obtain the created
 * instance via [createdServers] and drive reads/writes/subscriptions through [MockBluetoothServer.triggerRead],
 * [MockBluetoothServer.triggerWrite] and [MockBluetoothServer.subscribe].
 * @param setupMocks if `true` the mocks are configured with sensible default behaviour
 */
class MockBluetoothServerBuilder(setupMocks: Boolean = true) : BaseBluetoothServerBuilder {

    /**
     * A list of all [MockBluetoothServer]s created by this builder, in creation order.
     */
    val createdServers = concurrentMutableListOf<MockBluetoothServer>()

    /**
     * Mock for [createServer]
     */
    val createServerMock = ::createServer.mock()

    init {
        if (setupMocks) {
            createServerMock.on().doExecuteSuspended { (_, _, specs) ->
                val server = MockBluetoothServer()
                createdServers.add(server)
                DSL(server).apply(specs).build()
                server
            }
        }
    }

    override suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings,
        coroutineContext: CoroutineContext,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer = createServerMock.call(settingsBuilder, coroutineContext, specs)

    /**
     * Applies a [BluetoothServerDSL] to a [MockBluetoothServer]: `service { }` maps to [BluetoothServer.add] and
     * `advertise { }` to [BluetoothServer.advertise].
     */
    private class DSL(private val server: BluetoothServer) : BluetoothServerDSL {
        private var advertisement: (AdvertiseData.Builder.() -> Unit)? = null
        private val services = mutableListOf<Pair<UUID, LocalService.DSL.Primary.() -> Unit>>()

        override fun advertise(data: AdvertiseData.Builder.() -> Unit) {
            advertisement = data
        }

        override fun service(uuid: UUID, service: LocalService.DSL.Primary.() -> Unit) {
            services.add(uuid to service)
        }

        suspend fun build() {
            for ((uuid, service) in services) {
                server.add(uuid, service)
            }
            advertisement?.let { server.advertise(it) }
        }
    }
}
