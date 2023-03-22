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

import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

typealias AndroidMediaPlayer = android.media.MediaPlayer

actual class PlayableMedia(private val mediaPlayer: AndroidMediaPlayer) {
    actual val duration: Duration get() = mediaPlayer.duration.milliseconds
    actual val currentPlayTime: Duration get() = mediaPlayer.currentPosition.milliseconds
}

actual class DefaultMediaManager(coroutineContext: CoroutineContext) : BaseMediaManager(coroutineContext) {

    class Builder : BaseMediaManager.Builder {
        override fun create(coroutineContext: CoroutineContext): BaseMediaManager = DefaultMediaManager(coroutineContext)
    }

    private val mediaPlayer = AndroidMediaPlayer()

    init {
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
    
    override fun cleanUp() {
        mediaPlayer.release()
    }

    override fun createPlayableMedia(url: String): PlayableMedia? = try {
        mediaPlayer.setDataSource(url)
        PlayableMedia(mediaPlayer)
    } catch (e: Throwable) {
        when (e) {
            is IllegalStateException -> null
            is IOException -> null
            is IllegalArgumentException -> null
            is SecurityException -> null
            else -> throw e
        }
    }

    override fun initialize(playableMedia: PlayableMedia) {
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.setOnPreparedListener(null)
            handlePrepared(PlayableMedia(it))
        }
        mediaPlayer.prepareAsync()
    }

    override fun play() = try {
        mediaPlayer.start()
    } catch (e: IllegalStateException) {
        handleError(PlaybackError.Unknown)
    }

    override fun pause() = try {
        mediaPlayer.pause()
    } catch (e: IllegalStateException) {
        handleError(PlaybackError.Unknown)
    }

    override fun stop() = try {
        mediaPlayer.stop()
    } catch (e: IllegalStateException) {
        handleError(PlaybackError.Unknown)
    }

    override fun seekTo(duration: Duration) = mediaPlayer.seekTo(duration.inWholeMilliseconds.toInt())
}