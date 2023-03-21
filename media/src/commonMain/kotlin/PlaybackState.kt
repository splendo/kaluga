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

sealed class PlaybackError : Exception() {
    object Unsupported : PlaybackError()
    object IO : PlaybackError()
    object TimedOut : PlaybackError()
    object MalformedMediaSource : PlaybackError()
    object PlaybackHasEnded : PlaybackError()
    object Unknown : PlaybackError()
}

sealed interface PlaybackState : KalugaState {

    sealed class LoopMode {
        object NotLooping : LoopMode()
        object LoopingForever : LoopMode()
        data class LoopingForFixedNumber(val loops: UInt) : LoopMode()
    }

    sealed interface Active : PlaybackState {
        val end: suspend () -> Ended
        fun failWithError(error: PlaybackError): suspend () -> Error
    }

    interface Uninitialized : Active {
        fun initialize(url: String): suspend () -> InitializedOrError
    }

    sealed interface InitializedOrError : Active

    interface Initialized : InitializedOrError {
        fun prepared(media: PlayableMedia): suspend  () -> Idle
    }

    sealed interface Prepared : Active {

        val playableMedia: PlayableMedia

        val stop: suspend () -> Stopped
        fun seekTo(duration: Duration)
    }

    interface Idle : Prepared {
        fun start(loopMode: LoopMode = LoopMode.NotLooping): suspend () -> Started
    }

    sealed interface Playing : Prepared {
        val loopMode: LoopMode
        val updateLoopMode: suspend () -> Playing
    }

    interface Started : Playing {
        val pause: suspend () -> Paused
        val completed: suspend () -> Completed
    }

    interface Paused : Playing {
        val start: suspend () -> Started
    }

    interface Stopped : Active {
        val reset: suspend () -> Initialized
    }

    interface Completed : Prepared {
        val willLoop: Boolean
        fun start(loopMode: LoopMode = LoopMode.NotLooping): suspend () -> Started
        val restartIfLooping: suspend  () -> Prepared
    }

    interface Error : InitializedOrError {
        val error: PlaybackError
    }

    interface Ended : PlaybackState, SpecialFlowValue.Last
}

internal class PlaybackStateImpl {

    sealed class Active {
        internal abstract val mediaManager: MediaManager

        val end: suspend () -> PlaybackState.Ended = { Ended }
        fun failWithError(error: PlaybackError) = suspend { Error(error, mediaManager) }
    }

    data class Uninitialized(override val mediaManager: MediaManager) : Active(), PlaybackState.Uninitialized {
        override fun initialize(url: String): suspend () -> PlaybackState.InitializedOrError = mediaManager.createPlayableMedia(url)?.let {
            { Initialized(it, mediaManager) }
            } ?: failWithError(PlaybackError.MalformedMediaSource)
    }

    data class Initialized(private val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Active(), PlaybackState.Initialized, HandleAfterOldStateIsRemoved<PlaybackState> {
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
        fun seekTo(duration: Duration) = mediaManager.seekTo(duration)
    }

    data class Idle(override val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Prepared(), PlaybackState.Idle {
        override fun start(loopMode: PlaybackState.LoopMode): suspend () -> PlaybackState.Started = {
            Started(loopMode, playableMedia, mediaManager)
        }
    }

    data class Started(override val loopMode: PlaybackState.LoopMode, override val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Prepared(), PlaybackState.Started, HandleBeforeOldStateIsRemoved<PlaybackState> {

        override val completed: suspend () -> PlaybackState.Completed = {
            val newLoopMode = when (loopMode) {
                is PlaybackState.LoopMode.NotLooping -> PlaybackState.LoopMode.NotLooping
                is PlaybackState.LoopMode.LoopingForever -> PlaybackState.LoopMode.LoopingForever
                is PlaybackState.LoopMode.LoopingForFixedNumber -> {
                    val remainingLoops = loopMode.loops.toInt() - 1
                    if (remainingLoops > 0) PlaybackState.LoopMode.LoopingForFixedNumber(remainingLoops.toUInt()) else PlaybackState.LoopMode.NotLooping
                }
            }
            Completed(newLoopMode, playableMedia, mediaManager)
        }
        override val pause: suspend () -> PlaybackState.Paused = { Paused(loopMode, playableMedia, mediaManager) }
        override val updateLoopMode: suspend () -> PlaybackState.Playing = { copy(loopMode = loopMode) }

        override suspend fun beforeOldStateIsRemoved(oldState: PlaybackState) {
            when (oldState) {
                !is PlaybackState.Started -> mediaManager.play()
                else -> {}
            }
        }
    }

    data class Paused(override val loopMode: PlaybackState.LoopMode, override val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Prepared(), PlaybackState.Paused, HandleAfterOldStateIsRemoved<PlaybackState> {

        override val start: suspend () -> PlaybackState.Started = { Started(loopMode, playableMedia, mediaManager) }
        override val updateLoopMode: suspend () -> PlaybackState.Playing = { copy(loopMode = loopMode) }
        override suspend fun afterOldStateIsRemoved(oldState: PlaybackState) {
            when (oldState) {
                is PlaybackState.Started -> mediaManager.pause()
                else -> {}
            }
        }
    }

    data class Stopped(override val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Prepared(), PlaybackState.Stopped, HandleBeforeOldStateIsRemoved<PlaybackState> {

        override val reset: suspend () -> PlaybackState.Initialized = { Initialized(playableMedia, mediaManager) }

        override suspend fun beforeOldStateIsRemoved(oldState: PlaybackState) {
            when (oldState) {
                !is PlaybackState.Started -> mediaManager.play()
                else -> {}
            }
        }
    }

    data class Completed(private val loopMode: PlaybackState.LoopMode, override val playableMedia: PlayableMedia, override val mediaManager: MediaManager) : Prepared(), PlaybackState.Completed {

        override val willLoop: Boolean = when (loopMode) {
            is PlaybackState.LoopMode.NotLooping -> false
            is PlaybackState.LoopMode.LoopingForever -> true
            is PlaybackState.LoopMode.LoopingForFixedNumber -> loopMode.loops > 0U
        }

        override fun start(loopMode: PlaybackState.LoopMode): suspend () -> PlaybackState.Started = {
            mediaManager.seekTo(Duration.ZERO)
            Started(loopMode, playableMedia, mediaManager)
        }

        override val restartIfLooping: suspend () -> PlaybackState.Prepared get() = when {
            willLoop -> start(loopMode)
            else -> remain()
        }
    }

    data class Error(override val error: PlaybackError, override val mediaManager: MediaManager) : Active(), PlaybackState.Error

    object Ended : PlaybackState.Ended
}
