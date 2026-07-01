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

import androidx.compose.ui.window.ComposeUIViewController
import com.splendo.kaluga.example.arch.AppRootScreen
import platform.UIKit.UIViewController

/**
 * iOS framework entry point. The Swift host wraps this controller in its own navigation chrome.
 * `onNativeLaunch` is invoked with a contribution id whenever a non-compose feature
 * (mobile-only `:mobileshared` features) is selected — the Swift side routes by id to the
 * corresponding UIKit/SwiftUI screen.
 */
fun MainViewController(onNativeLaunch: (String) -> Unit): UIViewController = ComposeUIViewController {
    AppRootScreen(onNativeLaunch = onNativeLaunch)
}
