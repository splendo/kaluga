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
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ServiceMonitorRepoTest : BaseTest() {

    companion object {
        const val SERVICE_MONITOR_TIMEOUT = 200L
    }

    private val testCoroutine = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + testCoroutine)
    private val repo = StubMonitorStateRepo(coroutineScope.coroutineContext)

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
    fun test_state_is_initialized_enabled()  {
        runBlocking {
            test_init_state_is_not_initialized()
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            val job = launch {
                repo.collect {
                    assertIs<ServiceMonitorState.Initialized.Enabled>(it) }
                }
            delay(SERVICE_MONITOR_TIMEOUT)
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
                    assertIs<ServiceMonitorState.Initialized.Disabled>(it) }
            }
            delay(SERVICE_MONITOR_TIMEOUT)
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
    fun test_emit_current_state_with_same_state_emission_ignored() {
        runBlocking {
            val job = launch {
                repo.collect()
            }
            delay(SERVICE_MONITOR_TIMEOUT)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            delay(SERVICE_MONITOR_TIMEOUT)
            // emit same value
            delay(SERVICE_MONITOR_TIMEOUT)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            delay(SERVICE_MONITOR_TIMEOUT)
            repo.useState {
                assertIs<ServiceMonitorState.Initialized.Enabled>(it)
            }
            job.cancel()
        }
    }

    @Test
    fun test_emit_disabled_when_state_is_enabled() {
        runBlocking {
            val job = launch { repo.collect() }
            delay(SERVICE_MONITOR_TIMEOUT)

            delay(SERVICE_MONITOR_TIMEOUT)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Enabled } }
            delay(SERVICE_MONITOR_TIMEOUT)
            repo.useState {
                assertIs<ServiceMonitorState.Initialized.Enabled>(it)
            }

            delay(SERVICE_MONITOR_TIMEOUT)
            repo.takeAndChangeState { { ServiceMonitorState.Initialized.Disabled } }
            delay(SERVICE_MONITOR_TIMEOUT)
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
        }
    }

    private suspend fun DefaultServiceMonitor.launchJobCollectAndCancel() = coroutineScope {
        val job = launch {
            collect()
        }
        delay(SERVICE_MONITOR_TIMEOUT)
        job.cancel()
        delay(SERVICE_MONITOR_TIMEOUT)
    }
}
