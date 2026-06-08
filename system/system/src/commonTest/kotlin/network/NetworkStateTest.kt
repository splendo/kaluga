/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.test.mock.verifyWithin
import com.splendo.kaluga.system.network.state.NetworkState
import com.splendo.kaluga.system.network.state.NetworkStateRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NetworkStateTest : BaseNetworkStateTest<NetworkState, NetworkStateRepo>() {

    override val flowFromTestContext: suspend Context.() -> NetworkStateRepo =
        { networkStateRepo }

    override val filter: (Flow<NetworkState>) -> Flow<NetworkState> = { it.filterOnlyImportant().distinctUntilChanged() }

    @Test
    fun testInitialValueUnknown() = testNetworkState(
        NetworkConnectionType.Unknown.WithoutLastNetwork(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR,
        ),
    ) {
        assertInitialValue()

        action {
            resetFlow()
        }

        mainAction {
            networkManager.stopMonitoringMock.verifyWithin()
        }
    }

    @Test
    fun testNetworkStateChanged() = testNetworkState(NetworkConnectionType.Known.Wifi()) {
        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Wifi(), it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Absent
        }

        test {
            assertIs<NetworkState.Unavailable>(it)
            assertEquals(NetworkConnectionType.Known.Absent, it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Cellular
        }

        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Cellular, it.networkConnectionType)
        }
    }

    @Test
    fun testAvailableTransition() = testNetworkState(NetworkConnectionType.Known.Wifi()) {
        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Wifi(), it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Unknown.WithLastNetwork(
                NetworkConnectionType.Known.Wifi(),
                NetworkConnectionType.Unknown.Reason.LOSING,
            )
        }

        test {
            assertEquals(
                NetworkConnectionType.Unknown.WithLastNetwork(
                    NetworkConnectionType.Known.Wifi(),
                    NetworkConnectionType.Unknown.Reason.LOSING,
                ),
                it.networkConnectionType,
            )
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Wifi()
        }

        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Wifi(), it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Cellular
        }

        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Cellular, it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Absent
        }

        test {
            assertIs<NetworkState.Unavailable>(it)
        }
    }

    @Test
    fun testUnavailableTransition() = testNetworkState(NetworkConnectionType.Known.Absent) {
        test {
            assertIs<NetworkState.Unavailable>(it)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Cellular
        }

        test {
            assertEquals(NetworkConnectionType.Known.Cellular, it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Absent
        }

        test {
            assertIs<NetworkState.Unavailable>(it)
            assertEquals(NetworkConnectionType.Known.Absent, it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Wifi()
        }

        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Wifi(), it.networkConnectionType)
        }
    }

    @Test
    fun testUnknownTransition() = testNetworkState(
        NetworkConnectionType.Unknown.WithoutLastNetwork(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR,
        ),
    ) {
        test {
            assertIs<NetworkState.Unknown>(it)
            assertEquals(NetworkConnectionType.Unknown.WithoutLastNetwork(NetworkConnectionType.Unknown.Reason.NOT_CLEAR), it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Cellular
        }

        test {
            assertEquals(NetworkConnectionType.Known.Cellular, it.networkConnectionType)
        }

        action {
            resetFlow()
        }

        // After resetFlow, the new collector subscribes to NetworkStateRepo's
        // cached state (Cellular) before the change below has a chance to
        // propagate. Consume that replay explicitly to avoid a race where the
        // next `test` block sometimes saw Cellular instead of Wifi.
        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Cellular, it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Wifi()
        }

        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Wifi(), it.networkConnectionType)
        }

        action {
            resetFlow()
        }

        test {
            assertIs<NetworkState.Available>(it)
            assertEquals(NetworkConnectionType.Known.Wifi(), it.networkConnectionType)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Absent
        }

        test {
            assertIs<NetworkState.Unavailable>(it)
        }
    }

    private suspend fun assertInitialValue() {
        test {
            assertIs<NetworkState.Unknown>(it)
            assertEquals(
                NetworkConnectionType.Unknown.WithoutLastNetwork(NetworkConnectionType.Unknown.Reason.NOT_CLEAR),
                it.networkConnectionType,
            )
            networkManager.startMonitoringMock.verifyWithin()
        }
    }
}
