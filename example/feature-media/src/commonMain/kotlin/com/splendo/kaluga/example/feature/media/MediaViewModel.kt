/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.media

import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.lifecycle.compose.LifecycleViewModel
import com.splendo.kaluga.media.BaseMediaManager
import com.splendo.kaluga.media.DefaultMediaPlayer
import com.splendo.kaluga.media.MediaPlayer
import com.splendo.kaluga.media.MediaSource
import com.splendo.kaluga.media.PlaybackError
import com.splendo.kaluga.media.PlaybackState
import com.splendo.kaluga.media.compose.ComposeMediaSurfaceProvider
import com.splendo.kaluga.media.duration
import com.splendo.kaluga.media.isVideo
import com.splendo.kaluga.media.playTime
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

/**
 * Demoes the [LifecycleViewModel] pattern from `:lifecycle-compose` together with the
 * [ComposeMediaSurfaceProvider] / [MediaSurfaceContainer] interop from `:media-compose`:
 *
 * - The view model owns a [ComposeMediaSurfaceProvider] and declares it as a
 *   [com.splendo.kaluga.lifecycle.LifecycleSubscribable] via `super(subscribables = …)`. The
 *   screen calls `vm.AttachToCompose()` once and any lifecycle wiring (a no-op for the
 *   Compose-driven provider, but the same code path Activity/UIViewController/NSWindow providers
 *   would use) is done.
 * - [DefaultMediaPlayer] is wired with that provider, so a [MediaSurfaceContainer] composed in the
 *   screen pushes its native surface straight into the player.
 */
class MediaViewModel private constructor(val surfaceProvider: ComposeMediaSurfaceProvider, builder: BaseMediaManager.Builder) :
    LifecycleViewModel(subscribables = listOf(surfaceProvider)) {

    constructor(builder: BaseMediaManager.Builder) : this(ComposeMediaSurfaceProvider(), builder)

    enum class ViewState { NO_MEDIA_SELECTED, AUDIO, VIDEO }

    private val dispatcher = singleThreadDispatcher("MediaPlayer")
    private val playerJob = SupervisorJob()
    private val mediaPlayer = DefaultMediaPlayer(
        surfaceProvider,
        builder,
        playerJob + dispatcher,
    )

    val controls: StateFlow<MediaPlayer.Controls> =
        mediaPlayer.controls.stateIn(viewModelScope, SharingStarted.Eagerly, MediaPlayer.Controls())
    private val totalDuration: StateFlow<Duration> =
        mediaPlayer.duration.stateIn(viewModelScope, SharingStarted.Eagerly, ZERO)
    val isPreparing: StateFlow<Boolean> = controls.map {
        it.getControlType<MediaPlayer.Controls.AwaitPreparation>() != null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val viewState: StateFlow<ViewState> = mediaPlayer.playableMedia.map { media ->
        when {
            media == null -> ViewState.NO_MEDIA_SELECTED
            media.isVideo -> ViewState.VIDEO
            else -> ViewState.AUDIO
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ViewState.NO_MEDIA_SELECTED)
    val playTimeLabel: StateFlow<String> = mediaPlayer.playTime(100.milliseconds)
        .map { it.format() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ZERO.format())
    val totalDurationLabel: StateFlow<String> = totalDuration
        .map { it.format() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ZERO.format())
    val progress: StateFlow<Float> =
        combine(mediaPlayer.playTime(100.milliseconds), totalDuration) { play, total ->
            if (total > ZERO) (play / total).toFloat().coerceIn(0f, 1f) else 0f
        }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)
    val volume: StateFlow<Float> = mediaPlayer.currentVolume
        .stateIn(viewModelScope, SharingStarted.Eagerly, 1f)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            controls.mapNotNull { it.displayError }.collect { display ->
                _error.value = display.error.message ?: display.error::class.simpleName.orEmpty()
            }
        }
    }

    fun load(source: MediaSource?) = viewModelScope.launch {
        try {
            if (source != null) mediaPlayer.initializeFor(source) else mediaPlayer.reset()
        } catch (_: PlaybackError) {
            // surfaced via controls.displayError
        }
    }

    fun playOrUnpause() = viewModelScope.launch {
        val c = controls.value
        when {
            c.unpause != null -> c.tryUnpause()
            c.play != null -> c.tryPlay()
        }
    }
    fun pause() = viewModelScope.launch { controls.value.tryPause() }
    fun stop() = viewModelScope.launch { controls.value.tryStop() }
    fun seekTo(fraction: Double) = viewModelScope.launch {
        controls.value.trySeek(totalDuration.value * fraction)
    }
    fun setRate(rate: Float) = viewModelScope.launch { controls.value.trySetRate(rate) }
    fun setVolume(v: Float) = viewModelScope.launch { mediaPlayer.updateVolume(v) }
    fun toggleLoopMode() = viewModelScope.launch {
        val setLoop = controls.value.setLoopMode ?: return@launch
        val next: PlaybackState.LoopMode = when (setLoop.currentLoopMode) {
            PlaybackState.LoopMode.NotLooping -> PlaybackState.LoopMode.LoopingForever
            is PlaybackState.LoopMode.LoopingForever -> PlaybackState.LoopMode.LoopingForFixedNumber(1U)
            is PlaybackState.LoopMode.LoopingForFixedNumber -> PlaybackState.LoopMode.NotLooping
        }
        setLoop.perform(next)
    }
    fun dismissError() {
        _error.value = null
    }

    override fun onCleared() {
        playerJob.invokeOnCompletion { dispatcher.close() }
        mediaPlayer.close()
        playerJob.cancel()
        super.onCleared()
    }
}

private fun Duration.format() = toComponents { hours, minutes, seconds, _ ->
    if (hours > 0) {
        "${hours.padded()}:${minutes.padded()}:${seconds.padded()}"
    } else {
        "${minutes.padded()}:${seconds.padded()}"
    }
}

private fun Int.padded() = if (this < 10) "0$this" else "$this"
private fun Long.padded() = if (this < 10) "0$this" else "$this"
