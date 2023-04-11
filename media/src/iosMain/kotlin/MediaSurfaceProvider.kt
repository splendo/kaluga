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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import platform.AVFoundation.AVPlayerLayer
import platform.AVKit.AVPlayerViewController
import platform.UIKit.UIView

/**
 * A [MediaSurfaceProvider] that grabs a [MediaSurface] from a given [Value]
 * @param Value the type of value that can be mapped to a [MediaSurface]
 * @param initialValue the initial [Value] or 'null' to provide a [MediaSurface] for
 */
abstract class BaseMediaSurfaceProvider<Value>(initialValue: Value?) : MediaSurfaceProvider {
    private val valueState = MutableStateFlow(initialValue)

    /**
     * Updates the [Value] so that a new [MediaSurface] will be provided
     * @param value the new [Value] or `null`
     */
    fun update(value: Value?) {
        valueState.value = value
    }

    override val surface: Flow<MediaSurface?> = valueState.map { value ->
        withContext(Dispatchers.Main) {
            value?.asMediaSurface()
        }
    }

    protected abstract fun Value.asMediaSurface(): MediaSurface?
}

/**
 * A [BaseMediaSurfaceProvider] that attempts to grab the [MediaSurface] from a [UIView]
 * Requires that [UIView.layer] is an [AVPlayerLayer]
 * @param initialView the initial [UIView] or 'null' to provide a [MediaSurface] for
 */
class UIViewMediaSurfaceProvider(initialView: UIView?) : BaseMediaSurfaceProvider<UIView>(initialView) {

    override fun UIView.asMediaSurface(): MediaSurface? {
        val avPlayerLayer = layer as? AVPlayerLayer
        return avPlayerLayer?.let {
            MediaSurface(it)
        }
    }
}
/**
 * A [BaseMediaSurfaceProvider] that attempts to grab the [MediaSurface] from an [AVPlayerLayer]
 * @param initialLayer the initial [AVPlayerLayer] or 'null' to provide a [MediaSurface] for
 */
class AVPlayerLayerMediaSurfaceProvider(initialLayer: AVPlayerLayer?) : BaseMediaSurfaceProvider<AVPlayerLayer>(initialLayer) {
    override fun AVPlayerLayer.asMediaSurface(): MediaSurface = MediaSurface(this)
}

/**
 * A [BaseMediaSurfaceProvider] that attempts to grab the [MediaSurface] from an [AVPlayerViewController]
 * @param initialViewController the initial [AVPlayerViewController] or 'null' to provide a [MediaSurface] for
 */
class AVPlayerViewControllerMediaSurfaceProvider(initialViewController: AVPlayerViewController?) : BaseMediaSurfaceProvider<AVPlayerViewController>(initialViewController) {
    override fun AVPlayerViewController.asMediaSurface(): MediaSurface = MediaSurface(this)
}

/**
 * A [BaseMediaSurfaceProvider] that attempts to grab the [MediaSurface] from a [MediaSurfaceBinding]
 * @param initialBinding the initial [MediaSurfaceBinding] or 'null' to provide a [MediaSurface] for
 */
class BindingMediaSurfaceProvider(initialBinding: MediaSurfaceBinding?) : BaseMediaSurfaceProvider<MediaSurfaceBinding>(initialBinding) {
    override fun MediaSurfaceBinding.asMediaSurface(): MediaSurface = MediaSurface(this)
}
