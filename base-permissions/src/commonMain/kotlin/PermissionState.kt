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

    class Unknown<P : Permission> : PermissionState<P>(), SpecialFlowValue.NotImportant

    sealed class Known<P : Permission> : PermissionState<P>(), HandleBeforeOldStateIsRemoved<PermissionState<P>>, HandleAfterNewStateIsSet<PermissionState<P>> {
        abstract val monitoringInterval: Long
        abstract val permissionManager: PermissionManager<P>

        override suspend fun afterNewStateIsSet(newState: PermissionState<P>) {
            when (newState) {
                is Unknown -> permissionManager.stopMonitoring()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: PermissionState<P>) {
            when (oldState) {
                is Unknown -> permissionManager.startMonitoring(monitoringInterval)
                else -> {}
            }
        }
    }

    data class Initializing<P : Permission>(override val monitoringInterval: Long, override val permissionManager: PermissionManager<P>) : Known<P>(), SpecialFlowValue.NotImportant {
        fun initialize(allowed: Boolean, locked: Boolean): suspend() -> PermissionState<P> = {
            when {
                !allowed && locked -> Denied.Locked(monitoringInterval, permissionManager)
                !allowed ->  Denied.Requestable(monitoringInterval, permissionManager)
                else -> Allowed(monitoringInterval, permissionManager)
            }
        }
    }

    /**
     * When in this state the [Permission] has been granted
     */
    data class Allowed<P : Permission>(override val monitoringInterval: Long, override val permissionManager: PermissionManager<P>) : Known<P>() {

        internal fun deny(locked: Boolean): Denied<P> {
            return if (locked) Denied.Locked(monitoringInterval, permissionManager) else Denied.Requestable(monitoringInterval, permissionManager)
        }
    }

    /**
     * When in this state the [Permission] is denied. If can either be requestable of blocked from request.
     */
    sealed class Denied<P : Permission> : Known<P>() {

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

            suspend fun request(permissionManager: PermissionManager<out Permission>) {
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
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : ColdStateFlowRepo<PermissionState<P>>(
    coroutineContext,
    initChangeStateWithRepo = { _, repo ->
        val pm = (repo as PermissionStateRepo<P>).permissionManager
        suspend { PermissionState.Initializing(monitoringInterval, pm) }
    },
    deinitChangeStateWithRepo = { _, _ ->
        suspend { PermissionState.Unknown() }
    },
    firstState = { PermissionState.Unknown() }
) {

    companion object {
        const val defaultMonitoringInterval: Long = 1000
    }

    // TODO move to constructor, so no explicit cast is needed in init block
    abstract val permissionManager: PermissionManager<P>
}
