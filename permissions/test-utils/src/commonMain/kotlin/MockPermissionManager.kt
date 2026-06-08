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

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

/**
 * Mock implementation of [PermissionManager]
 * @param permissionRepo The [PermissionStateRepo] for managing this permission
 * @param initialState The initial [MockPermissionState] to configure for this [MockPermissionManager]
 * @param monitoringInterval The interval between checking whether permissions have changed
 */
class MockPermissionManager<P : Permission>(
    permission: P,
    val monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    private val initialState: MockPermissionState.ActiveState = MockPermissionState.ActiveState.REQUESTABLE,
    settings: Settings,
    setupMocks: Boolean = true,
    coroutineScope: CoroutineScope,
) : BasePermissionManager<P>(permission, settings, coroutineScope) {

    /**
     * Builder class for creating a [MockPermissionManager]
     * @param initialState The initial [MockPermissionState] to configure for created [MockPermissionManager]
     * @param monitoringInterval The interval between checking whether permissions have changes
     */
    class Builder<P : Permission>(
        val permission: P,
        val monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
        val initialState: MockPermissionState.ActiveState = MockPermissionState.ActiveState.REQUESTABLE,
        setupMocks: Boolean = true,
    ) {
        /**
         * List of built [MockPermissionManager]
         */
        val createdManagers = concurrentMutableListOf<MockPermissionManager<P>>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (settings, coroutineScope) ->
                    MockPermissionManager(
                        permission,
                        monitoringInterval,
                        initialState,
                        settings,
                        setupMocks,
                        coroutineScope,
                    ).also { createdManagers.add(it) }
                }
            }
        }

        fun create(settings: Settings, coroutineScope: CoroutineScope): MockPermissionManager<P> = createMock.call(settings, coroutineScope)
    }

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [requestPermission]
     */
    val requestPermissionDidStartMock = ::requestPermission.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoring]
     */
    val monitoringDidStartMock = ::startMonitoring.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoring]
     */
    val monitoringDidStopMock = ::stopMonitoring.mock()

    init {
        if (setupMocks) {
            monitoringDidStartMock.on().doExecute {
                debug("Initial State $initialState")
                emitEvent(
                    when (initialState) {
                        MockPermissionState.ActiveState.ALLOWED -> PermissionManager.Event.PermissionGranted
                        MockPermissionState.ActiveState.REQUESTABLE -> PermissionManager.Event.PermissionDenied(false)
                        MockPermissionState.ActiveState.LOCKED -> PermissionManager.Event.PermissionDenied(true)
                    },
                )
            }
        }
    }

    override fun requestPermissionDidStart(): Unit = requestPermissionDidStartMock.call()

    override fun monitoringDidStart(interval: Duration): Unit = monitoringDidStartMock.call(interval)

    override fun monitoringDidStop(): Unit = monitoringDidStopMock.call()

    fun grantPermission() {
        emitEvent(PermissionManager.Event.PermissionGranted)
    }

    fun revokePermission(locked: Boolean) {
        emitEvent(PermissionManager.Event.PermissionDenied(locked))
    }
}
