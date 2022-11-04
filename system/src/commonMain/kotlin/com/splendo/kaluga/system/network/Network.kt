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

interface ReachableNetworkConnection {
    val isExpensive: Boolean
}

sealed class Network {
    sealed class Unknown(open val reason: Reason) : Network() {
        enum class Reason {
            CONNECTING,
            LOSING,
            NOT_CLEAR
        }
        class WithoutLastNetwork(override val reason: Reason) : Unknown(reason)
        class WithLastNetwork(
            val lastKnownNetwork: Network.Known,
            override val reason: Reason
        ) : Unknown(reason)
    }
    sealed class Known : Network() {
        data class Cellular(override val isExpensive: Boolean = true) : Known(), ReachableNetworkConnection
        data class Wifi(override val isExpensive: Boolean = false) : Known(), ReachableNetworkConnection
        object Absent : Known()
    }
}
