/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.Foundation.NSTimer

class PermissionTimerHelper<P:Permission>(private val permissionManager: PermissionManager<P>, private val authorizationStatus: suspend () -> IOSPermissionsHelper.AuthorizationStatus, coroutineScope: CoroutineScope = permissionManager) : CoroutineScope by coroutineScope {

    private var lastPermission: IOSPermissionsHelper.AuthorizationStatus? = null
    var isWaiting: Boolean = false
    private var timer: NSTimer? = null

    suspend fun startMonitoring(interval: Long) {
        updateLastPermission()
        if (timer == null) return
        NSTimer.scheduledTimerWithTimeInterval(interval.toDouble(), true) {
            launch {
                val status = authorizationStatus()
                if (!isWaiting && lastPermission != status) {
                    updateLastPermission()
                    IOSPermissionsHelper.handleAuthorizationStatus(status, permissionManager)
                }
            }
        }
    }

    suspend fun stopMonitoring() {
        timer?.invalidate()
        timer = null
    }

    private suspend fun updateLastPermission() {
        lastPermission = authorizationStatus()
    }

}

