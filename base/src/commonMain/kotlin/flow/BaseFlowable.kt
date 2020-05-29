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

package com.splendo.kaluga.flow

import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first

/**
 * Base definition of [Flowable]. Abstract class, use [ColdFlowable] or [HotFlowable] instead.
 *
 * @param T the value type to flow on.
 * @param channelFactory Factory for generating a [BroadcastChannel] on which the data is flown
 */
abstract class BaseFlowable<T>(private val channelFactory: () -> BroadcastChannel<T> = { ConflatedBroadcastChannel() }) : Flowable<T> {

    protected var channel: Lazy<BroadcastChannel<T>>? = lazy {channelFactory() }

    @ExperimentalCoroutinesApi
    override fun flow(flowConfig: FlowConfig): Flow<T> {
        return channel?.value?.asFlow()?.let { flowConfig.apply(it) } ?: emptyFlow()
    }

    override suspend fun set(value: T) {
        channel?.value?.send(value)
    }

    fun close() {
        channel?.value?.close()
        channel = null
    }

    override fun setBlocking(value:T) {
        // if a conflated broadcast channel is used it always accepts input non-blocking (provided the channel is not closed)
        runBlocking {
            set(value)
        }
    }
}