/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.system.network

import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.test.FlowTestBlock
import com.splendo.kaluga.test.FlowableTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NetworkStateTest : FlowableTest<NetworkState>() {

    lateinit var networkStateRepo: NetworkStateRepo
    lateinit var networkManager: MockNetworkManager

    override fun flowable(): Flowable<NetworkState> = networkStateRepo.flowable

    private val testCoroutine = SupervisorJob()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + testCoroutine)

    private fun testNetworkState(context: Any?, test: FlowTestBlock<NetworkState>) {
        networkStateRepo = NetworkStateRepo(context, coroutineScope.coroutineContext)
        networkManager = MockNetworkManager(networkStateRepo)

        networkStateRepo.networkManager = networkManager

        testWithFlow(test)
    }

    @Test
    fun `test initialValue is Unavailable`() = testNetworkState(null) {
        test {
            assertTrue { it is NetworkState.Unavailable }
            assertEquals(Network.Absent, it.networkType)
        }
        resetFlow()
    }

    @Test
    fun `test network state changed`() = testNetworkState(null) {
        test {
            assertTrue { it is NetworkState.Unavailable }
            assertEquals(it.networkType, Network.Absent)
        }

        action {
            networkManager.isNetworkEnabled = true
            networkManager.handleNetworkStateChanged(Network.Wifi())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertEquals(Network.Wifi(), it.networkType)
        }

        action {
            networkManager.handleNetworkStateChanged(Network.Cellular())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertEquals(Network.Cellular(), it.networkType)
        }
        resetFlow()
    }

    @Test
    fun `test from available to unavailable`() = testNetworkState(null) {
        networkManager.isNetworkEnabled = true
        networkStateRepo.lastKnownNetwork = Network.Cellular()

        test {
            assertTrue { it is NetworkState.Available }
            assertEquals(Network.Cellular(), it.networkType)
        }

        action {
            networkManager.isNetworkEnabled = false
            networkManager.handleNetworkStateChanged(Network.Absent)
        }

        test {
            assertTrue { it is NetworkState.Unavailable }
            assertEquals(Network.Absent, it.networkType)
        }

        resetFlow()
    }

    @Test
    fun `return Absent if isNetworkEnabled is true, but lastKnownNetwork is Absent`() = testNetworkState(null) {
        networkManager.isNetworkEnabled = true
        networkStateRepo.lastKnownNetwork = Network.Absent

        test {
            assertTrue { it is NetworkState.Unavailable }
            assertEquals(Network.Absent, it.networkType)
        }

        resetFlow()
    }

    @Test
    fun `return Unavailable if isNetworkEnabled is true, and handleNetworkEnabledChanged is called with Absent`() = testNetworkState(null) {
        test {
            assertTrue { it is NetworkState.Unavailable }
            assertEquals(Network.Absent, it.networkType)
        }

        action {
            networkManager.isNetworkEnabled = true

            networkManager.handleNetworkStateChanged(Network.Absent)
        }

        test {
            assertTrue { it is NetworkState.Unavailable }
            assertEquals(Network.Absent, it.networkType)
        }
        resetFlow()
    }

    @Test
    fun `initialState as Available with Wifi then change to Cellular`() = testNetworkState(null) {
        networkManager.isNetworkEnabled = true
        networkStateRepo.lastKnownNetwork = Network.Wifi()

        test {
            assertTrue { it is NetworkState.Available }
            assertEquals(Network.Wifi(), it.networkType)
        }

        action {
            networkManager.handleNetworkStateChanged(Network.Cellular())
        }

        test {
            assertEquals(Network.Cellular(), it.networkType)
        }
    }
}