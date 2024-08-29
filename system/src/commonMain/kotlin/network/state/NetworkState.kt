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
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.NetworkManager
import com.splendo.kaluga.system.network.unknown
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A [KalugaState] of a [NetworkConnectionType]
 */
sealed interface NetworkState : KalugaState {

    /**
     * The [NetworkConnectionType] during this state
     */
    val networkConnectionType: NetworkConnectionType

    /**
     * A [NetworkState] indicating observation is not active
     */
    sealed interface Inactive :
        NetworkState,
        SpecialFlowValue.NotImportant

    /**
     * An [Inactive] State indicating observation has not started yet
     */
    interface NotInitialized : Inactive

    /**
     * An [Inactive] State indicating observation has stopped after being started
     */
    interface Deinitialized : Inactive {

        /**
         * Transitions into an [Initializing] State
         */
        val reinitialize: suspend () -> Initializing
    }

    /**
     * A [NetworkState] indicating observation has started
     */
    sealed interface Active : NetworkState {

        /**
         * Transitions into a [Deinitialized] State
         */
        val deinitialize: suspend () -> Deinitialized
    }

    /**
     * An [Active] State indicating the state is transitioning from [Inactive] to [Initialized]
     */
    interface Initializing :
        Active,
        SpecialFlowValue.NotImportant {

        /**
         * Transitions into an [Initialized] State
         * @param networkType the [NetworkConnectionType] to initialize with
         * @return method for transitioning into an [Initialized] State
         */
        fun initialized(networkType: NetworkConnectionType): suspend () -> Initialized
    }

    /**
     * An [Active] State indicating observation has started and initialization has completed
     */
    sealed interface Initialized : Active {

        /**
         * Transitions into an [Available] State
         * @param available the [NetworkConnectionType.Known.Available] of the [Available] State to transition to
         * @return method for transitioning into an [Available] State
         */
        fun available(available: NetworkConnectionType.Known.Available): suspend () -> Available

        /**
         * Transitions into an [Unknown] State
         * @param reason the [NetworkConnectionType.Unknown.Reason] of the [Unknown] State to transition to
         * @return method for transitioning into an [Unknown] State
         */
        fun unknown(reason: NetworkConnectionType.Unknown.Reason): suspend () -> Unknown
    }

    /**
     * An [Initialized] State indicating the [NetworkConnectionType] is [NetworkConnectionType.Unknown]
     */
    interface Unknown : Initialized {
        override val networkConnectionType: NetworkConnectionType.Unknown

        /**
         * Transitions into an [Unavailable] State
         */
        val unavailable: suspend () -> Unavailable
    }

    /**
     * An [Initialized] State indicating the [NetworkConnectionType] is [NetworkConnectionType.Known]
     */
    sealed interface Known : Initialized {
        override val networkConnectionType: NetworkConnectionType.Known
    }

    /**
     * A [Known] State indicating the [NetworkConnectionType] is [NetworkConnectionType.Known.Available]
     */
    interface Available : Known {
        override val networkConnectionType: NetworkConnectionType.Known.Available

        /**
         * Transitions into an [Unavailable] State
         */
        val unavailable: suspend () -> Unavailable
    }

    /**
     * A [Known] State indicating the [NetworkConnectionType] is [NetworkConnectionType.Known.Absent]
     */
    interface Unavailable : Known {
        override val networkConnectionType: NetworkConnectionType.Known.Absent
    }
}

internal sealed class NetworkStateImpl {
    abstract val networkConnectionType: NetworkConnectionType

    data object NotInitialized : NetworkStateImpl(), NetworkState.NotInitialized {
        override val networkConnectionType: NetworkConnectionType = NetworkConnectionType.Unknown.WithoutLastNetwork(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR,
        )
        fun startInitializing(networkManager: NetworkManager): suspend () -> NetworkState.Initializing = {
            Initializing(networkConnectionType, networkManager)
        }
    }

    data class Deinitialized(private val previousNetworkConnectionType: NetworkConnectionType, val networkManager: NetworkManager) :
        NetworkStateImpl(),
        NetworkState.Deinitialized {
        override val reinitialize: suspend () -> NetworkState.Initializing = { Initializing(previousNetworkConnectionType, networkManager) }
        override val networkConnectionType: NetworkConnectionType = previousNetworkConnectionType.unknown(
            NetworkConnectionType.Unknown.Reason.NOT_CLEAR,
        )
    }

    sealed class Active :
        NetworkStateImpl(),
        HandleBeforeOldStateIsRemoved<NetworkState>,
        HandleAfterNewStateIsSet<NetworkState> {
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

    data class Initializing(override val networkConnectionType: NetworkConnectionType, override val networkManager: NetworkManager) :
        Active(),
        NetworkState.Initializing {
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
                networkManager,
            )
        }
    }

    data class Unknown(override val networkConnectionType: NetworkConnectionType.Unknown, override val networkManager: NetworkManager) :
        Initialized(),
        NetworkState.Unknown {
        override val unavailable: suspend () -> NetworkState.Unavailable = { Unavailable(networkManager) }
    }

    data class Available(override val networkConnectionType: NetworkConnectionType.Known.Available, override val networkManager: NetworkManager) :
        Initialized(),
        NetworkState.Available {
        override val unavailable: suspend () -> NetworkState.Unavailable = { Unavailable(networkManager) }
    }

    data class Unavailable(override val networkManager: NetworkManager) :
        Initialized(),
        NetworkState.Unavailable {
        override val networkConnectionType: NetworkConnectionType.Known.Absent = NetworkConnectionType.Known.Absent
    }
}

/**
 * Transforms a [Flow] of [NetworkState] into a flow of its associated [NetworkConnectionType]
 * @return the [Flow] of [NetworkConnectionType] associated with the [NetworkState]
 */
fun Flow<NetworkState>.network(): Flow<NetworkConnectionType> = filterOnlyImportant().map { it.networkConnectionType }.distinctUntilChanged()

/**
 * Transforms a [Flow] of [NetworkState] into a flow indicating the network is considered to be available.
 * @return the [Flow] containing `true` if the network is considered available
 */
fun Flow<NetworkState>.online(): Flow<Boolean> = network().map {
    it is NetworkConnectionType.Known.Available
}
