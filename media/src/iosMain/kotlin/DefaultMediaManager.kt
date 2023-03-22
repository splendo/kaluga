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

import com.splendo.kaluga.base.kvo.observeKeyValueAsFlow
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import platform.AVFoundation.AVErrorContentIsNotAuthorized
import platform.AVFoundation.AVErrorContentIsProtected
import platform.AVFoundation.AVErrorContentIsUnavailable
import platform.AVFoundation.AVErrorContentNotUpdated
import platform.AVFoundation.AVErrorDecodeFailed
import platform.AVFoundation.AVErrorFailedToParse
import platform.AVFoundation.AVErrorFormatUnsupported
import platform.AVFoundation.AVErrorNoLongerPlayable
import platform.AVFoundation.AVFoundationErrorDomain
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemFailedToPlayToEndTimeErrorKey
import platform.AVFoundation.AVPlayerItemFailedToPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemStatus
import platform.AVFoundation.AVPlayerItemStatusReadyToPlay
import platform.AVFoundation.AVPlayerItemStatusUnknown
import platform.AVFoundation.AVPlayerStatus
import platform.AVFoundation.AVPlayerStatusFailed
import platform.AVFoundation.AVPlayerStatusReadyToPlay
import platform.AVFoundation.AVPlayerStatusUnknown
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSError
import platform.Foundation.NSKeyValueObservingOptionInitial
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue.Companion.currentQueue
import platform.Foundation.NSOperationQueue.Companion.mainQueue
import platform.Foundation.NSURL
import platform.Foundation.NSURLErrorDomain
import platform.darwin.NSObjectProtocol
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

actual class PlayableMedia(internal val avPlayerItem: AVPlayerItem) {
    actual val duration: Duration get() = CMTimeGetSeconds(avPlayerItem.duration).seconds
    actual val currentPlayTime: Duration get() = CMTimeGetSeconds(avPlayerItem.currentTime()).seconds
}

actual class DefaultMediaManager(coroutineContext: CoroutineContext) : BaseMediaManager(coroutineContext) {

    class Builder : BaseMediaManager.Builder {
        override fun create(coroutineContext: CoroutineContext): BaseMediaManager = DefaultMediaManager(coroutineContext)
    }

    private val avPlayer = AVPlayer(null)
    private val observers = atomic<List<NSObjectProtocol>>(emptyList())

    private var itemJob: Job? = null

    init {
        launch {
            avPlayer.observeKeyValueAsFlow<AVPlayerStatus>("status", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew, coroutineContext).collect { status ->
                when (status) {
                    AVPlayerStatusUnknown,
                    AVPlayerStatusReadyToPlay -> {}
                    AVPlayerStatusFailed -> {
                        avPlayer.error?.handleError()
                    }
                    else -> {}
                }
            }
        }

        observers.value.forEach { NSNotificationCenter.defaultCenter.removeObserver(it) }
        observers.value = listOf(
            NSNotificationCenter.defaultCenter.addObserverForName(
                AVPlayerItemDidPlayToEndTimeNotification,
                null,
                currentQueue ?: mainQueue
            ) {
                handleCompleted()
            },
            NSNotificationCenter.defaultCenter.addObserverForName(
                AVPlayerItemFailedToPlayToEndTimeNotification,
                null,
                currentQueue ?: mainQueue
            ) {
                (it?.userInfo?.get(AVPlayerItemFailedToPlayToEndTimeErrorKey) as? NSError)?.handleError()
            }
        )
    }

    override fun createPlayableMedia(url: String): PlayableMedia? = NSURL.URLWithString(url)?.let {
        PlayableMedia(AVPlayerItem(it))
    }

    override fun initialize(playableMedia: PlayableMedia) {
        avPlayer.replaceCurrentItemWithPlayerItem(playableMedia.avPlayerItem)
        itemJob = launch {
            playableMedia.avPlayerItem.observeKeyValueAsFlow<AVPlayerItemStatus>("status", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew, coroutineContext).collect { status ->
                when (status) {
                    AVPlayerItemStatusUnknown -> {}
                    AVPlayerItemStatusReadyToPlay -> handlePrepared(PlayableMedia(avPlayer.currentItem!!))
                    AVPlayerStatusFailed -> {
                        avPlayer.error?.handleError()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun play() = avPlayer.play()
    override fun pause() = avPlayer.pause()
    override fun stop() {
        avPlayer.replaceCurrentItemWithPlayerItem(null)
    }

    override fun cleanUp() {
        avPlayer.replaceCurrentItemWithPlayerItem(null)
        observers.getAndUpdate { emptyList() }.forEach { observer ->
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
        cancel()
    }

    override fun seekTo(duration: Duration) {
        avPlayer.seekToTime(CMTimeMakeWithSeconds(duration.toDouble(DurationUnit.SECONDS), 1))
    }

    private fun NSError.handleError() {
        val playbackError = avPlayer.error?.let { error ->
            when (error.domain) {
                AVFoundationErrorDomain -> when (error.code) {
                    AVErrorFormatUnsupported,
                    AVErrorContentIsNotAuthorized,
                    AVErrorContentIsProtected,
                    AVErrorContentIsUnavailable -> PlaybackError.Unsupported
                    AVErrorDecodeFailed,
                    AVErrorFailedToParse -> PlaybackError.MalformedMediaSource
                    AVErrorNoLongerPlayable -> PlaybackError.IO
                    AVErrorContentNotUpdated -> PlaybackError.TimedOut
                    else -> null
                }
                else -> null
            }
        } ?: PlaybackError.Unknown
        handleError(playbackError)
    }
}
