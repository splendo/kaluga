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

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.alerts.buildAlertWithInput
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toUninitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.media.BaseMediaManager
import com.splendo.kaluga.media.DefaultMediaPlayer
import com.splendo.kaluga.media.MediaPlayer
import com.splendo.kaluga.media.MediaSource
import com.splendo.kaluga.media.MediaSurfaceProvider
import com.splendo.kaluga.media.PlaybackError
import com.splendo.kaluga.media.PlaybackState
import com.splendo.kaluga.media.Resolution
import com.splendo.kaluga.media.duration
import com.splendo.kaluga.media.isVideo
import com.splendo.kaluga.media.mediaSourceFromUrl
import com.splendo.kaluga.media.playTime
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

sealed class MediaNavigationAction : SingleValueNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType) {
    data object SelectLocal : MediaNavigationAction()
}

class MediaViewModel(
    mediaSurfaceProvider: MediaSurfaceProvider,
    builder: BaseMediaManager.Builder,
    private val alertPresenterBuilder: BaseAlertPresenter.Builder,
    navigator: Navigator<MediaNavigationAction>,
) : NavigatingViewModel<MediaNavigationAction>(navigator, mediaSurfaceProvider, alertPresenterBuilder) {

    private companion object {
        val playbackFormatter = NumberFormatter(style = NumberFormatStyle.Decimal(minIntegerDigits = 1U)).apply { positiveSuffix = "x" }
        val volumeFormatter = NumberFormatter(style = NumberFormatStyle.Percentage(maxFractionDigits = 0U))
    }

    private val mediaPlayerDispatcher = singleThreadDispatcher("MediaPlayer")
    private val mediaPlayer = DefaultMediaPlayer(mediaSurfaceProvider, builder, coroutineScope.coroutineContext + mediaPlayerDispatcher)

    val isPreparing = mediaPlayer.controls.map { it.getControlType<MediaPlayer.Controls.AwaitPreparation>() != null }.toInitializedObservable(false, coroutineScope)
    val hasControls = mediaPlayer.controls.map {
        it.controlTypes.isNotEmpty() && it.getControlType<MediaPlayer.Controls.AwaitPreparation>() == null
    }.toInitializedObservable(false, coroutineScope)
    private val _totalDuration = mediaPlayer.duration.stateIn(coroutineScope, SharingStarted.Eagerly, ZERO)
    private val controls = mediaPlayer.controls.stateIn(coroutineScope, SharingStarted.Eagerly, MediaPlayer.Controls())

    val totalDuration = _totalDuration.map {
        it.format()
    }.toInitializedObservable(ZERO.format(), coroutineScope)

    val currentPlaytime = mediaPlayer.playTime(100.milliseconds).map {
        it.format()
    }.toInitializedObservable(ZERO.format(), coroutineScope)

    val progress = combine(mediaPlayer.playTime(100.milliseconds), _totalDuration) { playTime, totalDuration ->
        if (totalDuration > ZERO) {
            maxOf(0.0, minOf(1.0, playTime / totalDuration))
        } else {
            0.0
        }.toFloat()
    }.toInitializedObservable(0.0f, coroutineScope)

    val isSeekEnabled = controls.map { it.seek != null }.toInitializedObservable(false, coroutineScope)

    val playButton = controls.map { controls ->
        KalugaButton.Plain("\u23F5", ButtonStyles.mediaButton, controls.play != null || controls.unpause != null) {
            coroutineScope.launch {
                when {
                    controls.unpause != null -> controls.tryUnpause()
                    controls.play != null -> controls.tryPlay()
                    else -> {}
                }
            }
        }
    }.toUninitializedObservable(coroutineScope)

    val pauseButton = controls.map {
        KalugaButton.Plain("\u23F8", ButtonStyles.mediaButton, it.pause != null) {
            coroutineScope.launch { it.tryPause() }
        }
    }.toUninitializedObservable(coroutineScope)

    val stopButton = controls.map {
        KalugaButton.Plain("\u23F9", ButtonStyles.mediaButton, it.stop != null) {
            coroutineScope.launch { it.tryStop() }
        }
    }.toUninitializedObservable(coroutineScope)

    val loopButton = controls.map { controls ->
        controls.setLoopMode?.let { setLoopMode ->
            when (val loopMode = setLoopMode.currentLoopMode) {
                PlaybackState.LoopMode.NotLooping -> KalugaButton.Plain("\uD83D\uDD01", ButtonStyles.mediaButton, true) {
                    coroutineScope.launch {
                        setLoopMode.perform(PlaybackState.LoopMode.LoopingForever)
                    }
                }
                is PlaybackState.LoopMode.LoopingForever -> KalugaButton.Plain("\uD83D\uDD01", ButtonStyles.mediaButtonFocus, true) {
                    coroutineScope.launch {
                        setLoopMode.perform(PlaybackState.LoopMode.LoopingForFixedNumber(1U))
                    }
                }
                is PlaybackState.LoopMode.LoopingForFixedNumber -> KalugaButton.Plain("\uD83D\uDD01 x${loopMode.loops}", ButtonStyles.mediaButtonFocus, true) {
                    coroutineScope.launch {
                        setLoopMode.perform(PlaybackState.LoopMode.NotLooping)
                    }
                }
            }
        } ?: KalugaButton.Plain("\uD83D\uDD01", ButtonStyles.mediaButton, false) {}
    }.toUninitializedObservable(coroutineScope)

    val rateButton = controls.map { controls ->
        controls.setRate?.let { setRate ->
            KalugaButton.Plain(playbackFormatter.format(setRate.currentRate), ButtonStyles.mediaButton, true) {
                coroutineScope.launch {
                    var selectedRate = setRate.currentRate
                    alertPresenterBuilder.buildActionSheet(this) {
                        setTitle("media_playback_rate".localized())
                        val actions = listOf(1.0f, 0.5f, 2.0f, 4.0f).map {
                            Alert.Action(playbackFormatter.format(it)) {
                                selectedRate = it
                            }
                        }
                        addActions(actions)
                    }.show()
                    setRate.perform(selectedRate)
                }
            }
        } ?: KalugaButton.Plain(playbackFormatter.format(1), ButtonStyles.mediaButton, false) {}
    }.toUninitializedObservable(coroutineScope)

    val volumeButton = mediaPlayer.currentVolume.map {
        KalugaButton.Plain(
            "media_playback_volume".localized().format(volumeFormatter.format(it)),
            ButtonStyles.default,
        ) {
            updateVolume()
        }
    }.toUninitializedObservable(coroutineScope)

    fun seekTo(progress: Double) {
        coroutineScope.launch {
            controls.value.trySeek(_totalDuration.value * progress)
        }
    }

    private fun updateVolume() {
        coroutineScope.launch {
            val selectedVolume = CompletableDeferred<Float>()
            alertPresenterBuilder.buildActionSheet(coroutineScope) {
                setTitle("media_playback_update_volume".localized())
                addActions(
                    (0..100).map {
                        val asPercentage = it.toFloat() / 100.0f
                        Alert.Action(volumeFormatter.format(asPercentage)) {
                            selectedVolume.complete(asPercentage)
                        }
                    },
                )
            }.show()

            mediaPlayer.updateVolume(selectedVolume.await())
        }
    }

    val selectMediaButton = KalugaButton.Plain("media_select_media_button".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            val defaultAudio = Alert.Action("media_select_default_audio".localized())
            val defaultVideo = Alert.Action("media_select_default_video".localized())
            val selectLocalFile = Alert.Action("media_select_file_on_device".localized())
            val selectRemoteFile = Alert.Action("media_select_file_from_web".localized())
            val confirm = Alert.Action("confirm_selection".localized(), Alert.Action.Style.POSITIVE)
            val cancel = Alert.Action("cancel_selection".localized(), Alert.Action.Style.CANCEL)
            val actionSelected = alertPresenterBuilder.buildActionSheet(coroutineScope) {
                setTitle("media_select_title".localized())
                addActions(defaultAudio, defaultVideo, selectLocalFile, selectRemoteFile, cancel)
            }.show()

            when (actionSelected) {
                defaultAudio -> didSelectFileAt(mediaSourceFromUrl("https://cdn.freesound.org/previews/459/459992_6253486-lq.mp3"))
                defaultVideo -> didSelectFileAt(
                    mediaSourceFromUrl("https://joy1.videvo.net/videvo_files/video/free/2019-04/large_watermarked/190408_07_GulfSturgeon_03_preview.mp4"),
                )
                selectLocalFile -> navigator.navigate(MediaNavigationAction.SelectLocal)
                selectRemoteFile -> {
                    var input = ""
                    val remoteActionSelected = alertPresenterBuilder.buildAlertWithInput(coroutineScope) {
                        setTitle("media_select_from_web_title".localized())
                        addActions(confirm, cancel)
                        setTextInput(input, "media_select_from_web_hint".localized()) {
                            input = it
                        }
                    }.show()
                    when (remoteActionSelected) {
                        confirm -> didSelectFileAt(mediaSourceFromUrl(input))
                        else -> {}
                    }
                }
                else -> {}
            }
        }
    }

    val isShowingVideo = mediaPlayer.playableMedia.map { it?.isVideo ?: false }.toInitializedObservable(false, coroutineScope)
    val resolution = mediaPlayer.playableMedia.flatMapLatest { playableMedia ->
        playableMedia?.resolution ?: flowOf(Resolution.ZERO)
    }.toInitializedObservable(Resolution.ZERO, coroutineScope)

    init {
        coroutineScope.launch {
            controls.mapNotNull { it.displayError }.collect { error ->
                alertPresenterBuilder.buildAlert(coroutineScope) {
                    setTitle("media_error_title".localized())
                    setMessage(error.error.message ?: error.error::class.simpleName.orEmpty())
                    setNeutralButton("cancel_selection".localized())
                }.show()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.close()
        mediaPlayerDispatcher.close()
    }

    fun didSelectFileAt(source: MediaSource?) {
        coroutineScope.launch {
            try {
                source?.let {
                    mediaPlayer.initializeFor(it)
                } ?: mediaPlayer.reset()
            } catch (e: PlaybackError) {
                // Will be displayed automatically
            }
        }
    }

    private fun Duration.format() = toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) "%02d:%02d:%02d".format(hours, minutes, seconds) else "%02d:%02d".format(minutes, seconds)
    }
}
