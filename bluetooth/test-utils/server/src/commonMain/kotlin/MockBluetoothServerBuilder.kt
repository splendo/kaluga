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
import com.splendo.kaluga.bluetooth.server.BaseBluetoothServerBuilder
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.permissions.base.Permissions
import kotlin.coroutines.CoroutineContext

/**
 * A mock implementation of [BaseBluetoothServerBuilder] that creates [MockBluetoothServer]s.
 * @param setupMocks if `true` the mocks are configured with sensible default behaviour
 */
class MockBluetoothServerBuilder(setupMocks: Boolean = true) : BaseBluetoothServerBuilder {

    /**
     * A list of all [MockBluetoothServer]s created by this builder
     */
    val createdServers = concurrentMutableListOf<MockBluetoothServer>()

    /**
     * Mock for [createServer]
     */
    val createServerMock = ::createServer.mock()

    init {
        if (setupMocks) {
            createServerMock.on().doExecuteSuspended {
                MockBluetoothServer().also { createdServers.add(it) }
            }
        }
    }

    override suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings,
        coroutineContext: CoroutineContext,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer = createServerMock.call(settingsBuilder, coroutineContext, specs)
}
