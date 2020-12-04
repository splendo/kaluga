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
import com.splendo.kaluga.system.network.state.NetworkState
import com.splendo.kaluga.system.network.state.NetworkStateRepo
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.FlowTestBlock
import com.splendo.kaluga.test.FlowableTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NetworkStateTest : FlowableTest<NetworkState>() {

    private val networkStateRepoBuilder = MockNetworkStateRepoBuilder()

    lateinit var networkStateRepo: NetworkStateRepo

    override fun flowable(): Flowable<NetworkState> = networkStateRepo.flowable

    private fun testNetworkState(test: FlowTestBlock<NetworkState>) {
        networkStateRepo = networkStateRepoBuilder.create()

        testWithFlow(test)
    }

    @Test
    fun testInitialValueUnknown() = testNetworkState {
        assertInitialValue(this)
    }

    @Test
    fun testNetworkStateChanged() = testNetworkState {
        assertInitialValue(this)

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Wifi())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertEquals(Network.Known.Wifi(), it.networkType)
        }

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Absent)
        }

        test {
            assertTrue { it is NetworkState.Unavailable }
            assertEquals(Network.Known.Absent, it.networkType)
        }

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Cellular())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertEquals(Network.Known.Cellular(), it.networkType)
        }
    }

    @Test
    fun testAvailabletransition() = testNetworkState {
        assertInitialValue(this)

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Wifi())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertTrue { it.networkType is Network.Known.Wifi }
        }

        action {
            networkStateRepo.onNetworkStateChange(
                Network.Unknown.WithLastNetwork(
                    Network.Known.Wifi(),
                    Network.Unknown.Reason.NOT_CLEAR
                )
            )
        }

        test {
            assertTrue { it.networkType is Network.Unknown.WithLastNetwork }
            assertTrue { (it.networkType as Network.Unknown.WithLastNetwork).lastKnownNetwork is Network.Known.Wifi }
        }

        resetStateTo<NetworkState.Available>(Network.Known.Wifi(), this)

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Cellular())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertTrue { it.networkType is Network.Known.Cellular }
        }

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Absent)
        }

        test {
            assertTrue { it is NetworkState.Unavailable }
            assertTrue { it.networkType is Network.Known.Absent }
        }
    }

    @Test
    fun testUnavailableTransition() = testNetworkState {
        assertInitialValue(this)

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Absent)
        }

        test {
            assertTrue { it is NetworkState.Unavailable }
            assertTrue { it.networkType is Network.Known.Absent }
        }

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Cellular())
        }

        test {
            assertTrue { it.networkType is Network.Known.Cellular }
        }

        resetStateTo<NetworkState.Unavailable>(Network.Known.Absent, this)

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Wifi())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertTrue { it.networkType is Network.Known.Wifi }
        }
    }

    @Test
    fun testUnknownTransition() = testNetworkState {
        networkStateRepo.lastKnownNetwork = Network.Unknown.WithoutLastNetwork(
            Network.Unknown.Reason.NOT_CLEAR
        )

        test {
            assertTrue { it is NetworkState.Unknown }
            assertTrue { it.networkType is Network.Unknown.WithoutLastNetwork }
        }

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Cellular())
        }

        test {
            assertTrue { it.networkType is Network.Known.Cellular }
        }

        resetStateTo<NetworkState.Unknown>(
            Network.Unknown.WithoutLastNetwork(
                Network.Unknown.Reason.NOT_CLEAR
            ),
            this
        )

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Wifi())
        }

        test {
            assertTrue { it is NetworkState.Available }
            assertTrue { it.networkType is Network.Known.Wifi }
        }

        resetStateTo<NetworkState.Unknown>(
            Network.Unknown.WithoutLastNetwork(
                Network.Unknown.Reason.NOT_CLEAR
            ),
            this
        )

        action {
            networkStateRepo.onNetworkStateChange(Network.Known.Absent)
        }

        test {
            assertTrue { it is NetworkState.Unavailable }
            assertTrue { it.networkType is Network.Known.Absent }
        }
    }

    private suspend inline fun <reified T> resetStateTo(network: Network, testBlock: FlowTest<NetworkState>) {
        testBlock.action {
            networkStateRepo.onNetworkStateChange(network)
        }
        testBlock.test {
            assertTrue { it is T }
        }
    }

    private suspend fun assertInitialValue(testBlock: FlowTest<NetworkState>) {
        testBlock.test {
            assertTrue { it is NetworkState.Unknown }
            assertTrue { it.networkType is Network.Unknown.WithoutLastNetwork }
            assertEquals(Network.Unknown.Reason.NOT_CLEAR, (it.networkType as Network.Unknown.WithoutLastNetwork).reason)
        }
    }
}
