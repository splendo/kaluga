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

package com.splendo.kaluga.base

import com.splendo.kaluga.base.monitor.ServiceMonitorState
import com.splendo.kaluga.base.monitor.ServiceMonitorStateImpl
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.ColdStateFlowRepo
import kotlin.coroutines.CoroutineContext

interface ServiceMonitor {
    fun startMonitoring()
    fun stopMonitoring()
}

abstract class DefaultServiceMonitor(
    coroutineContext: CoroutineContext
) : ColdStateFlowRepo<ServiceMonitorStateImpl>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        debug("DefaultServiceMonitor") { "initChangeStateWithRepo with $state" }
        (repo as ServiceMonitor).run {
            startMonitoring()
        }
        when (state) {
            is ServiceMonitorState.Initialized,
            is ServiceMonitorState.NotInitialized,
            is ServiceMonitorState.NotSupported -> state.remain()
            else -> throw IllegalStateException("ServiceMonitorStateRepo's state cannot be null")
        }
    },
    deinitChangeStateWithRepo = { state, repo ->
        debug("DefaultServiceMonitor") { "deinitChangeStateWithRepo with $state" }
        (repo as ServiceMonitor).run {
            stopMonitoring()
        }
        when (state) {
            is ServiceMonitorState.Initialized -> {
                { ServiceMonitorStateImpl.NotInitialized }
            }
            is ServiceMonitorState.NotInitialized,
            is ServiceMonitorState.NotSupported -> state.remain()
            else -> throw IllegalStateException("ServiceMonitorStateRepo's state cannot be null")
        }
    },
    firstState = { ServiceMonitorStateImpl.NotInitialized }
),
    ServiceMonitor {

    protected val TAG: String = this::class.simpleName ?: "ServiceMonitor"

    override fun startMonitoring() {
        debug(TAG) { "Start monitoring service state (${stateFlow.value})" }
    }

    override fun stopMonitoring() {
        debug(TAG) { "Stop monitoring service state (${stateFlow.value})" }
    }
}
