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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Default implementation of [PlayableMedia]
 * @param source the [MediaSource] on which the media is found
 */
actual class DefaultPlayableMedia(actual override val source: MediaSource) : PlayableMedia {
    actual override val duration: Duration = Duration.ZERO
    actual override val currentPlayTime: Duration = Duration.ZERO
    actual override val tracks: List<TrackInfo> = emptyList()
    actual override val resolution: Flow<Resolution> = flowOf(Resolution.ZERO)
}

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

    private var mediaSurface: MediaSurface? = null
    private val volume = MutableStateFlow(1.0f)
    actual override val currentVolume: Flow<Float> = volume.asSharedFlow()
    actual override suspend fun updateVolume(volume: Float) {
        this.volume.value = volume
    }

    actual override fun handleCreatePlayableMedia(source: MediaSource): PlayableMedia? = DefaultPlayableMedia(source)

    actual override fun initialize(playableMedia: PlayableMedia) {
        handlePrepared(playableMedia)
    }

    actual override suspend fun renderVideoOnSurface(surface: MediaSurface?) {
        this.mediaSurface = surface
    }

    actual override fun play(rate: Float) {
        TODO("Not yet implemented")
    }

    actual override fun pause() {
        TODO("Not yet implemented")
    }

    actual override fun stop() {
        TODO("Not yet implemented")
    }

    actual override fun startSeek(duration: Duration) {
        TODO("Not yet implemented")
    }

    actual override fun cleanUp() {
        TODO("Not yet implemented")
    }

    actual override fun handleReset() {
        TODO("Not yet implemented")
    }
}
