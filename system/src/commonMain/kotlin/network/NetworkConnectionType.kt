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

interface ReachableNetworkConnection {
    val isExpensive: Boolean
}

sealed class NetworkConnectionType {
    sealed class Unknown(open val reason: Reason) : NetworkConnectionType() {
        enum class Reason {
            CONNECTING,
            LOSING,
            NOT_CLEAR
        }
        data class WithoutLastNetwork(override val reason: Reason) : Unknown(reason)
        data class WithLastNetwork(
            val lastKnown: Known,
            override val reason: Reason
        ) : Unknown(reason)
    }
    sealed class Known : NetworkConnectionType() {
        sealed class Available : Known(), ReachableNetworkConnection
        data class Cellular(override val isExpensive: Boolean = true) : Available()
        data class Wifi(override val isExpensive: Boolean = false) : Available()
        object Absent : Known()
    }
}

fun NetworkConnectionType.unknown(reason: NetworkConnectionType.Unknown.Reason) = when (this) {
    is NetworkConnectionType.Known -> NetworkConnectionType.Unknown.WithLastNetwork(this, reason)
    is NetworkConnectionType.Unknown.WithLastNetwork -> NetworkConnectionType.Unknown.WithLastNetwork(
        this.lastKnown,
        reason
    )
    is NetworkConnectionType.Unknown.WithoutLastNetwork -> NetworkConnectionType.Unknown.WithoutLastNetwork(
        reason
    )
}
