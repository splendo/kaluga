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

/**
 * A [MutableSharedFlow] that can be forced to emit in a thread-safe sequential way
 */
interface SequentialMutableSharedFlow<T> : MutableSharedFlow<T> {

    /**
     * Tries to call [tryEmit] but if this fails will launch and [emit]
     * The call order of this method is preserved in a thread-safe fashion
     * @return `true` if [tryEmit] succeeded, false otherwise
     */
    fun tryEmitOrLaunchAndEmit(value: T): Boolean
}

/**
 * Creates a new [SequentialMutableSharedFlow]
 * @param replay the number of values replayed to new subscriber (cannot be negative, defaults to zero).
 * @param extraBufferCapacity the number of values buffered in addition to replay.
 * [SequentialMutableSharedFlow.emit] or [SequentialMutableSharedFlow.tryEmitOrLaunchAndEmit] does not suspend while there is a buffer space remaining
 * (optional, cannot be negative. defaults to zero).
 * @param coroutineScope The [coroutineScope] responsible for emitting when [SequentialMutableSharedFlow.tryEmitOrLaunchAndEmit] returns `false`.
 */
fun <T> SequentialMutableSharedFlow(replay: Int = 0, extraBufferCapacity: Int = 0, coroutineScope: CoroutineScope): SequentialMutableSharedFlow<T> = SequentialMutableSharedFlowImpl(replay, extraBufferCapacity, coroutineScope)

private class SequentialMutableSharedFlowImpl<T>(replay: Int = 0, extraBufferCapacity: Int = 0, coroutineScope: CoroutineScope) : SequentialMutableSharedFlow<T>, CoroutineScope by coroutineScope {

    // Internal MutableSharedFlow to maintain data. Suspends when capacity is reached
    private val internal = MutableSharedFlow<T>(replay, extraBufferCapacity, BufferOverflow.SUSPEND)
    // This channel ensures jobs added in tryEmitOrLaunchAndEmit are executed in the order at which they were added
    private val bufferOverflowChannel = Channel<Job>(capacity = Channel.UNLIMITED).apply {
        launch {
            // When a job is received, call join to wait for it to complete.
            // Since CoroutineStart.LAZY is passed, this will launch the job and call emit
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
    override fun tryEmitOrLaunchAndEmit(value: T): Boolean = tryEmit(value).also { didEmit ->
        // If tryEmit succeeded, no need to do anything
        if (!didEmit) {
            // Add a job that will call emit to the channel.
            // Since the channel waits for the previously received job to finish this will be executed in a sequential order
            // Passing CoroutineStart.LAZY ensures that the job is only executed once started (such as via the join called by the channel)
            bufferOverflowChannel.trySend(
                launch(start = CoroutineStart.LAZY) {
                    emit(value)
                }
            )
        }
    }

    override fun resetReplayCache() = internal.resetReplayCache()

}