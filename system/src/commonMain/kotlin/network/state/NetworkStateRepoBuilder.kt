/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.system.network.state

import com.splendo.kaluga.base.singleThreadDispatcher
import kotlin.coroutines.CoroutineContext

private val defaultNetworkDispatcher by lazy {
    singleThreadDispatcher("Location")
}

/**
 * Builder for creating a [BaseNetworkStateRepo]
 */
interface BaseNetworkStateRepoBuilder {

    /**
     * Creates the [BaseNetworkStateRepo]
     * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
     * @return the created [BaseNetworkStateRepo]
     */
    fun create(coroutineContext: CoroutineContext = defaultNetworkDispatcher): BaseNetworkStateRepo
}

/**
 * Default implementation of [BaseNetworkStateRepoBuilder]
 */
expect class NetworkStateRepoBuilder : BaseNetworkStateRepoBuilder {
    override fun create(coroutineContext: CoroutineContext): NetworkStateRepo
}
