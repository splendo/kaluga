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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.parameters.mock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MockPermissionStateRepo<P : Permission> : PermissionStateRepo<P>() {

    override val permissionManager = MockPermissionManager(this)
}

class MockPermissionManager<P : Permission>(private val permissionRepo: PermissionStateRepo<P>, val monitoringInterval: Long = PermissionStateRepo.defaultMonitoringInterval) : PermissionManager<P>(permissionRepo) {

    val currentState = MutableStateFlow<PermissionState.Known<P>>(PermissionState.Denied.Requestable(monitoringInterval, this))

    suspend fun setPermissionAllowed() = changePermission(PermissionState.Allowed(monitoringInterval, this))

    suspend fun setPermissionDenied() = changePermission(PermissionState.Denied.Requestable(monitoringInterval, this))

    suspend fun setPermissionLocked() = changePermission(PermissionState.Denied.Locked(monitoringInterval, this))

    private suspend fun changePermission(desiredState: PermissionState.Known<P>) {
        permissionRepo.takeAndChangeState { state ->
            when (state) {
                is PermissionState.Unknown -> state.remain()
                else -> suspend { desiredState }
            }
        }
        currentState.value = desiredState
    }

    val requestPermissionMock = ::requestPermission.mock()
    val startMonitoringMock = ::startMonitoring.mock()
    val stopMonitoringMock = ::stopMonitoring.mock()

    override suspend fun requestPermission(): Unit = requestPermissionMock.call()

    override suspend fun startMonitoring(interval: Long) {
        launch {
            changePermission(currentState.value)
        }
        startMonitoringMock.call(interval)
    }

    override suspend fun stopMonitoring(): Unit = stopMonitoringMock.call()
}
