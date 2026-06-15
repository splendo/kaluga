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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.example.arch.IconSet
import com.splendo.kaluga.example.arch.LocalIconSet
import com.splendo.kaluga.media.PlaybackState
import com.splendo.kaluga.media.compose.MediaSurfaceContainer
import com.splendo.kaluga.media.mediaSourceFromUrl
import org.koin.compose.viewmodel.koinViewModel

private const val DEFAULT_AUDIO_URL = "https://interactive-examples.mdn.mozilla.net/media/cc0-audio/t-rex-roar.mp3"
private const val DEFAULT_VIDEO_URL = "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4"
private val RATE_OPTIONS = listOf(0.5f, 1.0f, 2.0f, 4.0f)

@Composable
fun MediaScreen(modifier: Modifier = Modifier) {
    val icons = LocalIconSet.current
    val viewModel = koinViewModel<MediaViewModel>()
    val controls by viewModel.controls.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val playTime by viewModel.playTimeLabel.collectAsState()
    val totalDuration by viewModel.totalDurationLabel.collectAsState()
    val volume by viewModel.volume.collectAsState()
    val aspectRatio by viewModel.aspectRatio.collectAsState()
    val isPreparing by viewModel.isPreparing.collectAsState()
    val viewState by viewModel.viewState.collectAsState()
    val error by viewModel.error.collectAsState()

    var showSelectDialog by remember { mutableStateOf(false) }
    var showRemoteUrlDialog by remember { mutableStateOf(false) }
    var showRateDialog by remember { mutableStateOf(false) }
    var showVolumeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Fills the space left above the bottom-locked controls/button.
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            when (viewState) {
                MediaViewModel.ViewState.NO_MEDIA_SELECTED -> {
                    if (isPreparing) CircularProgressIndicator() else Text("No Media Selected")
                }

                MediaViewModel.ViewState.AUDIO -> Text("Audio")

                MediaViewModel.ViewState.VIDEO -> MediaSurfaceContainer(
                    binder = viewModel.surfaceBinder,
                    modifier = Modifier.fillMaxHeight().aspectRatio(aspectRatio, matchHeightConstraintsFirst = true),
                )
            }
        }

        if (viewState != MediaViewModel.ViewState.NO_MEDIA_SELECTED) {
            Slider(
                value = progress,
                onValueChange = { viewModel.seekTo(it.toDouble()) },
                enabled = controls.seek != null,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(playTime)
                Text(totalDuration)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                FilledIconButton(
                    enabled = controls.play != null || controls.unpause != null,
                    onClick = { viewModel.playOrUnpause() },
                ) { Icon(icons.play, icons.fontFamily) }
                FilledIconButton(
                    enabled = controls.pause != null,
                    onClick = { viewModel.pause() },
                ) { Icon(icons.pause, icons.fontFamily) }
                FilledIconButton(
                    enabled = controls.stop != null,
                    onClick = { viewModel.stop() },
                ) { Icon(icons.stop, icons.fontFamily) }
                FilledIconButton(
                    enabled = controls.setLoopMode != null,
                    onClick = { viewModel.toggleLoopMode() },
                ) { LoopIcon(controls.setLoopMode?.currentLoopMode, icons) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    enabled = controls.setRate != null,
                    onClick = { showRateDialog = true },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(icons.rate, icons.fontFamily)
                    Text(" ${controls.setRate?.currentRate ?: 1f}x")
                }
                Button(
                    onClick = { showVolumeDialog = true },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(icons.volume, icons.fontFamily)
                    Text(" ${(volume * 100).toInt()}%")
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showSelectDialog = true },
        ) { Text("Select Media") }
    }

    if (showSelectDialog) {
        SelectMediaDialog(
            onDismiss = { showSelectDialog = false },
            onSelectAudio = {
                showSelectDialog = false
                viewModel.load(mediaSourceFromUrl(DEFAULT_AUDIO_URL))
            },
            onSelectVideo = {
                showSelectDialog = false
                viewModel.load(mediaSourceFromUrl(DEFAULT_VIDEO_URL))
            },
            onSelectRemote = {
                showSelectDialog = false
                showRemoteUrlDialog = true
            },
        )
    }
    if (showRemoteUrlDialog) {
        RemoteUrlDialog(
            onDismiss = { showRemoteUrlDialog = false },
            onConfirm = { url ->
                showRemoteUrlDialog = false
                viewModel.load(mediaSourceFromUrl(url))
            },
        )
    }
    if (showRateDialog) {
        OptionsDialog(
            title = "Playback Rate",
            options = RATE_OPTIONS.map { "${it}x" to it },
            onDismiss = { showRateDialog = false },
            onSelect = {
                showRateDialog = false
                viewModel.setRate(it)
            },
        )
    }
    if (showVolumeDialog) {
        OptionsDialog(
            title = "Volume",
            options = (0..10).map { "${it * 10}%" to (it.toFloat() / 10f) },
            onDismiss = { showVolumeDialog = false },
            onSelect = {
                showVolumeDialog = false
                viewModel.setVolume(it)
            },
        )
    }
    error?.let { msg ->
        AlertDialog(
            onDismissRequest = viewModel::dismissError,
            confirmButton = { TextButton(onClick = viewModel::dismissError) { Text("OK") } },
            title = { Text("Playback Error") },
            text = { Text(msg) },
        )
    }
}

@Composable
private fun SelectMediaDialog(onDismiss: () -> Unit, onSelectAudio: () -> Unit, onSelectVideo: () -> Unit, onSelectRemote: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Select Media") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = onSelectAudio) { Text("Default Audio") }
                Button(modifier = Modifier.fillMaxWidth(), onClick = onSelectVideo) { Text("Default Video") }
                Button(modifier = Modifier.fillMaxWidth(), onClick = onSelectRemote) { Text("From URL") }
            }
        },
    )
}

@Composable
private fun RemoteUrlDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = { onConfirm(input) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Enter Media URL") },
        text = { OutlinedTextField(value = input, onValueChange = { input = it }) },
    )
}

@Composable
private fun <T> OptionsDialog(title: String, options: List<Pair<String, T>>, onDismiss: () -> Unit, onSelect: (T) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                options.forEach { (label, value) ->
                    TextButton(modifier = Modifier.fillMaxWidth(), onClick = { onSelect(value) }) { Text(label) }
                }
            }
        },
    )
}

// Renders a single icon glyph in the icon font; the surrounding label text keeps the default font so
// digits/units still render when the icon font (e.g. Material Icons) has no glyphs for them.
@Composable
private fun Icon(glyph: String, fontFamily: FontFamily) {
    Text(glyph, fontFamily = fontFamily)
}

@Composable
private fun LoopIcon(mode: PlaybackState.LoopMode?, icons: IconSet) {
    when (mode) {
        is PlaybackState.LoopMode.LoopingForFixedNumber -> {
            Icon(icons.repeatOne, icons.fontFamily)
            Text("${mode.loops}")
        }

        else -> Icon(icons.repeat, icons.fontFamily)
    }
}
