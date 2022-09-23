/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.monitor

import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.KalugaState
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

interface ServiceMonitor<S: ServiceMonitorState> : StateFlow<S>

abstract class DefaultServiceMonitor(
    coroutineContext: CoroutineContext
) : ColdStateFlowRepo<DefaultServiceMonitor.ServiceMonitorStateImpl>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        debug("DefaultServiceMonitor") { "initChangeStateWithRepo with $state" }
        (repo as DefaultServiceMonitor).run {
            startMonitoring()
        }
        when (state) {
            is ServiceMonitorState.Initialized,
            is ServiceMonitorState.NotInitialized,
            is ServiceMonitorState.NotSupported -> state.remain()
            else -> throw IllegalStateException("ServiceMonitorStateRepo's state cannot be null or $state")
        }
    },
    deinitChangeStateWithRepo = { state, repo ->
        debug("DefaultServiceMonitor") { "deinitChangeStateWithRepo with $state" }
        (repo as DefaultServiceMonitor).run {
            stopMonitoring()
        }
        when (state) {
            is ServiceMonitorState.Initialized -> {
                { ServiceMonitorStateImpl.NotInitialized }
            }
            is ServiceMonitorState.NotInitialized,
            is ServiceMonitorState.NotSupported -> state.remain()
            else -> throw IllegalStateException("ServiceMonitorStateRepo's state cannot be null or $state")

        }
    },
    firstState = { ServiceMonitorStateImpl.NotInitialized }
), ServiceMonitor<DefaultServiceMonitor.ServiceMonitorStateImpl> {

    sealed class ServiceMonitorStateImpl : KalugaState, ServiceMonitorState {
        sealed class Initialized : ServiceMonitorStateImpl(), ServiceMonitorState.Initialized {
            object Enabled : Initialized(), ServiceMonitorState.Initialized.Enabled
            object Disabled : Initialized(), ServiceMonitorState.Initialized.Disabled
            object Unauthorized : Initialized(), ServiceMonitorState.Initialized.Unauthorized
        }

        object NotInitialized : ServiceMonitorStateImpl(), ServiceMonitorState.NotInitialized

        object NotSupported : ServiceMonitorStateImpl(), ServiceMonitorState.NotSupported
    }

    protected val TAG: String = this::class.simpleName ?: "ServiceMonitor"

    override val replayCache: List<ServiceMonitorStateImpl>
        get() = stateFlow.replayCache

    override val value: ServiceMonitorStateImpl
        get() = stateFlow.value

    open fun startMonitoring() {
        debug(TAG) { "Start monitoring service state (${stateFlow.value})" }
    }

    open fun stopMonitoring() {
        debug(TAG) { "Stop monitoring service state (${stateFlow.value})" }
    }
}
