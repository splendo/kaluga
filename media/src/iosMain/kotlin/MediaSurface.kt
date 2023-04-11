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
import platform.AVKit.AVPlayerViewController

typealias MediaSurfaceBinding = (AVPlayer?) -> Unit

/**
 * A surface on which the video component of a [PlayableMedia] can be rendered.
 * @property bind the [MediaSurfaceBinding] method to bind to an [AVPlayer]
 */
actual data class MediaSurface(val bind: MediaSurfaceBinding) {

    /**
     * Constructor to create a [MediaSurface] that binds the [AVPlayer] of a [MediaManager] to an [AVPlayerLayer]
     * @param avPlayerLayer the [AVPlayerLayer] to bind to
     */
    constructor(avPlayerLayer: AVPlayerLayer) : this(
        {
            avPlayerLayer.player = it
        }
    )

    /**
     * Constructor to create a [MediaSurface] that binds the [AVPlayer] of a [MediaManager] to an [AVPlayerViewController]
     * @param viewController the [AVPlayerViewController] to bind to
     */
    constructor(viewController: AVPlayerViewController) : this(
        {
            viewController.player = it
        }
    )
}
