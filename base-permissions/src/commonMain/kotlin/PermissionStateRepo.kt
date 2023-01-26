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

package com.splendo.kaluga.permissions.base

import com.splendo.kaluga.base.state.ColdStateFlowRepo
import com.splendo.kaluga.base.state.StateRepo
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

typealias PermissionStateFlowRepo<P> = StateRepo<PermissionState<P>, MutableStateFlow<PermissionState<P>>>

abstract class BasePermissionStateRepo<P : Permission>(
    createUninitializedState: () -> PermissionState.Uninitialized<P>,
    createInitializingState: suspend ColdStateFlowRepo<PermissionState<P>>.(PermissionState.Inactive<P>) -> suspend () -> PermissionState.Initializing<P>,
    createDeinitializedState: suspend ColdStateFlowRepo<PermissionState<P>>.(PermissionState.Active<P>) -> suspend () -> PermissionState.Deinitialized<P> = { it.deinitialize },
    coroutineContext: CoroutineContext
) : ColdStateFlowRepo<PermissionState<P>>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        when (state) {
            is PermissionState.Inactive -> repo.createInitializingState(state)
            is PermissionState.Active -> state.remain()
        }
    },
    deinitChangeStateWithRepo = { state, repo ->
        when (state) {
            is PermissionState.Active -> repo.createDeinitializedState(state)
            is PermissionState.Inactive -> state.remain()
        }
    },
    firstState = createUninitializedState
)

open class PermissionStateRepo<P : Permission>(
    protected val monitoringInterval: Duration = defaultMonitoringInterval,
    createPermissionManager: (CoroutineScope) -> PermissionManager<P>,
    coroutineContext: CoroutineContext
) : BasePermissionStateRepo<P>(
    createUninitializedState = { PermissionStateImpl.Uninitialized() },
    createInitializingState = { state ->
        @Suppress("UNCHECKED_CAST")
        when (val stateImpl = state as PermissionStateImpl.Inactive<P>) {
            is PermissionStateImpl.Uninitialized -> {
                val permissionManager = createPermissionManager(CoroutineScope(coroutineContext + CoroutineName("PermissionManager")))
                (this as PermissionStateRepo<P>).startMonitoringManager(permissionManager)
                stateImpl.initialize(monitoringInterval, permissionManager)
            }
            is PermissionStateImpl.Deinitialized -> {
                (this as PermissionStateRepo<P>).startMonitoringManager(stateImpl.permissionManager)
                stateImpl.reinitialize
            }
        }
    },
    createDeinitializedState = { state ->
        (this as PermissionStateRepo<P>).superVisorJob.cancelChildren()
        state.deinitialize
    },
    coroutineContext = coroutineContext
) {

    companion object {
        val defaultMonitoringInterval: Duration = 1.seconds
    }

    private val superVisorJob = SupervisorJob(coroutineContext[Job])
    private fun startMonitoringManager(permissionManager: PermissionManager<P>) {
        CoroutineScope(coroutineContext + superVisorJob).launch {
            permissionManager.events.collect { event ->
                when (event) {
                    is PermissionManager.Event.PermissionGranted -> handlePermissionGranted()
                    is PermissionManager.Event.PermissionDenied -> handlePermissionDenied(event.locked)
                }
            }
        }
    }

    private suspend fun handlePermissionGranted() = takeAndChangeState { state ->
        when (state) {
            is PermissionState.Initializing -> state.initialize(
                allowed = true,
                locked = false
            )
            is PermissionState.Denied -> state.allow
            is PermissionState.Allowed, is PermissionState.Inactive -> state.remain()
        }
    }

    private suspend fun handlePermissionDenied(locked: Boolean) = takeAndChangeState { state ->
        when (state) {
            is PermissionState.Initializing -> state.initialize(false, locked)
            is PermissionState.Allowed -> state.deny(locked)
            is PermissionState.Denied.Requestable -> if (locked) {
                state.lock
            } else {
                state.remain()
            }
            is PermissionState.Denied.Locked, is PermissionState.Inactive -> state.remain()
        }
    }
}
