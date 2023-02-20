/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.test.system.network

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.NetworkManager
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Mock implementation of [NetworkManager]
 * @param initialNetworkConnectionType Sets the initial [NetworkConnectionType]
 * @param setupMocks If `true` sets up [createMock] to build [MockNetworkManager]
 */
class MockNetworkManager(initialNetworkConnectionType: NetworkConnectionType) : NetworkManager {

    class Builder(initialNetworkConnectionType: NetworkConnectionType, setupMocks: Boolean = true) : NetworkManager.Builder {

        /**
         * List of built [MockNetworkManager]
         */
        val builtNetworkManagers = concurrentMutableListOf<MockNetworkManager>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on()
                    .doExecute {
                        MockNetworkManager(initialNetworkConnectionType).also {
                            builtNetworkManagers.add(it)
                        }
                    }
            }
        }

        override fun create(): NetworkManager = createMock.call()
    }

    override val network = MutableStateFlow(initialNetworkConnectionType)
    val startMonitoringMock = ::startMonitoring.mock()
    override suspend fun startMonitoring(): Unit = startMonitoringMock.call()

    val stopMonitoringMock = ::stopMonitoring.mock()
    override suspend fun stopMonitoring(): Unit = stopMonitoringMock.call()
}
