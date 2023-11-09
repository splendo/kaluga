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

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.base.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import kotlin.time.Duration

/**
 * An [Exception] that may occur during playback
 */
sealed class PlaybackError : Exception() {

    /**
     * A [PlaybackError] that indicates a [MediaSource] is not supported
     */
    data object Unsupported : PlaybackError()

    /**
     * A [PlaybackError] that indicates file or network related operation errors.
     */
    data object IO : PlaybackError()

    /**
     * A [PlaybackError] that indicates some operation takes too long to complete, usually more than 3-5 seconds.
     */
    data object TimedOut : PlaybackError()

    /**
     * A [PlaybackError] that indicates bitstream is not conforming to the related coding standard or file spec.
     */
    data object MalformedMediaSource : PlaybackError()

    /**
     * A [PlaybackError] that indicates that playback has ended and resources have been released.
     */
    data object PlaybackHasEnded : PlaybackError()

    /**
     * An unknown [PlaybackError].
     */
    data object Unknown : PlaybackError()
}

/**
 * A [KalugaState] of playback
 */
sealed interface PlaybackState : KalugaState {

    /**
     * Parameters to configure playback
     * @property rate the rate at which playback should be set. Should be a positive number. Defaults to `1.0`.
     * @property loopMode the [LoopMode] to apply when playback has completed
     */
    data class PlaybackParameters(
        val rate: Float = 1.0f,
        val loopMode: LoopMode = LoopMode.NotLooping,
    )

    /**
     * Determines the behaviour when playback of a [PlayableMedia] has completed
     */
    sealed class LoopMode {

        /**
         * A [LoopMode] that never loops
         */
        data object NotLooping : LoopMode()

        /**
         * A [LoopMode] that loops infinitely
         */
        data object LoopingForever : LoopMode()

        /**
         * A [LoopMode] that loops for a fixed number of times
         * @property loops the number of loops still remaining (including this loop)
         */
        data class LoopingForFixedNumber(val loops: UInt) : LoopMode()
    }

    /**
     * A [PlaybackState] indicating that playback has not ended
     */
    sealed interface Active : PlaybackState {

        /**
         * The [VolumeController] for adjusting the volume
         */
        val volumeController: VolumeController

        /**
         * The [MediaSurfaceController] for rendering to a video
         */
        val mediaSurfaceController: MediaSurfaceController

        /**
         * Transitions back into an [Uninitialized] state
         */
        val reset: suspend () -> Uninitialized

        /**
         * Transitions into an [Closed] state
         */
        val end: suspend () -> Closed

        /**
         * Transitions into an [Error] state
         * @param error the [PlaybackError] that occurred
         * @return a method for transitioning into an [Error] state
         */
        fun failWithError(error: PlaybackError): suspend () -> Error
    }

    /**
     * An [Active] state indicating no [PlayableMedia] has been initialized
     */
    interface Uninitialized : Active {

        /**
         * Attempts to initialize for a [MediaSource] and transitions into an [InitializedOrError] state
         * @param source the [MediaSource] to attempt to initialize for
         * @return a method for transitioning into an [InitializedOrError] state
         */
        fun initialize(source: MediaSource): suspend () -> InitializedOrError
    }

    /**
     * An [Active] state indicating either initialization or a failure to do so
     */
    sealed interface InitializedOrError : Active

    /**
     * An [InitializedOrError] state indicating a media source was successfully initialized
     */
    interface Initialized : InitializedOrError {

        val source: MediaSource

        /**
         * Transitions into an [Idle] state
         * @param media the [PlayableMedia] that was prepared
         * @return a method for transition into an [Idle] state
         */
        fun prepared(media: PlayableMedia): suspend () -> Idle
    }

    /**
     * An [Active] state indicating that [PlayableMedia] was prepared and can be managed
     */
    sealed interface Prepared : Active {

        /**
         * The [PlayableMedia] prepared
         */
        val playableMedia: PlayableMedia

        /**
         * Transitions into a [Stopped] state
         */
        val stop: suspend () -> Stopped

        /**
         * Seeks to a specified time position. Will suspend until seek has completed
         * @param duration the [Duration] of the position to seek to
         * @return `true` if the seek was successful
         */
        suspend fun seekTo(duration: Duration): Boolean
    }

