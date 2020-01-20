/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.flow

import co.touchlab.stately.concurrency.AtomicInt
import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.flow.FlowConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

/**
 * A [BaseFlowable] that represents a Cold flow. This flowable will only become active once observed and deinitialises once no observers are present.
 *
 * @param T the type of the value to flow on
 * @param initialize method for determining the initial value of the flow. Will be called when the flow transitions from zero to one or more observers.
 * @param deinitialize method for deinitializing the flow, passing the last known value. Will be called when the flow transitions from one or more to zero observers.
 * @param channelFactory Factory for generating a [BroadcastChannel] on which the data is flown
 */
class ColdFlowable<T>(private val initialize: () -> T, private val deinitialize: (T) -> Unit, channelFactory: () -> BroadcastChannel<T> = { ConflatedBroadcastChannel() }) : BaseFlowable<T>(channelFactory) {

    private val flowingCounter = AtomicInt(0)

    @ExperimentalCoroutinesApi
    override fun flow(flowConfig: FlowConfig): Flow<T> {
        return super.flow(flowConfig).onStart {
                if (flowingCounter.incrementAndGet() <= 1) {
                    set(initialize())
                }
            }.onCompletion {
                if (flowingCounter.decrementAndGet() == 0) {
                    deinitialize(channel.value.asFlow().first())
                }
            }
    }

    override suspend fun set(value: T) {
        if (flowingCounter.get() > 0)
            super.set(value)
    }
}