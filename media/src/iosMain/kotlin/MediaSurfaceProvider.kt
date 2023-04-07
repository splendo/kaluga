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

abstract class BaseMediaSurfaceProvider<Value>(initialValue: Value?) : MediaSurfaceProvider {
    private val valueState = MutableStateFlow(initialValue)
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

class UIViewMediaSurfaceProvider(initialView: UIView?) : BaseMediaSurfaceProvider<UIView>(initialView) {

    override fun UIView.asMediaSurface(): MediaSurface? {
        val avPlayerLayer = layer as? AVPlayerLayer
        return avPlayerLayer?.let {
            MediaSurface(it)
        }
    }
}

class AVPlayerLayerMediaSurfaceProvider(initialLayer: AVPlayerLayer?) : BaseMediaSurfaceProvider<AVPlayerLayer>(initialLayer) {
    override fun AVPlayerLayer.asMediaSurface(): MediaSurface = MediaSurface(this)
}

class AVPlayerViewControllerMediaSurfaceProvider(initialViewController: AVPlayerViewController?) : BaseMediaSurfaceProvider<AVPlayerViewController>(initialViewController) {
    override fun AVPlayerViewController.asMediaSurface(): MediaSurface = MediaSurface(this)
}

class BindingMediaSurfaceProvider(initialBinding: MediaSurfaceBinding?) : BaseMediaSurfaceProvider<MediaSurfaceBinding>(initialBinding) {
    override fun MediaSurfaceBinding.asMediaSurface(): MediaSurface = MediaSurface(this)
}
