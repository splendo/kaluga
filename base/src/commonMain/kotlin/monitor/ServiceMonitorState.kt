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
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

sealed class ServiceMonitorState : State() {

    sealed class Initialized(
        private val monitor: ServiceMonitor
    ) : ServiceMonitorState() {
        val notInitialized : suspend () -> NotInitialized = { NotInitialized(monitor) }

        fun deinitialize(): suspend () -> NotInitialized {
            debug("ServiceMonitorState") { "Initialized.deinitialize" }
            monitor.stopMonitoring()
            return notInitialized
        }

        class Enabled(monitor: ServiceMonitor) : Initialized(monitor) {
            val disabled : suspend () -> Disabled = { Disabled(monitor) }
        }
        class Disabled(monitor: ServiceMonitor) : Initialized(monitor) {
            val enabled : suspend () -> Enabled = { Enabled(monitor) }
        }
    }

    class NotInitialized(
        private val monitor: ServiceMonitor
    ) : ServiceMonitorState(), SpecialFlowValue.NotImportant {
        val initialized : suspend () -> Initialized = { Initialized.Enabled(monitor) }
        val initializedDisabled : suspend () -> Initialized = { Initialized.Disabled(monitor) }

        fun initialize(): suspend () -> Initialized {
            debug("ServiceMonitorState") { "NotInitialized.initialize" }
            monitor.startMonitoring()
            return if (monitor.isServiceEnabled) {
                initialized
            } else {
                initializedDisabled
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
        debug("ServiceMonitorStateRepo") { "ServiceMonitorStateRepo.initChangeState with $state" }
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
        debug("ServiceMonitorStateRepo") { "ServiceMonitorStateRepo.deinitChangeState with $state" }
        when (state) {
            is ServiceMonitorState.Initialized -> state.deinitialize()
            is ServiceMonitorState.NotInitialized -> state.remain()
        }
    },
    firstState = {
        debug("ServiceMonitorStateRepo") { "ServiceMonitorStateRepo.firstState" }
        ServiceMonitorState.NotInitialized(
            monitor = monitor
        )
    }
) {
    init {
        launch {
            monitor.isEnabled.collect { isEnabled ->
                takeAndChangeState { currentState ->
                    println("monitor.isEnabled.collect: isEnabled = $isEnabled")
                    println("takeAndChangeState: currentState = $currentState")
                    when (currentState) {
                        is ServiceMonitorState.Initialized.Enabled -> {
                            if (isEnabled) {
                                { currentState }
                            } else {
                                currentState.disabled
                            }
                        }
                        is ServiceMonitorState.Initialized.Disabled -> {
                            if (isEnabled) {
                                currentState.enabled
                            } else {
                                { currentState }
                            }
                        }
                        is ServiceMonitorState.NotInitialized -> {
                            { currentState }
                        }
                    }
                }
            }
        }
    }
}