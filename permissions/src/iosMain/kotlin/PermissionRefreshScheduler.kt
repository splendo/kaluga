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

import co.touchlab.stately.concurrency.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Convenience class for scheduling checks to changes in permission state.
 * @param permissionManager The [PermissionManager] to monitor changes for.
 * @param authorizationStatus Method for requesting a the current [IOSPermissionsHelper.AuthorizationStatus] for the permission associated with the [PermissionManager]
 * @param coroutineScope The [CoroutineScope] on which to run the checks.
 */
class PermissionRefreshScheduler<P : Permission>(
    private val permissionManager: PermissionManager<P>,
    private val authorizationStatus: suspend () -> IOSPermissionsHelper.AuthorizationStatus,
    coroutineScope: CoroutineScope = permissionManager
) : CoroutineScope by coroutineScope {

    private sealed class TimerJobState(internal val coroutineScope: CoroutineScope) {
        class TimerNotRunning(coroutineScope: CoroutineScope) : TimerJobState(coroutineScope) {
            fun startTimer(interval: Long, block: suspend () -> Unit) = TimerRunning(interval, block, coroutineScope)
        }

        class TimerRunning(val interval: Long, val block: suspend () -> Unit, coroutineScope: CoroutineScope) : TimerJobState(coroutineScope) {
            private val timerLoop = coroutineScope.launch {
                while (isActive) {
                    delay(interval)
                    block()
                }
            }

            fun stopTimer() = TimerNotRunning(coroutineScope).also { timerLoop.cancel() }
        }
    }

    private var lastPermission: IOSPermissionsHelper.AuthorizationStatus? = null
    private var timerState: TimerJobState = TimerJobState.TimerNotRunning(this)
    var isWaiting = AtomicBoolean(false)
    private var timerLock = Mutex()

    /**
     * Starts monitoring for changes to the permission
     * @param interval The interval in milliseconds between checking for changes to the permission.
     */
    suspend fun startMonitoring(interval: Long) {
        updateLastPermission()
        launchTimerJob(interval)
    }

    private suspend fun launchTimerJob(interval: Long) {
        timerLock.withLock {
            val timerJobState = timerState
            if (timerJobState is TimerJobState.TimerNotRunning) {
                this.timerState = timerJobState.startTimer(interval) {
                    val status = authorizationStatus()
                    if (!isWaiting && lastPermission != status) {
                        updateLastPermission()
                        IOSPermissionsHelper.handleAuthorizationStatus(status, permissionManager)
                    }
                }
            }
        }
    }

    /**
     * Stops monitoring for changes to the permission.
     */
    suspend fun stopMonitoring() {
        timerLock.withLock {
            val timerJobState = timerState
            if (timerJobState is TimerJobState.TimerRunning) {
                this.timerState = timerJobState.stopTimer()
            }
        }
    }

    private suspend fun updateLastPermission() {
        lastPermission = authorizationStatus()
    }
}
