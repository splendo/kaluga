/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.base.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SequentialMutableSharedFlow<T>(replay: Int = 0, extraBufferCapacity: Int = 0, coroutineScope: CoroutineScope) : MutableSharedFlow<T>, CoroutineScope by coroutineScope {

    private val internal = MutableSharedFlow<T>(replay, extraBufferCapacity, BufferOverflow.SUSPEND)
    private val bufferOverflowChannel = Channel<Job>(capacity = Channel.UNLIMITED).apply {
        launch {
            consumeEach { it.join() }
        }
    }

    override val replayCache: List<T>
        get() = internal.replayCache
    override val subscriptionCount: StateFlow<Int>
        get() = internal.subscriptionCount

    override suspend fun collect(collector: FlowCollector<T>) = internal.collect(collector)

    override suspend fun emit(value: T) = internal.emit(value)
    override fun tryEmit(value: T): Boolean = internal.tryEmit(value)
    fun tryEmitOrLaunchAndEmit(value: T): Boolean = tryEmit(value).also { didEmit ->
        if (!didEmit) {
            bufferOverflowChannel.trySend(
                launch(start = CoroutineStart.LAZY) {
                    emit(value)
                }
            )
        }
    }

    override fun resetReplayCache() = internal.resetReplayCache()
}
