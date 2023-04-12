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
import com.splendo.kaluga.media.PlayableMedia
import com.splendo.kaluga.media.PlaybackError
import com.splendo.kaluga.media.PlaybackState
import com.splendo.kaluga.test.base.mock.SuspendMethodMock
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.parameters.SingleParameters
import kotlin.time.Duration

typealias PlayableMediaProvider = (MediaSource) -> PlayableMedia?
typealias SeekToMock = SuspendMethodMock<SingleParameters.Matchers<Duration>, SingleParameters.MatchersOrCaptor<Duration>, SingleParameters.Values<Duration>, SingleParameters<Duration>, Boolean>

/**
 * Mock implementation of [PlaybackState]
 */
sealed class MockPlaybackState {

    companion object {
        fun createSeekToMock(): SeekToMock = SuspendMethodMock(SingleParameters())
    }

    /**
     * Mock implementation of [PlaybackState.Active]
     */
    sealed class Active : MockPlaybackState() {

        abstract val mediaProvider: PlayableMediaProvider
        abstract val seekToMock: SeekToMock

        val reset: suspend () -> PlaybackState.Uninitialized = { Uninitialized(mediaProvider, seekToMock) }
        val end: suspend () -> PlaybackState.Ended = { Ended }
        fun failWithError(error: PlaybackError) = suspend {
            Error(error, mediaProvider, seekToMock)
        }
    }

    /**
     * Mock implementation of [PlaybackState.Uninitialized]
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Uninitialized(
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Active(), PlaybackState.Uninitialized {
        override fun initialize(source: MediaSource): suspend () -> PlaybackState.InitializedOrError =
            mediaProvider(source)?.let {
                { Initialized(it, mediaProvider, seekToMock) }
            } ?: failWithError(PlaybackError.MalformedMediaSource)
    }

    /**
     * Mock implementation of [PlaybackState.Initialized]
     * @param playableMedia the [PlayableMedia] to prepare
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Initialized(
        private val playableMedia: PlayableMedia,
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Active(), PlaybackState.Initialized {
        override fun prepared(media: PlayableMedia): suspend () -> PlaybackState.Idle =
            { Idle(media, mediaProvider, seekToMock) }
    }

    /**
     * Mock implementation of [PlaybackState.Prepared]
     */
    sealed class Prepared : Active() {
        abstract val playableMedia: PlayableMedia

        val stop: suspend () -> PlaybackState.Stopped =
            { Stopped(playableMedia, mediaProvider, seekToMock) }

        suspend fun seekTo(duration: Duration) = seekToMock.call(duration)
    }

    /**
     * Mock implementation of [PlaybackState.Idle]
     * @param playableMedia the [PlayableMedia] prepared
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Idle(
        override val playableMedia: PlayableMedia,
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Prepared(), PlaybackState.Idle {
        override fun play(playbackParameters: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing =
            {
                Playing(playbackParameters, playableMedia, mediaProvider, seekToMock)
            }
    }

    /**
     * Mock implementation of [PlaybackState.Playing]
     * @param playbackParameters the [PlaybackState.PlaybackParameters] used for playback
     * @param playableMedia the [PlayableMedia] prepared
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Playing(
        override val playbackParameters: PlaybackState.PlaybackParameters,
        override val playableMedia: PlayableMedia,
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Prepared(), PlaybackState.Playing {

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
                Completed(playableMedia, mediaProvider, seekToMock)
            }
        }
        override val pause: suspend () -> PlaybackState.Paused =
            { Paused(playbackParameters, playableMedia, mediaProvider, seekToMock) }

        override fun updatePlaybackParameters(new: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing =
            {
                copy(playbackParameters = new)
            }
    }

    /**
     * Mock implementation of [PlaybackState.Paused]
     * @param playbackParameters the [PlaybackState.PlaybackParameters] used for playback
     * @param playableMedia the [PlayableMedia] prepared
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Paused(
        override val playbackParameters: PlaybackState.PlaybackParameters,
        override val playableMedia: PlayableMedia,
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Prepared(), PlaybackState.Paused {

        override val play: suspend () -> PlaybackState.Playing =
            { Playing(playbackParameters, playableMedia, mediaProvider, seekToMock) }

        override fun updatePlaybackParameters(new: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Paused =
            { copy(playbackParameters = new) }
    }

    /**
     * Mock implementation of [PlaybackState.Stopped]
     * @param playableMedia the [PlayableMedia] that was stopped
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Stopped(
        private val playableMedia: PlayableMedia,
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Active(), PlaybackState.Stopped {

        override val reinitialize: suspend () -> PlaybackState.Initialized =
            { Initialized(playableMedia, mediaProvider, seekToMock) }
    }

    /**
     * Mock implementation of [PlaybackState.Completed]
     * @param playableMedia the [PlayableMedia] prepared
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Completed(
        override val playableMedia: PlayableMedia,
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Prepared(), PlaybackState.Completed {

        override fun start(playbackParameters: PlaybackState.PlaybackParameters): suspend () -> PlaybackState.Playing =
            {
                Playing(playbackParameters, playableMedia, mediaProvider, seekToMock)
            }
    }

    /**
     * Mock implementation of [PlaybackState.Error]
     * @param error the [PlaybackError] that occurred
     * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
     * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
     */
    data class Error(
        override val error: PlaybackError,
        override val mediaProvider: PlayableMediaProvider,
        override val seekToMock: SeekToMock
    ) : Active(), PlaybackState.Error

    /**
     * Mock implementation of [PlaybackState.Ended]
     */
    object Ended : MockPlaybackState(), PlaybackState.Ended
}
