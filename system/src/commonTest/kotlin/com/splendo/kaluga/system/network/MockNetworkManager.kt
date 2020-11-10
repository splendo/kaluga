/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.system.network

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.logging.debug

class MockNetworkManager(
    networkStateRepo: NetworkStateRepo
) : BaseNetworkManager(networkStateRepo) {

    private var _isNetworkEnabled = AtomicReference(false)
    internal var isNetworkEnabled: Boolean
        get() = _isNetworkEnabled.get()
        set(value) = _isNetworkEnabled.set(value)

    val startMonitoringNetworkCompleted = EmptyCompletableDeferred()
    val stopMonitoringNetworkCompleted = EmptyCompletableDeferred()

    override suspend fun isNetworkEnabled(): Boolean {
        debug { "Inside MockeNetworkManager isNetworkEnabled is $isNetworkEnabled" }
        return isNetworkEnabled
    }

    override suspend fun startMonitoringNetwork() {
        startMonitoringNetworkCompleted.complete()
    }

    override suspend fun stopMonitoringNetwork() {
        stopMonitoringNetworkCompleted.complete()
    }
}