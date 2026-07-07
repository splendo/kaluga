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

package com.splendo.kaluga.bluetooth.test

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.parameters.mock
import com.splendo.kaluga.bluetooth.BaseBluetoothClientBuilder
import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.permissions.base.Permissions
import kotlin.coroutines.CoroutineContext

/**
 * A mock implementation of [BaseBluetoothClientBuilder] that creates [MockBluetoothClient]s.
 * @param setupMocks if `true` the mocks are configured with sensible default behaviour
 */
class MockBluetoothClientBuilder(setupMocks: Boolean = true) : BaseBluetoothClientBuilder {

    /**
     * A list of all [MockBluetoothClient]s created by this builder
     */
    val createdClients = concurrentMutableListOf<MockBluetoothClient>()

    /**
     * Mock for [createClient]
     */
    val createClientMock = ::createClient.mock()

    init {
        if (setupMocks) {
            createClientMock.on().doExecute {
                MockBluetoothClient().also { createdClients.add(it) }
            }
        }
    }

    override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): BluetoothClient =
        createClientMock.call(scannerSettingsBuilder, coroutineContext)
}
