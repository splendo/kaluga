/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.service

import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

/**
 * Interface to monitor whether a given service is enabled
 */
interface ServiceMonitor {
    /**
     * If `true` the service is currently enabled.
     */
    val isServiceEnabled: Boolean

    /**
     * A [Flow] representing the enabled status of the service
     */
    val isEnabled: Flow<Boolean>

    /**
     * When called, the [ServiceMonitor] will start monitoring for changes to [isServiceEnabled]
     */
    fun startMonitoring()

    /**
     * When called, the [ServiceMonitor] will stop monitoring for changes to [isServiceEnabled]
     */
    fun stopMonitoring()
}

/**
 * Default implementation of [ServiceMonitor].
 * @param logger The [Logger] to log and changes to.
 */
abstract class DefaultServiceMonitor(protected val logger: Logger = RestrictedLogger(RestrictedLogLevel.None)) : ServiceMonitor {

    protected open val logTag: String = this::class.simpleName ?: "ServiceMonitor"

    private val _isEnabled = MutableStateFlow<Boolean?>(null)
    override val isEnabled get() = _isEnabled.filterNotNull()

    final override fun startMonitoring() {
        logger.debug(logTag) { "Start monitoring service state ($isServiceEnabled)" }
        updateState()
        monitoringDidStart()
    }

    protected abstract fun monitoringDidStart()

    final override fun stopMonitoring() {
        logger.debug(logTag) { "Stop monitoring service state" }
        _isEnabled.value = null
        monitoringDidStop()
    }

    protected abstract fun monitoringDidStop()

    protected fun updateState() {
        logger.debug(logTag) { "updateState isLocationEnabled = $isServiceEnabled" }
        _isEnabled.value = isServiceEnabled
    }
}
