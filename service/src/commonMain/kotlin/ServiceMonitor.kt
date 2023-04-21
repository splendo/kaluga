/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.service

import com.splendo.kaluga.base.state.ColdStateFlowRepo
import com.splendo.kaluga.base.state.KalugaState
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

interface ServiceMonitor<S: ServiceMonitorState> : StateFlow<S> {
    val isEnabled: Boolean
    val isEnabledFlow: Flow<Boolean>
}

// /**
//  * Interface to monitor whether a given service is enabled
//  */
// interface ServiceMonitor {
//     /**
//      * If `true` the service is currently enabled.
//      */
//     val isServiceEnabled: Boolean
//
//     /**
//      * A [Flow] representing the enabled status of the service
//      */
//     val isEnabled: Flow<Boolean>
//
//     /**
//      * When called, the [ServiceMonitor] will start monitoring for changes to [isServiceEnabled]
//      */
//     fun startMonitoring()
//
//     /**
//      * When called, the [ServiceMonitor] will stop monitoring for changes to [isServiceEnabled]
//      */
//     fun stopMonitoring()
// }

/**
 * Default implementation of [ServiceMonitor].
 * @param logger The [Logger] to log and changes to.
 * @param coroutineContext CoroutineContext to flow on.
 */
abstract class DefaultServiceMonitor(
    protected val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    coroutineContext: CoroutineContext
) : ColdStateFlowRepo<DefaultServiceMonitor.ServiceMonitorStateImpl>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        debug("DefaultServiceMonitor") { "initChangeStateWithRepo with $state" }
        (repo as DefaultServiceMonitor).run {
            monitoringDidStart()
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
            monitoringDidStop()
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

    override val isEnabled: Boolean
        get() = value is ServiceMonitorStateImpl.Initialized.Enabled

    override val isEnabledFlow: Flow<Boolean> = map {
        it is ServiceMonitorStateImpl.Initialized.Enabled
    }.distinctUntilChanged()

    open fun monitoringDidStart() {
        debug(TAG) { "Start monitoring service state (${stateFlow.value})" }
    }

    open fun monitoringDidStop() {
        debug(TAG) { "Stop monitoring service state (${stateFlow.value})" }
    }
}
