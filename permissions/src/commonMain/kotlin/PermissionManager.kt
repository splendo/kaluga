package com.splendo.kaluga.permissions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

abstract class PermissionManager<P : Permission> (private val stateRepo: PermissionStateRepo<P>) : CoroutineScope by stateRepo {

    abstract suspend fun requestPermission()
    abstract suspend fun initializeState() : PermissionState<P>

    abstract suspend fun startMonitoring(interval: Long)
    abstract suspend fun stopMonitoring()

    open fun grantPermission() {
        stateRepo.launch {
            stateRepo.takeAndChangeState { state ->
                when (state) {
                    is PermissionState.Denied -> state.allow
                    is PermissionState.Allowed -> state.remain
                }
            }
        }
    }

    open fun revokePermission(locked: Boolean) {
        stateRepo.launch {
            stateRepo.takeAndChangeState { state ->
                when (state) {
                    is PermissionState.Allowed -> state.deny(locked)
                    is PermissionState.Denied.Requestable -> {
                        if (locked) state.lock else state.remain
                    }
                    is PermissionState.Denied.Locked -> {
                        if (locked) state.remain else state.unlock
                    }
                }
            }
        }
    }

}