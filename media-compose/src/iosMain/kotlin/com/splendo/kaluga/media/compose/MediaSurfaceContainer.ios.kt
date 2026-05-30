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

package com.splendo.kaluga.media.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import com.splendo.kaluga.media.MediaSurface
import kotlinx.cinterop.readValue
import platform.AVFoundation.AVPlayerLayer
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView

@Composable
actual fun MediaSurfaceContainer(provider: ComposeMediaSurfaceProvider, modifier: Modifier) {
    UIKitView(
        modifier = modifier,
        factory = {
            val layer = AVPlayerLayer()
            val view = object : UIView(frame = CGRectZero.readValue()) {
                override fun layoutSubviews() {
                    super.layoutSubviews()
                    layer.frame = bounds
                }
            }
            view.layer.addSublayer(layer)
            provider.set(MediaSurface(layer))
            view
        },
    )
    DisposableEffect(provider) {
        onDispose { provider.set(null) }
    }
}
