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

import com.splendo.kaluga.base.flow.SequentialMutableSharedFlow
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.time.Duration

interface PermissionManager<P : Permission> {

    sealed class Event {
        object PermissionGranted : Event()
        data class PermissionDenied(val locked: Boolean) : Event()
    }

    val permission: P
    val events: SharedFlow<Event>

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
    protected val coroutineScope: CoroutineScope
) : PermissionManager<P>, CoroutineScope by coroutineScope {

    companion object {
        const val DEFAULT_EVENT_BUFFER_SIZE = 256
    }

    data class Settings(
        val eventBufferSize: Int = DEFAULT_EVENT_BUFFER_SIZE,
        val logLevel: RestrictedLogLevel = RestrictedLogLevel.None
    )

    protected val logTag = "PermissionManager $permission"
    protected val logger = RestrictedLogger(settings.logLevel)
    protected val sharedEvents = SequentialMutableSharedFlow<PermissionManager.Event>(0, settings.eventBufferSize, coroutineScope)
    override val events: SharedFlow<PermissionManager.Event> = sharedEvents.asSharedFlow()

    override fun requestPermission() {
        logger.info(logTag) { "Request Permission" }
    }

    override fun startMonitoring(interval: Duration) {
        logger.debug(logTag) { "Start monitoring with interval $interval" }
    }

    override fun stopMonitoring() {
        logger.debug(logTag) { "Stop monitoring with interval" }
    }

    protected fun emitSharedEvent(event: PermissionManager.Event) {
        if (!sharedEvents.tryEmitOrLaunchAndEmit(event)) {
            logger.error(logTag) { "Failed to Emit $event instantly. This may indicate that your event buffer is full. Increase the buffer size or reduce the number of events on this thread" }
        }
    }
}
