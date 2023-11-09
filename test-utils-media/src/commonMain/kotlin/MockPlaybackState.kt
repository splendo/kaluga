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

package com.splendo.kaluga.test.media

import com.splendo.kaluga.media.MediaSource
import com.splendo.kaluga.media.MediaSurface
import com.splendo.kaluga.media.MediaSurfaceController
import com.splendo.kaluga.media.PlayableMedia
import com.splendo.kaluga.media.PlaybackError
import com.splendo.kaluga.media.PlaybackState
import com.splendo.kaluga.media.VolumeController
import com.splendo.kaluga.test.base.mock.SuspendMethodMock
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.SingleParameters
import kotlin.time.Duration

typealias PlayableMediaProvider = (MediaSource) -> PlayableMedia?
typealias SeekToMock = SuspendMethodMock<
    SingleParameters.Matchers<Duration>,
    SingleParameters.MatchersOrCaptor<Duration>,
    SingleParameters.Values<Duration>,
    SingleParameters<Duration>,
    Boolean,
    >

/**
 * Mock implementation of [PlaybackState]
 */
sealed class MockPlaybackState {

    companion object {
        fun createSeekToMock(): SeekToMock = SeekToMock(SingleParameters()).also {
            it.on().doReturn(true)
        }
    }

    /**
     * Configuration to persist across [MockPlaybackState.Active] states
     * @property mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @property volumeController the [VolumeController] for controlling the volume
     * @property mediaSurfaceController the [MediaSurfaceController] for managing the [MediaSurface]
     * @property seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Configuration(
        val mediaProvider: PlayableMediaProvider = { MockPlayableMedia(it) },
        val volumeController: VolumeController = MockVolumeController(),
        val mediaSurfaceController: MediaSurfaceController = MockMediaSurfaceController(),
        val seekToMock: SeekToMock = createSeekToMock(),
    )

    abstract val playbackState: PlaybackState

    /**
     * Mock implementation of [PlaybackState.Active]
     */
    sealed class Active : MockPlaybackState() {

        abstract val configuration: Configuration
        val volumeController: VolumeController by lazy { configuration.volumeController }
        val mediaSurfaceController: MediaSurfaceController by lazy { configuration.mediaSurfaceController }

        val reset: suspend () -> PlaybackState.Uninitialized = { Uninitialized(configuration) }
        val end: suspend () -> PlaybackState.Closed = { Closed }
        fun failWithError(error: PlaybackError) = suspend {
            Error(error, configuration)
        }
    }

    /**
     * Mock implementation of [PlaybackState.Uninitialized]
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Uninitialized(
        override val configuration: Configuration,
    ) : Active(), PlaybackState.Uninitialized {
        override val playbackState: PlaybackState = this
        override fun initialize(source: MediaSource): suspend () -> PlaybackState.InitializedOrError = configuration.mediaProvider(source)?.let {
            {
                Initialized(it, configuration)
            }
        } ?: failWithError(PlaybackError.MalformedMediaSource)
    }

    /**
     * Mock implementation of [PlaybackState.Initialized]
     * @param playableMedia the [PlayableMedia] to prepare
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Initialized(
        private val playableMedia: PlayableMedia,
        override val configuration: Configuration,
    ) : Active(), PlaybackState.Initialized {
        override val source: MediaSource get() = playableMedia.source
        override val playbackState: PlaybackState = this
        override fun prepared(media: PlayableMedia): suspend () -> PlaybackState.Idle = { Idle(media, configuration) }
    }

    /**
     * Mock implementation of [PlaybackState.Prepared]
     */
    sealed class Prepared : Active() {
        abstract val playableMedia: PlayableMedia

        val stop: suspend () -> PlaybackState.Stopped =
            { Stopped(playableMedia, configuration) }

        suspend fun seekTo(duration: Duration) = configuration.seekToMock.call(duration)
    }

    /**
     * Mock implementation of [PlaybackState.Idle]
     * @param playableMedia the [PlayableMedia] prepared
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Idle(
        override val playableMedia: PlayableMedia,
        override val configuration: Configuration,
    ) : Prepared(), PlaybackState.Idle {
        override val playbackState: PlaybackState = this
        override fun play(playbackParameters: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing = {
            Playing(playbackParameters, playableMedia, configuration)
        }
    }

    /**
     * Mock implementation of [PlaybackState.Playing]
     * @param playbackParameters the [PlaybackState.PlaybackParameters] used for playback
     * @param playableMedia the [PlayableMedia] prepared
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Playing(
        override val playbackParameters: PlaybackState.PlaybackParameters,
        override val playableMedia: PlayableMedia,
        override val configuration: Configuration,
    ) : Prepared(), PlaybackState.Playing {

        override val playbackState: PlaybackState = this
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
                copy(playbackParameters = playbackParameters.copy(loopMode = newLoopMode))
            } else {
                Completed(playableMedia, configuration)
            }
        }
        override val pause: suspend () -> PlaybackState.Paused =
            { Paused(playbackParameters, playableMedia, configuration) }

        override fun updatePlaybackParameters(new: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing = {
            copy(playbackParameters = new)
        }
    }

    /**
     * Mock implementation of [PlaybackState.Paused]
     * @param playbackParameters the [PlaybackState.PlaybackParameters] used for playback
     * @param playableMedia the [PlayableMedia] prepared
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Paused(
        override val playbackParameters: PlaybackState.PlaybackParameters,
        override val playableMedia: PlayableMedia,
        override val configuration: Configuration,
    ) : Prepared(), PlaybackState.Paused {

        override val playbackState: PlaybackState = this
        override val play: suspend () -> PlaybackState.Playing =
            { Playing(playbackParameters, playableMedia, configuration) }

        override fun updatePlaybackParameters(new: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Paused = { copy(playbackParameters = new) }
    }

    /**
     * Mock implementation of [PlaybackState.Stopped]
     * @param playableMedia the [PlayableMedia] that was stopped
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Stopped(
        private val playableMedia: PlayableMedia,
        override val configuration: Configuration,
    ) : Active(), PlaybackState.Stopped {

        override val playbackState: PlaybackState = this
        override val reinitialize: suspend () -> PlaybackState.Initialized =
            { Initialized(playableMedia, configuration) }
    }

    /**
     * Mock implementation of [PlaybackState.Completed]
     * @param playableMedia the [PlayableMedia] prepared
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Completed(
        override val playableMedia: PlayableMedia,
        override val configuration: Configuration,
    ) : Prepared(), PlaybackState.Completed {

        override val playbackState: PlaybackState = this
        override fun start(playbackParameters: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing = {
            Playing(playbackParameters, playableMedia, configuration)
        }
    }

    /**
     * Mock implementation of [PlaybackState.Error]
     * @param error the [PlaybackError] that occurred
     * @param configuration the [Configuration] to persist across all [MockPlaybackState.Active] states
     */
    data class Error(
        override val error: PlaybackError,
        override val configuration: Configuration,
    ) : Active(), PlaybackState.Error {
        override val playbackState: PlaybackState = this
    }

    /**
     * Mock implementation of [PlaybackState.Closed]
     */
    data object Closed : MockPlaybackState(), PlaybackState.Closed {
        override val playbackState: PlaybackState = this
    }
}
