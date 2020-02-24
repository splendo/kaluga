/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlin.test.*

class PermissionStateTest : FlowableTest<PermissionState<Permission.Microphone>>() {

    private lateinit var permissionStateRepo: MockPermissionStateRepo

    @BeforeTest
    fun setup() {
        super.beforeTest()

        permissionStateRepo = MockPermissionStateRepo()
        flowable.complete(permissionStateRepo.flowable.value)
    }

    @AfterTest
    fun tearDown() {
        super.afterTest()
    }

    @Test
    fun testInitialState() = runBlocking {
        assertFalse(permissionStateRepo.permissionManager.hasStartedMonitoring.isCompleted)
        assertFalse(permissionStateRepo.permissionManager.hasStoppedMonitoring.isCompleted)
        testWithFlow {
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionStateRepo.permissionManager.hasStartedMonitoring.getCompleted())
                assertTrue(it is PermissionState.Denied.Requestable)
            }
        }
        permissionStateRepo.permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testRequestPermission() = testWithFlow {
        var denied: PermissionState.Denied.Requestable<Permission.Microphone>? = null
        test {
            denied = it as PermissionState.Denied.Requestable
        }
        action {
            denied?.request()
            assertTrue(permissionStateRepo.permissionManager.hasRequestedPermission.isCompleted)
            permissionStateRepo.takeAndChangeState { state ->
                when(state) {
                    is PermissionState.Denied -> state.allow
                    else -> state.remain
                }
            }
        }
        test {
            assertTrue(it is PermissionState.Allowed)
        }
    }

    @Test
    fun testPermissionDenied() = runBlocking {
        permissionStateRepo.permissionManager.initialState = PermissionState.Allowed(permissionStateRepo.permissionManager)
        testWithFlow {
            test {
                assertTrue(it is PermissionState.Allowed<Permission.Microphone>)
            }
            action {
                permissionStateRepo.takeAndChangeState { state ->
                    when(state) {
                        is PermissionState.Allowed -> state.deny(true)
                        else -> state.remain
                    }
                }
            }
            test {
                assertTrue(it is PermissionState.Denied.Locked)
            }
        }
    }

    @Test
    fun testRequestFromFlow() = runBlocking {
        val hasRequested = CompletableDeferred<Boolean>()
        launch {
            hasRequested.complete(permissionStateRepo.flow().request())
        }
        launch {
            permissionStateRepo.permissionManager.hasRequestedPermission.await()
            permissionStateRepo.takeAndChangeState { state ->
                when(state) {
                    is PermissionState.Denied.Requestable -> state.allow
                    else -> state.remain
                }
            }
        }
        assertTrue(hasRequested.await())
    }

    @Test
    fun testRequestDeniedFromFlow() = runBlocking {
        val hasRequested = CompletableDeferred<Boolean>()
        launch {
            hasRequested.complete(permissionStateRepo.flow().request())
        }
        launch {
            permissionStateRepo.permissionManager.hasRequestedPermission.await()
            permissionStateRepo.takeAndChangeState { state ->
                when(state) {
                    is PermissionState.Denied.Requestable -> state.lock
                    else -> state.remain
                }
            }
        }
        assertFalse(hasRequested.await())
    }

}

private class MockPermissionStateRepo : PermissionStateRepo<Permission.Microphone>() {

    override val permissionManager = MockPermissionManager(this)

}

private class MockPermissionManager(mockPermissionRepo: MockPermissionStateRepo) : PermissionManager<Permission.Microphone>(mockPermissionRepo) {

    var initialState: PermissionState<Permission.Microphone> = PermissionState.Denied.Requestable(this)

    val hasRequestedPermission = EmptyCompletableDeferred()
    val hasStartedMonitoring = CompletableDeferred<Long>()
    val hasStoppedMonitoring = EmptyCompletableDeferred()

    override suspend fun requestPermission() {
        hasRequestedPermission.complete()
    }

    override suspend fun initializeState(): PermissionState<Permission.Microphone> {
        return initialState
    }

    override suspend fun startMonitoring(interval: Long) {
        hasStartedMonitoring.complete(interval)
    }

    override suspend fun stopMonitoring() {
        hasStoppedMonitoring.complete()
    }
}