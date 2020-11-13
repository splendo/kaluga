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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.system.network.BaseNetworkManager
import com.splendo.kaluga.system.network.Network
import com.splendo.kaluga.system.network.NetworkManager
import com.splendo.kaluga.system.network.unknownNetworkOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkStateRepo(
    context: Any?,
) : ColdStateRepo<NetworkState>() {

    private var _lastKnownNetwork = AtomicReference<Network>(Network.Unknown.WithoutLastNetwork(Network.Unknown.Reason.NOT_CLEAR))
    internal var lastKnownNetwork: Network
        get() = _lastKnownNetwork.get()
        set(value) { _lastKnownNetwork.set(value) }

    internal var networkManager: BaseNetworkManager = NetworkManager(this, context)

    override suspend fun deinitialize(state: NetworkState) {
        lastKnownNetwork = state.networkType
    }

    override suspend fun initialValue(): NetworkState {
        return when(lastKnownNetwork) {
            is Network.Unknown.WithoutLastNetwork -> {
                with (lastKnownNetwork as Network.Unknown.WithoutLastNetwork) {
                    NetworkState.Unknown(unknownNetworkOf(reason), networkManager)
                }
            }
            is Network.Unknown.WithLastNetwork -> {
                with (lastKnownNetwork as Network.Unknown.WithLastNetwork) {
                    NetworkState.Unknown(unknownNetworkOf(reason), networkManager)
                }
            }
            is Network.Known.Cellular -> NetworkState.Available(Network.Known.Cellular(), networkManager)
            is Network.Known.Wifi -> {
                with(lastKnownNetwork as Network.Known.Wifi) {
                    NetworkState.Available(Network.Known.Wifi(isExpensive), networkManager)
                }
            }
            Network.Known.Absent -> NetworkState.Unavailable(Network.Known.Absent, networkManager)
        }
    }
}

fun Flow<NetworkState>.network(): Flow<Network> {
    return this.map { it.networkType }
}
