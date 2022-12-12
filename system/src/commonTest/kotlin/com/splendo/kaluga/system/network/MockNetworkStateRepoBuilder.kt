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

package com.splendo.kaluga.system.network

import com.splendo.kaluga.system.network.state.NetworkStateRepo

class MockNetworkStateRepoBuilder : NetworkStateRepo.Builder {

    val networkStateRepo = MockNetworkStateRepo(MockNetworkManagerBuilder())

    override fun create(): NetworkStateRepo = networkStateRepo
}

class MockNetworkStateRepo(networkManagerBuilder: BaseNetworkManager.Builder) : NetworkStateRepo(networkManagerBuilder) {

    fun simulateNetworkStateChange(network: Network) {
        onNetworkStateChange(network)
    }
}

fun MockNetworkStateRepo.simulateOnlineStateChange(online: Boolean) {
    val newNetworkState = if (online) Network.Known.Wifi() else Network.Known.Absent
    simulateNetworkStateChange(newNetworkState)
}

fun MockNetworkStateRepoBuilder.simulateOnlineStateChange(online: Boolean) {
    networkStateRepo.simulateOnlineStateChange(online)
}

fun MockNetworkStateRepoBuilder.simulateNetworkStateChange(network: Network) {
    networkStateRepo.simulateNetworkStateChange(network)
}
