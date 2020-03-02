package com.splendo.kaluga.permissions

import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.state.State

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

sealed class PermissionState<P : Permission>(private val permissionManager: PermissionManager<P>) : State<PermissionState<P>>() {

    class Allowed<P : Permission>(private val permissionManager: PermissionManager<P>) : PermissionState<P>(permissionManager) {

        internal fun deny(locked: Boolean) : suspend () -> Denied<P> = {
            if (locked) Denied.Locked(permissionManager) else Denied.Requestable(permissionManager)
        }

    }

    sealed class Denied<P : Permission>(private val permissionManager: PermissionManager<P>) : PermissionState<P>(permissionManager) {

        internal val allow: suspend () -> Allowed<P> = {
            Allowed(permissionManager)
        }

        class Locked<P : Permission>(permissionManager: PermissionManager<P>) : Denied<P>(permissionManager) {

            internal val unlock: suspend () -> Requestable<P> = {
                Requestable(permissionManager)
            }

        }

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

abstract class PermissionStateRepo<P : Permission>(private val monitoringInterval: Long = defaultMonitoringInterval) : ColdStateRepo<PermissionState<P>>() {

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
