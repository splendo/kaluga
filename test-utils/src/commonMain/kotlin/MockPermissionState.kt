/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.freeze
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.logging.d
import kotlinx.coroutines.CompletableDeferred

class MockPermissionStateRepo<P:Permission> : PermissionStateRepo<P>() {

    override val permissionManager = MockPermissionManager(this)

}

class MockPermissionManager<P:Permission>(private val permissionRepo: PermissionStateRepo<P>) : PermissionManager<P>(permissionRepo) {

    private val _currentState:AtomicReference<PermissionState<P>> = AtomicReference(PermissionState.Denied.Requestable())
    var currentState:PermissionState<P>
        get() = _currentState.get()
        set(s) = _currentState.set(s)

    fun setPermissionAllowed() {
        changePermission { grantPermission() }
    }

    fun setPermissionDenied() {
        changePermission { revokePermission(false) }
    }

    fun setPermissionLocked() {
        changePermission { revokePermission(true) }
    }

    private fun changePermission(action: suspend () -> Unit) = runBlocking {
        permissionRepo.useState { action() }
        permissionRepo.useState { currentState = it }
    }

    val hasRequestedPermission = EmptyCompletableDeferred()
    val hasStartedMonitoring = CompletableDeferred<Long>()
    val hasStoppedMonitoring = EmptyCompletableDeferred()

    override suspend fun requestPermission() {
        hasRequestedPermission.complete()
    }

    override suspend fun initializeState(): PermissionState<P> {
        return currentState
    }

    override suspend fun startMonitoring(interval: Long) {
        hasStartedMonitoring.complete(interval)
    }

    override suspend fun stopMonitoring() {
        hasStoppedMonitoring.complete()
    }
}
