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

package com.splendo.kaluga.permissions.base

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.base.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import com.splendo.kaluga.permissions.base.PermissionState.Active
import com.splendo.kaluga.permissions.base.PermissionState.Allowed
import com.splendo.kaluga.permissions.base.PermissionState.Deinitialized
import com.splendo.kaluga.permissions.base.PermissionState.Denied
import com.splendo.kaluga.permissions.base.PermissionState.Inactive
import com.splendo.kaluga.permissions.base.PermissionState.Initialized
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest
import kotlin.time.Duration

/**
 * A [KalugaState] of a [Permission]
 * @param P the type of [Permission] associated with the state
 */
sealed interface PermissionState<P : Permission> : KalugaState {

    /**
     * A [PermissionState] indicating observation is not active
     * @param P the type of [Permission] associated with the state
     */
    sealed interface Inactive<P : Permission> : PermissionState<P>, SpecialFlowValue.NotImportant

    /**
     * A [Inactive] State indicating observation has not started yet
     * @param P the type of [Permission] associated with the state
     */
    interface Uninitialized<P : Permission> : Inactive<P>

    /**
     * A [Inactive] State indicating observation has stopped after being started
     * @param P the type of [Permission] associated with the state
     */
    interface Deinitialized<P : Permission> : Inactive<P> {

        /**
         * Transitions into an [Initializing] State
         */
        val reinitialize: suspend () -> Initializing<P>
    }

    /**
     * A [PermissionState] indicating observation has started
     * @param P the type of [Permission] associated with the state
     */
    sealed interface Active<P : Permission> : PermissionState<P> {

        /**
         * Transitions into a [Deinitialized] State
         */
        val deinitialize: suspend () -> Deinitialized<P>
    }

    /**
     * An [Active] State indicating the state is transitioning from [Inactive] to [Initialized]
     * @param P the type of [Permission] associated with the state
     */
    interface Initializing<P : Permission> : Active<P>, SpecialFlowValue.NotImportant {

        /**
         * Transitions into an [Initialized] state
         * @param allowed if `true` the permission is currently granted
         * @param locked if `true` and [allowed] is `false`, this indicates the permission can not be requested.
         * @return the method for transitioning into an [Initialized] State
         */
        fun initialize(allowed: Boolean, locked: Boolean): suspend () -> Initialized<P>
    }

    /**
     * An [Active] State indicating observation has started and initialization has completed
     * @param P the type of [Permission] associated with the state
     */
    sealed interface Initialized<P : Permission> : Active<P>

    /**
     * A [Initialized] State indicating the permission has been granted
     * @param P the type of [Permission] associated with the state
     */
    interface Allowed<P : Permission> : Initialized<P> {

        /**
         * Transitions into a [Denied] State
         * @param locked if `true` this indicates the permission can not be requested again
         * @return the method for transitioning into a [Denied] State
         */
        fun deny(locked: Boolean): suspend () -> Denied<P>
    }

    /**
     * A [Initialized] State indicating the permission has been denied.
     * @param P the type of [Permission] associated with the state
     */
    sealed interface Denied<P : Permission> : Initialized<P> {

        /**
         * Transitions into an [Allowed] State
         */
        val allow: suspend () -> Allowed<P>

        /**
         * A [Denied] State indicating the permission cannot be granted
         * @param P the type of [Permission] associated with the state
         */
        interface Locked<P : Permission> : Denied<P> {

            /**
             * Transitions into a [Requestable] State
             */
            val unlock: suspend () -> Requestable<P>
        }

        /**
         * A [Denied] State indicating the permission can be granted
         * Use [request] to request the permission.
         * @param P the type of [Permission] associated with the state
         */
        interface Requestable<P : Permission> : Denied<P> {

            /**
             * Transitions into a [Locked] State
             */
            val lock: suspend () -> Locked<P>

            /**
             * Attempts to request the permission
             */
            fun request()
        }
    }
}

