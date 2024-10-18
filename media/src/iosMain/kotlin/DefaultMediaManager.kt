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
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import kotlinx.cinterop.value
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionRouteChangeNotification
import platform.AVFAudio.AVAudioSessionRouteChangeReasonKey
import platform.AVFAudio.AVAudioSessionRouteChangeReasonOldDeviceUnavailable
import platform.AVFAudio.AVAudioSessionRouteChangeReasonUnknown
import platform.AVFAudio.setActive
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
import platform.AVFoundation.AVPlayerRateDidChangeNotification
import platform.AVFoundation.AVPlayerRateDidChangeReasonKey
import platform.AVFoundation.AVPlayerStatus
import platform.AVFoundation.AVPlayerStatusFailed
import platform.AVFoundation.AVPlayerStatusReadyToPlay
import platform.AVFoundation.AVPlayerStatusUnknown
import platform.AVFoundation.AVURLAsset
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.defaultRate
import platform.AVFoundation.duration
import platform.AVFoundation.languageCode
import platform.AVFoundation.mediaType
import platform.AVFoundation.pause
import platform.AVFoundation.presentationSize
import platform.AVFoundation.rate
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.tracks
import platform.AVFoundation.volume
import platform.CoreMedia.CMTimeCompare
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.CoreMedia.CMTimeSubtract
import platform.CoreMedia.kCMTimeIndefinite
import platform.CoreMedia.kCMTimeZero
import platform.Foundation.NSError
import platform.Foundation.NSKeyValueObservingOptionInitial
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSNotificationName
import platform.Foundation.NSOperationQueue.Companion.currentQueue
import platform.Foundation.NSOperationQueue.Companion.mainQueue
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillEnterForegroundNotification
import platform.darwin.NSObjectProtocol
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/**
 * Default implementation of [PlayableMedia]
 * @param source the [MediaSource] on which the media is found
 * @param avPlayerItem the [AVPlayerItem] associated with the media
 */
actual class DefaultPlayableMedia(actual override val source: MediaSource, internal val avPlayerItem: AVPlayerItem) : PlayableMedia {
    actual override val duration: Duration get() = CMTimeGetSeconds(avPlayerItem.duration).seconds
    actual override val currentPlayTime: Duration get() = CMTimeGetSeconds(avPlayerItem.currentTime()).seconds
    actual override val tracks: List<TrackInfo> get() = avPlayerItem.tracks.mapNotNull { (it as? AVPlayerItemTrack).asTrackInfo() }
    actual override val resolution: Flow<Resolution> = avPlayerItem.observeKeyValueAsFlow<Any>(
        "presentationSize",
        NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew,
    ).map { _ ->
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
        it.languageCode.orEmpty(),
    )
}

/**
 * Default implementation of [BaseMediaManager]
 * @param mediaSurfaceProvider a [MediaSurfaceProvider] that will automatically call [renderVideoOnSurface] for the latest [MediaSurface]
 * @param settings the [Settings] used to configure the Media Manager
 * @param coroutineContext the [CoroutineContext] on which the media will be managed
 */
