/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.permissions

import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.parameters.mock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MockPermissionManager<P : Permission>(
    private val permissionRepo: PermissionStateRepo<P>,
    initialState: MockPermissionState = MockPermissionState.DENIED,
    val monitoringInterval: Long = PermissionStateRepo.defaultMonitoringInterval,
) : PermissionManager<P>(permissionRepo) {

    enum class MockPermissionState {
        ALLOWED,
        DENIED,
        LOCKED
    }

    val currentState = MutableStateFlow(initialState.toPermissionState())
    private val isMonitoring = AtomicBoolean(false)

    suspend fun setPermissionAllowed() = changePermission(MockPermissionState.ALLOWED)

    suspend fun setPermissionDenied() = changePermission(MockPermissionState.DENIED)

    suspend fun setPermissionLocked() = changePermission(MockPermissionState.LOCKED)

    private suspend fun changePermission(desiredState: MockPermissionState) = changePermission(desiredState.toPermissionState())
    private suspend fun changePermission(desiredState: PermissionState.Active<P>) {
        if (isMonitoring.value) {
            permissionRepo.takeAndChangeState { state ->
                when (state) {
                    is PermissionState.Uninitialized -> state.remain()
                    else -> suspend { desiredState }
                }
            }
        }
        currentState.value = desiredState
    }

    val requestPermissionMock = ::requestPermission.mock()
    val startMonitoringMock = ::startMonitoring.mock()
    val stopMonitoringMock = ::stopMonitoring.mock()
    val grandPermissionMock = ::grantPermission.mock()
    val revokePermissionMock = ::revokePermission.mock()

    override suspend fun requestPermission(): Unit = requestPermissionMock.call()

    override suspend fun startMonitoring(interval: Long) {
        isMonitoring.value = true
        launch {
            changePermission(currentState.value)
        }
        startMonitoringMock.call(interval)
    }

    override suspend fun stopMonitoring() {
        isMonitoring.value = false
        stopMonitoringMock.call()
    }

    override fun grantPermission() {
        grandPermissionMock.call()
        super.grantPermission()
    }

    override fun revokePermission(locked: Boolean) {
        revokePermissionMock.call(locked)
        super.revokePermission(locked)
    }

    private fun MockPermissionState.toPermissionState(): PermissionState.Active<P> = when (this) {
        MockPermissionState.ALLOWED -> PermissionState.Allowed(monitoringInterval, this@MockPermissionManager)
        MockPermissionState.DENIED -> PermissionState.Denied.Requestable(monitoringInterval, this@MockPermissionManager)
        MockPermissionState.LOCKED -> PermissionState.Denied.Locked(monitoringInterval, this@MockPermissionManager)
    }

}
