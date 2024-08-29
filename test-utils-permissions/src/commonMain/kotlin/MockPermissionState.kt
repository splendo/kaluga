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

import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.parameters.mock

sealed class MockPermissionState<P : Permission> {

    enum class ActiveState {
        ALLOWED,
        REQUESTABLE,
        LOCKED,
    }

    val requestMock = ::request.mock()

    abstract val activeState: ActiveState

    val reinitialize: suspend () -> PermissionState.Initializing<P> = suspend { Initializing(activeState) }
    val deinitialize: suspend () -> PermissionState.Deinitialized<P> = suspend { Deinitialized(activeState) }
    val allow: suspend () -> PermissionState.Allowed<P> = suspend { Allowed() }
    val unlock: suspend () -> PermissionState.Denied.Requestable<P> = suspend { Requestable() }
    val lock: suspend () -> PermissionState.Denied.Locked<P> = suspend { Locked() }

    fun request(): Unit = requestMock.call()

    fun initialize(allowed: Boolean, locked: Boolean): suspend () -> PermissionState.Initialized<P> = suspend {
        when {
            allowed -> Allowed()
            locked -> Locked()
            else -> Requestable()
        }
    }
    fun deny(locked: Boolean): suspend () -> PermissionState.Denied<P> = suspend {
        if (locked) {
            Locked()
        } else {
            Requestable()
        }
    }

    class Uninitialized<P : Permission>(override val activeState: ActiveState) :
        MockPermissionState<P>(),
        PermissionState.Uninitialized<P>
    class Deinitialized<P : Permission>(override val activeState: ActiveState) :
        MockPermissionState<P>(),
        PermissionState.Deinitialized<P>
    class Initializing<P : Permission>(override val activeState: ActiveState) :
        MockPermissionState<P>(),
        PermissionState.Initializing<P>
    class Allowed<P : Permission> :
        MockPermissionState<P>(),
        PermissionState.Allowed<P> {
        override val activeState: ActiveState = ActiveState.ALLOWED
    }
    class Locked<P : Permission> :
        MockPermissionState<P>(),
        PermissionState.Denied.Locked<P> {
        override val activeState: ActiveState = ActiveState.LOCKED
    }
    class Requestable<P : Permission> :
        MockPermissionState<P>(),
        PermissionState.Denied.Requestable<P> {
        override val activeState: ActiveState = ActiveState.REQUESTABLE
    }
}
