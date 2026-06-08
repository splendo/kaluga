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

import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer

typealias MediaSurfaceBinding = (AVPlayer?) -> Unit

/**
 * A surface on which the video component of a [PlayableMedia] can be rendered.
 * Shared across iOS + macOS. A view constructs a [MediaSurface] (e.g. from its [AVPlayerLayer]) and binds
 * it to a [MediaSurfaceBinder].
 *
 * @property bind the [MediaSurfaceBinding] that wires the surface to an [AVPlayer].
 */
actual data class MediaSurface(val bind: MediaSurfaceBinding) {

    /**
     * Convenience: bind the [AVPlayer] of a `MediaManager` to an [AVPlayerLayer].
     */
    constructor(avPlayerLayer: AVPlayerLayer) : this(
        { avPlayerLayer.player = it },
    )
}

/**
 * Binds this [AVPlayerLayer] to [binder] so a [MediaPlayer] using that binder renders its video here.
 * Call [MediaSurfaceBinder.unbind] to detach. Simplifies non-Compose video layout, mirroring
 * `SurfaceView.bind` on Android.
 * @param binder the [MediaSurfaceBinder] to bind to, typically obtained from [MediaPlayer.surfaceBinder].
 */
fun AVPlayerLayer.bind(binder: MediaSurfaceBinder) {
    binder.bind(MediaSurface(this))
}
