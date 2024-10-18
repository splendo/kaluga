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

import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

typealias AndroidMediaPlayer = android.media.MediaPlayer
typealias AndroidTrackInfo = android.media.MediaPlayer.TrackInfo

/**
 * Default implementation of [PlayableMedia]
 * @param source the [MediaSource] on which the media is found
 * @param mediaPlayer the [android.media.MediaPlayer] playing the media
 */
actual class DefaultPlayableMedia(actual override val source: MediaSource, private val mediaPlayer: AndroidMediaPlayer) : PlayableMedia {
    actual override val duration: Duration get() = mediaPlayer.duration.milliseconds
    actual override val currentPlayTime: Duration get() = mediaPlayer.currentPosition.milliseconds
    actual override val tracks get() = mediaPlayer.trackInfo.mapIndexed { index, trackInfo -> trackInfo.asTrackInfo(index) }
    private val mutex = Mutex()
    private var videoSizeListener: android.media.MediaPlayer.OnVideoSizeChangedListener? = null
    private val _resolution = MutableStateFlow(Resolution.ZERO)
    actual override val resolution: Flow<Resolution> = _resolution.asSharedFlow().onSubscription {
        mutex.withLock {
            if (_resolution.subscriptionCount.value > 0 && videoSizeListener == null) {
                videoSizeListener = MediaPlayer.OnVideoSizeChangedListener { _, width, height ->
                    _resolution.value = Resolution(width, height)
                }
                mediaPlayer.setOnVideoSizeChangedListener(videoSizeListener)
            }
        }
    }.onCompletion {
        mutex.withLock {
            if (_resolution.subscriptionCount.value == 0) {
                mediaPlayer.setOnVideoSizeChangedListener(null)
            }
        }
    }
}

private fun AndroidTrackInfo.asTrackInfo(identifier: Int): TrackInfo = TrackInfo(
    identifier,
    when (trackType) {
        AndroidTrackInfo.MEDIA_TRACK_TYPE_VIDEO -> TrackInfo.Type.VIDEO
        AndroidTrackInfo.MEDIA_TRACK_TYPE_AUDIO -> TrackInfo.Type.AUDIO
        AndroidTrackInfo.MEDIA_TRACK_TYPE_METADATA -> TrackInfo.Type.METADATA
        AndroidTrackInfo.MEDIA_TRACK_TYPE_SUBTITLE -> TrackInfo.Type.SUBTITLE
        AndroidTrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT -> TrackInfo.Type.TIMED_TEXT
        else -> TrackInfo.Type.UNKNOWN
    },
    language,
)

/**
 * Default implementation of [BaseMediaManager]
 * @param mediaSurfaceProvider a [MediaSurfaceProvider] that will automatically call [renderVideoOnSurface] for the latest [MediaSurface]
 * @param coroutineContext the [CoroutineContext] on which the media will be managed
 */
actual class DefaultMediaManager(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext) : BaseMediaManager(mediaSurfaceProvider, coroutineContext) {

    /**
     * Builder for creating a [DefaultMediaManager]
     */
    class Builder : BaseMediaManager.Builder {
        override fun create(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext): DefaultMediaManager = DefaultMediaManager(
            mediaSurfaceProvider,
            coroutineContext,
        )
    }

    private val mediaPlayer = AndroidMediaPlayer()
    private val volume = MutableStateFlow(1.0f)
    actual override val currentVolume: Flow<Float> = volume.asSharedFlow()
    actual override suspend fun updateVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
        this.volume.value = volume
    }

    init {
        mediaPlayer.setOnSeekCompleteListener {
            handleSeekCompleted(true)
        }
        mediaPlayer.setOnCompletionListener { handleCompleted() }
        mediaPlayer.setOnErrorListener { _, _, extra ->
            val error = when (extra) {
                AndroidMediaPlayer.MEDIA_ERROR_IO -> PlaybackError.IO
                AndroidMediaPlayer.MEDIA_ERROR_MALFORMED -> PlaybackError.MalformedMediaSource
                AndroidMediaPlayer.MEDIA_ERROR_UNSUPPORTED -> PlaybackError.Unsupported
                AndroidMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> PlaybackError.Unsupported
                AndroidMediaPlayer.MEDIA_ERROR_TIMED_OUT -> PlaybackError.TimedOut
                else -> PlaybackError.Unknown
            }
            handleError(error)
            false
        }
    }

    actual override fun cleanUp() {
        mediaPlayer.release()
    }

    actual override fun handleCreatePlayableMedia(source: MediaSource): PlayableMedia? = try {
        when (source) {
            is MediaSource.Asset -> mediaPlayer.setDataSource(source.descriptor)
            is MediaSource.File -> mediaPlayer.setDataSource(source.descriptor)
            is MediaSource.Url -> mediaPlayer.setDataSource(source.url.toExternalForm())
            is MediaSource.Content -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.setDataSource(source.context, source.uri, source.headers, source.cookies)
            } else {
                mediaPlayer.setDataSource(source.context, source.uri, source.headers)
            }
        }
        DefaultPlayableMedia(source, mediaPlayer)
    } catch (e: Throwable) {
        when (e) {
            is IllegalStateException -> null
            is IOException -> null
            is IllegalArgumentException -> null
            is SecurityException -> null
            else -> throw e
        }
    }

    actual override fun initialize(playableMedia: PlayableMedia) {
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.setOnPreparedListener(null)
            handlePrepared(DefaultPlayableMedia(playableMedia.source, it))
        }
        mediaPlayer.prepareAsync()
    }

    actual override suspend fun renderVideoOnSurface(surface: MediaSurface?) {
        try {
            mediaPlayer.setDisplay(surface?.holder)
        } catch (e: IllegalStateException) {
            //
        }
    }

    actual override fun play(rate: Float) {
        try {
            mediaPlayer.playbackParams = PlaybackParams().apply {
                speed = rate
            }
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        } catch (e: IllegalStateException) {
            handleError(PlaybackError.Unknown)
        }
    }

    actual override fun pause() = try {
        mediaPlayer.pause()
    } catch (e: IllegalStateException) {
        handleError(PlaybackError.Unknown)
    }

    actual override fun stop() = try {
        mediaPlayer.stop()
    } catch (e: IllegalStateException) {
        handleError(PlaybackError.Unknown)
    }

    actual override fun startSeek(duration: Duration) = try {
        mediaPlayer.seekTo(duration.inWholeMilliseconds.toInt())
    } catch (e: IllegalStateException) {
        handleSeekCompleted(false)
    }

    actual override fun handleReset() {
        mediaPlayer.reset()
    }
}
