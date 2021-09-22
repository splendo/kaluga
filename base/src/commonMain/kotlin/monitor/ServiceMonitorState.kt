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

import com.splendo.kaluga.base.ServiceMonitor
import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

sealed class ServiceMonitorState : State() {

    sealed class Initialized(
        private val monitor: ServiceMonitor
    ) : ServiceMonitorState() {

        fun deinitialize(): suspend () -> NotInitialized {
            monitor.stopMonitoring()
            return { NotInitialized(monitor) }
        }

        class Enabled(monitor: ServiceMonitor) : Initialized(monitor) {
            val disabled: suspend () -> Disabled = { Disabled(monitor) }
        }
        class Disabled(monitor: ServiceMonitor) : Initialized(monitor) {
            val enabled: suspend () -> Enabled = { Enabled(monitor) }
        }
    }

    class NotInitialized(
        private val monitor: ServiceMonitor
    ) : ServiceMonitorState(), SpecialFlowValue.NotImportant {

        fun initialize(): suspend () -> Initialized {
            monitor.startMonitoring()
            return {
                if (monitor.isServiceEnabled) {
                    Initialized.Enabled(monitor)
                } else {
                    Initialized.Disabled(monitor)
                }
            }
        }
    }
}

class ServiceMonitorStateRepo(
    monitor: ServiceMonitor,
    override val coroutineContext: CoroutineContext
) : ColdStateFlowRepo<ServiceMonitorState>(
    coroutineContext = coroutineContext,
    initChangeState = { state ->
        when (state) {
            is ServiceMonitorState.Initialized -> {
                { state }
            }
            is ServiceMonitorState.NotInitialized -> {
                state.initialize()
            }
        }
    },
    deinitChangeState = { state ->
        when (state) {
            is ServiceMonitorState.Initialized -> state.deinitialize()
            is ServiceMonitorState.NotInitialized -> state.remain()
        }
    },
    firstState = {
        ServiceMonitorState.NotInitialized(
            monitor = monitor
        )
    }
) {
    init {
        launch {
            monitor.isEnabled.collect { isEnabled ->
                takeAndChangeState { currentState ->
                    when (currentState) {
                        is ServiceMonitorState.Initialized.Enabled -> {
                            if (isEnabled) {
                                currentState.remain()
                            } else {
                                currentState.disabled
                            }
                        }
                        is ServiceMonitorState.Initialized.Disabled -> {
                            if (isEnabled) {
                                currentState.enabled
                            } else {
                                currentState.remain()
                            }
                        }
                        is ServiceMonitorState.NotInitialized -> {
                            currentState.remain()
                        }
                    }
                }
            }
        }
    }
}
