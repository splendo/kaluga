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

package com.splendo.kaluga.example.shared.ui

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.splendo.kaluga.example.arch.AppRootScreen
import com.splendo.kaluga.lifecycle.compose.ProvideNSWindow

/**
 * macOS framework entry point. CMP owns the NSApplication lifecycle so the Swift `@main` calls
 * this from `applicationDidFinishLaunching`. [ProvideNSWindow] lifts the `WindowScope.window`
 * receiver onto `LocalNSWindow` so any `WindowLifecycleSubscribable` further down the tree can
 * auto-subscribe via `LifecycleSubscribable.AttachToCompose()` — required by media surface
 * providers and similar window-bound services on macOS.
 */
fun startMainWindow() {
    Window(title = "Kaluga Example", size = DpSize(800.dp, 640.dp)) {
        ProvideNSWindow {
            AppRootScreen()
        }
    }
}
