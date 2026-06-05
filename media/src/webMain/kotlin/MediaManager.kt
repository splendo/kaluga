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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Default implementation of [BaseMediaManager] for the JS family, backed by an `HTMLMediaElement`.
 */
actual class DefaultMediaManager internal constructor(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext) :
    BaseMediaManager(mediaSurfaceProvider, coroutineContext) {

    /**
     * Builder for creating a [DefaultMediaManager]
     */
    class Builder : BaseMediaManager.Builder {
        override fun create(mediaSurfaceProvider: MediaSurfaceProvider?, coroutineContext: CoroutineContext): BaseMediaManager =
            DefaultMediaManager(mediaSurfaceProvider, coroutineContext)
    }

    private val id = nextMediaId()
    private val volume = MutableStateFlow(1.0f)
    actual override val currentVolume: Flow<Float> = volume.asStateFlow()
    private val resolution = MutableStateFlow(Resolution.ZERO)
    private var initializedMedia: PlayableMedia? = null

    init {
        ensureMediaRegistry()
        mediaCreate(id)
        mediaRegisterListeners(
            id,
            onPrepared = { initializedMedia?.let { handlePrepared(it) } },
            onEnded = { handleCompleted() },
            onError = { code -> handleError(playbackErrorForCode(code)) },
            onRate = { rate -> handleRateChanged(rate.toFloat()) },
            onSeeked = { handleSeekCompleted(true) },
            onVolume = { newVolume -> volume.value = newVolume.toFloat() },
            onResize = { resolution.value = Resolution(mediaWidth(id), mediaHeight(id)) },
        )
    }

    actual override fun handleCreatePlayableMedia(source: MediaSource): PlayableMedia? {
        mediaSetSource(id, source.url)
        return DefaultPlayableMedia(source, id, resolution.asStateFlow())
    }

    actual override fun initialize(playableMedia: PlayableMedia) {
        initializedMedia = playableMedia
        // If the element already loaded its metadata before initialize, prepare immediately; otherwise the
        // loadedmetadata listener will.
        if (mediaHasMetadata(id)) {
            handlePrepared(playableMedia)
        }
    }

    actual override fun play(rate: Float) = mediaPlay(id, rate.toDouble())

    actual override fun pause() = mediaPause(id)

    actual override fun stop() = mediaStop(id)

    actual override suspend fun updateVolume(volume: Float) {
        mediaSetVolume(id, volume.toDouble())
        this.volume.value = volume
    }

    actual override fun startSeek(duration: Duration) = mediaSeek(id, duration.inWholeMilliseconds / 1000.0)

    actual override suspend fun renderVideoOnSurface(surface: MediaSurface?) {
        if (surface != null) {
            mediaAttachToSurface(id, surface.elementId)
        } else {
            mediaDetachFromSurface(id)
        }
    }

    actual override fun handleReset() {
        initializedMedia = null
        mediaReset(id)
    }

    actual override fun cleanUp() {
        initializedMedia = null
        mediaRelease(id)
    }
}
