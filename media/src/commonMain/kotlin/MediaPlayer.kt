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

import com.splendo.kaluga.base.flow.takeUntilLast
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.time.Duration

/**
 * Plays a [MediaSource]
 */
interface MediaPlayer {

    /**
     * The controls available to manage playback on a [MediaPlayer]
     * @property play the [Play] control available
     * @property pause the [Pause] control available
     * @property unpause the [Unpause] control available
     * @property stop the [Stop] control available
     * @property seek the [Seek] control available
     * @property setRate the [SetRate] control available
     * @property setLoopMode the [SetLoopMode] control available
     * @property awaitPreparation the [AwaitPreparation] control available
     * @property displayError the [DisplayError] control available
     */
    data class Controls(
        val play: Play? = null,
        val pause: Pause? = null,
        val unpause: Unpause? = null,
        val stop: Stop? = null,
        val seek: Seek? = null,
        val setRate: SetRate? = null,
        val setLoopMode: SetLoopMode? = null,
        val awaitPreparation: AwaitPreparation? = null,
        val displayError: DisplayError? = null
    ) {

        /**
         * A type of control to manage playback on a [MediaPlayer]
         */
        sealed class ControlType

        /**
         * A [ControlType] that allows a [MediaPlayer] to be started
         * @property perform action to start the [MediaPlayer] with [PlaybackState.PlaybackParameters]
         */
        class Play(val perform: suspend (PlaybackState.PlaybackParameters) -> Unit) : ControlType() {

            @JvmName("performDefault")
            @JsName("performDefault")
            /**
             * Start the [MediaPlayer]
             */
            suspend fun perform() = perform(PlaybackState.PlaybackParameters())
        }

        /**
         * A [ControlType] that allows a [MediaPlayer] to be paused
         * @property perform action to pause the [MediaPlayer]
         */
        class Pause(val perform: suspend () -> Unit) : ControlType()

        /**
         * A [ControlType] that allows a [MediaPlayer] to be resumed after being paused
         * @property perform action to resume the [MediaPlayer]
         */
        class Unpause(val perform: suspend () -> Unit) : ControlType()

        /**
         * A [ControlType] that allows a [MediaPlayer] to be stopped
         * @property perform action to stop the [MediaPlayer]
         */
        class Stop(val perform: suspend () -> Unit) : ControlType()

        /**
         * A [ControlType] that allows a [MediaPlayer] to seek its current playback to a given [Duration]
         * @property perform action to seek the current playback of the [MediaPlayer] to a given [Duration]
         */
        class Seek(val perform: suspend (Duration) -> Boolean) : ControlType()

        /**
         * A [ControlType] that provides and adjusts the rate of the current playback of a [MediaPlayer]
         * @property currentRate the rate at which playback is currently set
         * @property perform action to change the rate of the current playback of the current [MediaPlayer]
         */
        class SetRate(val currentRate: Float, val perform: suspend (Float) -> Unit) : ControlType()

        /**
         * A [ControlType] that provides and adjusts the [PlaybackState.LoopMode] of the current playback of a [MediaPlayer]
         * @property currentLoopMode the [PlaybackState.LoopMode] at which playback is currently set
         * @property perform action to change the [PlaybackState.LoopMode] of the current playback of the current [MediaPlayer]
         */
        class SetLoopMode(val currentLoopMode: PlaybackState.LoopMode, val perform: suspend (PlaybackState.LoopMode) -> Unit) : ControlType()

        /**
         * A [ControlType] that indicates a [MediaPlayer] is preparing its [PlayableMedia]
         */
        object AwaitPreparation : ControlType()

        /**
         * A [ControlType] that indicates a [MediaPlayer] is in an error state
         * @property error the [PlaybackError] encountered by the [MediaPlayer]
         */
        class DisplayError(val error: PlaybackError) : ControlType()

        /**
         * A set of all [ControlType] currently available
         */
        val controlTypes: Set<ControlType> = setOfNotNull(play, pause, unpause, stop, seek, setRate, setLoopMode, awaitPreparation, displayError)

        /**
         * Gets a given [ControlType] if available
         * @param Type the type of [ControlType] to get
         * @return an instance of [Type] or `null` if it is not available
         */
        inline fun <reified Type : ControlType> getControlType(): Type? = controlTypes.filterIsInstance<Type>().firstOrNull()
    }

