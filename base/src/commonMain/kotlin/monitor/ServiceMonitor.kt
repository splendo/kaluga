/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.monitor

import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull

interface ServiceMonitor {
    val isServiceEnabled: Boolean
    val isEnabled: Flow<Boolean>
    fun startMonitoring()
    fun stopMonitoring()
}

abstract class DefaultServiceMonitor : ServiceMonitor {

    protected val TAG: String = this::class.simpleName ?: "ServiceMonitor"

    private val _isEnabled = MutableStateFlow<Boolean?>(null)
    override val isEnabled get() = _isEnabled.filterNotNull()

    override fun startMonitoring() {
        debug(TAG) { "Start monitoring service state ($isServiceEnabled)" }
        updateState()
    }
    override fun stopMonitoring() {
        debug(TAG) { "Stop monitoring service state" }
        _isEnabled.value = null
    }

    protected fun updateState() {
        debug(TAG) { "updateState isLocationEnabled = $isServiceEnabled" }
        _isEnabled.value = isServiceEnabled
    }
}
