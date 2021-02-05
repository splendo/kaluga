/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.system.network

import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.system.network.Network
import com.splendo.kaluga.system.network.state.NetworkStateRepoBuilder
import com.splendo.kaluga.system.network.state.network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NetworkViewModel(
    networkStateRepoBuilder: NetworkStateRepoBuilder
) : BaseViewModel() {

    private val networkRepo = networkStateRepoBuilder.create()

    private val _networkState: HotFlowable<String?> = HotFlowable(null)
    val networkState = _networkState.toObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            networkRepo.flow().network().collect {
                when(it) {
                    is Network.Unknown.WithoutLastNetwork -> _networkState.set(
                        "Network's state is Unknown and without the last available connection."
                    )
                    is Network.Unknown.WithLastNetwork -> _networkState.set(
                        "Network's state is Unknown and with last known connection as ${it.lastKnownNetwork}."
                    )
                    is Network.Known.Cellular -> _networkState.set(
                        "Network's state is Available through Cellular."
                    )
                    is Network.Known.Wifi -> _networkState.set(
                        "Network's state is Available through WIFI."
                    )
                    Network.Known.Absent -> _networkState.set(
                        "Network's state is Absent."
                    )
                }
            }
        }
    }
}