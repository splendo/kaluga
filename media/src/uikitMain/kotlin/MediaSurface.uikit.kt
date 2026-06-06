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

import platform.AVKit.AVPlayerViewController

/** Convenience constructor binding an `AVPlayer` to an [AVPlayerViewController]. */
@Suppress("FunctionName")
fun MediaSurface(viewController: AVPlayerViewController): MediaSurface = MediaSurface(
    { viewController.player = it },
)

/**
 * Binds this [AVPlayerViewController] to [binder] so a [MediaPlayer] using that binder renders its video
 * here (the controller handles its own layout). Call [MediaSurfaceBinder.unbind] to detach.
 * @param binder the [MediaSurfaceBinder] to bind to, typically obtained from [MediaPlayer.surfaceBinder].
 */
fun AVPlayerViewController.bind(binder: MediaSurfaceBinder) {
    binder.bind(MediaSurface(this))
}
