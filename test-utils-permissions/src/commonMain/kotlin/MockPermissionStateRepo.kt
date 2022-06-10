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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionStateRepo
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Mock implementation of [BasePermissionStateRepo]
 */
class MockBasePermissionStateRepo<P : Permission>(
    val permission: P,
    createInitializingState: () -> MockPermissionState.Uninitialized<P>,
    setupMocks: Boolean = true,
    coroutineContext: CoroutineContext
) : BasePermissionStateRepo<P>(
    createUninitializedState = createInitializingState,
    createInitializingState = { state ->
        val mockState = state as MockPermissionState<P>
        (this as MockBasePermissionStateRepo<P>).didInitialize(mockState)
        mockState.reinitialize
    },
    createDeinitializedState = { state ->
        val mockState = state as MockPermissionState<P>
        (this as MockBasePermissionStateRepo<P>).didDeinitialize(mockState)
        mockState.deinitialize
   },
    coroutineContext
) {

    val didInitializeMock = ::didInitialize.mock()
    fun didInitialize(state: MockPermissionState<P>): Unit = didInitializeMock.call(state)

    val didDeinitializeMock = ::didDeinitialize.mock()
    fun didDeinitialize(state: MockPermissionState<P>): Unit = didDeinitializeMock.call(state)

    val currentMockState get() = peekState() as MockPermissionState<P>

    init {
        if (setupMocks) {
            didInitializeMock.on().doExecute { (mockState) ->
                launchTakeAndChangeState { state ->
                    if (state is PermissionState.Initializing)
                        state.initialize(mockState.activeState == MockPermissionState.ActiveState.ALLOWED, mockState.activeState == MockPermissionState.ActiveState.LOCKED)
                    else
                        state.remain()
                }
            }
        }
    }
}

class MockPermissionStateRepo<P : Permission>(
    monitoringInterval: Duration = defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
    val builder: MockPermissionManager.Builder<P>,
    coroutineContext: CoroutineContext
) : PermissionStateRepo<P>(
    monitoringInterval = monitoringInterval,
    createPermissionManager = { builder.create(settings, it) },
    coroutineContext = coroutineContext
) {
    /**
     * The [MockPermissionManager] of this repo
     */
    val permissionManager get() = builder.createdManagers.first()
}
