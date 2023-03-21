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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

interface MediaPlayer {

    val playableMedia: Flow<PlayableMedia?>

    val isPrepared: Flow<Boolean>

    val canStart: Flow<Boolean>
    suspend fun start(loopMode: PlaybackState.LoopMode = PlaybackState.LoopMode.NotLooping)

    val canPause: Flow<Boolean>
    suspend fun pause()

    val canStop: Flow<Boolean>
    suspend fun stop()

    suspend fun awaitCompletion()

    fun end()
}

class DefaultMediaPlayer(
    private val url: String,
    mediaManagerBuilder: BaseMediaManager.Builder,
    coroutineContext: CoroutineContext
) : MediaPlayer, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("MediaPlayer for $url")) {

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
                when (state) {
                    is PlaybackState.Uninitialized -> playbackStateRepo.takeAndChangeState(PlaybackState.Uninitialized::class) { it.initialize(url) }
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

    override val isPrepared: Flow<Boolean> = playbackStateRepo.map { state ->
        when (state) {
            is PlaybackState.Prepared -> true
            is PlaybackState.Error,
            is PlaybackState.Ended,
            is PlaybackState.Stopped,
            is PlaybackState.Uninitialized,
            is PlaybackState.Initialized -> false
        }
    }

    override val canStart: Flow<Boolean> = playbackStateRepo.map { state ->
        when (state) {
            is PlaybackState.Uninitialized,
            is PlaybackState.Initialized,
            is PlaybackState.Started,
            is PlaybackState.Error,
            is PlaybackState.Ended -> false
            is PlaybackState.Idle,
            is PlaybackState.Paused,
            is PlaybackState.Completed,
            is PlaybackState.Stopped -> true
        }
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

    override val canPause: Flow<Boolean> = playbackStateRepo.map { state ->
        state is PlaybackState.Started
    }

    override suspend fun pause() = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Playing -> playbackStateRepo.takeAndChangeState(PlaybackState.Started::class) { it.pause }
            is PlaybackState.Paused -> emit(Unit)
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
            is PlaybackState.Active -> emit(Unit) // If not playing we should consider it paused
        }
    }.first()

    override val canStop: Flow<Boolean> = playbackStateRepo.map { state ->
        state is PlaybackState.Prepared
    }

    override suspend fun stop() = playbackStateRepo.transformLatest { state ->
        when (state) {
            is PlaybackState.Prepared -> playbackStateRepo.takeAndChangeState(PlaybackState.Prepared::class) { it.stop }
            is PlaybackState.Stopped -> emit(Unit)
            is PlaybackState.Ended -> throw PlaybackError.PlaybackHasEnded
            is PlaybackState.Error -> throw state.error
            is PlaybackState.Active -> emit(Unit) // If not playing we should consider it stopped
        }
    }.first()

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
