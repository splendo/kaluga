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

package com.splendo.kaluga.test.service

import com.splendo.kaluga.service.ServiceMonitor
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.flow.StateFlow

/**
 * Mock implementation of [ServiceMonitor]
 */
abstract class MockServiceMonitor : ServiceMonitor {
    abstract override val isEnabled: StateFlow<Boolean>
    override val isServiceEnabled: Boolean
        get() = isEnabled.value

    /**
     * [com.splendo.kaluga.test.base.mock.MethodMock] for [startMonitoring]
     */
    val startMonitoringMock = ::startMonitoring.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.MethodMock] for [stopMonitoring]
     */
    val stopMonitoringMock = ::stopMonitoring.mock()

    override fun startMonitoring(): Unit = startMonitoringMock.call()
    override fun stopMonitoring(): Unit = stopMonitoringMock.call()
}
