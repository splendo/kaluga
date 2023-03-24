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

import com.splendo.kaluga.base.flow.collectUntilLast
import com.splendo.kaluga.base.utils.firstInstance
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

interface MediaPlayer {

    enum class Controls {
        PLAY,
        PAUSE,
        STOP,
        SEEK
    }

    val playableMedia: Flow<PlayableMedia?>

    val availableControls: Flow<Set<Controls>>

    suspend fun initializeFor(url: String)

    suspend fun start(loopMode: PlaybackState.LoopMode = PlaybackState.LoopMode.NotLooping)
    suspend fun pause()
    suspend fun stop()

    suspend fun seekTo(duration: Duration)

    suspend fun awaitCompletion()

    fun end()
}

class DefaultMediaPlayer(
    mediaManagerBuilder: BaseMediaManager.Builder,
    coroutineContext: CoroutineContext
) : MediaPlayer, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("MediaPlayer")) {

    private val mediaManager = mediaManagerBuilder.create(coroutineContext)
    private val playbackStateRepo = PlaybackStateRepo(mediaManager, coroutineContext)

    init {
        launch {
            mediaManager.events.collect { event ->
                when (event) {
                    is MediaManager.Event.DidPrepare -> playbackStateRepo.takeAndChangeState(PlaybackState.Initialized::class) { it.prepared(event.playableMedia) }
                    is MediaManager.Event.DidFailWithError -> playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.failWithError(event.error) }
                    is MediaManager.Event.DidComplete -> playbackStateRepo.takeAndChangeState(PlaybackState.Started::class) { it.completed }
                    is MediaManager.Event.DidEnd -> playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.end }
                }
            }
        }
        launch {
            playbackStateRepo.collectUntilLast(false) { state ->
                debug("Media", "State is now $state")
                when (state) {
                    is PlaybackState.Completed ->  playbackStateRepo.takeAndChangeState(PlaybackState.Completed::class) { it.restartIfLooping }
                    else -> {}
                }
            }
        }.invokeOnCompletion {
            if (it != null) {
                mediaManager.end()
            }
        }
    }

    override val playableMedia: Flow<PlayableMedia?> = playbackStateRepo.map {
        (it as? PlaybackState.Prepared)?.playableMedia
    }.distinctUntilChanged()

    override val availableControls: Flow<Set<MediaPlayer.Controls>> = playbackStateRepo.map { state ->
        when (state) {
            is PlaybackState.Idle -> setOf(MediaPlayer.Controls.PLAY, MediaPlayer.Controls.SEEK)
            is PlaybackState.Started -> setOf(MediaPlayer.Controls.PAUSE, MediaPlayer.Controls.STOP, MediaPlayer.Controls.SEEK)
            is PlaybackState.Paused -> setOf(MediaPlayer.Controls.PLAY, MediaPlayer.Controls.STOP, MediaPlayer.Controls.SEEK)
            is PlaybackState.Completed -> setOf(MediaPlayer.Controls.PLAY, MediaPlayer.Controls.STOP, MediaPlayer.Controls.SEEK)
            is PlaybackState.Stopped -> setOf(MediaPlayer.Controls.PLAY)
            is PlaybackState.Uninitialized,
            is PlaybackState.Initialized,
            is PlaybackState.Ended,
            is PlaybackState.Error -> emptySet()
        }
    }.shareIn(this, SharingStarted.WhileSubscribed())

    override suspend fun initializeFor(url: String) {
        var resetOnError = true
        playbackStateRepo.transformLatest { state ->
            when (state) {
                is PlaybackState.Uninitialized -> playbackStateRepo.takeAndChangeState(PlaybackState.Uninitialized::class) {
                    resetOnError = false
                    it.initialize(
                        url
                    )
                }
                is PlaybackState.Initialized -> emit(Unit)
                is PlaybackState.Idle -> if (state.playableMedia.url == url) emit(Unit) else playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.reset }
                is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
                is PlaybackState.Error -> if (resetOnError) {
                    playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.reset }
                } else throw state.error
                is PlaybackState.Active -> playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.reset }
            }
        }.first()
    }

    override suspend fun start(loopMode: PlaybackState.LoopMode) = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Uninitialized -> {} // Do nothing until Initialized
            is PlaybackState.Initialized -> {} // Do nothing until prepared
            is PlaybackState.Idle -> playbackStateRepo.takeAndChangeState(PlaybackState.Idle::class) { it.start(loopMode) }
            is PlaybackState.Completed -> playbackStateRepo.takeAndChangeState(PlaybackState.Completed::class) { it.start(loopMode) }
            is PlaybackState.Stopped -> playbackStateRepo.takeAndChangeState(PlaybackState.Stopped::class) { it.reset }
            is PlaybackState.Started -> emit(Unit)
            is PlaybackState.Paused -> playbackStateRepo.takeAndChangeState(PlaybackState.Paused::class) { it.start }
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
        }
    }.first()

    override suspend fun pause() = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Started -> playbackStateRepo.takeAndChangeState(PlaybackState.Started::class) { it.pause }
            is PlaybackState.Paused -> emit(Unit)
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
            is PlaybackState.Active -> emit(Unit) // If not playing we should consider it paused
        }
    }.first()

    override suspend fun stop() = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Prepared -> playbackStateRepo.takeAndChangeState(PlaybackState.Prepared::class) { it.stop }
            is PlaybackState.Stopped -> emit(Unit)
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
            is PlaybackState.Active -> emit(Unit) // If not playing we should consider it stopped
        }
    }.first()

    override suspend fun seekTo(duration: Duration) = playbackStateRepo.useState { state ->
        when (state) {
            is PlaybackState.Prepared -> state.seekTo(duration)
            is PlaybackState.Active,
            is PlaybackState.Ended -> {}
        }
    }

    override suspend fun awaitCompletion() = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Completed -> if (!state.willLoop) {
                emit(Unit)
            }
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
            is PlaybackState.Active -> {} // Wait until completed
        }
    }.first()

    override fun end() {
        launch { playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) { it.end } }
    }
}

val MediaPlayer.duration: Flow<Duration> get() = playableMedia.map { it?.duration ?: Duration.ZERO }
fun MediaPlayer.playTime(pollingInterval: Duration) = playableMedia.transformLatest { media ->
    media?.let {
        while (currentCoroutineContext().isActive) {
            emit(it.currentPlayTime)
            delay(pollingInterval)
        }
    } ?: emit(Duration.ZERO)
}

suspend fun MediaPlayer.play(loopMode: PlaybackState.LoopMode = PlaybackState.LoopMode.NotLooping) {
    start(loopMode)
    awaitCompletion()
}
