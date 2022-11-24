/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions.base

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.KalugaState
import kotlin.time.Duration

/**
 * State of a [Permission]
 */
sealed interface PermissionState<P : Permission> : KalugaState {
    sealed interface Inactive<P : Permission> : PermissionState<P>, SpecialFlowValue.NotImportant
    interface Uninitialized<P : Permission> : Inactive<P>
    interface Deinitialized<P : Permission> : Inactive<P> {
        val reinitialize: suspend () -> Initializing<P>
    }

    sealed interface Active<P : Permission> : PermissionState<P> {
        val deinitialize: suspend () -> Deinitialized<P>
    }
    interface Initializing<P : Permission> : Active<P>, SpecialFlowValue.NotImportant {
        fun initialize(allowed: Boolean, locked: Boolean): suspend () -> Initialized<P>
    }

    sealed interface Initialized<P : Permission> : Active<P>
    interface Allowed<P : Permission> : Initialized<P> {
        fun deny(locked: Boolean): suspend () -> Denied<P>
    }

    sealed interface Denied<P : Permission> : Initialized<P> {
        val allow: suspend () -> Allowed<P>

        interface Locked<P : Permission> : Denied<P> {
            val unlock: suspend () -> Requestable<P>
        }

        interface Requestable<P : Permission> : Denied<P> {
            val lock: suspend () -> Locked<P>

            fun request()
        }
    }
}

sealed class PermissionStateImpl<P : Permission> {

    sealed class Inactive<P : Permission> : PermissionStateImpl<P>()
    class Uninitialized<P : Permission> : Inactive<P>(), PermissionState.Uninitialized<P> {
        fun initialize(monitoringInterval: Duration, permissionManager: PermissionManager<P>): suspend () -> Initializing<P> = { Initializing(monitoringInterval, permissionManager) }
    }

    data class Deinitialized<P : Permission>(
        val monitoringInterval: Duration,
        val permissionManager: PermissionManager<P>
    ) : Inactive<P>(), PermissionState.Deinitialized<P> {
        override val reinitialize: suspend () -> Initializing<P> = { Initializing(monitoringInterval, permissionManager) }
    }

    sealed class Active<P : Permission> : PermissionStateImpl<P>(), HandleBeforeOldStateIsRemoved<PermissionState<P>>, HandleAfterNewStateIsSet<PermissionState<P>> {
        abstract val monitoringInterval: Duration
        abstract val permissionManager: PermissionManager<P>

        val deinitialize: suspend () -> Deinitialized<P> = { Deinitialized(monitoringInterval, permissionManager) }

        override suspend fun afterNewStateIsSet(newState: PermissionState<P>) {
            when (newState) {
                is PermissionState.Inactive -> permissionManager.stopMonitoring()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: PermissionState<P>) {
            when (oldState) {
                is PermissionState.Inactive -> permissionManager.startMonitoring(monitoringInterval)
                else -> {}
            }
        }
    }

    data class Initializing<P : Permission>(
        override val monitoringInterval: Duration,
        override val permissionManager: PermissionManager<P>
    ) : Active<P>(), PermissionState.Initializing<P> {
        override fun initialize(allowed: Boolean, locked: Boolean): suspend() -> PermissionState.Initialized<P> = {
            when {
                !allowed && locked -> Denied.Locked(monitoringInterval, permissionManager)
                !allowed -> Denied.Requestable(monitoringInterval, permissionManager)
                else -> Allowed(monitoringInterval, permissionManager)
            }
        }
    }

    /**
     * When in this state the [Permission] has been granted
     */
    data class Allowed<P : Permission>(
        override val monitoringInterval: Duration,
        override val permissionManager: PermissionManager<P>
    ) : Active<P>(), PermissionState.Allowed<P> {

        override fun deny(locked: Boolean): suspend () -> PermissionState.Denied<P> = {
            if (locked) Denied.Locked(monitoringInterval, permissionManager) else Denied.Requestable(monitoringInterval, permissionManager)
        }
    }

    /**
     * When in this state the [Permission] is denied. If can either be requestable of blocked from request.
     */
    sealed class Denied<P : Permission> : Active<P>() {

        val allow: suspend () -> Allowed<P> = {
            Allowed(monitoringInterval, permissionManager)
        }

        /**
         * When in this state the [Permission] is denied and cannot be requested
         */
        data class Locked<P : Permission>(
            override val monitoringInterval: Duration,
            override val permissionManager: PermissionManager<P>
        ) : Denied<P>(), PermissionState.Denied.Locked<P> {

            override val unlock: suspend () -> Requestable<P> = {
                Requestable(monitoringInterval, permissionManager)
            }
        }

        /**
         * When in this state the [Permission] is denied but can be requested. Use [request] to request the permission.
         */
        data class Requestable<P : Permission>(
            override val monitoringInterval: Duration,
            override val permissionManager: PermissionManager<P>
        ) : Denied<P>(), PermissionState.Denied.Requestable<P> {

            override fun request() {
                permissionManager.requestPermission()
            }

            override val lock: suspend () -> Locked<P> = {
                Locked(monitoringInterval, permissionManager)
            }
        }
    }
}
