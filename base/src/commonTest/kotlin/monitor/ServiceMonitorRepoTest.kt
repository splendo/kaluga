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

import com.splendo.kaluga.base.DefaultServiceMonitor
import com.splendo.kaluga.base.ServiceMonitor
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.SimpleFlowTest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertIsNot
import kotlin.test.assertTrue

class ServiceMonitorRepoTest : SimpleFlowTest<ServiceMonitorState>() {

    companion object {
        const val SERVICE_MONITOR_DELAY = 50L
    }

    private val repo = StubMonitorStateRepo(scope.coroutineContext)

    override val flow: suspend () -> Flow<ServiceMonitorState>
        get() = suspend { repo.stateFlow }

    internal class StubMonitorStateRepo(
        coroutineContext: CoroutineContext
    ) : DefaultServiceMonitor(coroutineContext), ServiceMonitor {

        val startMonitoringDeferred = EmptyCompletableDeferred()
        val stopMonitoringDeferred = EmptyCompletableDeferred()

        override fun startMonitoring() {
            startMonitoringDeferred.complete()
        }

        override fun stopMonitoring() {
            stopMonitoringDeferred.complete()
        }
    }

    @Test
    fun test_init_state_is_not_initialized() {
        runBlocking {
            val firstValue = repo.first()
            assertIs<ServiceMonitorState.NotInitialized>(firstValue)
        }
    }

    @Test
    fun test_state_is_initialized_enabled() {
        runBlocking {
            test_init_state_is_not_initialized()
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            val job = launch {
                repo.collect {
                    assertIs<ServiceMonitorState.Initialized.Enabled>(it)
                }
            }
            delay(SERVICE_MONITOR_DELAY)
            job.cancel()
        }
    }

    @Test
    fun test_state_is_initialized_disabled() {
        runBlocking {
            test_init_state_is_not_initialized()
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Disabled } }
            val job = launch {
                repo.collect {
                    assertIs<ServiceMonitorState.Initialized.Disabled>(it)
                }
            }
            delay(SERVICE_MONITOR_DELAY)
            job.cancel()
        }
    }

    @Test
    fun test_start_monitoring_is_called_on_first_collection() {
        runBlocking {
            assertFalse { repo.startMonitoringDeferred.isCompleted }
            repo.launchJobCollectAndCancel()
            assertTrue { repo.startMonitoringDeferred.isCompleted }
        }
    }

    @Test
    fun test_stop_monitoring_is_called_on_deinitialization() {
        runBlocking {
            assertFalse { repo.stopMonitoringDeferred.isCompleted }
            repo.launchJobCollectAndCancel()
            assertTrue { repo.stopMonitoringDeferred.isCompleted }
        }
    }

    @Test
    fun test_not_initilized_emitted_when_flow_is_cancelled() {
        runBlocking {
            val job = launch { repo.collect() }
            delay(SERVICE_MONITOR_DELAY)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            job.cancel()
            delay(SERVICE_MONITOR_DELAY)
            val state = repo.first()
            assertIs<ServiceMonitorState.NotInitialized>(state)
            assertTrue { repo.stopMonitoringDeferred.isCompleted }
        }
    }

    @Test
    fun test_emit_current_state_with_same_state_emission_ignored() {
        runBlocking {
            val job = launch {
                repo.collect()
            }
            delay(SERVICE_MONITOR_DELAY)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            delay(SERVICE_MONITOR_DELAY)
            // emit same value
            delay(SERVICE_MONITOR_DELAY)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            delay(SERVICE_MONITOR_DELAY)
            repo.useState {
                assertIs<ServiceMonitorState.Initialized.Enabled>(it)
            }
            assertEquals(1, repo.stateFlow.replayCache.size)

            job.cancel()
        }
    }

    @Test
    fun test_emit_disabled_when_state_is_enabled() {
        runBlocking {
            val job = launch { repo.collect() }

            delay(SERVICE_MONITOR_DELAY)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            delay(SERVICE_MONITOR_DELAY)
            repo.useState {
                assertIs<ServiceMonitorState.Initialized.Enabled>(it)
            }

            delay(SERVICE_MONITOR_DELAY)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Disabled } }
            delay(SERVICE_MONITOR_DELAY)
            repo.useState {
                assertIs<ServiceMonitorState.Initialized.Disabled>(it)
            }
            job.cancel()
        }
    }

    @Test
    fun test_last_state_is_not_initialized() {
        runBlocking {
            assertFalse { repo.stopMonitoringDeferred.isCompleted }
            repo.launchJobCollectAndCancel()
            repo.useState { assertIs<ServiceMonitorState.NotInitialized>(it) }
            assertTrue { repo.stopMonitoringDeferred.isCompleted }
        }
    }

    @Test
    fun test_state_on_deinitialization_when_is_enabled() {
        runBlocking {
            val firstJob = launch { repo.collect() }
            delay(SERVICE_MONITOR_DELAY)
            repo.useState {
                assertIs<ServiceMonitorState.NotInitialized>(it)
            }
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }

            repo.useState {
                assertIs<ServiceMonitorState.Initialized.Enabled>(it)
            }

            firstJob.cancel()
            delay(SERVICE_MONITOR_DELAY)
            repo.useState {
                assertIs<ServiceMonitorState.NotInitialized>(it)
            }
        }
    }

    @Test
    fun test_emit_not_supported() {
        runBlocking {
            repo.useState {
                assertIs<ServiceMonitorState.NotInitialized>(it)
            }
            repo.takeAndChangeState { { ServiceMonitorState.NotSupported } }
            repo.useState {
                assertIs<ServiceMonitorState.NotSupported>(it)
            }
        }
    }

    @Test
    fun test_state_is_unauthorized() {
        runBlocking {
            val job = launch {
                repo.filterOnlyImportant().collect {
                    assertIsNot<ServiceMonitorState.NotInitialized>(it)
                }
            }
            delay(SERVICE_MONITOR_DELAY)
            repo.takeAndChangeState {
                { ServiceMonitorState.Initialized.Unauthorized }
            }

            repo.useState {
                assertIs<ServiceMonitorState.Initialized.Unauthorized>(it)
            }

            delay(SERVICE_MONITOR_DELAY)
            job.cancel()
        }
    }

    @Test
    fun test_filter_out_not_initiliazed() {
        runBlocking {
            val job = launch {
                repo.filterOnlyImportant().collect {
                    assertIsNot<ServiceMonitorState.NotInitialized>(it)
                }
            }
            delay(SERVICE_MONITOR_DELAY)
            repo.takeAndChangeState {
                { ServiceMonitorState.Initialized.Enabled }
            }

            repo.takeAndChangeState {
                { ServiceMonitorState.NotInitialized }
            }

            delay(SERVICE_MONITOR_DELAY)
            job.cancel()
        }
    }

    private suspend fun DefaultServiceMonitor.launchJobCollectAndCancel() = coroutineScope {
        val job = launch {
            collect()
        }
        delay(SERVICE_MONITOR_DELAY)
        job.cancel()
        delay(SERVICE_MONITOR_DELAY)
    }
}
