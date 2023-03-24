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

package com.splendo.kaluga.example.shared.viewmodel.media

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toUninitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.media.BaseMediaManager
import com.splendo.kaluga.media.DefaultMediaPlayer
import com.splendo.kaluga.media.MediaPlayer
import com.splendo.kaluga.media.duration
import com.splendo.kaluga.media.play
import com.splendo.kaluga.media.playTime
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

class MediaViewModel(builder: BaseMediaManager.Builder) : BaseLifecycleViewModel() {

    private val mediaPlayer = DefaultMediaPlayer(builder, coroutineScope.coroutineContext)

    val isLoaded = mediaPlayer.availableControls.map { it.isNotEmpty() }.toInitializedObservable(false, coroutineScope)
    private val _totalDuration = mediaPlayer.duration.stateIn(coroutineScope, SharingStarted.Eagerly, ZERO)
    private val _availableControls = mediaPlayer.availableControls.stateIn(coroutineScope, SharingStarted.Eagerly, emptySet())

    val totalDuration = _totalDuration.map {
        it.format()
    }.toInitializedObservable(ZERO.format(), coroutineScope)

    val currentPlaytime = mediaPlayer.playTime(1.seconds).map { it.format() }.toInitializedObservable(
        ZERO.format(), coroutineScope)
    val progress = combine(mediaPlayer.playTime(1.seconds), _totalDuration, _availableControls) { playTime, totalDuration, availableControls ->
        if (totalDuration > ZERO && availableControls.contains(MediaPlayer.Controls.SEEK)) {
            playTime / totalDuration
        } else {
            0.0
        }.toFloat()
    }.toInitializedObservable(0.0f, coroutineScope)

    val isSeekEnabled = _availableControls.map { it.contains(MediaPlayer.Controls.SEEK) }.toInitializedObservable(false, coroutineScope)

    val playButton = _availableControls.map {
        KalugaButton.Plain("\u23F5", ButtonStyles.mediaButton, it.contains(MediaPlayer.Controls.PLAY)) {
            coroutineScope.launch { mediaPlayer.start() }
        }
    }.toUninitializedObservable(coroutineScope)

    val pauseButton = _availableControls.map {
        KalugaButton.Plain("\u23F8", ButtonStyles.mediaButton, it.contains(MediaPlayer.Controls.PAUSE)) {
            coroutineScope.launch { mediaPlayer.pause() }
        }
    }.toUninitializedObservable(coroutineScope)

    val stopButton = _availableControls.map {
        KalugaButton.Plain("\u23F9", ButtonStyles.mediaButton, it.contains(MediaPlayer.Controls.STOP)) {
            coroutineScope.launch { mediaPlayer.stop() }
        }
    }.toUninitializedObservable(coroutineScope)

    fun seekTo(progress: Double) {
        coroutineScope.launch {
            mediaPlayer.seekTo(_totalDuration.value * progress)
        }
    }

    init {
        initialize()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.end()
    }

    private fun initialize() {
        coroutineScope.launch {
            mediaPlayer.initializeFor("https://www.orangefreesounds.com/wp-content/uploads/2016/01/Waves-mp3.mp3")
        }
    }

    private fun Duration.format() = toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) "%02d:%02d:%02d".format(hours, minutes, seconds) else "%02d:%02d".format(minutes, seconds)
    }

}