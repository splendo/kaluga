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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import com.splendo.kaluga.media.MediaSurface
import com.splendo.kaluga.media.MediaSurfaceProvider

private var surfaceElementCounter = 0

/**
 * Web implementation. Compose Multiplatform renders to a `<canvas>`, so the player's `<video>` element
 * cannot live inside the Compose tree. Instead this hosts a DOM container positioned (via
 * [onGloballyPositioned]) to overlay the area this composable occupies, and binds it as the
 * [MediaSurface]; the container — and the video the `MediaManager` appends to it — is removed on
 * composition exit. Positions are in CSS pixels, converted from Compose pixels through the current density.
 *
 * The container sits *behind* the Compose canvas, and this composable clears its own rectangle to
 * transparent ([BlendMode.Clear]) so the video shows through that hole. Everything Compose draws after the
 * surface — controls, overlays, dialogs — therefore composites on top of the video instead of being hidden
 * behind a DOM element painted over the canvas.
 */
@Composable
actual fun MediaSurfaceContainer(provider: MediaSurfaceProvider, modifier: Modifier) {
    if (provider !is ComposeMediaSurfaceProvider) {
        Box(modifier)
        return
    }

    val elementId = remember { "kaluga-media-surface-${surfaceElementCounter++}" }
    val density = LocalDensity.current.density

    DisposableEffect(provider, elementId) {
        createSurfaceElement(elementId)
        provider.set(MediaSurface(elementId))
        onDispose {
            provider.set(null)
            removeSurfaceElement(elementId)
        }
    }

    Box(
        modifier = modifier
            .drawBehind { drawRect(Color.Transparent, blendMode = BlendMode.Clear) }
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow()
                positionSurfaceElement(
                    elementId,
                    (position.x / density).toDouble(),
                    (position.y / density).toDouble(),
                    (coordinates.size.width / density).toDouble(),
                    (coordinates.size.height / density).toDouble(),
                )
            },
    )
}

private fun createSurfaceElement(id: String) {
    js(
        """
        if (typeof document === 'undefined') return;
        var container = document.createElement('div');
        container.id = id;
        container.style.position = 'fixed';
        container.style.overflow = 'hidden';
        container.style.pointerEvents = 'none';
        container.style.zIndex = '-1';
        // Opaque backing so the cleared canvas hole shows the media surface (and video letterbox) rather
        // than punching through to the page background where the video does not paint.
        container.style.backgroundColor = '#000';
        document.body.appendChild(container);
        """,
    )
}

private fun positionSurfaceElement(id: String, x: Double, y: Double, width: Double, height: Double) {
    js(
        """
        var container = document.getElementById(id);
        if (!container) return;
        container.style.left = x + 'px';
        container.style.top = y + 'px';
        container.style.width = width + 'px';
        container.style.height = height + 'px';
        """,
    )
}

private fun removeSurfaceElement(id: String) {
    js(
        """
        var container = document.getElementById(id);
        if (container && container.parentNode) container.parentNode.removeChild(container);
        """,
    )
}
