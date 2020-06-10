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

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.state.State
import kotlin.coroutines.CoroutineContext

/**
 * State of a [Permission]
 * @param permissionManager The [PermissionManager] managing the associated [Permission]
 */
sealed class PermissionState<P : Permission>(private val permissionManager: PermissionManager<P>) : State<PermissionState<P>>() {

    /**
     * When in this state the [Permission] has been granted
     */
    class Allowed<P : Permission>(private val permissionManager: PermissionManager<P>) : PermissionState<P>(permissionManager) {

        internal fun deny(locked: Boolean): Denied<P> {
            return if (locked) Denied.Locked(permissionManager) else Denied.Requestable(permissionManager)
        }
    }

    /**
     * When in this state the [Permission] is denied. If can either be requestable of blocked from request.
     */
    sealed class Denied<P : Permission>(private val permissionManager: PermissionManager<P>) : PermissionState<P>(permissionManager) {

        internal val allow: suspend () -> Allowed<P> = {
            Allowed(permissionManager)
        }

        /**
         * When in this state the [Permission] is denied and cannot be requested
         */
        class Locked<P : Permission>(permissionManager: PermissionManager<P>) : Denied<P>(permissionManager) {

            internal val unlock: suspend () -> Requestable<P> = {
                Requestable(permissionManager)
            }
        }

        /**
         * When in this state the [Permission] is denied but can be requested. Use [request] to request the permission.
         */
        class Requestable<P : Permission>(private val permissionManager: PermissionManager<P>) : Denied<P>(permissionManager) {

            suspend fun request() {
                permissionManager.requestPermission()
            }

            internal val lock: suspend () -> Locked<P> = {
                Locked(permissionManager)
            }
        }
    }
}

/**
 * State machine for managing a given [Permission].
 * Since this is a [ColdStateRepo], it will only monitor for changes to permissions while being observed.
 * @param monitoringInterval The interval in milliseconds between checking for a change in [PermissionState]
 */
abstract class PermissionStateRepo<P : Permission>(
    private val monitoringInterval: Long = defaultMonitoringInterval,
    coroutineContext: CoroutineContext = MainQueueDispatcher
) : ColdStateRepo<PermissionState<P>>(coroutineContext) {

    companion object {
        const val defaultMonitoringInterval: Long = 1000
    }

    abstract val permissionManager: PermissionManager<P>

    override suspend fun initialValue(): PermissionState<P> {
        permissionManager.startMonitoring(monitoringInterval)
        return permissionManager.initializeState()
    }

    override suspend fun deinitialize(state: PermissionState<P>) {
        permissionManager.stopMonitoring()
    }
}
