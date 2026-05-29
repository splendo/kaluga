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

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window

/**
 * Entry point for the macOS demo. CMP on macOS owns the NSApplication lifecycle, so the Swift
 * `@main` calls this from `applicationDidFinishLaunching`. The composable creates the NSWindow
 * internally.
 *
 * Only macOS-capable features are listed. Anything unmigrated is reported through
 * [onUnmigratedFeatureSelected] (defensive — should never fire on macOS).
 */
fun startMainWindow(onUnmigratedFeatureSelected: (Feature) -> Unit = {}) {
    Window(title = "Kaluga Example", size = DpSize(800.dp, 640.dp)) {
        val features = remember { Feature.entries.filter { it.availableOnMacOS } }
        AppRootScreen(
            features = features,
            onUnmigratedFeatureSelected = onUnmigratedFeatureSelected,
        )
    }
}