/**
 * State of a [Permission] closely matching [PermissionState]
 * @param P the type of [Permission] associated with the state
 */
sealed class PermissionStateImpl<P : Permission> {

    /**
     * A [PermissionStateImpl] indicating observation is not active
     * @param P the type of [Permission] associated with the state
     */
    sealed class Inactive<P : Permission> : PermissionStateImpl<P>()

    /**
     * A [Inactive] State indicating observation has not started yet
     * @param P the type of [Permission] associated with the state
     */
    class Uninitialized<P : Permission> : Inactive<P>(), PermissionState.Uninitialized<P> {

        /**
         * Transitions into a [Initializing] state
         * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
         * @param permissionManager the [PermissionManager] managing the [P] if this State
         * @return method for transitioning into an [Initializing] State
         */
        fun initialize(monitoringInterval: Duration, permissionManager: PermissionManager<P>): suspend () -> Initializing<P> = { Initializing(monitoringInterval, permissionManager) }
    }

    /**
     * A [Inactive] State indicating observation has stopped after being started
     * @param P the type of [Permission] associated with the state
     * @property monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
     * @property permissionManager the [PermissionManager] managing the [P] if this State
     */
    data class Deinitialized<P : Permission>(
        val monitoringInterval: Duration,
        val permissionManager: PermissionManager<P>
    ) : Inactive<P>(), PermissionState.Deinitialized<P> {
        override val reinitialize: suspend () -> Initializing<P> = { Initializing(monitoringInterval, permissionManager) }
    }

    /**
     * A [PermissionState] indicating observation has started
     * @param P the type of [Permission] associated with the state
     */
    sealed class Active<P : Permission> : PermissionStateImpl<P>(), HandleBeforeOldStateIsRemoved<PermissionState<P>>, HandleAfterNewStateIsSet<PermissionState<P>> {

        /**
         * The [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
         */
        abstract val monitoringInterval: Duration

        /**
         * The [PermissionManager] managing the [P] if this State
         */
        abstract val permissionManager: PermissionManager<P>

        /**
         * Transitions into a [Deinitialized] State
         */
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

    /**
     * A [Active] State indicating the state is transitioning from [Inactive] to [Initialized]
     * @param P the type of [Permission] associated with the state
     * @property monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
     * @property permissionManager the [PermissionManager] managing the [P] if this State
     */
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
     * A [Active] State indicating the permission has been granted
     * @param P the type of [Permission] associated with the state
     * @property monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
     * @property permissionManager the [PermissionManager] managing the [P] if this State
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
     * A [Active] State indicating the permission has been denied.
     * @param P the type of [Permission] associated with the state
     */
    sealed class Denied<P : Permission> : Active<P>() {

        /**
         * Transitions into an [Allowed] State
         */
        val allow: suspend () -> Allowed<P> = {
            Allowed(monitoringInterval, permissionManager)
        }

        /**
         * A [Denied] State indicating the permission cannot be granted
         * @param P the type of [Permission] associated with the state
         * @property monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
         * @property permissionManager the [PermissionManager] managing the [P] if this State
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
         * A [Denied] State indicating the permission can be granted
         * Use [request] to request the permission.
         * @param P the type of [Permission] associated with the state
         * @property monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
         * @property permissionManager the [PermissionManager] managing the [P] if this State
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

/**
 * Requests a [Permission] on a [Flow] of [PermissionState]
 * @param P the type of [Permission] associated with the [PermissionState]
 * @return `true` if the permission was granted, `false` otherwise.
 */
suspend fun <P : Permission> Flow<PermissionState<out P>>.request(): Boolean {
    return this.transformLatest { state ->
        when (state) {
            is PermissionState.Allowed -> emit(true)
            is PermissionState.Denied.Requestable -> state.request()
            is PermissionState.Denied.Locked -> emit(false)
            is PermissionState.Inactive, is PermissionState.Active -> {}
        }
    }.first()
}
