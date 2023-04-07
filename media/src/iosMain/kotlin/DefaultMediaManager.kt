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
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
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
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.AVMediaTypeClosedCaption
import platform.AVFoundation.AVMediaTypeMetadata
import platform.AVFoundation.AVMediaTypeMetadataObject
import platform.AVFoundation.AVMediaTypeSubtitle
import platform.AVFoundation.AVMediaTypeText
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemFailedToPlayToEndTimeErrorKey
import platform.AVFoundation.AVPlayerItemFailedToPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemStatus
import platform.AVFoundation.AVPlayerItemStatusReadyToPlay
import platform.AVFoundation.AVPlayerItemStatusUnknown
import platform.AVFoundation.AVPlayerItemTrack
import platform.AVFoundation.AVPlayerStatus
import platform.AVFoundation.AVPlayerStatusFailed
import platform.AVFoundation.AVPlayerStatusReadyToPlay
import platform.AVFoundation.AVPlayerStatusUnknown
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.languageCode
import platform.AVFoundation.mediaType
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.presentationSize
import platform.AVFoundation.rate
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.tracks
import platform.AVFoundation.volume
import platform.CoreGraphics.CGSize
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.CoreMedia.kCMTimeZero
import platform.Foundation.NSError
import platform.Foundation.NSKeyValueObservingOptionInitial
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue.Companion.currentQueue
import platform.Foundation.NSOperationQueue.Companion.mainQueue
import platform.darwin.NSObjectProtocol
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

actual class PlayableMedia(actual val source: MediaSource, internal val avPlayerItem: AVPlayerItem) {
    actual val duration: Duration get() = CMTimeGetSeconds(avPlayerItem.duration).seconds
    actual val currentPlayTime: Duration get() = CMTimeGetSeconds(avPlayerItem.currentTime()).seconds
    actual val tracks: List<TrackInfo> get() = avPlayerItem.tracks.mapNotNull { (it as? AVPlayerItemTrack).asTrackInfo() }
    actual val resolution: Flow<Resolution> = avPlayerItem.observeKeyValueAsFlow<Any>("presentationSize", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew).map { size ->
        // Mapping from typealias NSSize seems to fail so we'll just grab the value ourselves
        avPlayerItem.presentationSize.useContents { Resolution(width.toInt(), height.toInt()) }
    }
}

private fun AVPlayerItemTrack?.asTrackInfo(): TrackInfo? = this?.assetTrack?.let {
    TrackInfo(
        it.trackID,
        when (it.mediaType) {
            AVMediaTypeAudio -> TrackInfo.Type.AUDIO
            AVMediaTypeClosedCaption -> TrackInfo.Type.SUBTITLE
            AVMediaTypeMetadata -> TrackInfo.Type.METADATA
            AVMediaTypeMetadataObject -> TrackInfo.Type.METADATA
            AVMediaTypeSubtitle -> TrackInfo.Type.SUBTITLE
            AVMediaTypeText -> TrackInfo.Type.TIMED_TEXT
            AVMediaTypeVideo -> TrackInfo.Type.VIDEO
            else -> TrackInfo.Type.UNKNOWN
        },
        it.languageCode.orEmpty()
    )
}

actual class DefaultMediaManager(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext) : BaseMediaManager(mediaSurfaceProvider, coroutineContext) {

    class Builder : BaseMediaManager.Builder {
        override fun create(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext): BaseMediaManager = DefaultMediaManager(mediaSurfaceProvider, coroutineContext)
    }

    private val avPlayer = AVPlayer(null)
    private var surface: MediaSurface? by Delegates.observable(null) { _, old, new ->
        old?.bind?.invoke(null)
        new?.bind?.invoke(avPlayer)
    }

    override var volume: Float
        get() = avPlayer.volume
        set(value) { avPlayer.volume = value }

    private val observers = atomic<List<NSObjectProtocol>>(emptyList())

    private var itemJob: Job? = null

    init {
        launch {
            avPlayer.observeKeyValueAsFlow<AVPlayerStatus>("status", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew).collect { status ->
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

    override fun handleCreatePlayableMedia(source: MediaSource): PlayableMedia = PlayableMedia(
        source,
        when (source) {
            is MediaSource.Asset -> AVPlayerItem(source.asset)
            is MediaSource.URL -> AVPlayerItem(source.url)
        }
    )

    override fun initialize(playableMedia: PlayableMedia) {
        avPlayer.replaceCurrentItemWithPlayerItem(playableMedia.avPlayerItem)
        itemJob = launch {
            playableMedia.avPlayerItem.observeKeyValueAsFlow<AVPlayerItemStatus>("status", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew).collect { status ->
                when (status) {
                    AVPlayerItemStatusUnknown -> {}
                    AVPlayerItemStatusReadyToPlay -> handlePrepared(PlayableMedia(playableMedia.source, avPlayer.currentItem!!))
                    AVPlayerStatusFailed -> {
                        avPlayer.error?.handleError()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun renderVideoOnSurface(surface: MediaSurface?) {
        this.surface = surface
    }

    override fun play(rate: Float) {
        avPlayer.play()
        avPlayer.rate = rate
    }

    override fun pause() = avPlayer.pause()
    override fun stop() {
        avPlayer.seekToTime(kCMTimeZero.readValue(), kCMTimeZero.readValue(), kCMTimeZero.readValue()) {}
        avPlayer.replaceCurrentItemWithPlayerItem(null)
    }

    override fun cleanUp() {
        surface = null
        avPlayer.replaceCurrentItemWithPlayerItem(null)
        observers.getAndUpdate { emptyList() }.forEach { observer ->
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
        cancel()
    }

    override fun startSeek(duration: Duration) = avPlayer.seekToTime(CMTimeMakeWithSeconds(duration.toDouble(DurationUnit.SECONDS), 1000), kCMTimeZero.readValue(), kCMTimeZero.readValue()) {
        handleSeekCompleted(it)
    }

    override fun handleReset() {
        avPlayer.replaceCurrentItemWithPlayerItem(null)
    }

    private fun NSError.handleError() {
        val playbackError = when (domain) {
            AVFoundationErrorDomain -> when (code) {
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
        } ?: PlaybackError.Unknown
        handleError(playbackError)
    }
}
