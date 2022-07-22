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

import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
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
    final override val permission: P,
    settings: Settings,
    coroutineScope: CoroutineScope
) : PermissionManager<P>, CoroutineScope by coroutineScope {

    data class Settings(
        val logger: Logger = RestrictedLogger(RestrictedLogLevel.None)
    )

    protected val logTag = "PermissionManager $permission"
    protected val logger = settings.logger

    protected val eventChannel = Channel<PermissionManager.Event>(UNLIMITED)
    override val events: Flow<PermissionManager.Event> = eventChannel.receiveAsFlow()

    final override fun requestPermission() {
        logger.info(logTag) { "Request Permission" }
        requestPermissionDidStart()
    }

    protected abstract fun requestPermissionDidStart()

    final override fun startMonitoring(interval: Duration) {
        logger.debug(logTag) { "Start monitoring with interval $interval" }
        monitoringDidStart(interval)
    }

    protected abstract fun monitoringDidStart(interval: Duration)

    final override fun stopMonitoring() {
        logger.debug(logTag) { "Stop monitoring with interval" }
        monitoringDidStop()
    }

    protected abstract fun monitoringDidStop()

    protected fun emitEvent(event: PermissionManager.Event) {
        eventChannel.trySend(event)
    }
}
