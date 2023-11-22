/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.test.system.network

import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.state.NetworkState
import com.splendo.kaluga.system.network.unknown

sealed class MockNetworkState {

    abstract val networkConnectionType: NetworkConnectionType

    sealed class Inactive : MockNetworkState()
    data object NotInitialized : Inactive(), NetworkState.NotInitialized {
        override val networkConnectionType: NetworkConnectionType = NetworkConnectionType.Unknown.WithoutLastNetwork(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR,
        )
    }

    data class Deinitialized(private val previousNetworkConnectionType: NetworkConnectionType) : MockNetworkState(), NetworkState.Deinitialized {
        override val networkConnectionType: NetworkConnectionType = previousNetworkConnectionType.unknown(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR,
        )
        override val reinitialize: suspend () -> NetworkState.Initializing = { Initializing(networkConnectionType) }
    }

    sealed class Active : MockNetworkState() {
        val deinitialize: suspend () -> Deinitialized = suspend { Deinitialized(networkConnectionType) }
    }

    data class Initializing(override val networkConnectionType: NetworkConnectionType) : Active(), NetworkState.Initializing {
        override fun initialized(networkType: NetworkConnectionType): suspend () -> NetworkState.Initialized = {
            when (networkType) {
                is NetworkConnectionType.Unknown -> Unknown(networkType)
                is NetworkConnectionType.Known.Available -> Available(networkType)
                is NetworkConnectionType.Known.Absent -> Unavailable
            }
        }
    }

    sealed class Initialized : Active() {
        fun available(available: NetworkConnectionType.Known.Available) = suspend { Available(available) }
        fun unknown(reason: NetworkConnectionType.Unknown.Reason): suspend () -> NetworkState.Unknown = {
            Unknown(
                networkConnectionType.unknown(reason),
            )
        }
    }

    data class Unknown(override val networkConnectionType: NetworkConnectionType.Unknown) : Initialized(), NetworkState.Unknown {
        override val unavailable: suspend () -> NetworkState.Unavailable = { Unavailable }
    }

    data class Available(
        override val networkConnectionType: NetworkConnectionType.Known.Available,
    ) : Initialized(), NetworkState.Available {
        override val unavailable: suspend () -> NetworkState.Unavailable = { Unavailable }
    }

    data object Unavailable : Initialized(), NetworkState.Unavailable {
        override val networkConnectionType: NetworkConnectionType.Known.Absent = NetworkConnectionType.Known.Absent
    }
}
