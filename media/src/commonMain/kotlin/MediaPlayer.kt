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

import com.splendo.kaluga.media.MediaPlayer.Controls.AwaitPreparation
import com.splendo.kaluga.media.MediaPlayer.Controls.DisplayError
import com.splendo.kaluga.media.MediaPlayer.Controls.Pause
import com.splendo.kaluga.media.MediaPlayer.Controls.Play
import com.splendo.kaluga.media.MediaPlayer.Controls.Seek
import com.splendo.kaluga.media.MediaPlayer.Controls.SetLoopMode
import com.splendo.kaluga.media.MediaPlayer.Controls.SetRate
import com.splendo.kaluga.media.MediaPlayer.Controls.Stop
import com.splendo.kaluga.media.MediaPlayer.Controls.Unpause
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
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
interface MediaPlayer : VolumeController, MediaSurfaceController {

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
        val displayError: DisplayError? = null,
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
        data object AwaitPreparation : ControlType()

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

        /**
         * Attempts to do [Play.perform] on [play] if it exists
         * @param playbackParameters the [PlaybackState.PlaybackParameters] to play with
         * @return `true` if play was successfully completed, `false` if [play] was empty
         */
        suspend fun tryPlay(playbackParameters: PlaybackState.PlaybackParameters = PlaybackState.PlaybackParameters()) = tryPerformControlType<Play> {
            perform(playbackParameters)
        }

        /**
         * Attempts to do [Pause.perform] on [pause] if it exists
         * @return `true` if pause was successfully completed, `false` if [pause] was empty
         */
        suspend fun tryPause(): Boolean = tryPerformControlType<Pause> { perform() }

        /**
         * Attempts to do [Unpause.perform] on [unpause] if it exists
         * @return `true` if unpause was successfully completed, `false` if [unpause] was empty
         */
        suspend fun tryUnpause(): Boolean = tryPerformControlType<Unpause> { perform() }

        /**
         * Attempts to do [Stop.perform] on [stop] if it exists
         * @return `true` if stop was successfully completed, `false` if [stop] was empty
         */
        suspend fun tryStop(): Boolean = tryPerformControlType<Stop> { perform() }

        /**
         * Attempts to do [Seek.perform] on [seek] if it exists
         * @return `true` if seek was successfully completed, `false` if [seek] was empty or failed to complete
         */
        suspend fun trySeek(duration: Duration): Boolean = tryPerformControlTypeWithResult<Seek> { perform(duration) }

        /**
         * Attempts to do [SetRate.perform] on [setRate] if it exists
         * @return `true` if setRate was successfully completed, `false` if [setRate] was empty
         */
        suspend fun trySetRate(rate: Float): Boolean = tryPerformControlType<SetRate> { perform(rate) }

        /**
         * Attempts to do [SetLoopMode.perform] on [setLoopMode] if it exists
         * @return `true` if setLoopMode was successfully completed, `false` if [setLoopMode] was empty
         */
        suspend fun trySetLoopMode(loopMode: PlaybackState.LoopMode): Boolean = tryPerformControlType<SetLoopMode> { perform(loopMode) }

        private inline fun <reified Type : ControlType> tryPerformControlType(block: Type.() -> Unit): Boolean = tryPerformControlTypeWithResult<Type> {
            block()
            true
        }

        private inline fun <reified Type : ControlType> tryPerformControlTypeWithResult(block: Type.() -> Boolean): Boolean = getControlType<Type>()?.let {
            block.invoke(it)
        } ?: false
    }

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
    fun close()
}

/**
 * A default implementation of [MediaPlayer]
 * @param createPlaybackStateRepo method for creating a [BasePlaybackStateRepo] to manage the [PlaybackState] of this player
 * @param coroutineContext the [CoroutineContext] on which to run the media player
 */
