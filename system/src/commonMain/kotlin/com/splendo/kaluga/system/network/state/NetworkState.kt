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

package com.splendo.kaluga.system.network.state

import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.State
import com.splendo.kaluga.system.network.BaseNetworkManager
import com.splendo.kaluga.system.network.Network

sealed class NetworkState(open val networkType: Network, open val networkManager: BaseNetworkManager) : State() {

    override suspend fun initialState() {
        super.initialState()
        networkManager.startMonitoringNetwork()
    }

    override suspend fun finalState() {
        super.finalState()
        networkManager.stopMonitoringNetwork()
    }

    val unknownWithLastNetwork: suspend (network: Network.Known, reason: Network.Unknown.Reason) -> Unknown = { network, reason ->
        Unknown(Network.Unknown.WithLastNetwork(network, reason), networkManager)
    }

    val unknownWithoutLastNetwork: suspend (reason: Network.Unknown.Reason) -> Unknown = {
        Unknown(Network.Unknown.WithoutLastNetwork(it), networkManager)
    }

    val availableWithWifi: suspend () -> Available = {
        Available(Network.Known.Wifi(), networkManager)
    }

    val availableWithCellular: suspend () -> Available = {
        Available(Network.Known.Cellular(), networkManager)
    }

    val unavailable: suspend () -> Unavailable = {
        Unavailable(Network.Known.Absent, networkManager)
    }

    data class Unknown(
        override val networkType: Network.Unknown,
        override val networkManager: BaseNetworkManager
    ) : NetworkState(networkType, networkManager),
        HandleBeforeOldStateIsRemoved<NetworkState>,
        HandleAfterNewStateIsSet<NetworkState> {

        override suspend fun afterNewStateIsSet(newState: NetworkState) {
            when(newState) {
                is Unknown -> Unit
                is Available -> networkManager.startMonitoringNetwork()
                is Unavailable -> networkManager.stopMonitoringNetwork()
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: NetworkState) {
            when(oldState) {
                is Unknown -> Unit
                is Available -> networkManager.stopMonitoringNetwork()
                is Unavailable -> networkManager.stopMonitoringNetwork()
            }
        }
    }

    data class Available(
        override val networkType: Network.Known,
        override val networkManager: BaseNetworkManager
    ) : NetworkState(networkType, networkManager),
        HandleBeforeOldStateIsRemoved<NetworkState>,
        HandleAfterNewStateIsSet<NetworkState> {

        override suspend fun afterNewStateIsSet(newState: NetworkState) {
            when (newState) {
                is Unavailable -> networkManager.stopMonitoringNetwork()
                is Available -> Unit
                is Unknown -> networkManager.startMonitoringNetwork()
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: NetworkState) {
            when (oldState) {
                is Unavailable -> networkManager.startMonitoringNetwork()
                is Available -> Unit
                is Unknown -> networkManager.startMonitoringNetwork()
            }
        }
    }

    data class Unavailable(
        override val networkType: Network.Known,
        override val networkManager: BaseNetworkManager
    ) : NetworkState(networkType, networkManager),
        HandleBeforeOldStateIsRemoved<NetworkState>,
        HandleAfterNewStateIsSet<NetworkState> {

        override suspend fun afterNewStateIsSet(newState: NetworkState) {
            when (newState) {
                is Available -> networkManager.startMonitoringNetwork()
                is Unavailable -> Unit
                is Unknown -> networkManager.startMonitoringNetwork()
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: NetworkState) {
            when (oldState) {
                is Available -> networkManager.stopMonitoringNetwork()
                is Unavailable -> Unit
                is Unknown -> networkManager.stopMonitoringNetwork()
            }
        }
    }
}
