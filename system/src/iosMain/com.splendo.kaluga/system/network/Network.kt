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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOf

actual class Network(
    private val reachability: NativeReachability,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {
    /**
     * Subscribe to Network.
     */
    actual fun subscribe() {
        reachability.subscribe()
    }

    /**
     * Unsubscribe from Network.
     */
    actual fun unsubscribe() {
        reachability.unsubscribe()
    }

    /**
     * Returns true when the device is connected to a network and it has connection,
     * otherwise false when the device is connected to a network but has no connection.
     *
     * @return A boolean value that identify the availability of connection.
     */
    actual val isConnectivityAvailable: kotlinx.coroutines.flow.Flow<Boolean>
        get() = flowOf(reachability.isNetworkAvailable())
}
