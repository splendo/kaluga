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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first

/**
 * Base definition of [Flowable]. Abstract class, use [ColdFlowable] or [HotFlowable] instead.
 *
 * @param T the value type to flow on.
 * @param channelFactory Factory for generating a [BroadcastChannel] on which the data is flown
 */
abstract class BaseFlowable<T>(private val channelFactory: () -> BroadcastChannel<T> = { ConflatedBroadcastChannel() }) : Flowable<T> {

    private val channel: AtomicReference<BroadcastChannel<T>?> = AtomicReference(null)
    protected fun ensureChannel(): BroadcastChannel<T> {
        return channel.get() ?: channelFactory().also { channel.set(it) }
    }

    override fun flow(flowConfig: FlowConfig): Flow<T> {
        val channel = ensureChannel()
        if (channel.isClosedForSend)
            error("channelFactory returned a channel closed for sending")
        return channel.asFlow().let { flowConfig.apply(it) }
    }

    override suspend fun set(value: T) {
        channel.get()?.send(value) ?: warn("'$value' offered to Flowable but there is no channel active")
    }


    override fun cancelFlows() {
        channel.get()?.let {
            it.close()
            channel.compareAndSet(it, null)
        }
    }

    override fun setBlocking(value: T) {
        // if a conflated broadcast channel is used it always accepts input non-blocking (provided the channel is not closed)
        runBlocking {
            set(value)
        }
    }

    protected suspend fun currentValue(): T? = channel.get()?.asFlow()?.first()
}
