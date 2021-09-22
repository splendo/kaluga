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
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ServiceMonitorRepoTest : BaseTest() {

    companion object {
        const val SERVICE_MONITOR_TIMEOUT = 200L
    }

    private val testCoroutine = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + testCoroutine)

    private val stateHolder: MutableStateFlow<ServiceMonitorState?> = MutableStateFlow(null)

    internal class StubMonitor(initialValue: Boolean = false) : ServiceMonitor {

        var output = MutableStateFlow(initialValue)
        val startMonitoringDeferred = EmptyCompletableDeferred()
        val stopMonitoringDeferred = EmptyCompletableDeferred()

        override val isServiceEnabled: Boolean = output.value
        override val isEnabled: Flow<Boolean> = output

        override fun startMonitoring() {
            println("startMonitoring started")
            startMonitoringDeferred.complete()
        }

        override fun stopMonitoring() {
            stopMonitoringDeferred.complete()
        }
    }

    @Test
    fun test_first_state_is_not_initialized() = runBlockingWithStubMonitor(StubMonitor(false)) { _, monitorRepo ->
        val firstValue = monitorRepo.first()
        assertIs<ServiceMonitorState.NotInitialized>(firstValue)
    }

    @Test
    fun test_init_state_is_initialized_enabled() = runBlockingWithStubMonitor(StubMonitor(true)) { _, monitorRepo ->
        val job = launch { monitorRepo.collect() }
        delay(SERVICE_MONITOR_TIMEOUT)
        monitorRepo.useState { assertIs<ServiceMonitorState.Initialized.Enabled>(it) }
        job.cancel()
    }

    @Test
    fun test_init_state_is_initialized_disabled() = runBlockingWithStubMonitor(StubMonitor()) { _, monitorRepo ->
        val job = launch { monitorRepo.collect() }
        delay(SERVICE_MONITOR_TIMEOUT)
        monitorRepo.useState { assertIs<ServiceMonitorState.Initialized.Disabled>(it) }
        job.cancel()
    }

    @Test
    fun test_start_monitoring_is_called_on_first_collection() = runBlockingWithStubMonitor(StubMonitor()) { stubMonitor, monitorRepo ->
        assertFalse { stubMonitor.startMonitoringDeferred.isCompleted }
        monitorRepo.launchJobCollectAndCancel()
        assertTrue { stubMonitor.startMonitoringDeferred.isCompleted }
    }

    @Test
    fun test_stop_monitoring_is_called_on_deinitialization() = runBlockingWithStubMonitor(StubMonitor()) { stubMonitor, monitorRepo ->
        assertFalse { stubMonitor.stopMonitoringDeferred.isCompleted }
        monitorRepo.launchJobCollectAndCancel()
        assertTrue { stubMonitor.stopMonitoringDeferred.isCompleted }
    }

    @Test
    fun test_emit_current_state_with_same_state_emission() = runBlockingWithStubMonitor(StubMonitor()) { stubMonitor, monitorRepo ->
        val job = launch {
            monitorRepo.collect()
        }
        delay(SERVICE_MONITOR_TIMEOUT)
        stubMonitor.output.value = true
        delay(SERVICE_MONITOR_TIMEOUT)
        monitorRepo.useState {
            stateHolder.value = it
            assertIs<ServiceMonitorState.Initialized.Enabled>(it)
        }

        // emit same value
        delay(SERVICE_MONITOR_TIMEOUT)
        stubMonitor.output.value = true
        delay(SERVICE_MONITOR_TIMEOUT)
        monitorRepo.useState {
            assertEquals(stateHolder.value, it)
        }
        job.cancel()
    }

    @Test
    fun test_emit_disabled_when_state_is_enabled_and_value_false() = runBlockingWithStubMonitor(StubMonitor()) { stubMonitor, monitorRepo ->
        val job = launch { monitorRepo.collect() }
        delay(SERVICE_MONITOR_TIMEOUT)

        delay(SERVICE_MONITOR_TIMEOUT)
        stubMonitor.output.value = true
        delay(SERVICE_MONITOR_TIMEOUT)
        monitorRepo.useState {
            stateHolder.value = it
            assertIs<ServiceMonitorState.Initialized.Enabled>(it)
        }

        delay(SERVICE_MONITOR_TIMEOUT)
        stubMonitor.output.value = false
        delay(SERVICE_MONITOR_TIMEOUT)
        monitorRepo.useState {
            stateHolder.value = it
            assertIs<ServiceMonitorState.Initialized.Disabled>(it)
        }
        job.cancel()
    }

    @Test
    fun test_last_state_is_not_initialized() = runBlockingWithStubMonitor(StubMonitor()) { stubMonitor, monitorRepo ->
        assertFalse { stubMonitor.stopMonitoringDeferred.isCompleted }
        monitorRepo.launchJobCollectAndCancel()
        monitorRepo.useState { assertIs<ServiceMonitorState.NotInitialized>(it) }
    }

    private suspend fun ServiceMonitorStateRepo.launchJobCollectAndCancel() = coroutineScope {
        val job = launch {
            collect()
        }
        delay(SERVICE_MONITOR_TIMEOUT)
        job.cancel()
        delay(SERVICE_MONITOR_TIMEOUT)
    }

    private fun runBlockingWithStubMonitor(
        stubMonitor: StubMonitor,
        block: suspend CoroutineScope.(StubMonitor, ServiceMonitorStateRepo) -> Unit
    ) = runBlocking {
        val monitorRepo = ServiceMonitorStateRepo(stubMonitor, coroutineScope.coroutineContext)
        block(stubMonitor, monitorRepo)
    }
}
