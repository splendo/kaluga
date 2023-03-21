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

package com.splendo.kaluga.media

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

expect class PlayableMedia {
    val duration: Duration
    val currentPlayTime: Duration
}

interface MediaManager {

    sealed class Event {
        data class DidPrepare(val playableMedia: PlayableMedia) : Event()
        data class DidFailWithError(val error: PlaybackError) : Event()
        object DidComplete : Event()
        object DidEnd : Event()
    }

    val events: Flow<Event>

    fun createPlayableMedia(url: String): PlayableMedia?
    fun initialize(playableMedia: PlayableMedia)

    fun play()
    fun pause()
    fun stop()
    fun seekTo(duration: Duration)
    fun end()
}

abstract class BaseMediaManager(coroutineContext: CoroutineContext) : MediaManager, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("MediaManager")) {

    interface Builder {
        fun create(coroutineContext: CoroutineContext): BaseMediaManager
    }

    private val _events = Channel<MediaManager.Event>(UNLIMITED)
    override val events: Flow<MediaManager.Event> = _events.receiveAsFlow()

    protected fun handlePrepared(playableMedia: PlayableMedia) {
        _events.trySend(MediaManager.Event.DidPrepare(playableMedia))
    }

    protected fun handleError(error: PlaybackError) {
        _events.trySend(MediaManager.Event.DidFailWithError(error))
    }

    protected fun handleCompleted() {
        _events.trySend(MediaManager.Event.DidComplete)
    }

    override fun end() {
        cleanUp()
        _events.trySend(MediaManager.Event.DidEnd)
    }

    protected abstract fun cleanUp()
}

expect class DefaultMediaManager : BaseMediaManager