    /**
     * A [Prepared] state indicating no playback has started
     */
    interface Idle : Prepared {

        /**
         * Transitions to a [Playing] state
         * @param playbackParameters the [PlaybackParameters] at which to play
         * @return method for transitioning into a [Playing] state
         */
        fun play(playbackParameters: PlaybackParameters = PlaybackParameters()): suspend () -> Playing
    }

    /**
     * A [Prepared] state indicating playback is in progress
     */
    sealed interface Started : Prepared {
        /**
         * The [PlaybackParameters] used for playback
         */
        val playbackParameters: PlaybackParameters

        /**
         * Transitions into a [Started] state with new [PlaybackParameters]
         * @param new the [PlaybackParameters] to update to
         * @return a method for transitioning into a new [Started] state
         */
        fun updatePlaybackParameters(new: PlaybackParameters): suspend () -> Started
    }

    /**
     * A [Prepared] state indicating playback was either started or completed
     */
    sealed interface PlayingOrCompleted : Prepared

    /**
     * A [Started] state indicating playback is currently ongoing
     */
    interface Playing : Started, PlayingOrCompleted {

        /**
         * Transitions into a [Paused] state
         */
        val pause: suspend () -> Paused

        /**
         * Transitions into a [PlayingOrCompleted] state
         */
        val completedLoop: suspend () -> PlayingOrCompleted
    }

    /**
     * A [Started] state indicating playback is currently paused
     */
    interface Paused : Started {

        /**
         * Transitions back into a [Playing] state
         */
        val play: suspend () -> Playing
    }

    /**
     * An [Active] state indicating playback has been fully stopped
     */
    interface Stopped : Active {

        /**
         * Transitions back to an [Initialized] state
         */
        val reinitialize: suspend () -> Initialized
    }

    /**
     * A [PlayingOrCompleted] state indicating playback has completed
     */
    interface Completed : PlayingOrCompleted {

        /**
         * Transitions into a [Playing] state
         * @param playbackParameters the [PlaybackParameters] use for playing
         * @return method for transitioning into a [Playing] state
         */
        fun start(playbackParameters: PlaybackParameters = PlaybackParameters()): suspend () -> Playing
    }

    /**
     * An [InitializedOrError] state indicating an error has occurred
     */
    interface Error : InitializedOrError {

        /**
         * The [PlaybackError] that occurred
         */
        val error: PlaybackError
    }

    /**
     * A [PlaybackState] indicating playback has ended and all resources have been released
     */
    interface Closed : PlaybackState, SpecialFlowValue.Last
}

internal sealed class PlaybackStateImpl {

    sealed class Active : PlaybackStateImpl() {
        internal abstract val mediaManager: MediaManager

        val volumeController: VolumeController by lazy { mediaManager }
        val mediaSurfaceController: MediaSurfaceController by lazy { mediaManager }

        val reset: suspend () -> PlaybackState.Uninitialized = { Uninitialized(mediaManager) }
        val end: suspend () -> PlaybackState.Closed = { Closed }
        fun failWithError(error: PlaybackError) = suspend { Error(error, mediaManager) }
    }

    data class Uninitialized(override val mediaManager: MediaManager) : Active(), PlaybackState.Uninitialized, HandleBeforeOldStateIsRemoved<PlaybackState> {
        override fun initialize(source: MediaSource): suspend () -> PlaybackState.InitializedOrError = {
            mediaManager.createPlayableMedia(source)?.let {
                Initialized(it, mediaManager)
            } ?: Error(PlaybackError.MalformedMediaSource, mediaManager)
        }

        override suspend fun beforeOldStateIsRemoved(oldState: PlaybackState) {
            when (oldState) {
                !is Uninitialized -> mediaManager.reset()
                else -> {}
            }
        }
    }

    data class Initialized(
        private val playableMedia: PlayableMedia,
        override val mediaManager: MediaManager,
    ) : Active(), PlaybackState.Initialized, HandleAfterOldStateIsRemoved<PlaybackState> {

        override val source: MediaSource get() = playableMedia.source

        override fun prepared(media: PlayableMedia): suspend () -> PlaybackState.Idle = { Idle(media, mediaManager) }

        override suspend fun afterOldStateIsRemoved(oldState: PlaybackState) {
            when (oldState) {
                is Uninitialized, is Stopped -> mediaManager.initialize(playableMedia)
                else -> {}
            }
        }
    }

