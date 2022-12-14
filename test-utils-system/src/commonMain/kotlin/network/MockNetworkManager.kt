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

import com.splendo.kaluga.system.network.BaseNetworkManager
import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.NetworkManager
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Mock implementation of [NetworkManager]
 *
 */
class MockNetworkManager(override val network: MutableSharedFlow<NetworkConnectionType>) : NetworkManager {

    val startMonitoringMock = ::startMonitoring.mock()
    override suspend fun startMonitoring(): Unit = startMonitoringMock.call()

    val stopMonitoringMock = ::stopMonitoring.mock()
    override suspend fun stopMonitoring(): Unit = stopMonitoringMock.call()
}

/**
 * Mock implementation of [BaseNetworkManager]
 * @param initialNetworkConnectionType Sets the initial [NetworkConnectionType]
 * @param setupMocks If `true` sets up [createMock] to build [MockBaseNetworkManager]
 */
class MockBaseNetworkManager(initialNetworkConnectionType: NetworkConnectionType) : BaseNetworkManager() {

    class Builder(initialNetworkConnectionType: NetworkConnectionType, setupMocks: Boolean = true) : BaseNetworkManager.Builder {

        /**
         * List of built [BaseNetworkManager]
         */
        val builtNetworkManagers = mutableListOf<BaseNetworkManager>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on()
                    .doExecute {
                        MockBaseNetworkManager(initialNetworkConnectionType).also {
                            builtNetworkManagers.add(it)
                        }
                    }
            }
        }

        override fun create(): BaseNetworkManager = createMock.call()
    }

    override val network = MutableStateFlow(initialNetworkConnectionType)
    val startMonitoringMock = ::startMonitoring.mock()
    override suspend fun startMonitoring(): Unit = startMonitoringMock.call()

    val stopMonitoringMock = ::stopMonitoring.mock()
    override suspend fun stopMonitoring(): Unit = stopMonitoringMock.call()
}

