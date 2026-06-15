/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.state.NetworkStateRepoBuilder
import com.splendo.kaluga.system.network.state.network
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NetworkViewModel(builder: NetworkStateRepoBuilder) : ViewModel() {

    val description: StateFlow<String> = builder.create().network()
        .map(::describe)
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Resolving network state…")
}

private fun describe(type: NetworkConnectionType): String = when (type) {
    is NetworkConnectionType.Unknown.WithoutLastNetwork ->
        "Network's state is Unknown and without the last available connection."

    is NetworkConnectionType.Unknown.WithLastNetwork ->
        "Network's state is Unknown and with last known connection as ${type.lastKnown}."

    is NetworkConnectionType.Known.Cellular ->
        "Network's state is Available through Cellular."

    is NetworkConnectionType.Known.Wifi ->
        "Network's state is Available through WIFI."

    is NetworkConnectionType.Known.Absent ->
        "Network's state is Absent."
}