    sealed class Prepared : Active() {

        abstract override val mediaManager: MediaManager
        abstract val playableMedia: PlayableMedia

        val stop: suspend () -> PlaybackState.Stopped = { Stopped(playableMedia, mediaManager) }
        suspend fun seekTo(duration: Duration) = mediaManager.seekTo(duration)
    }

    data class Idle(override val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Prepared(), PlaybackState.Idle {
        override fun play(playbackParameters: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing = {
            Playing(playbackParameters, playableMedia, mediaManager)
        }
    }

    class Playing(
        override val playbackParameters: PlaybackState.PlaybackParameters,
        override val playableMedia: PlayableMedia,
        override val mediaManager: MediaManager,
    ) : Prepared(), PlaybackState.Playing, HandleBeforeOldStateIsRemoved<PlaybackState> {

        override val completedLoop: suspend () -> PlaybackState.PlayingOrCompleted = {
            val newLoopMode = when (val loopMode = playbackParameters.loopMode) {
                is PlaybackState.LoopMode.NotLooping -> PlaybackState.LoopMode.NotLooping
                is PlaybackState.LoopMode.LoopingForever -> PlaybackState.LoopMode.LoopingForever
                is PlaybackState.LoopMode.LoopingForFixedNumber -> {
                    if (loopMode.loops > 0U) {
                        PlaybackState.LoopMode.LoopingForFixedNumber(loopMode.loops - 1U)
                    } else {
                        PlaybackState.LoopMode.NotLooping
                    }
                }
            }

            if (newLoopMode != PlaybackState.LoopMode.NotLooping) {
                mediaManager.seekTo(Duration.ZERO)
                Playing(playbackParameters.copy(loopMode = newLoopMode), playableMedia, mediaManager)
            } else {
                Completed(playableMedia, mediaManager)
            }
        }
        override val pause: suspend () -> PlaybackState.Paused = { Paused(playbackParameters, playableMedia, mediaManager) }
        override fun updatePlaybackParameters(new: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing = {
            Playing(new, playableMedia, mediaManager)
        }

        override suspend fun beforeOldStateIsRemoved(oldState: PlaybackState) {
            mediaManager.play(playbackParameters.rate)
        }
    }

    class Paused(
        override val playbackParameters: PlaybackState.PlaybackParameters,
        override val playableMedia: PlayableMedia,
        override val mediaManager: MediaManager,
    ) : Prepared(), PlaybackState.Paused, HandleAfterOldStateIsRemoved<PlaybackState> {

        override val play: suspend () -> PlaybackState.Playing = { Playing(playbackParameters, playableMedia, mediaManager) }
        override fun updatePlaybackParameters(new: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Paused = {
            Paused(new, playableMedia, mediaManager)
        }
        override suspend fun afterOldStateIsRemoved(oldState: PlaybackState) {
            when (oldState) {
                is PlaybackState.Playing -> mediaManager.pause()
                else -> {}
            }
        }
    }

    data class Stopped(
        private val playableMedia: PlayableMedia,
        override val mediaManager: MediaManager,
    ) : Active(), PlaybackState.Stopped, HandleBeforeOldStateIsRemoved<PlaybackState> {

        override val reinitialize: suspend () -> PlaybackState.Initialized = { Initialized(playableMedia, mediaManager) }

        override suspend fun beforeOldStateIsRemoved(oldState: PlaybackState) {
            when (oldState) {
                is PlaybackState.Prepared -> mediaManager.stop()
                else -> {}
            }
        }
    }

    data class Completed(override val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Prepared(), PlaybackState.Completed {

        override fun start(playbackParameters: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing = {
            mediaManager.seekTo(Duration.ZERO)
            Playing(playbackParameters, playableMedia, mediaManager)
        }
    }

    data class Error(override val error: PlaybackError, override val mediaManager: MediaManager) : Active(), PlaybackState.Error

    data object Closed : PlaybackStateImpl(), PlaybackState.Closed
}
