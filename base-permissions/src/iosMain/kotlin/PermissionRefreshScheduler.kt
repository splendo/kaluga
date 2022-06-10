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

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration

/**
 * Convenience class for scheduling checks to changes in permission state.
 * @param authorizationStatus Method for requesting a the current [IOSPermissionsHelper.AuthorizationStatus] for the permission associated with the [PermissionManager]
 * @param onPermissionChanged Method for notifying changes to a permission.
 * @param coroutineScope The [CoroutineScope] on which to run the checks.
 */
class PermissionRefreshScheduler(
    private val authorizationStatus: suspend () -> IOSPermissionsHelper.AuthorizationStatus,
    private val onPermissionChanged: (IOSPermissionsHelper.AuthorizationStatus) -> Unit,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    private sealed class TimerJobState {
        object TimerNotRunning : TimerJobState() {
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

    private val lastPermission: AtomicReference<IOSPermissionsHelper.AuthorizationStatus?> = AtomicReference(null)
    val isWaiting = AtomicBoolean(false)
    private val timerState: AtomicReference<TimerJobState> = AtomicReference(TimerJobState.TimerNotRunning)
    private val timerLock = Mutex()

    /**
     * Starts monitoring for changes to the permission
     * @param interval The interval in milliseconds between checking for changes to the permission.
     */
    fun startMonitoring(interval: Duration) {
        launch {
            launchTimerJob(interval)
        }
    }

    private suspend fun launchTimerJob(interval: Duration) {
        timerLock.withLock {
            val timerJobState = timerState.get()
            if (timerJobState is TimerJobState.TimerNotRunning) {
                this.timerState.set(
                    timerJobState.startTimer(interval, this) {
                        val status = authorizationStatus()
                        if (!isWaiting.value && lastPermission.get() != status) {
                            updateLastPermission()
                            onPermissionChanged(status)
                        }
                    }
                )
            }
        }
    }

    /**
     * Stops monitoring for changes to the permission.
     */
    fun stopMonitoring() {
        launch {
            timerLock.withLock {
                val timerJobState = timerState.get()
                if (timerJobState is TimerJobState.TimerRunning) {
                    this@PermissionRefreshScheduler.timerState.set(timerJobState.stopTimer())
                }
            }
        }
        lastPermission.set(null)
    }

    private suspend fun updateLastPermission() {
        lastPermission.set(authorizationStatus())
    }
}
