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
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.ColdStateFlowRepo
import kotlin.coroutines.CoroutineContext

interface ServiceMonitor {
    fun startMonitoring()
    fun stopMonitoring()
}

abstract class DefaultServiceMonitor(
    coroutineContext: CoroutineContext
) : ColdStateFlowRepo<ServiceMonitorState>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        (repo as ServiceMonitor).run {
            startMonitoring()
        }
        debug("DefaultServiceMonitor") { "initChangeStateWithRepo with $state" }
        when (state) {
            is ServiceMonitorState -> state.remain()
            else -> throw IllegalStateException("ServiceMonitorStateRepo's state cannot be null")
        }
    },
    deinitChangeStateWithRepo = { state, repo ->
        (repo as ServiceMonitor).run {
            stopMonitoring()
        }
        when (state) {
            is ServiceMonitorState.Initialized -> state.deinitialize()
            is ServiceMonitorState.NotInitialized -> state.remain()
            ServiceMonitorState.NotSupported -> state.remain()
        }
    },
    firstState = { ServiceMonitorState.NotInitialized() }
) {

    protected val TAG: String = this::class.simpleName ?: "ServiceMonitor"

    open fun startMonitoring() {
        debug(TAG) { "Start monitoring service state (${stateFlow.value})" }

    }

    open fun stopMonitoring() {
        debug(TAG) { "Stop monitoring service state (${stateFlow.value})" }
    }
}