    /**
     * The volume of the audio playback. A value of `0.0` indicates silence; a value of `1.0` (the default) indicates full audio volume for the player instance.
     */
    var volume: Float

    /**
     * A [Flow] of the [PlayableMedia] for which the player is controlling playback
     */
    val playableMedia: Flow<PlayableMedia?>

    /**
     * A [Flow] of the [Controls] available for playback
     */
    val controls: Flow<Controls>

    /**
     * Loads a [MediaSource] into a [PlayableMedia] to control playback for
     */
    suspend fun initializeFor(source: MediaSource)

    /**
     * Renders the video component of the [PlayableMedia] on a [MediaSurface]
     */
    fun renderVideoOnSurface(surface: MediaSurface?)

    /**
     * Suspends until playback has started.
     * @param playbackParameters the [PlaybackState.PlaybackParameters] to play with
     * @param restartIfStarted if `true` this will ensure that playback starts at [Duration.ZERO]
     * @throws [PlaybackError] if something went wrong when attempting to start playback
     */
    suspend fun forceStart(playbackParameters: PlaybackState.PlaybackParameters, restartIfStarted: Boolean = false)

    /**
     * Suspends until playback has completed
     * @throws [PlaybackError] if something went wrong during playback
     */
    suspend fun awaitCompletion()

    /**
     * Stops current playback and resets the player.
     * @throws [PlaybackError] if the player has been ended
     */
    suspend fun reset()

    /**
     * Releases all resources associated with the media player.
     * This method should be called when done with the media player.
     * After calling this playback is disabled. Any subsequent calls will result in a [PlaybackError.PlaybackHasEnded]
     */
    fun end()
}

/**
 * A default implementation of [MediaPlayer]
 * @param mediaManager the [MediaManager] to manage media playback
 * @param coroutineContext the [CoroutineContext] on which to run the media player
 */
