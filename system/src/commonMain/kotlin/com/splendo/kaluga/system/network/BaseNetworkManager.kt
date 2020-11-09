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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseNetworkManager(private val networkStateRepo: NetworkStateRepo) : CoroutineScope by networkStateRepo {

    internal abstract suspend fun isNetworkEnabled(): Boolean

    internal fun handleNetworkEnabledChanged() {
        launch {
            networkStateRepo.takeAndChangeState { state: NetworkState ->
                when (state) {
                    is NetworkState.Available -> {
                        if (isNetworkEnabled()) state.remain() else state.unavailable
                    }
                    is NetworkState.Unavailable -> {
                        if (isNetworkEnabled()) state.available else state.remain()
                    }
                }
            }
        }
    }

    internal abstract suspend fun startMonitoringNetwork()
    internal abstract suspend fun stopMonitoringNetwork()

    internal fun handleNetworkStateChanged(network: Network) {
        launch {
            networkStateRepo.takeAndChangeState { state: NetworkState ->
                when (state) {
                    is NetworkState.Available -> {
                        { state.copy(networkType = network) }
                    }
                    is NetworkState.Unavailable -> {
                        { state.copy(networkType = Network.Absent) }
                    }
                }
            }
        }
    }
}

expect class NetworkManager(networkStateRepo: NetworkStateRepo, context: Any? = null) : BaseNetworkManager
