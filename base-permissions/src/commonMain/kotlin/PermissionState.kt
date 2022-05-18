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

package com.splendo.kaluga.permissions

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.KalugaState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * State of a [Permission]
 */
sealed class PermissionState<P : Permission> : KalugaState {

    sealed class Inactive<P : Permission> : PermissionState<P>(), SpecialFlowValue.NotImportant
    class Uninitialized<P : Permission> : Inactive<P>() {
        fun initialize(monitoringInterval: Long, permissionManager: PermissionManager<P>): suspend () -> PermissionState<P> = { Initializing(monitoringInterval, permissionManager) }
    }

    data class Deinitialized<P : Permission>(val monitoringInterval: Long, val permissionManager: PermissionManager<P>) : Inactive<P>() {
        val initialize: suspend () -> PermissionState<P> = { Initializing(monitoringInterval, permissionManager) }
    }

    sealed class Active<P : Permission> : PermissionState<P>(), HandleBeforeOldStateIsRemoved<PermissionState<P>>, HandleAfterNewStateIsSet<PermissionState<P>> {
        abstract val monitoringInterval: Long
        abstract val permissionManager: PermissionManager<P>

        val deinitialize: suspend () -> PermissionState<P> = { Deinitialized(monitoringInterval, permissionManager) }

        override suspend fun afterNewStateIsSet(newState: PermissionState<P>) {
            when (newState) {
                is Inactive -> permissionManager.stopMonitoring()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: PermissionState<P>) {
            when (oldState) {
                is Inactive -> permissionManager.startMonitoring(monitoringInterval)
                else -> {}
            }
        }
    }

    data class Initializing<P : Permission>(override val monitoringInterval: Long, override val permissionManager: PermissionManager<P>) : Active<P>(), SpecialFlowValue.NotImportant {
        fun initialize(allowed: Boolean, locked: Boolean): suspend() -> PermissionState<P> = {
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
    data class Allowed<P : Permission>(override val monitoringInterval: Long, override val permissionManager: PermissionManager<P>) : Active<P>() {

        internal fun deny(locked: Boolean): Denied<P> {
            return if (locked) Denied.Locked(monitoringInterval, permissionManager) else Denied.Requestable(monitoringInterval, permissionManager)
        }
    }

    /**
     * When in this state the [Permission] is denied. If can either be requestable of blocked from request.
     */
    sealed class Denied<P : Permission> : Active<P>() {

        internal val allow: suspend () -> Allowed<P> = {
            Allowed(monitoringInterval, permissionManager)
        }

        /**
         * When in this state the [Permission] is denied and cannot be requested
         */
        data class Locked<P : Permission>(override val monitoringInterval: Long, override val permissionManager: PermissionManager<P>) : Denied<P>() {

            internal val unlock: suspend () -> Requestable<P> = {
                Requestable(monitoringInterval, permissionManager)
            }
        }

        /**
         * When in this state the [Permission] is denied but can be requested. Use [request] to request the permission.
         */
        data class Requestable<P : Permission>(override val monitoringInterval: Long, override val permissionManager: PermissionManager<P>) : Denied<P>() {

            suspend fun request() {
                permissionManager.requestPermission()
            }

            internal val lock: suspend () -> Locked<P> = {
                Locked(monitoringInterval, permissionManager)
            }
        }
    }
}

/**
 * State machine for managing a given [Permission].
 * Since this is a [ColdStateFlowRepo], it will only monitor for changes to permissions while being observed.
 * @param monitoringInterval The interval in milliseconds between checking for a change in [PermissionState]
 */
abstract class PermissionStateRepo<P : Permission>(
    protected val monitoringInterval: Long = defaultMonitoringInterval,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    permissionManagerBuilder: (PermissionStateRepo<P>) -> PermissionManager<P>
) : ColdStateFlowRepo<PermissionState<P>>(
    coroutineContext,
    initChangeStateWithRepo = { permissionState, repo ->
        val pm = (repo as PermissionStateRepo<P>)
        when (permissionState) {
            is PermissionState.Uninitialized -> permissionState.initialize(monitoringInterval, permissionManagerBuilder(pm))
            is PermissionState.Deinitialized -> permissionState.initialize
            is PermissionState.Active -> permissionState.remain()
        }
    },
    deinitChangeStateWithRepo = { permissionState, _ ->
        when (permissionState) {
            is PermissionState.Active -> permissionState.deinitialize
            is PermissionState.Inactive -> permissionState.remain()
        }
    },
    firstState = { PermissionState.Uninitialized() }
) {

    companion object {
        const val defaultMonitoringInterval: Long = 1000
    }
}
