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

import com.splendo.kaluga.architecture.observable.toUninitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.media.DefaultSoundPlayer
import com.splendo.kaluga.media.MediaSource
import com.splendo.kaluga.resources.asImage
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class MediaSoundViewModel : BaseLifecycleViewModel() {

    private companion object {
        const val SOUND_BPM_INITIAL = 80
        const val SOUND_BPM_STEP = 20
        const val SOUND_BPM_MIN = 20
        const val SOUND_BPM_MAX = 500
    }

    private var soundPlayer = MediaSoundLoopPlayer(coroutineScope, mediaSource = SoundsSources.beep).apply {
        updateBPM(SOUND_BPM_INITIAL)
    }
    private val playingSound = MutableStateFlow(false)
    val playStopSoundButton = playingSound.map {
        if (it) {
            KalugaButton.WithoutText(ButtonStyles.mediaButton("stop".asImage()!!)) {
                playingSound.value = false
                soundPlayer.stop()
            }
        } else {
            KalugaButton.WithoutText(ButtonStyles.mediaButton("play_arrow".asImage()!!)) {
                playingSound.value = true
                soundPlayer.play()
            }
        }
    }.toUninitializedObservable(coroutineScope)

    private val soundPlayBPM = MutableStateFlow(SOUND_BPM_INITIAL)
    private val plusBPMEnabled = MutableStateFlow(true)
    val plusBPMButton = plusBPMEnabled.map {
        KalugaButton.Plain("+", ButtonStyles.default, isEnabled = it) {
            updateBPM(soundPlayBPM.value + SOUND_BPM_STEP)
        }
    }.toUninitializedObservable(coroutineScope)

    private val minusBPMEnabled = MutableStateFlow(true)
    val minusBPMButton = minusBPMEnabled.map {
        KalugaButton.Plain("-", ButtonStyles.default, isEnabled = soundPlayBPM.value > SOUND_BPM_MIN) {
            updateBPM(soundPlayBPM.value - SOUND_BPM_STEP)
        }
    }.toUninitializedObservable(coroutineScope)

    private fun updateBPM(bpm: Int) {
        soundPlayBPM.value = bpm
        plusBPMEnabled.value = bpm < SOUND_BPM_MAX
        minusBPMEnabled.value = bpm > SOUND_BPM_MIN
        soundPlayer.updateBPM(bpm)
    }

    val soundBPMLabel = soundPlayBPM.map { "$it bpm" }.toUninitializedObservable(coroutineScope)

    override fun onCleared() {
        super.onCleared()
        soundPlayer.stop()
    }
}

private class MediaSoundLoopPlayer(private val coroutineScope: CoroutineScope, private val mediaSource: MediaSource.Local) {

    private var player: DefaultSoundPlayer? = null

    private var isRunning = false
    private var delay: Duration = 100.milliseconds

    init {
        coroutineScope.launch {
            while (isActive) {
                delay(delay)
                if (isRunning) {
                    player?.play()
                }
            }
        }
    }

    fun play() = coroutineScope.launch {
        player = DefaultSoundPlayer(source = mediaSource)
        isRunning = true
    }

    fun stop() = coroutineScope.launch {
        isRunning = false
        player?.close()
        player = null
    }

    fun updateBPM(bpm: Int) {
        delay = 1.minutes / bpm
    }
}
