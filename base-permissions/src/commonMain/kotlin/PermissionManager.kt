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

package com.splendo.kaluga.permissions.base

import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.time.Duration

interface PermissionManager<P : Permission> {

    sealed class Event {
        object PermissionGranted : Event()
        data class PermissionDenied(val locked: Boolean) : Event()
    }

    val permission: P
    val events: Flow<Event>

    /**
     * Starts to request the permission
     */
    fun requestPermission()

    /**
     * Starts monitoring for changes to the permission.
     * @param interval The [Duration] in between checking for changes to the permission state
     */
    fun startMonitoring(interval: Duration)

    /**
     * Stops monitoring for changes to the permission.
     */
    fun stopMonitoring()
}

/**
 * Manager for maintaining the [PermissionState] of a given [Permission]
 * @param stateRepo The [PermissionStateRepo] managed by this manager.
 */
abstract class BasePermissionManager<P : Permission>(
    override val permission: P,
    private val settings: Settings,
    private val coroutineScope: CoroutineScope
) : PermissionManager<P>, CoroutineScope by coroutineScope {

    data class Settings(
        val logLevel: LogLevel = LogLevel.NONE
    )

    enum class LogLevel {
        NONE,
        INFO,
        VERBOSE
    }

    private val logTag = "PermissionManager $permission"
    private val sharedEvents = Channel<PermissionManager.Event>(UNLIMITED)
    override val events: Flow<PermissionManager.Event> = sharedEvents.receiveAsFlow()

    override fun requestPermission() {
        logInfo { "Request Permission" }
    }

    override fun startMonitoring(interval: Duration) {
        logDebug { "Start monitoring with interval $interval" }
    }

    override fun stopMonitoring() {
        logDebug { "Stop monitoring with interval" }
    }

    open fun grantPermission() {
        logInfo { "Permission Granted" }
        emitSharedEvent(PermissionManager.Event.PermissionGranted)
    }

    open fun revokePermission(locked: Boolean) {
        logInfo { if (locked) { "Permission Locked" } else { "Permission Revoked" } }
        emitSharedEvent(PermissionManager.Event.PermissionDenied(locked))
    }

    private fun emitSharedEvent(event: PermissionManager.Event) {
        sharedEvents.trySend(event)
    }

    protected fun logInfo(message: () -> String) {
        if (settings.logLevel != LogLevel.NONE) {
            info(logTag, message)
        }
    }

    protected fun logDebug(message: () -> String) {
        if (settings.logLevel == LogLevel.VERBOSE) {
            debug(logTag, message)
        }
    }

    protected fun logError(message: () -> String) {
        if (settings.logLevel == LogLevel.VERBOSE) {
            com.splendo.kaluga.logging.error(logTag, message)
        }
    }
}