class DefaultMediaPlayer(
    private val mediaManager: MediaManager,
    coroutineContext: CoroutineContext
) : MediaPlayer, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("MediaPlayer")) {

    /**
     * Constructor that provides a [BaseMediaManager] to manage media playback
     * @param mediaSurfaceProvider an optional [MediaSurfaceProvider] to attach to the [BaseMediaManager]
     * @param mediaManagerBuilder a [BaseMediaManager.Builder] to provide the [BaseMediaManager] to manage playback
     * @param coroutineContext the [CoroutineContext] on which to run the media player
     */
    constructor(
        mediaSurfaceProvider: MediaSurfaceProvider?,
        mediaManagerBuilder: BaseMediaManager.Builder,
        coroutineContext: CoroutineContext
    ) : this(mediaManagerBuilder.create(mediaSurfaceProvider, coroutineContext), coroutineContext)

    private val playbackStateRepo = PlaybackStateRepo(mediaManager, coroutineContext)

    init {
        launch {
            mediaManager.events.collect { event ->
                when (event) {
                    is MediaManager.Event.DidPrepare -> playbackStateRepo.takeAndChangeState(PlaybackState.Initialized::class) { it.prepared(event.playableMedia) }
                    is MediaManager.Event.DidFailWithError -> playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.failWithError(event.error) }
                    is MediaManager.Event.DidComplete -> playbackStateRepo.takeAndChangeState(PlaybackState.Playing::class) { it.completedLoop }
                    is MediaManager.Event.DidEnd -> playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.end }
                }
            }
        }
        launch {
            playbackStateRepo.takeUntilLast(false).last()
        }.invokeOnCompletion {
            if (it != null) {
                mediaManager.end()
            }
        }
    }

    override val playableMedia: Flow<PlayableMedia?> = playbackStateRepo.map {
        (it as? PlaybackState.Prepared)?.playableMedia
    }.distinctUntilChanged()
    override var volume: Float
        get() = mediaManager.volume
        set(value) { mediaManager.volume = value }

    override val controls: Flow<MediaPlayer.Controls> = playbackStateRepo.map { state ->
        when (state) {
            is PlaybackState.Idle -> MediaPlayer.Controls(
                play = MediaPlayer.Controls.Play { parameters -> forceStart(parameters) },
                seek = MediaPlayer.Controls.Seek { duration -> seekTo(duration) }
            )
            is PlaybackState.Playing -> MediaPlayer.Controls(
                pause = MediaPlayer.Controls.Pause { pause() },
                stop = MediaPlayer.Controls.Stop { stop() },
                seek = MediaPlayer.Controls.Seek { duration -> seekTo(duration) },
                setRate = MediaPlayer.Controls.SetRate(state.playbackParameters.rate) { newRate -> updateRate(newRate) },
                setLoopMode = MediaPlayer.Controls.SetLoopMode(state.playbackParameters.loopMode) { newLoopMode -> updateLoopMode(newLoopMode) }
            )
            is PlaybackState.Paused -> MediaPlayer.Controls(
                play = MediaPlayer.Controls.Play { parameters -> forceStart(parameters) },
                unpause = MediaPlayer.Controls.Unpause { unpause() },
                stop = MediaPlayer.Controls.Stop { stop() },
                seek = MediaPlayer.Controls.Seek { duration -> seekTo(duration) },
                setRate = MediaPlayer.Controls.SetRate(state.playbackParameters.rate) { newRate -> updateRate(newRate) },
                setLoopMode = MediaPlayer.Controls.SetLoopMode(state.playbackParameters.loopMode) { newLoopMode -> updateLoopMode(newLoopMode) }
            )
            is PlaybackState.Completed -> MediaPlayer.Controls(
                play = MediaPlayer.Controls.Play { parameters -> forceStart(parameters) },
                stop = MediaPlayer.Controls.Stop { stop() },
                seek = MediaPlayer.Controls.Seek { duration -> seekTo(duration) }
            )
            is PlaybackState.Stopped -> MediaPlayer.Controls(play = MediaPlayer.Controls.Play { parameters -> forceStart(parameters) })
            is PlaybackState.Error -> MediaPlayer.Controls(displayError = MediaPlayer.Controls.DisplayError(state.error))
            is PlaybackState.Ended -> MediaPlayer.Controls(displayError = MediaPlayer.Controls.DisplayError(PlaybackError.PlaybackHasEnded))
            is PlaybackState.Uninitialized -> MediaPlayer.Controls()
            is PlaybackState.Initialized -> MediaPlayer.Controls(awaitPreparation = MediaPlayer.Controls.AwaitPreparation)
        }
    }.shareIn(this, SharingStarted.WhileSubscribed())

    override suspend fun initializeFor(source: MediaSource) {
        var resetOnError = true
        playbackStateRepo.transformLatest { state ->
            when (state) {
                is PlaybackState.Uninitialized -> playbackStateRepo.takeAndChangeState(PlaybackState.Uninitialized::class) {
                    resetOnError = false
                    it.initialize(
                        source
                    )
                }
                is PlaybackState.Initialized -> emit(Unit)
                is PlaybackState.Idle -> if (state.playableMedia.source == source) emit(Unit) else playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.reset }
                is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
                is PlaybackState.Error -> if (resetOnError) {
                    playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.reset }
                } else throw state.error
                is PlaybackState.Active -> playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.reset }
            }
        }.first()
    }

    override fun renderVideoOnSurface(surface: MediaSurface?) {
        mediaManager.renderVideoOnSurface(surface)
    }

    private suspend fun start(playbackParameters: PlaybackState.PlaybackParameters) = try {
        forceStart(playbackParameters, false)
    } catch (e: PlaybackError) {
        // Do nothing as this method should be safe to call
    }

    override suspend fun forceStart(playbackParameters: PlaybackState.PlaybackParameters, restartIfStarted: Boolean) = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Uninitialized -> {} // Do nothing until Initialized
            is PlaybackState.Initialized -> {} // Do nothing until prepared
            is PlaybackState.Idle -> playbackStateRepo.takeAndChangeState(PlaybackState.Idle::class) { it.play(playbackParameters) }
            is PlaybackState.Completed -> playbackStateRepo.takeAndChangeState(PlaybackState.Completed::class) { it.start(playbackParameters) }
            is PlaybackState.Stopped -> playbackStateRepo.takeAndChangeState(PlaybackState.Stopped::class) { it.reinitialize }
            is PlaybackState.Playing -> {
                if (restartIfStarted) {
                    seekTo(Duration.ZERO)
                }
                emit(Unit)
            }
            is PlaybackState.Paused -> {
                playbackStateRepo.takeAndChangeState(PlaybackState.Paused::class) {
                    if (state.playbackParameters != playbackParameters) {
                        it.updatePlaybackParameters(playbackParameters)
                    } else {
                        if (restartIfStarted) {
                            seekTo(Duration.ZERO)
                        }
                        it.play
                    }
                }
            }
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
        }
    }.first()

    private suspend fun pause() = playbackStateRepo.takeAndChangeState(PlaybackState.Playing::class) { it.pause }

    private suspend fun unpause() = playbackStateRepo.takeAndChangeState(PlaybackState.Paused::class) { it.play }

    private suspend fun stop() = playbackStateRepo.takeAndChangeState(PlaybackState.Prepared::class) { it.stop }

    private suspend fun seekTo(duration: Duration) = playbackStateRepo.useState { state ->
        when (state) {
            is PlaybackState.Prepared -> state.seekTo(duration)
            is PlaybackState.Active,
            is PlaybackState.Ended -> false
        }
    }

    private suspend fun updateRate(rate: Float) {
        playbackStateRepo.takeAndChangeState(PlaybackState.Started::class) { state ->
            state.updatePlaybackParameters(state.playbackParameters.copy(rate = rate))
        }
    }

    private suspend fun updateLoopMode(loopMode: PlaybackState.LoopMode) = playbackStateRepo.takeAndChangeState(PlaybackState.Started::class) { state ->
        state.updatePlaybackParameters(state.playbackParameters.copy(loopMode = loopMode))
    }

    override suspend fun awaitCompletion() = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Completed -> emit(Unit)
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
            is PlaybackState.Active -> {} // Wait until completed
        }
    }.first()

    override suspend fun reset() {
        playbackStateRepo.transformLatest { state ->
            when (state) {
                is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
                is PlaybackState.Uninitialized -> emit(Unit)
                is PlaybackState.Active -> playbackStateRepo.takeAndChangeState(remainIfStateNot = PlaybackState.Active::class) { it.reset }
                is PlaybackState.Error -> playbackStateRepo.takeAndChangeState(remainIfStateNot = PlaybackState.Error::class) { it.reset }
            }
        }.first()
    }

    override fun end() {
        launch {
            playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.end }
        }
    }
}

