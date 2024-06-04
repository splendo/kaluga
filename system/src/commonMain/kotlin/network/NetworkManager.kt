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

package com.splendo.kaluga.system.network

import kotlinx.coroutines.flow.Flow

/**
 * Manages monitoring for [NetworkConnectionType]
 */
interface NetworkManager {

    /**
     * Builder for creating a [NetworkManager]
     */
    interface Builder {

        /**
         * Creates the [NetworkManager]
         */
        fun create(): NetworkManager
    }

    /**
     * A [Flow] of the [NetworkConnectionType] detected by the network manager
     */
    val network: Flow<NetworkConnectionType>

    /**
     * Starts monitoring for changes to [NetworkConnectionType] that will be emitted on the [network] flow
     */
    suspend fun startMonitoring()

    /**
     * Stops monitoring for changes to [NetworkConnectionType]
     */
    suspend fun stopMonitoring()
}

/**
 * Default implementation of [NetworkManager]
 */
expect class DefaultNetworkManager : NetworkManager {
    override val network: Flow<NetworkConnectionType>

    override suspend fun startMonitoring()
    override suspend fun stopMonitoring()
}