actual class DefaultMediaManager(mediaSurfaceProvider: MediaSurfaceProvider?, private val settings: Settings, coroutineContext: CoroutineContext) :
    BaseMediaManager(mediaSurfaceProvider, coroutineContext) {

    /**
     * Settings used for configuring a [DefaultMediaManager]
     * @property playInBackground if `true` playback will resume when the app moves to the background. Note that this will not loop
     * @property playAfterDeviceUnavailable if `true` playback will continue after the device on which audio was playing becomes unavailable (e.g. headphones disconnect).
     */
    data class Settings(val playInBackground: Boolean = false, val playAfterDeviceUnavailable: Boolean = false)

    /**
     * Builder for creating a [DefaultMediaManager]
     * @param settings the [Settings] used to configure the [DefaultMediaManager] created
     */
    class Builder(private val settings: Settings) : BaseMediaManager.Builder {

        constructor() : this(Settings())

        override fun create(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext): DefaultMediaManager = DefaultMediaManager(
            mediaSurfaceProvider,
            settings,
            coroutineContext,
        )
    }

    private val avPlayer = AVPlayer(null)
    private var surface: MediaSurface? by Delegates.observable(null) { _, old, new ->
        old?.bind?.invoke(null)
        new?.bind?.invoke(avPlayer)
    }

    actual override val currentVolume: Flow<Float> = avPlayer.observeKeyValueAsFlow<Float>("volume", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew)
    actual override suspend fun updateVolume(volume: Float) {
        avPlayer.volume = volume
    }

    private val observers = atomic<List<NSObjectProtocol>>(emptyList())

    private var itemJob: Job? = null

    init {
        handleIfPlayingInBackgroundEnabled { error ->
            AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, error)
        }
        launch {
            avPlayer.observeKeyValueAsFlow<AVPlayerStatus>("status", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew).collect { status ->
                when (status) {
                    AVPlayerStatusUnknown,
                    AVPlayerStatusReadyToPlay,
                    -> {}
                    AVPlayerStatusFailed -> {
                        avPlayer.error?.handleError()
                    }
                    else -> {}
                }
            }
        }
    }

    actual override fun handleCreatePlayableMedia(source: MediaSource): PlayableMedia? = DefaultPlayableMedia(
        source,
        source.avPlayerItem,
    )

    actual override fun initialize(playableMedia: PlayableMedia) {
        removeObservers()
        observers.value = createObservers()
        val avPlayerItem = playableMedia.source.avPlayerItem
        avPlayer.replaceCurrentItemWithPlayerItem(avPlayerItem)
        itemJob = launch {
            avPlayerItem.observeKeyValueAsFlow<AVPlayerItemStatus>("status", NSKeyValueObservingOptionInitial or NSKeyValueObservingOptionNew).collect { status ->
                when (status) {
                    AVPlayerItemStatusUnknown -> {}
                    AVPlayerItemStatusReadyToPlay -> handlePrepared(DefaultPlayableMedia(playableMedia.source, avPlayer.currentItem!!))
                    AVPlayerStatusFailed -> {
                        avPlayer.error?.handleError()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun createObservers() = listOf(
        observeNotification(AVPlayerItemDidPlayToEndTimeNotification) {
            avPlayer.currentItem?.let { currentItem ->
                // Completion event may come in after a rewind has occurred. To ensure it is intended to complete, we compare current time to duration
                // Current time may be slightly off however, so we maintain a margin of 1 second
                val secondsFromCompletion = CMTimeGetSeconds(CMTimeSubtract(currentItem.duration, currentItem.currentTime()))
                if (CMTimeCompare(currentItem.duration, kCMTimeIndefinite.readValue()) == 0 || secondsFromCompletion.absoluteValue < 1.0) {
                    handleCompleted()
                } else {
                    // Otherwise reset the rate since completion events will pause playback
                    avPlayer.rate = avPlayer.defaultRate
                }
            }
        },
        observeNotification(AVPlayerItemFailedToPlayToEndTimeNotification) {
            (it?.userInfo?.get(AVPlayerItemFailedToPlayToEndTimeErrorKey) as? NSError)?.handleError()
        },
        observeNotification(AVPlayerRateDidChangeNotification) { notification ->
            // When no reason is given, it is unknown. This includes completion and Audio Session Route changes, which are covered by other notifications
            notification?.userInfo?.get(AVPlayerRateDidChangeReasonKey)?.let {
                handleRateChanged(avPlayer.rate)
            }
        },
        observeNotification(UIApplicationDidEnterBackgroundNotification) {
            surface?.bind?.invoke(null)
        },
        observeNotification(UIApplicationWillEnterForegroundNotification) {
            surface?.bind?.invoke(avPlayer)
        },
        observeNotification(AVAudioSessionRouteChangeNotification) { notification ->
            val reason = notification?.userInfo?.get(AVAudioSessionRouteChangeReasonKey) as Int
            when (reason.toULong()) {
                AVAudioSessionRouteChangeReasonUnknown,
                AVAudioSessionRouteChangeReasonOldDeviceUnavailable,
                -> {
                    // This Route change will cause rate to be set to 0.0
                    // If we don't want that, we should wait until the change has occurred and then revert it
                    launch {
                        avPlayer.observeKeyValueAsFlow<Float>("rate").first { it == 0.0f }
                        if (settings.playAfterDeviceUnavailable) {
                            avPlayer.rate = avPlayer.defaultRate
                        } else {
                            handleRateChanged(0.0f)
                        }
                    }
                }
                else -> {}
            }
        },
    )

    private val MediaSource.avPlayerItem: AVPlayerItem get() = when (this) {
        is MediaSource.Asset -> AVPlayerItem(asset)
        is MediaSource.URL -> AVPlayerItem(AVURLAsset.URLAssetWithURL(url, options.associate { it.entry }))
    }

    actual override suspend fun renderVideoOnSurface(surface: MediaSurface?) {
        this.surface = surface
    }

    actual override fun play(rate: Float) {
        avPlayer.defaultRate = rate
        avPlayer.rate = rate
        handleIfPlayingInBackgroundEnabled { error ->
            AVAudioSession.sharedInstance().setActive(true, error)
        }
    }

    actual override fun pause() = avPlayer.pause()
    actual override fun stop() {
        avPlayer.seekToTime(kCMTimeZero.readValue(), kCMTimeZero.readValue(), kCMTimeZero.readValue()) {}
        avPlayer.replaceCurrentItemWithPlayerItem(null)
        handleIfPlayingInBackgroundEnabled { error ->
            AVAudioSession.sharedInstance().setActive(false, error)
        }
    }

    actual override fun cleanUp() {
        surface = null
        handleReset()
        cancel()
    }

    actual override fun startSeek(duration: Duration) = avPlayer.seekToTime(
        CMTimeMakeWithSeconds(duration.toDouble(DurationUnit.SECONDS), 1000),
        kCMTimeZero.readValue(),
        kCMTimeZero.readValue(),
    ) {
        handleSeekCompleted(it)
    }

    actual override fun handleReset() {
        removeObservers()
        avPlayer.replaceCurrentItemWithPlayerItem(null)
        handleIfPlayingInBackgroundEnabled { error ->
            AVAudioSession.sharedInstance().setActive(false, error)
        }
    }

    private fun removeObservers() {
        observers.getAndUpdate { emptyList() }.forEach { observer ->
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    private fun handleIfPlayingInBackgroundEnabled(action: (CPointer<ObjCObjectVar<NSError?>>) -> Unit) {
        if (settings.playInBackground) {
            memScoped {
                val error = alloc<ObjCObjectVar<NSError?>>()
                action(error.ptr)
                error.value?.handleError()
            }
        }
    }

    private fun NSError.handleError() {
        val playbackError = when (domain) {
            AVFoundationErrorDomain -> when (code) {
                AVErrorFormatUnsupported,
                AVErrorContentIsNotAuthorized,
                AVErrorContentIsProtected,
                AVErrorContentIsUnavailable,
                -> PlaybackError.Unsupported
                AVErrorDecodeFailed,
                AVErrorFailedToParse,
                -> PlaybackError.MalformedMediaSource
                AVErrorNoLongerPlayable -> PlaybackError.IO
                AVErrorContentNotUpdated -> PlaybackError.TimedOut
                else -> null
            }
            else -> null
        } ?: PlaybackError.Unknown
        handleError(playbackError)
    }

    private fun observeNotification(name: NSNotificationName, onNotification: (NSNotification?) -> Unit) = NSNotificationCenter.defaultCenter.addObserverForName(
        name,
        null,
        currentQueue ?: mainQueue,
        onNotification,
    )
}
