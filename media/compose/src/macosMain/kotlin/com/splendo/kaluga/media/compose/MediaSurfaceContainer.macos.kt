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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.media.MediaSurfaceBinder

/**
 * Hosts a platform-native video surface and binds the resulting [com.splendo.kaluga.media.MediaSurface]
 * to [binder] (via [MediaSurfaceBinder.bind]); the surface is detached ([MediaSurfaceBinder.unbind]) on
 * composition exit. Obtain [binder] from `MediaPlayer.surfaceBinder`.
 *
 * MacOS implementation — currently a placeholder as CMP for MacOs does not support interop with NSView.
 */
@Composable
actual fun MediaSurfaceContainer(binder: MediaSurfaceBinder, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        BasicText("Video playback inside Compose-on-macOS is not yet supported (CMP-macOS-Native lacks AppKit interop in 1.11.0).")
    }
}
