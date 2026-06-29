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

package com.splendo.kaluga.lifecycle.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.WindowScope
import platform.AppKit.NSWindow

/**
 * Composition local exposing the macOS host's [NSWindow]. CMP 1.11+ provides `WindowScope.window`
 * as a receiver only — there is no built-in composition local — so the host has to install one
 * for any code below it that needs the window. Use [ProvideNSWindow] inside a `Window { … }`
 * block:
 *
 * ```
 * Window(title = "App") {
 *     ProvideNSWindow {
 *         AppRootScreen()
 *     }
 * }
 * ```
 *
 * Defaults to `null`; consumers (e.g. `WindowLifecycleSubscribable.AttachToCompose`) silently
 * no-op when no window is installed.
 */
val LocalNSWindow = staticCompositionLocalOf<NSWindow?> { null }

/**
 * Helper that lifts the [WindowScope.window] receiver onto [LocalNSWindow] so children can read
 * it without being inside a `WindowScope`.
 */
@Composable
fun WindowScope.ProvideNSWindow(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNSWindow provides window) {
        content()
    }
}
