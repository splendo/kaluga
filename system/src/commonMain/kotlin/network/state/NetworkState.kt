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

package com.splendo.kaluga.system.network.state

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.base.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.NetworkManager
import com.splendo.kaluga.system.network.unknown

sealed interface NetworkState : KalugaState {

    val networkConnectionType: NetworkConnectionType

    sealed interface Inactive : NetworkState, SpecialFlowValue.NotImportant
    interface NotInitialized : Inactive

    interface Deinitialized : Inactive {
        val previousNetworkConnectionType: NetworkConnectionType
        val reinitialize: suspend () -> Initializing
    }

    sealed interface Active : NetworkState {
        val deinitialize: suspend () -> Deinitialized
    }

    interface Initializing : Active, SpecialFlowValue.NotImportant {
        fun initialized(networkType: NetworkConnectionType): suspend () -> Initialized
    }

    sealed interface Initialized : Active {
        fun available(available: NetworkConnectionType.Known.Available): suspend () -> Available
        fun unknown(reason: NetworkConnectionType.Unknown.Reason): suspend () -> Unknown
    }

    interface Unknown : Initialized {
        override val networkConnectionType: NetworkConnectionType.Unknown
        val unavailable: suspend () -> Unavailable
    }

    sealed interface Known : Initialized {
        override val networkConnectionType: NetworkConnectionType.Known
    }

    interface Available : Known {
        override val networkConnectionType: NetworkConnectionType.Known.Available
        val unavailable: suspend () -> Unavailable
    }

    interface Unavailable : Known {
        override val networkConnectionType: NetworkConnectionType.Known.Absent
    }
}

sealed class NetworkStateImpl {
    abstract val networkConnectionType: NetworkConnectionType

    object NotInitialized : NetworkStateImpl(), NetworkState.NotInitialized {
        override val networkConnectionType: NetworkConnectionType = NetworkConnectionType.Unknown.WithoutLastNetwork(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR)
        fun startInitializing(networkManager: NetworkManager): suspend () -> NetworkState.Initializing = {
            Initializing(networkConnectionType, networkManager)
        }
    }

    data class Deinitialized(override val previousNetworkConnectionType: NetworkConnectionType, val networkManager: NetworkManager) : NetworkStateImpl(), NetworkState.Deinitialized {
        override val reinitialize: suspend () -> NetworkState.Initializing = { Initializing(previousNetworkConnectionType, networkManager) }
        override val networkConnectionType: NetworkConnectionType = previousNetworkConnectionType.unknown(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR)
    }

    sealed class Active : NetworkStateImpl(), HandleBeforeOldStateIsRemoved<NetworkState>, HandleAfterNewStateIsSet<NetworkState> {
        abstract val networkManager: NetworkManager
        val deinitialize: suspend () -> Deinitialized = { Deinitialized(networkConnectionType, networkManager) }

        override suspend fun beforeOldStateIsRemoved(oldState: NetworkState) {
            when (oldState) {
                is NetworkState.Inactive -> networkManager.startMonitoring()
                is NetworkState.Active -> {}
            }
        }

        override suspend fun afterNewStateIsSet(newState: NetworkState) {
            when (newState) {
                is NetworkState.Inactive -> networkManager.stopMonitoring()
                is NetworkState.Active -> {}
            }
        }
    }

    data class Initializing(override val networkConnectionType: NetworkConnectionType, override val networkManager: NetworkManager) : Active(), NetworkState.Initializing {
        override fun initialized(networkType: NetworkConnectionType): suspend () -> NetworkState.Initialized = {
            when (networkType) {
                is NetworkConnectionType.Unknown -> Unknown(networkType, networkManager)
                is NetworkConnectionType.Known.Available -> Available(networkType, networkManager)
                is NetworkConnectionType.Known.Absent -> Unavailable(networkManager)
            }
        }
    }

    sealed class Initialized : Active() {
        fun available(available: NetworkConnectionType.Known.Available) = suspend { Available(available, networkManager) }
        fun unknown(reason: NetworkConnectionType.Unknown.Reason): suspend () -> NetworkState.Unknown = {
            Unknown(
                networkConnectionType.unknown(reason),
                networkManager
            )
        }
    }

    data class Unknown(override val networkConnectionType: NetworkConnectionType.Unknown, override val networkManager: NetworkManager) : Initialized(), NetworkState.Unknown {
        override val unavailable: suspend () -> NetworkState.Unavailable = { Unavailable(networkManager) }
    }

    data class Available(
        override val networkConnectionType: NetworkConnectionType.Known.Available,
        override val networkManager: NetworkManager
    ) : Initialized(), NetworkState.Available {
        override val unavailable: suspend () -> NetworkState.Unavailable = { Unavailable(networkManager) }
    }

    data class Unavailable(override val networkManager: NetworkManager) : Initialized(), NetworkState.Unavailable {
        override val networkConnectionType: NetworkConnectionType.Known.Absent = NetworkConnectionType.Known.Absent
    }
}
