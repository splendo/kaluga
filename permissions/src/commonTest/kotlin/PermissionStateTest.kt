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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.permissions.PermissionState.Denied.Requestable
import com.splendo.kaluga.test.FlowTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PermissionStateTest : FlowTest<PermissionState<Permission.Microphone>, MockPermissionStateRepo>() {

    override val filter:(Flow<PermissionState<Permission.Microphone>>) -> (Flow<PermissionState<Permission.Microphone>>) = {
        it.filterOnlyImportant()
    }

    override val flow = suspend { MockPermissionStateRepo() }

    @Test
    fun testInitialState() = testWithFlow { permissionStateRepo ->

        assertFalse(permissionStateRepo.permissionManager.hasStartedMonitoring.isCompleted)
        assertFalse(permissionStateRepo.permissionManager.hasStoppedMonitoring.isCompleted)

        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionStateRepo.permissionManager.hasStartedMonitoring.getCompleted())
            assertTrue(it is Requestable)
        }
        permissionStateRepo.permissionManager.hasStartedMonitoring.await()
        delay(50) /// wait for init and de-init
        resetFlow()
        permissionStateRepo.permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testRequestPermission() = testWithFlow { permissionStateRepo ->
        val denied: CompletableDeferred<Requestable<Permission.Microphone>> = CompletableDeferred()
        test {
            assertTrue(it is Requestable)
            denied.complete(it)
        }
        action {
            denied.await().request(permissionStateRepo.permissionManager)
            assertTrue(permissionStateRepo.permissionManager.hasRequestedPermission.isCompleted)
            permissionStateRepo.takeAndChangeState { state ->
                when (state) {
                    is PermissionState.Denied -> state.allow
                    else -> state.remain()
                }
            }
        }
        test {
            assertTrue(it is PermissionState.Allowed)
        }
    }

    @Test
    fun testPermissionDenied() = testWithFlow { permissionStateRepo ->
        permissionStateRepo.permissionManager.initialState = PermissionState.Allowed()

        test {
            assertTrue(it is PermissionState.Allowed<Permission.Microphone>)
        }
        action {
            permissionStateRepo.takeAndChangeState { state ->
                when (state) {
                    is PermissionState.Allowed -> suspend { state.deny(true) }
                    else -> state.remain()
                }
            }
        }
        test {
            assertTrue(it is PermissionState.Denied.Locked)
        }
    }

    @Test
    fun testRequestFromFlow() = testWithFlow { permissionStateRepo ->

        val hasRequested = CompletableDeferred<Boolean>()
        launch(Dispatchers.Main) {
            hasRequested.complete(permissionStateRepo.request(permissionStateRepo.permissionManager))
        }
        launch {
            permissionStateRepo.permissionManager.hasRequestedPermission.await()
            permissionStateRepo.permissionManager.grantPermission()
        }
        assertTrue(hasRequested.await())
    }

    @Test
    fun testRequestDeniedFromFlow() = testWithFlow { permissionStateRepo ->
        val hasRequested = CompletableDeferred<Boolean>()
        launch {
            hasRequested.complete(permissionStateRepo.request(permissionStateRepo.permissionManager))
        }
        launch {
            permissionStateRepo.permissionManager.hasRequestedPermission.await()
            permissionStateRepo.permissionManager.revokePermission(true)
        }
        assertFalse(hasRequested.await())
    }

}

class MockPermissionStateRepo : PermissionStateRepo<Permission.Microphone>() {

    override val permissionManager = MockPermissionManager(this)
}

class MockPermissionManager(mockPermissionRepo: MockPermissionStateRepo) : PermissionManager<Permission.Microphone>(mockPermissionRepo) {

    var initialState: PermissionState<Permission.Microphone> = Requestable()

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
