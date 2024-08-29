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
import com.splendo.kaluga.system.network.NetworkManager
import com.splendo.kaluga.system.network.state.BaseNetworkStateRepoBuilder
import com.splendo.kaluga.system.network.state.NetworkStateRepo
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlin.coroutines.CoroutineContext

/**
 * Mock implementation of [BaseNetworkStateRepoBuilder]
 * @param networkManagerBuilder The [NetworkManager.Builder] for building the network manager
 * @param setupMocks If `true` sets up [createMock] to automatically create a [NetworkStateRepo]
 */
class MockNetworkStateRepoBuilder<NMB : NetworkManager.Builder>(val networkManagerBuilder: NMB, setupMocks: Boolean = true) : BaseNetworkStateRepoBuilder {

    /**
     * List of built [NetworkStateRepo]
     */
    val builtNetworkStateRepo = concurrentMutableListOf<NetworkStateRepo>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
     */
    val createMock = ::create.mock()

    init {
        if (setupMocks) {
            createMock.on()
                .doExecute { (coroutineContext) ->
                    NetworkStateRepo(networkManagerBuilder, coroutineContext).also {
                        builtNetworkStateRepo.add(it)
                    }
                }
        }
    }

    override fun create(coroutineContext: CoroutineContext): NetworkStateRepo = createMock.call(coroutineContext)
}
