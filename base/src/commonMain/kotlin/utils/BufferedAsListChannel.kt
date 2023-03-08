/*
 * Copyright 2023 Splendo Consulting B.V. The Netherlands
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

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.singleThreadDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelIterator
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.SelectClause1
import kotlinx.coroutines.selects.SelectClause2
import kotlin.coroutines.CoroutineContext

/**
 * A Rendezvous Channel that buffers all elements sent to it in a list until the next receive
 * @param T the type of element to batch. Must be non-nullable
 */
interface BufferedAsListChannel<T : Any> : SendChannel<T>, ReceiveChannel<List<T>>

/**
 * Creates a [BufferedAsListChannel] that batches its elements with a given [CoroutineContext]
 * @param T the type of element to batch. Must be non-nullable
 * @param coroutineContext the [CoroutineContext] to use for batching
 * @return the [BufferedAsListChannel] created
 */
fun <T : Any> BufferedAsListChannel(coroutineContext: CoroutineContext): BufferedAsListChannel<T> = BatchingChannelInt(coroutineContext)

internal class BatchingChannelInt<T : Any>(coroutineContext: CoroutineContext) : BufferedAsListChannel<T> {
    private val sendChannel = Channel<T>(Channel.UNLIMITED)
    private val receiveChannel = Channel<List<T>>()

    init {
        val dispatcher = singleThreadDispatcher("GroupingChannel")
        CoroutineScope(coroutineContext + dispatcher).launch {
            while (!sendChannel.isClosedForReceive && !receiveChannel.isClosedForSend) {
                val list = mutableListOf<T>()
                do {
                    do {
                        val lastReceived = sendChannel.tryReceive().getOrNull()?.also {
                            list.add(it)
                        }
                    } while (lastReceived != null)
                } while (
                    list.isNotEmpty() && !receiveChannel.isClosedForSend && receiveChannel.trySend(list).isFailure
                )
            }
            receiveChannel.close()
        }.invokeOnCompletion {
            dispatcher.close()
        }
    }

    override val isClosedForSend: Boolean
        get() = sendChannel.isClosedForSend
    override val isClosedForReceive: Boolean
        get() = receiveChannel.isClosedForReceive

    override val isEmpty: Boolean
        get() = receiveChannel.isEmpty
    override val onReceive: SelectClause1<List<T>>
        get() = receiveChannel.onReceive
    override val onReceiveCatching: SelectClause1<ChannelResult<List<T>>>
        get() = receiveChannel.onReceiveCatching

    override fun cancel(cause: Throwable?): Boolean {
        sendChannel.cancel(cause as? CancellationException)
        receiveChannel.cancel(cause as? CancellationException)
        return true
    }

    override val onSend: SelectClause2<T, SendChannel<T>>
        get() = sendChannel.onSend

    override fun trySend(element: T): ChannelResult<Unit> = sendChannel.trySend(element)
    override suspend fun send(element: T) {
        sendChannel.send(element)
    }

    override fun tryReceive(): ChannelResult<List<T>> = receiveChannel.tryReceive()
    override suspend fun receive(): List<T>  = receiveChannel.receive()
    override suspend fun receiveCatching(): ChannelResult<List<T>> = receiveChannel.receiveCatching()

    override fun iterator(): ChannelIterator<List<T>> = receiveChannel.iterator()

    override fun cancel(cause: CancellationException?) {
        sendChannel.cancel(cause)
        receiveChannel.cancel(cause)
    }

    override fun close(cause: Throwable?): Boolean = sendChannel.close(cause)
    override fun invokeOnClose(handler: (cause: Throwable?) -> Unit) {
        sendChannel.invokeOnClose(handler)
        receiveChannel.invokeOnClose(handler)
    }
}
