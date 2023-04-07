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

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

interface MediaManager {

    sealed class Event {
        data class DidPrepare(val playableMedia: PlayableMedia) : Event()
        data class DidFailWithError(val error: PlaybackError) : Event()
        object DidComplete : Event()
        object DidEnd : Event()
    }

    val events: Flow<Event>
    var volume: Float

    fun createPlayableMedia(source: MediaSource): PlayableMedia?
    fun initialize(playableMedia: PlayableMedia)
    fun renderVideoOnSurface(surface: MediaSurface?)

    fun play(rate: Float)
    fun pause()
    fun stop()
    suspend fun seekTo(duration: Duration): Boolean
    fun reset()
    fun end()
}

abstract class BaseMediaManager(private val mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext) : MediaManager, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("MediaManager")) {

    interface Builder {
        fun create(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext): BaseMediaManager
    }

    private val _events = Channel<MediaManager.Event>(UNLIMITED)
    override val events: Flow<MediaManager.Event> = _events.receiveAsFlow()

    private var mediaSurfaceJob: Job? = null

    private val seekMutex = Mutex()
    private var activeSeek: Pair<Duration, CompletableDeferred<Boolean>>? = null
    private var queuedSeek: Pair<Duration, CompletableDeferred<Boolean>>? = null

    final override fun createPlayableMedia(source: MediaSource): PlayableMedia? {
        return handleCreatePlayableMedia(source).also {
            mediaSurfaceJob?.cancel()
            mediaSurfaceJob = mediaSurfaceProvider?.let {
                launch {
                    mediaSurfaceProvider.surface.onCompletion {
                        renderVideoOnSurface(null)
                    }.collect {
                        renderVideoOnSurface(it)
                    }
                }
            }
        }
    }

    protected abstract fun handleCreatePlayableMedia(source: MediaSource): PlayableMedia?

    protected fun handlePrepared(playableMedia: PlayableMedia) {
        _events.trySend(MediaManager.Event.DidPrepare(playableMedia))
    }

    protected fun handleError(error: PlaybackError) {
        _events.trySend(MediaManager.Event.DidFailWithError(error))
    }

    protected fun handleCompleted() {
        _events.trySend(MediaManager.Event.DidComplete)
    }

    final override suspend fun seekTo(duration: Duration): Boolean {
        val result = CompletableDeferred<Boolean>()
        return seekMutex.withLock {
            val queuedSeek = queuedSeek
            when {
                activeSeek == null -> {
                    activeSeek = duration to result
                    startSeek(duration)
                    result
                }
                queuedSeek != null && queuedSeek.first == duration -> queuedSeek.second
                else -> {
                    this.queuedSeek?.second?.complete(false)
                    this.queuedSeek = duration to result
                    result
                }
            }
        }.await()
    }

    final override fun reset() {
        mediaSurfaceJob?.cancel()
        handleReset()
    }

    protected abstract fun handleReset()

    override fun end() {
        cleanUp()
        _events.trySend(MediaManager.Event.DidEnd)
    }

    protected fun handleSeekCompleted(success: Boolean) {
        launch {
            seekMutex.withLock {
                activeSeek?.second?.complete(success)
                val queuedSeek = queuedSeek
                activeSeek = when {
                    queuedSeek == null -> null
                    activeSeek?.first == queuedSeek.first -> {
                        queuedSeek.second.complete(success)
                        null
                    }
                    else -> queuedSeek
                }
                activeSeek?.let { startSeek(it.first) }
            }
        }
    }

    protected abstract fun startSeek(duration: Duration)

    protected abstract fun cleanUp()
}

expect class DefaultMediaManager : BaseMediaManager
