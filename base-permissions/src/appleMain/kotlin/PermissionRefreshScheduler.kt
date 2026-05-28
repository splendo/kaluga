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

package com.splendo.kaluga.permissions.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration

/**
 * Provides the current [IOSPermissionsHelper.AuthorizationStatus] of a given permission.
 */
interface CurrentAuthorizationStatusProvider {
    suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus
}

/**
 * Convenience class for scheduling checks to changes in permission state.
 * @param currentAuthorizationStatusProvider The [CurrentAuthorizationStatusProvider] used for getting the [IOSPermissionsHelper.AuthorizationStatus] of the permission.
 * @param authorizationStatusHandler [AuthorizationStatusHandler] that is notified when changes to a permission occurs.
 * @param coroutineScope The [CoroutineScope] on which to run the checks.
 */
class PermissionRefreshScheduler(
    private val currentAuthorizationStatusProvider: CurrentAuthorizationStatusProvider,
    private val authorizationStatusHandler: AuthorizationStatusHandler,
    coroutineScope: CoroutineScope,
) : CoroutineScope by coroutineScope {

    private sealed class TimerJobState {
        data object TimerNotRunning : TimerJobState() {
            fun startTimer(interval: Duration, coroutineScope: CoroutineScope, block: suspend () -> Unit): TimerJobState = TimerRunning(interval, block, coroutineScope)
        }

        class TimerRunning(val interval: Duration, val block: suspend () -> Unit, coroutineScope: CoroutineScope) : TimerJobState() {
            private val timerLoop = coroutineScope.launch {
                while (isActive) {
                    delay(interval)
                    block()
                }
            }

            fun stopTimer() = TimerNotRunning.also { timerLoop.cancel() }
        }
    }

    private var lastPermission: IOSPermissionsHelper.AuthorizationStatus? = null
    internal val waitingLock = Mutex()
    private var timerState: TimerJobState = TimerJobState.TimerNotRunning
    private val lock = Mutex()

    /**
     * Starts monitoring for changes to the [IOSPermissionsHelper.AuthorizationStatus]
     * @param interval The [Duration] between checking for changes to the permission.
     */
    fun startMonitoring(interval: Duration) {
        launch {
            launchTimerJob(interval)
        }
    }

    private suspend fun launchTimerJob(interval: Duration) {
        lock.withLock {
            val timerJobState = timerState
            if (timerJobState is TimerJobState.TimerNotRunning) {
                this.timerState = timerJobState.startTimer(interval, this) {
                    val status = currentAuthorizationStatusProvider.provide()
                    if (!waitingLock.isLocked && lastPermission != status) {
                        lastPermission = currentAuthorizationStatusProvider.provide()
                        authorizationStatusHandler.status(status)
                    }
                }
            }
        }
    }

    /**
     * Stops monitoring for changes to the [IOSPermissionsHelper.AuthorizationStatus].
     */
    fun stopMonitoring() {
        launch {
            lock.withLock {
                val timerJobState = timerState
                if (timerJobState is TimerJobState.TimerRunning) {
                    this@PermissionRefreshScheduler.timerState = timerJobState.stopTimer()
                }
                lastPermission = null
            }
        }
    }
}