class DefaultMediaPlayer(
    createPlaybackStateRepo: (CoroutineContext) -> BasePlaybackStateRepo,
    coroutineContext: CoroutineContext,
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
        coroutineContext: CoroutineContext,
    ) : this(
        { context ->
            val mediaManager = mediaManagerBuilder.create(mediaSurfaceProvider, context)
            PlaybackStateRepo(mediaManager, context)
        },
        coroutineContext,
    )

    private val playbackStateRepo = createPlaybackStateRepo(coroutineContext)

    override val playableMedia: Flow<PlayableMedia?> = playbackStateRepo.map {
        (it as? PlaybackState.Prepared)?.playableMedia
    }.distinctUntilChanged()

    override val currentVolume: Flow<Float> = playbackStateRepo.flatMapLatest { state ->
        when (state) {
            is PlaybackState.Active -> state.volumeController.currentVolume
            is PlaybackState.Closed -> emptyFlow()
        }
    }

    override suspend fun updateVolume(volume: Float) {
        playbackStateRepo.useState { state ->
            when (state) {
                is PlaybackState.Active -> state.volumeController.updateVolume(volume)
                is PlaybackState.Closed -> throw PlaybackError.PlaybackHasEnded
            }
        }
    }

    override val controls: Flow<MediaPlayer.Controls> = playbackStateRepo.map { state ->
        when (state) {
            is PlaybackState.Idle -> MediaPlayer.Controls(
                play = playControl,
                seek = seekControl,
            )
            is PlaybackState.Playing -> MediaPlayer.Controls(
                pause = pauseControl,
                stop = stopControl,
                seek = seekControl,
                setRate = state.createSetRateControls(),
                setLoopMode = state.createSetLoopModeControls(),
            )
            is PlaybackState.Paused -> MediaPlayer.Controls(
                play = Play { parameters -> forceStart(parameters, true) },
                unpause = Unpause { unpause() },
                stop = stopControl,
                seek = seekControl,
                setRate = state.createSetRateControls(),
                setLoopMode = state.createSetLoopModeControls(),
            )
            is PlaybackState.Completed -> MediaPlayer.Controls(
                play = playControl,
                stop = stopControl,
                seek = seekControl,
            )
            is PlaybackState.Stopped -> MediaPlayer.Controls(play = playControl)
            is PlaybackState.Error -> MediaPlayer.Controls(displayError = DisplayError(state.error))
            is PlaybackState.Closed -> MediaPlayer.Controls(displayError = DisplayError(PlaybackError.PlaybackHasEnded))
            is PlaybackState.Uninitialized -> MediaPlayer.Controls()
            is PlaybackState.Initialized -> MediaPlayer.Controls(awaitPreparation = AwaitPreparation)
        }
    }.shareIn(this, SharingStarted.WhileSubscribed())

    private val playControl: Play = Play { parameters -> forceStart(parameters) }
    private val pauseControl: Pause = Pause { pause() }
    private val stopControl: Stop = Stop { stop() }
    private val seekControl: Seek = Seek { duration -> seekTo(duration) }
    private fun PlaybackState.Started.createSetRateControls(): SetRate = SetRate(playbackParameters.rate) { newRate -> updateRate(newRate) }
    private fun PlaybackState.Started.createSetLoopModeControls(): SetLoopMode = SetLoopMode(playbackParameters.loopMode) { newLoopMode -> updateLoopMode(newLoopMode) }

    override suspend fun initializeFor(source: MediaSource) {
        var resetOnError = true
        playbackStateRepo.transformLatest { state ->
            when (state) {
                is PlaybackState.Uninitialized -> changePlaybackState<PlaybackState.Uninitialized> {
                    resetOnError = false
                    it.initialize(
                        source,
                    )
                }
                is PlaybackState.Initialized -> {
                    if (state.source == source) {
                        emit(Unit)
                    } else {
                        changePlaybackState<PlaybackState.Active> { it.reset }
                    }
                }
                is PlaybackState.Idle -> {
                    if (state.playableMedia.source == source) {
                        emit(Unit)
                    } else {
                        changePlaybackState<PlaybackState.Active> { it.reset }
                    }
                }
                is PlaybackState.Closed -> throw PlaybackError.PlaybackHasEnded
                is PlaybackState.Error -> {
                    if (resetOnError) {
                        changePlaybackState<PlaybackState.Active> { it.reset }
                    } else {
                        throw state.error
                    }
                }
                is PlaybackState.Active -> changePlaybackState<PlaybackState.Active> { it.reset }
            }
        }.first()
    }

    override suspend fun renderVideoOnSurface(surface: MediaSurface?) {
        playbackStateRepo.useState { state ->
            when (state) {
                is PlaybackState.Active -> state.mediaSurfaceController.renderVideoOnSurface(surface)
                is PlaybackState.Closed -> throw PlaybackError.PlaybackHasEnded
            }
        }
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
            is PlaybackState.Idle -> changePlaybackState<PlaybackState.Idle> { it.play(playbackParameters) }
            is PlaybackState.Completed -> changePlaybackState<PlaybackState.Completed> { it.start(playbackParameters) }
            is PlaybackState.Stopped -> changePlaybackState<PlaybackState.Stopped> { it.reinitialize }
            is PlaybackState.Playing -> {
                if (state.playbackParameters != playbackParameters) {
                    changePlaybackState<PlaybackState.Playing> {
                        it.updatePlaybackParameters(playbackParameters)
                    }
                } else {
                    if (restartIfStarted) {
                        state.seekTo(Duration.ZERO)
                    }
                    emit(Unit)
                }
            }
            is PlaybackState.Paused -> {
                changePlaybackState<PlaybackState.Paused> {
                    if (state.playbackParameters != playbackParameters) {
                        it.updatePlaybackParameters(playbackParameters)
                    } else {
                        if (restartIfStarted) {
                            state.seekTo(Duration.ZERO)
                        }
                        it.play
                    }
                }
            }
            is PlaybackState.Closed -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
        }
    }.first()

    private suspend fun pause() = changePlaybackState<PlaybackState.Playing> { it.pause }

    private suspend fun unpause() = changePlaybackState<PlaybackState.Paused> { it.play }

    private suspend fun stop() = changePlaybackState<PlaybackState.Prepared> { it.stop }

    private suspend fun seekTo(duration: Duration) = playbackStateRepo.useState { state ->
        when (state) {
            is PlaybackState.Prepared -> state.seekTo(duration)
            is PlaybackState.Active,
            is PlaybackState.Closed,
            -> false
        }
    }

    private suspend fun updateRate(rate: Float) {
        changePlaybackState<PlaybackState.Started> { state ->
            state.updatePlaybackParameters(state.playbackParameters.copy(rate = rate))
        }
    }

    private suspend fun updateLoopMode(loopMode: PlaybackState.LoopMode) = changePlaybackState<PlaybackState.Started> { state ->
        state.updatePlaybackParameters(state.playbackParameters.copy(loopMode = loopMode))
    }

    override suspend fun awaitCompletion() = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Completed -> emit(Unit)
            is PlaybackState.Closed -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
            is PlaybackState.Active -> {} // Wait until completed
        }
    }.first()

    override suspend fun reset() {
        playbackStateRepo.transformLatest { state ->
            when (state) {
                is PlaybackState.Closed -> throw PlaybackError.PlaybackHasEnded
                is PlaybackState.Uninitialized -> emit(Unit)
                is PlaybackState.Active -> changePlaybackState<PlaybackState.Active> { it.reset }
                is PlaybackState.Error -> changePlaybackState<PlaybackState.Error> { it.reset }
            }
        }.first()
    }

    private suspend inline fun <reified State : PlaybackState> changePlaybackState(noinline action: suspend (State) -> suspend () -> PlaybackState) =
        playbackStateRepo.takeAndChangeState(remainIfStateNot = State::class, action)

    override fun close() {
        launch {
            changePlaybackState<PlaybackState.Active> { it.end }
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
