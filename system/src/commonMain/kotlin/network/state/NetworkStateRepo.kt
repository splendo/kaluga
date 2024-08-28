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

import com.splendo.kaluga.base.state.ColdStateFlowRepo
import com.splendo.kaluga.base.state.StateRepo
import com.splendo.kaluga.system.network.NetworkConnectionType
import com.splendo.kaluga.system.network.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A [StateRepo]/[MutableStateFlow] of [NetworkState]
 */
typealias NetworkStateFlowRepo = StateRepo<NetworkState, MutableStateFlow<NetworkState>>

/**
 * An abstract [ColdStateFlowRepo] for managing [NetworkState]
 * @param createNotInitializedState method for creating the initial [NetworkState.NotInitialized] State
 * @param createInitializingState method for transitioning from a [NetworkState.Inactive] into a [NetworkState.Initializing] given an implementation of this [ColdStateFlowRepo]
 * @param createDeinitializingState method for transitioning from a [NetworkState.Active] into a [NetworkState.Deinitialized] given an implementation of this [ColdStateFlowRepo]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
abstract class BaseNetworkStateRepo(
    createNotInitializedState: () -> NetworkState.NotInitialized,
    createInitializingState: suspend ColdStateFlowRepo<NetworkState>.(NetworkState.Inactive) -> suspend () -> NetworkState,
    createDeinitializingState: suspend ColdStateFlowRepo<NetworkState>.(NetworkState.Active) -> suspend () -> NetworkState.Deinitialized,
    coroutineContext: CoroutineContext,
) : ColdStateFlowRepo<NetworkState>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        when (state) {
            is NetworkState.Inactive -> {
                repo.createInitializingState(state)
            }
            is NetworkState.Active -> state.remain()
        }
    },
    deinitChangeStateWithRepo = { state, repo ->
        when (state) {
            is NetworkState.Active -> repo.createDeinitializingState(state)
            is NetworkState.Inactive -> state.remain()
        }
    },
    firstState = createNotInitializedState,
)

/**
 * A [BaseNetworkStateRepo] managed using a [NetworkManager]
 * @param createNetworkManager method for creating the [NetworkManager] to manage the [NetworkState]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
open class NetworkStateImplRepo(createNetworkManager: suspend () -> NetworkManager, coroutineContext: CoroutineContext) :
    BaseNetworkStateRepo(
        createNotInitializedState = { NetworkStateImpl.NotInitialized },
        createInitializingState = { state ->
            when (state) {
                is NetworkStateImpl.NotInitialized -> {
                    val manager = createNetworkManager()
                    (this as NetworkStateImplRepo).startMonitoringNetworkManager(manager)
                    state.startInitializing(manager)
                }
                is NetworkStateImpl.Deinitialized -> {
                    (this as NetworkStateImplRepo).startMonitoringNetworkManager(state.networkManager)
                    state.reinitialize
                }
                else -> state.remain()
            }
        },
        createDeinitializingState = { state ->
            (this as NetworkStateImplRepo).superVisorJob.cancelChildren()
            state.deinitialize
        },
        coroutineContext = coroutineContext,
    ) {
    private val superVisorJob = SupervisorJob(coroutineContext[Job])
    private fun startMonitoringNetworkManager(manager: NetworkManager) {
        CoroutineScope(coroutineContext + superVisorJob).launch {
            manager.network.collect { networkType ->
                takeAndChangeState { networkState ->
                    when (networkState) {
                        is NetworkState.Initializing -> networkState.initialized(networkType)
                        is NetworkState.Available -> when (networkType) {
                            is NetworkConnectionType.Known.Available -> networkState.available(networkType)
                            is NetworkConnectionType.Known.Absent -> networkState.unavailable
                            is NetworkConnectionType.Unknown -> networkState.unknown(networkType.reason)
                        }
                        is NetworkState.Unavailable -> when (networkType) {
                            is NetworkConnectionType.Known.Available -> networkState.available(networkType)
                            is NetworkConnectionType.Known.Absent -> networkState.remain()
                            is NetworkConnectionType.Unknown -> networkState.unknown(networkType.reason)
                        }
                        is NetworkState.Unknown -> when (networkType) {
                            is NetworkConnectionType.Known.Available -> networkState.available(networkType)
                            is NetworkConnectionType.Known.Absent -> networkState.unavailable
                            is NetworkConnectionType.Unknown -> networkState.unknown(networkType.reason)
                        }
                        is NetworkState.Deinitialized, is NetworkState.NotInitialized -> networkState.remain()
                    }
                }
            }
        }
    }
}

/**
 * A [NetworkStateImplRepo] using a [NetworkManager]
 * @param networkManagerBuilder the [NetworkManager.Builder] for building a [NetworkManager]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine
 */
class NetworkStateRepo(private val networkManagerBuilder: NetworkManager.Builder, coroutineContext: CoroutineContext) :
    NetworkStateImplRepo(
        createNetworkManager = {
            networkManagerBuilder.create()
        },
        coroutineContext,
    )
