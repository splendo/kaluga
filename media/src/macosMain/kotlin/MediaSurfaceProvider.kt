/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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
import platform.AVKit.AVPlayerView
import platform.AppKit.NSView

/**
 * A [BaseMediaSurfaceProvider] that attempts to grab the [MediaSurface] from an [NSView].
 * Requires the view's backing `layer` to be an [AVPlayerLayer] (i.e. `wantsLayer = true` and a
 * Core-Animation layer-hosted view).
 */
class NSViewMediaSurfaceProvider(initialView: NSView?) : BaseMediaSurfaceProvider<NSView>(initialView) {
    override fun NSView.asMediaSurface(): MediaSurface? {
        val avPlayerLayer = layer as? AVPlayerLayer
        return avPlayerLayer?.let { MediaSurface(it) }
    }
}

/** A [BaseMediaSurfaceProvider] that takes an [AVPlayerView] directly. */
class AVPlayerViewMediaSurfaceProvider(initialView: AVPlayerView?) :
    BaseMediaSurfaceProvider<AVPlayerView>(initialView) {
    override fun AVPlayerView.asMediaSurface(): MediaSurface = MediaSurface(this)
}
