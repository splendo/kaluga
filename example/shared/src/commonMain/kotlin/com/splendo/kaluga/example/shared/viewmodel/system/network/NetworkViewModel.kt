/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.state.NetworkStateRepoBuilder
import com.splendo.kaluga.system.network.state.network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NetworkViewModel(networkStateRepoBuilder: NetworkStateRepoBuilder) : BaseLifecycleViewModel() {

    private val networkRepo = networkStateRepoBuilder.create()

    private val _networkState: MutableStateFlow<String?> = MutableStateFlow(null)
    val networkState = _networkState.toInitializedObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            networkRepo.network().collect {
                when (it) {
                    is NetworkConnectionType.Unknown.WithoutLastNetwork ->
                        _networkState.value =
                            "Network's state is Unknown and without the last available connection."
                    is NetworkConnectionType.Unknown.WithLastNetwork ->
                        _networkState.value =
                            "Network's state is Unknown and with last known connection as ${it.lastKnown}."
                    is NetworkConnectionType.Known.Cellular ->
                        _networkState.value =
                            "Network's state is Available through Cellular."
                    is NetworkConnectionType.Known.Wifi ->
                        _networkState.value =
                            "Network's state is Available through WIFI."
                    is NetworkConnectionType.Known.Absent ->
                        _networkState.value =
                            "Network's state is Absent."
                }
            }
        }
    }
}
