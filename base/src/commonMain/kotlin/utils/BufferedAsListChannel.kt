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
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
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

/**
 * Creates a [BufferedAsListChannel] that batches its elements with a given [CoroutineContext]
 * @param T the type of element to batch. Must be non-nullable
 * @param coroutineContext the [CoroutineContext] to use for batching
 * @param dispatcher the [CloseableCoroutineDispatcher] to which grouping will be dispatched
 * @param closeDispatcherOnCompletion if `true` the [dispatcher] will be closed once the channel is closed
 * @return the [BufferedAsListChannel] created
 */
fun <T : Any> BufferedAsListChannel(coroutineContext: CoroutineContext, dispatcher: CloseableCoroutineDispatcher, closeDispatcherOnCompletion: Boolean): BufferedAsListChannel<T> =
    BufferedAsListChannelInt(coroutineContext, dispatcher, closeDispatcherOnCompletion)

internal class BufferedAsListChannelInt<T : Any> private constructor(
    private val sendChannel: Channel<T>,
    private val receiveChannel: Channel<List<T>>,
    coroutineContext: CoroutineContext,
    dispatcher: CloseableCoroutineDispatcher,
    closeDispatcherOnCompletion: Boolean,
) : BufferedAsListChannel<T>, SendChannel<T> by sendChannel, ReceiveChannel<List<T>> by receiveChannel {

    constructor(
        coroutineContext: CoroutineContext,
    ) : this(coroutineContext, singleThreadDispatcher("GroupingChannel"), true)

    constructor(
        coroutineContext: CoroutineContext,
        dispatcher: CloseableCoroutineDispatcher,
        closeDispatcherOnCompletion: Boolean,
    ) : this(
        Channel<T>(Channel.UNLIMITED),
        Channel<List<T>>(),
        coroutineContext,
        dispatcher,
        closeDispatcherOnCompletion,
    )

    init {
        // Dispatch grouping to a separate thread so that the produce/consumption is not blocked by grouping
        CoroutineScope(coroutineContext + dispatcher).launch {
            do {
                val didSendBuffer = bufferUntilNextSend()
            } while (didSendBuffer)

            // When done buffering (i.e. the send channel is closed and all values have been send to receive channel) we should close the receive channel
            receiveChannel.close()
        }.invokeOnCompletion {
            // Close the dispatcher to prevent thread leaks
            if (closeDispatcherOnCompletion) {
                dispatcher.close()
            }
        }
    }

    private suspend fun bufferUntilNextSend(): Boolean = try {
        // Wait until at least one element is send to the send channel
        val buffer = mutableListOf(sendChannel.receive())
        do {
            // Use the select method to either:
            // - send the current buffer to the receiveChannel or
            // - to add it the next item in sendChannel to the buffer
            // Sending will take priority
            select {
                receiveChannel.onSend(buffer.toList()) {
                    buffer.clear()
                }
                // OnReceiveCatching will complete before onSend if the channel is closed
                // To account for this, we check whether its closed first
                if (!sendChannel.isClosedForReceive) {
                    sendChannel.onReceiveCatching { result ->
                        result.getOrNull()?.let {
                            buffer.add(it)
                        }
                    }
                }
            }
        } while (buffer.isNotEmpty())
        true
    } catch (_: ClosedReceiveChannelException) {
        false
    }

    @Deprecated(
        "Since 1.2.0, binary compatibility with versions <= 1.1.x",
        level = DeprecationLevel.HIDDEN,
    )
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
        // Receive channel is the last to close, so we should invoke the method there instead of on sendChannel
        receiveChannel.invokeOnClose(handler)
    }
}
