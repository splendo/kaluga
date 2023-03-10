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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
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
fun <T : Any> BufferedAsListChannel(coroutineContext: CoroutineContext): BufferedAsListChannel<T> = BufferedAsListChannelInt(coroutineContext)

internal class BufferedAsListChannelInt<T : Any> private constructor(
    private val sendChannel: Channel<T>,
    private val receiveChannel: Channel<List<T>>,
    coroutineContext: CoroutineContext
) : BufferedAsListChannel<T>, SendChannel<T> by sendChannel, ReceiveChannel<List<T>> by receiveChannel {

    constructor(
        coroutineContext: CoroutineContext
    ) : this(
        Channel<T>(Channel.UNLIMITED),
        Channel<List<T>>(),
        coroutineContext
    )

    init {
        val dispatcher = singleThreadDispatcher("GroupingChannel")
        CoroutineScope(coroutineContext + dispatcher).launch {
            while (!sendChannel.isClosedForReceive && !receiveChannel.isClosedForSend) {
                try {
                    val list = mutableListOf(sendChannel.receive())
                    do {
                        do {
                            val lastReceived = sendChannel.tryReceive().getOrNull()?.also {
                                list.add(it)
                            }
                        } while (lastReceived != null)
                    } while (
                        !receiveChannel.isClosedForSend && receiveChannel.trySend(list).isFailure.also { yield() }
                    )
                } catch (_: ClosedReceiveChannelException) { }
            }
            receiveChannel.close()
        }.invokeOnCompletion {
            dispatcher.close()
        }
    }

    override fun cancel(cause: Throwable?): Boolean {
        sendChannel.cancel(cause as? CancellationException)
        receiveChannel.cancel(cause as? CancellationException)
        return true
    }

    override fun cancel(cause: CancellationException?) {
        sendChannel.cancel(cause)
        receiveChannel.cancel(cause)
    }

    override fun invokeOnClose(handler: (cause: Throwable?) -> Unit) {
        sendChannel.invokeOnClose(handler)
        receiveChannel.invokeOnClose(handler)
    }
}
