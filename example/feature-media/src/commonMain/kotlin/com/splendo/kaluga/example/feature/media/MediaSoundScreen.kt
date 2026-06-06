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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.example.arch.LocalIconSet
import com.splendo.kaluga.media.DefaultSoundPlayer
import com.splendo.kaluga.media.MediaSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

private const val SOUND_BPM_INITIAL = 80
private const val SOUND_BPM_STEP = 20
private const val SOUND_BPM_MIN = 20
private const val SOUND_BPM_MAX = 500

@Composable
fun MediaSoundScreen(modifier: Modifier = Modifier) {
    val icons = LocalIconSet.current
    val scope = rememberCoroutineScope()
    val looper = remember { MediaSoundLoopPlayer(scope, SoundsSources.beep) }
    DisposableEffect(looper) { onDispose { looper.stop() } }

    var bpm by remember { mutableStateOf(SOUND_BPM_INITIAL) }
    var playing by remember { mutableStateOf(false) }

    DisposableEffect(bpm) {
        looper.updateBPM(bpm)
        onDispose { }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilledIconButton(
            onClick = {
                if (playing) looper.stop() else looper.play()
                playing = !playing
            },
        ) {
            Text(if (playing) icons.stop else icons.play, fontFamily = icons.fontFamily)
        }
        Text("$bpm bpm")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            Button(
                onClick = { bpm = (bpm - SOUND_BPM_STEP).coerceAtLeast(SOUND_BPM_MIN) },
                enabled = bpm > SOUND_BPM_MIN,
            ) { Text("-") }
            Button(
                onClick = { bpm = (bpm + SOUND_BPM_STEP).coerceAtMost(SOUND_BPM_MAX) },
                enabled = bpm < SOUND_BPM_MAX,
            ) { Text("+") }
        }
    }
}

private class MediaSoundLoopPlayer(private val scope: CoroutineScope, private val source: MediaSource.Local) {
    private var player: DefaultSoundPlayer? = null
    private val isRunning = MutableStateFlow(false)
    private val delay = MutableStateFlow<Duration>(1.minutes / SOUND_BPM_INITIAL)

    init {
        scope.launch {
            while (isActive) {
                delay(delay.value.coerceAtLeast(1.milliseconds))
                if (isRunning.value) player?.play()
            }
        }
    }

    fun play() = scope.launch {
        player = DefaultSoundPlayer(source = source)
        isRunning.value = true
    }

    fun stop() = scope.launch {
        isRunning.value = false
        player?.close()
        player = null
    }

    fun updateBPM(bpm: Int) {
        delay.value = 1.minutes / bpm
    }
}
