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

import com.splendo.kaluga.state.KalugaState
import com.splendo.kaluga.system.network.Network

sealed class NetworkState(
    open val networkType: Network,
) : KalugaState {

    data class Unknown(
        override val networkType: Network.Unknown
    ) : NetworkState(networkType) {

        val availableWithWifi: suspend () -> Available = {
            Available(Network.Known.Wifi())
        }

        val availableWithCellular: suspend () -> Available = {
            Available(Network.Known.Cellular())
        }

        val unavailable: suspend () -> Unavailable = {
            Unavailable(Network.Known.Absent)
        }
    }
    data class Available(
        override val networkType: Network.Known,
    ) : NetworkState(networkType) {

        val unknownWithLastNetwork: suspend (network: Network.Known, reason: Network.Unknown.Reason) -> Unknown = { network, reason ->
            Unknown(Network.Unknown.WithLastNetwork(network, reason))
        }

        val unknownWithoutLastNetwork: suspend (reason: Network.Unknown.Reason) -> Unknown = {
            Unknown(Network.Unknown.WithoutLastNetwork(it))
        }

        val availableWithWifi: suspend () -> Available = {
            Available(Network.Known.Wifi())
        }

        val availableWithCellular: suspend () -> Available = {
            Available(Network.Known.Cellular())
        }

        val unavailable: suspend () -> Unavailable = {
            Unavailable(Network.Known.Absent)
        }
    }

    data class Unavailable(
        override val networkType: Network.Known,
    ) : NetworkState(networkType) {

        val availableWithWifi: suspend () -> Available = {
            Available(Network.Known.Wifi())
        }

        val availableWithCellular: suspend () -> Available = {
            Available(Network.Known.Cellular())
        }

        val unknownWithLastNetwork: suspend (network: Network.Known, reason: Network.Unknown.Reason) -> Unknown = { network, reason ->
            Unknown(Network.Unknown.WithLastNetwork(network, reason))
        }

        val unknownWithoutLastNetwork: suspend (reason: Network.Unknown.Reason) -> Unknown = {
            Unknown(Network.Unknown.WithoutLastNetwork(it))
        }
    }
}
