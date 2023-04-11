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

/**
 * Manages media playback
 */
interface MediaManager {

    /**
     * Events detected by [MediaManager]
     */
    sealed class Event {

        /**
         * An [Event] indicating preparation for a [PlayableMedia] has completed successfully
         * @property playableMedia the [PlayableMedia] that was prepared
         */
        data class DidPrepare(val playableMedia: PlayableMedia) : Event()

        /**
         * An [Event] indicating an unrecoverable failure occurred
         * @property error the [PlaybackError] that occurred
         */
        data class DidFailWithError(val error: PlaybackError) : Event()

        /**
         * An [Event] indicating playback completed
         */
        object DidComplete : Event()

        /**
         * An [Event] indicating the manager was ended
         */
        object DidEnd : Event()
    }

    /**
     * A [Flow] of all the [Event] detected by the media manager
     */
    val events: Flow<Event>

    /**
     * The volume of the audio playback. A value of `0.0` indicates silence; a value of `1.0` (the default) indicates full audio volume for the player instance.
     */
    var volume: Float

    /**
     * Attempts to create a [PlayableMedia] for a given [MediaSource]
     * @param source the [MediaSource] for which to create the [PlayableMedia]
     * @return the [PlayableMedia] associated with [source] or `null` if no media could be created1
     */
    fun createPlayableMedia(source: MediaSource): PlayableMedia?

    /**
     * Initializes to manage for a given [PlayableMedia]
     * @param playableMedia the [PlayableMedia] for which to initialize
     */
    fun initialize(playableMedia: PlayableMedia)

    /**
     * Renders the video component of any initialized [PlayableMedia] on a [MediaSurface]
     */
    fun renderVideoOnSurface(surface: MediaSurface?)

    /**
     * Starts playback at a given rate
     * @param rate the rate at which playback should occur
     */
    fun play(rate: Float)

    /**
     * Pauses playback
     */
    fun pause()

    /**
     * Stops playback
     */
    fun stop()

    /**
     * Seeks to a specified time position. Will suspend until seek has completed
     * @param duration the [Duration] of the position to seek to
     * @return `true` if the seek was successful
     */
    suspend fun seekTo(duration: Duration): Boolean

    /**
     * Resets the media manager to an uninitialized state. After calling this [initialize] will have to be called again.
     */
    fun reset()

    /**
     * Releases all resources associated with the media manager.
     * This method should be called when done with the media manager.
     * After calling this playback is disabled. Any subsequent calls will result in a [PlaybackError.PlaybackHasEnded]
     */
    fun end()
}

/**
 * An abstract implementation for [MediaManager]
 * @param mediaSurfaceProvider a [MediaSurfaceProvider] that will automatically call [renderVideoOnSurface] for the latest [MediaSurface]
 * @param coroutineContext the [CoroutineContext] on which the media will be managed
 */
abstract class BaseMediaManager(private val mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext) : MediaManager, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("MediaManager")) {

    /**
     * Builder for creating a [BaseMediaManager]
     */
    interface Builder {

        /**
         * Creates a [BaseMediaManager]
         * @param mediaSurfaceProvider a [MediaSurfaceProvider] that will automatically call [renderVideoOnSurface] for the latest [MediaSurface]
         * @param coroutineContext the [CoroutineContext] on which the media will be managed
         */
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

/**
 * Default implementation of [BaseMediaManager]
 */
expect class DefaultMediaManager : BaseMediaManager
