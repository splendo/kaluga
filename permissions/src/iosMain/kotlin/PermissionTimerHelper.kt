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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PermissionTimerHelper<P:Permission>(private val permissionManager: PermissionManager<P>, private val authorizationStatus: suspend () -> IOSPermissionsHelper.AuthorizationStatus, coroutineScope: CoroutineScope = permissionManager) : CoroutineScope by coroutineScope {

    private var lastPermission: IOSPermissionsHelper.AuthorizationStatus? = null
    var isWaiting: Boolean = false
    private var timerJob: Job? = null
    private var timerLock = Mutex()

    suspend fun startMonitoring(interval: Long) {
        updateLastPermission()
        launchTimerJob(interval, false)
    }

    private suspend fun launchTimerJob(interval: Long, isRelaunch: Boolean) {
        timerLock.withLock {
            if (timerJob != null && !isRelaunch) return@withLock
            timerJob = launch {
                delay(interval)
                val status = authorizationStatus()
                if (!isWaiting && lastPermission != status) {
                    updateLastPermission()
                    IOSPermissionsHelper.handleAuthorizationStatus(status, permissionManager)
                }
                launchTimerJob(interval, true)
            }
        }
    }

    suspend fun stopMonitoring() {
        timerLock.withLock {
            timerJob?.cancel()
            timerJob = null
        }
    }

    private suspend fun updateLastPermission() {
        lastPermission = authorizationStatus()
    }

}
