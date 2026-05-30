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
import androidx.compose.ui.Modifier
import com.splendo.kaluga.media.MediaSurface
import com.splendo.kaluga.media.MediaSurfaceProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A [MediaSurfaceProvider] whose [surface] is populated by a [MediaSurfaceContainer] composable
 * — the inverse of the platform "walk the view tree to find a SurfaceView" providers. Construct
 * one, hand it to a `MediaManager`, and render it inside Compose via [MediaSurfaceContainer];
 * the container creates the platform surface and pushes it into [surface].
 */
class ComposeMediaSurfaceProvider : MediaSurfaceProvider {

    private val _surface = MutableStateFlow<MediaSurface?>(null)
    override val surface: Flow<MediaSurface?> = _surface.asStateFlow()

    internal fun set(surface: MediaSurface?) {
        _surface.value = surface
    }
}

/**
 * Hosts a platform-native video surface (Android `SurfaceView`, iOS `AVPlayerView` host,
 * macOS placeholder until CMP grows AppKit interop) and binds the resulting [MediaSurface] to
 * [provider]. The surface is detached on composition exit.
 */
@Composable
expect fun MediaSurfaceContainer(provider: ComposeMediaSurfaceProvider, modifier: Modifier = Modifier)
