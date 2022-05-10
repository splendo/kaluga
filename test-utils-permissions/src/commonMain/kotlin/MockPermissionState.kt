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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.parameters.mock

class MockPermissionStateRepo<P : Permission> : PermissionStateRepo<P>() {

    override val permissionManager = MockPermissionManager(this)
}

class MockPermissionManager<P : Permission>(private val permissionRepo: PermissionStateRepo<P>) : PermissionManager<P>(permissionRepo) {

    private val _currentState: AtomicReference<PermissionState.Known<P>> = AtomicReference(PermissionState.Denied.Requestable())
    var currentState: PermissionState.Known<P>
        get() = _currentState.get()
        set(s) = _currentState.set(s)

    suspend fun setPermissionAllowed() = changePermission(PermissionState.Allowed())

    suspend fun setPermissionDenied() = changePermission(PermissionState.Denied.Requestable())

    suspend fun setPermissionLocked() = changePermission(PermissionState.Denied.Locked())

    private suspend fun changePermission(desiredState: PermissionState.Known<P>) {
        permissionRepo.takeAndChangeState { state ->
            when (state) {
                is PermissionState.Unknown -> state.remain()
                else -> suspend { desiredState }
            }
        }
        currentState = desiredState
    }

    val requestPermissionMock = ::requestPermission.mock()
    val startMonitoringMock = ::startMonitoring.mock()
    val stopMonitoringMock = ::stopMonitoring.mock()

    override suspend fun requestPermission(): Unit = requestPermissionMock.call()

    override suspend fun initializeState(): PermissionState<P> {
        return currentState
    }

    override suspend fun startMonitoring(interval: Long): Unit {
        startMonitoringMock.call(interval)
    }

    override suspend fun stopMonitoring(): Unit {
        stopMonitoringMock.call()
    }
}