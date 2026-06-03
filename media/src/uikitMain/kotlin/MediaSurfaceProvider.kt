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

import platform.AVFoundation.AVPlayerLayer
import platform.AVKit.AVPlayerViewController
import platform.UIKit.UIView

/**
 * A [BaseMediaSurfaceProvider] that attempts to grab the [MediaSurface] from a [UIView].
 * Requires [UIView.layer] to be an [AVPlayerLayer].
 */
class UIViewMediaSurfaceProvider(initialView: UIView?) : BaseMediaSurfaceProvider<UIView>(initialView) {
    override fun UIView.asMediaSurface(): MediaSurface? {
        val avPlayerLayer = layer as? AVPlayerLayer
        return avPlayerLayer?.let { MediaSurface(it) }
    }
}

/** A [BaseMediaSurfaceProvider] that takes an [AVPlayerViewController] directly. */
class AVPlayerViewControllerMediaSurfaceProvider(initialViewController: AVPlayerViewController?) : BaseMediaSurfaceProvider<AVPlayerViewController>(initialViewController) {
    override fun AVPlayerViewController.asMediaSurface(): MediaSurface = MediaSurface(this)
}
