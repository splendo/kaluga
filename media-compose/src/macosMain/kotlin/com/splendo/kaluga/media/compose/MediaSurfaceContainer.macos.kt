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

/**
 * macOS implementation — currently a placeholder.
 *
 * Compose Multiplatform ships two distinct macOS rendering paths:
 *
 * 1. **Compose Desktop on the JVM** (`org.jetbrains.compose.ui:ui-desktop`) — runs on the JVM
 *    via Skia/Skiko + AWT, and exposes `androidx.compose.ui.awt.ComposePanel` plus full Swing
 *    interop. AppKit/NSView interop is reachable here through Swing's heavyweight components.
 *
 * 2. **Compose Multiplatform Native macOS** (`org.jetbrains.compose.ui:ui-macosarm64`) — what
 *    this Kotlin/Native module compiles against. Renders directly via Skiko, no JVM, no AWT.
 *
 * The example app uses path (2) (Kotlin/Native macOS), and as of CMP **1.11.0** that path
 * defines `androidx.compose.ui.viewinterop.InteropView = typealias Any` and ships **no**
 * `@Composable fun NSView(...)` — verified by inspecting `ui-macosarm64-1.11.0-sources.jar`
 * (only `macosMain/androidx/compose/ui/appkit/Events.macos.kt` exists, providing `NSEvent`
 * access; no `NSView.macos.kt`). Importing `androidx.compose.ui.interop.NSView` produces
 * `Unresolved reference 'interop'`.
 *
 * The `awt`/`ComposePanel` API the user referenced is from the JVM Desktop variant and is not
 * available on the macOS-Native target.
 *
 * Until CMP-macOS-Native grows AppKit interop, the working path on macOS is the native
 * `NSViewMediaSurfaceProvider` / `WindowLifecycleSubscribable` against an AppKit `AVPlayerView`
 * hosted **outside** the Compose tree (e.g. SwiftUI/AppKit hosts that consume `KalugaExample.framework`).
 */
@Composable
actual fun MediaSurfaceContainer(provider: ComposeMediaSurfaceProvider, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        BasicText("Video playback inside Compose-on-macOS is not yet supported (CMP-macOS-Native lacks AppKit interop in 1.11.0).")
    }
}
