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

import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.State

sealed class NetworkState(open val networkType: Network, private val networkManager: BaseNetworkManager) : State() {

    override suspend fun finalState() {
        super.finalState()
    }

    override suspend fun initialState() {
        super.initialState()
    }

    val availableWithWifi: suspend () -> Available = {
        Available(Network.Wifi(), networkManager)
    }

    val availableWithCellular: suspend () -> Available = {
        Available(Network.Cellular(), networkManager)
    }

    val unavailable: suspend () -> Unavailable = {
        Unavailable(Network.Absent, networkManager)
    }

    data class Available(
        override val networkType: Network,
        val networkManager: BaseNetworkManager
    ) : NetworkState(networkType, networkManager),
        HandleBeforeOldStateIsRemoved<NetworkState>,
        HandleAfterNewStateIsSet<NetworkState> {

        override suspend fun afterNewStateIsSet(newState: NetworkState) {
            when (newState) {
                is Unavailable -> networkManager.stopMonitoringNetwork()
                is Available -> Unit
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: NetworkState) {
            when (oldState) {
                is Unavailable -> networkManager.startMonitoringNetwork()
                is Available -> Unit
            }
        }

        override suspend fun initialState() {
            networkManager.startMonitoringNetwork()
        }

        override suspend fun finalState() {
            networkManager.stopMonitoringNetwork()
        }
    }

    data class Unavailable(
        override val networkType: Network, val networkManager: BaseNetworkManager
    ) : NetworkState(networkType, networkManager),
        HandleBeforeOldStateIsRemoved<NetworkState>,
        HandleAfterNewStateIsSet<NetworkState> {

        override suspend fun afterNewStateIsSet(newState: NetworkState) {
            when (newState) {
                is Available -> networkManager.startMonitoringNetwork()
                is Unavailable -> Unit
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: NetworkState) {
            when (oldState) {
                is Available -> networkManager.stopMonitoringNetwork()
                is Unavailable -> Unit
            }
        }

        override suspend fun initialState() {
            networkManager.startMonitoringNetwork()
        }

        override suspend fun finalState() {
            networkManager.stopMonitoringNetwork()
        }

    }
}
