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

import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlin.coroutines.CoroutineContext

/**
 * Mock implementation of [ServiceMonitor]
 */
abstract class MockServiceMonitor(
    private val initialState: Boolean,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext = coroutineContext) {

    /**
     * [com.splendo.kaluga.test.base.mock.MethodMock] for [startMonitoring]
     */
    val startMonitoringMock = ::monitoringDidStart.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.MethodMock] for [stopMonitoring]
     */
    val stopMonitoringMock = ::monitoringDidStop.mock()

    override fun monitoringDidStart() {
        super.monitoringDidStart()
        launchTakeAndChangeState {
            if (initialState) {
                { ServiceMonitorStateImpl.Initialized.Enabled }
            } else {
                { ServiceMonitorStateImpl.Initialized.Disabled }
            }
        }
        startMonitoringMock.call()
    }
    override fun monitoringDidStop() {
        super.monitoringDidStop()
        launchTakeAndChangeState { { ServiceMonitorStateImpl.Initialized.Disabled } }
        stopMonitoringMock.call()
    }
}