/**
 * A [Flow] that provides the [Duration] of the current [PlayableMedia] of a [MediaPlayer]. If no [PlayableMedia] is loaded this will result in [Duration.ZERO].
 */
val MediaPlayer.duration: Flow<Duration> get() = playableMedia.map { it?.duration ?: Duration.ZERO }

/**
 * A [Flow] that polls the playtime of the current [PlayableMedia] of a [MediaPlayer] as a [Duration]. If no [PlayableMedia] is loaded this will result in [Duration.ZERO].
 * @param pollingInterval the [Duration] between polling for the playtime
 */
fun MediaPlayer.playTime(pollingInterval: Duration) = playableMedia.transformLatest { media ->
    media?.let {
        while (currentCoroutineContext().isActive) {
            emit(it.currentPlayTime)
            delay(pollingInterval)
        }
    } ?: emit(Duration.ZERO)
}

/**
 * Forces the [MediaPlayer] to start playback and suspends until playback has completed
 * @param playbackParameters the [PlaybackState.PlaybackParameters] to play with
 * @param restartIfStarted if `true` this will ensure that playback starts at [Duration.ZERO]
 */
suspend fun MediaPlayer.play(playbackParameters: PlaybackState.PlaybackParameters = PlaybackState.PlaybackParameters(), restartIfStarted: Boolean = false) {
    forceStart(playbackParameters, restartIfStarted)
    awaitCompletion()
}
