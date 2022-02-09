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
import com.splendo.kaluga.state.State
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * State of a [Permission]
 */
sealed class PermissionState<P : Permission> : State {

    class Unknown<P : Permission> : PermissionState<P>(), SpecialFlowValue.NotImportant

    // TODO: consider below states could be wrapped in a sealed Known, and Flows could expose PermissionState.Known where applicable

    /**
     * When in this state the [Permission] has been granted
     */
    class Allowed<P : Permission> : PermissionState<P>() {

        internal fun deny(locked: Boolean): Denied<P> {
            return if (locked) Denied.Locked() else Denied.Requestable()
        }
    }

    /**
     * When in this state the [Permission] is denied. If can either be requestable of blocked from request.
     */
    sealed class Denied<P : Permission> : PermissionState<P>() {

        internal val allow: suspend () -> Allowed<P> = {
            Allowed()
        }

        /**
         * When in this state the [Permission] is denied and cannot be requested
         */
        class Locked<P : Permission>() : Denied<P>() {

            internal val unlock: suspend () -> Requestable<P> = {
                Requestable()
            }
        }

        /**
         * When in this state the [Permission] is denied but can be requested. Use [request] to request the permission.
         */
        class Requestable<P : Permission> : Denied<P>() {

            suspend fun request(permissionManager: PermissionManager<out Permission>) {
                permissionManager.requestPermission()
            }

            internal val lock: suspend () -> Locked<P> = {
                Locked()
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
    private val monitoringInterval: Long = defaultMonitoringInterval,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : ColdStateFlowRepo<PermissionState<P>>(
    coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        val pm = (repo as PermissionStateRepo<P>).permissionManager
        pm.startMonitoring(monitoringInterval)
        if (state == null || state is PermissionState.Unknown<P>)
            suspend { pm.initializeState() }
        else
            suspend { state }
    },
    deinitChangeStateWithRepo = { state, repo ->
        (repo as PermissionStateRepo<P>).permissionManager.stopMonitoring() // TODO: could also be replaced by a state
        null
    },
    firstState = { PermissionState.Unknown() }
) {

    companion object {
        const val defaultMonitoringInterval: Long = 1000
    }

    // TODO move to constructor, so no explicit cast is needed in init block
    abstract val permissionManager: PermissionManager<P>
}
