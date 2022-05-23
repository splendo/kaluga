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

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Mock implementation of [PermissionManager]
 * @param permissionRepo The [PermissionStateRepo] for managing this permission
 * @param initialState The initial [MockPermissionState] to configure for this [MockPermissionManager]
 * @param monitoringInterval The interval between checking whether permissions have changed
 */
class MockPermissionManager<P : Permission>(
    private val permissionRepo: PermissionStateRepo<P>,
    initialState: MockPermissionState = MockPermissionState.DENIED,
    val monitoringInterval: Long = PermissionStateRepo.defaultMonitoringInterval,
) : PermissionManager<P>(permissionRepo) {

    /**
     * Builder class for creating a [MockPermissionManager]
     * @param initialState The initial [MockPermissionState] to configure for created [MockPermissionManager]
     * @param monitoringInterval The interval between checking whether permissions have changes
     */
    class Builder<P : Permission>(
        val initialState: MockPermissionState = MockPermissionState.DENIED,
        val monitoringInterval: Long = PermissionStateRepo.defaultMonitoringInterval,
    ) {
        /**
         * List of built [MockPermissionManager]
         */
        val createdManagers = sharedMutableListOf<MockPermissionManager<P>>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            createMock.on().doExecute { (repo) ->
                MockPermissionManager(repo, initialState, monitoringInterval).also { createdManagers.add(it) }
            }
        }

        fun create(repo: PermissionStateRepo<P>): MockPermissionManager<P> = createMock.call(repo)
    }

    /**
     * Mocked permission state
     */
    enum class MockPermissionState {
        /**
         * Permission is granted
         */
        ALLOWED,

        /**
         * Permission is denied but requestable
         */
        DENIED,

        /**
         * Permission is denied and locked.
         */
        LOCKED
    }

    /**
     * Current [PermissionState]
     */
    val currentState = MutableStateFlow(initialState.toPermissionState())
    private val isMonitoring = AtomicBoolean(false)

    /**
     * Sets the permission to be granted
     */
    suspend fun setPermissionAllowed() = changePermission(MockPermissionState.ALLOWED)

    /**
     * Sets the permission to be denied
     */
    suspend fun setPermissionDenied() = changePermission(MockPermissionState.DENIED)

    /**
     * Sets the permission to be locked
     */
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

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [requestPermission]
     */
    val requestPermissionMock = ::requestPermission.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoring]
     */
    val startMonitoringMock = ::startMonitoring.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoring]
     */
    val stopMonitoringMock = ::stopMonitoring.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [grantPermission]
     */
    val grandPermissionMock = ::grantPermission.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [revokePermission]
     */
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
